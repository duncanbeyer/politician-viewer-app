package com.example.assignment4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "MainActivity";
    Toolbar myToolbar;
    LinearLayoutManager linearLayoutManager;

    TextView tLocation;
    String sLocation;
    ConstraintLayout loc_layout;
    ArrayList<Person> people = new ArrayList<>();
    RecyclerView recyclerView;
    PersonAdapter adapter;
    private FusedLocationProviderClient locationClient;
    private static final int LOCATION_REQUEST = 111;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loc_layout = findViewById(R.id.loc_layout);
        loc_layout.setBackgroundColor(getResources().getColor(R.color.prim_purple));
        tLocation = findViewById(R.id.location);

        myToolbar = findViewById(R.id.toolbar);
        myToolbar.setTitle(R.string.toolbar_title);
        myToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        myToolbar.setBackgroundColor(getResources().getColor(R.color.toolbar_purple));

        setSupportActionBar(myToolbar);

        adapter = new PersonAdapter(people, this);
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setAdapter(adapter);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setBackgroundColor(getResources().getColor(R.color.white));

        tryDownload();

        locationClient =
                LocationServices.getFusedLocationProviderClient(this);

        getLocation();

    }

    void getLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            return;
        }

        locationClient.getLastLocation().addOnSuccessListener(this, location -> {
                    if (location != null) {
                        sLocation = getPlace(location);
                        doLocation();
                    }
                })
                .addOnFailureListener(this, e ->
                        Toast.makeText(MainActivity.this,
                                e.getMessage(), Toast.LENGTH_LONG).show());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST) {
            if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    tLocation.setText(R.string.denied_location);
                }
            }
        }
    }

    private String getPlace(Location loc) {

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        String s = "";

        try {
            addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
            if (addresses != null && !addresses.isEmpty()) {
                s = addresses.get(0).getAddressLine(0);
            } else {
                s = getString(R.string.location_error);
            }
        } catch (IOException e) {}
        return s;
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menuA) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            Log.d(TAG,"HELP PRESSED");
            return true;

        } else if (item.getItemId() == R.id.menuB) {
            Log.d(TAG,"SEARCH PRESSED");
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }

    void doLocation() {

        String[] ss = sLocation.split(", ");
        String temp = ", ";
        tLocation.setText(ss[0] + temp + ss[1] + temp + ss[2]);

    }

    Person setPerson(String role, JSONObject data) {
        try {
            return new Person(role, data);

        } catch (Exception e) {
            Log.e(TAG,"Error in setPerson", e);
            return null;}
    }

    void fillList(JSONObject jso) {

        JSONArray offices;
        JSONArray officials;
        try {
            offices = jso.getJSONArray("offices");
            officials = jso.getJSONArray("officials");
            JSONObject temp;
            for (int i = 0;i < offices.length();i++) {
                temp = offices.getJSONObject(i);
                for (int j = 0;j < temp.getJSONArray("officialIndices").length();j++) {
                    Person tempGuy = setPerson(temp.getString("name"), officials.getJSONObject(temp.getJSONArray("officialIndices").getInt(j)));
                    tempGuy.printPerson();
                    people.add(tempGuy);
                }
            }
        } catch (Exception e) {Log.e(TAG,"error in fillList", e);}

        adapter.notifyDataSetChanged();

    }


    void tryDownload() {
        String url = "https://www.googleapis.com/civicinfo/v2/representatives?key=AIzaSyCgnHHKJhsR_5VKif-cOOmku2R0egOQL70&address=60605";

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                null,
                response -> {
//                    try {
//                        doLocation(response.getJSONObject("normalizedInput").getString("city"), response.getJSONObject("normalizedInput").getString("state"), response.getJSONObject("normalizedInput").getString("zip"));
//                        Log.d(TAG,response.getJSONObject("normalizedInput").toString());
//                    } catch (Exception e) {}
                    fillList(response);
                },
                error -> {
                    Log.e(TAG, "Error getting JSON data: " + error.getMessage());
                });

        queue.add(jsonObjectRequest);


    }

    @Override
    public void onClick(View view) {

    }
}

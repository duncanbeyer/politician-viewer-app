package com.example.assignment4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "MainActivity";
    final String key = "AIzaSyCgnHHKJhsR_5VKif-cOOmku2R0egOQL70";
    Toolbar myToolbar;
    LinearLayoutManager linearLayoutManager;

    TextView tLocation;
    String sLocation;
    String addr;
    ConstraintLayout loc_layout, noInternetLayout;
    ArrayList<Person> people = new ArrayList<>();
    RecyclerView recyclerView;
    PersonAdapter adapter;
    private FusedLocationProviderClient locationClient;
    private static final int LOCATION_REQUEST = 111;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(getResources().getColor(R.color.white));


        loc_layout = findViewById(R.id.loc_layout);
        loc_layout.setBackgroundColor(getResources().getColor(R.color.prim_purple));
        tLocation = findViewById(R.id.location);

        noInternetLayout = findViewById(R.id.no_int_layout);
        noInternetLayout.setVisibility(View.GONE);

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

        locationClient =
                LocationServices.getFusedLocationProviderClient(this);

        getLocation();

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, OfficialActivity.class);
        intent.putExtra("person", people.get(recyclerView.getChildLayoutPosition(v)));
        intent.putExtra("addr", addr);
        startActivity(intent);
    }

    void doLocation() {

        String[] ss = sLocation.split(", ");
        String temp = ", ";
        addr = ss[0] + temp + ss[1] + temp + ss[2];
        tLocation.setText(addr);
        Log.d(TAG,"just set addr to " + addr);
        tryDownload();

    }

    void getLocation() {

        if (!gotInternet()) {
            noInternet();
            return;
        }

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

            LayoutInflater inflater = getLayoutInflater();
            View dialog = inflater.inflate(R.layout.search_layout, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            EditText temp = dialog.findViewById(R.id.text_box);
            builder.setView(dialog)
                    .setPositiveButton(getString(R.string.alert_ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d(TAG,"ok clicked");

                            newAddr(temp.getText().toString());

                        }
                    });
            builder.setView(dialog)
                    .setNegativeButton(getString(R.string.alert_cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d(TAG,"cancel clicked");
                        }
                    });

            builder.show();

            Log.d(TAG,"SEARCH PRESSED");
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }

    void newAddr(String s) {

        Log.d(TAG,"new addr with " + s);
        String[] temp = s.split(" ");
        addr = temp[0];
        for (int i = 1;i < temp.length;i++) {
            addr += "%20";
            addr += temp[i];
        }

        tryDownload();
        tLocation.setText(addr);
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

    void noInternet() {
        tLocation.setText(R.string.no_loc);
        noInternetLayout.setVisibility(View.VISIBLE);
        noInternetLayout.setBackground(getResources().getDrawable(R.drawable.shape));
        recyclerView.setVisibility(View.GONE);


    }

    void emptyList() {
        while (people.size() > 0) {
            people.remove(0);
            adapter.notifyItemRemoved(0);
        }
    }


    void tryDownload() {

        emptyList();
        Log.d(TAG,"in tryDownload addr is " + addr);
        String url = "https://www.googleapis.com/civicinfo/v2/representatives?key=" + key + "&address=" + addr;

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                null,
                response -> {
                    fillList(response);
                },
                error -> {
                    Log.e(TAG, "Error getting JSON data: " + error.getMessage());
                });

        queue.add(jsonObjectRequest);
    }

    boolean gotInternet() {

        ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return (networkInfo != null && networkInfo.isConnectedOrConnecting());

    }


}

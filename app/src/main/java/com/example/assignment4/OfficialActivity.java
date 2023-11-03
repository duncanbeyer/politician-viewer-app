package com.example.assignment4;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


public class OfficialActivity  extends AppCompatActivity implements View.OnClickListener{
    Toolbar toolbar;
    private static final String TAG = "OfficialActivity";
    String addr = "";
    TextView location, name, job, party, label1, box1, label2, box2, label3, box3, label4, box4;
    ImageView portrait, partyLogo, twitterLogo, youtubeLogo, facebookLogo;
    Person person;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);

        location = findViewById(R.id.location);
        name = findViewById(R.id.name);
        job = findViewById(R.id.job);
        party = findViewById(R.id.party);
        label1 = findViewById(R.id.label1);
        box1 = findViewById(R.id.box1);
        label2 = findViewById(R.id.label2);
        box2 = findViewById(R.id.box2);
        label3 = findViewById(R.id.label3);
        box3 = findViewById(R.id.box3);
        label4 = findViewById(R.id.label4);
        box4 = findViewById(R.id.box4);

        portrait = findViewById(R.id.portrait);
        partyLogo = findViewById(R.id.party_logo);
        twitterLogo = findViewById(R.id.twitter_logo);
        youtubeLogo = findViewById(R.id.youtube_logo);
        facebookLogo = findViewById(R.id.facebook_logo);

        person = getIntent().getParcelableExtra("person");
        addr = getIntent().getStringExtra("addr");

        setters();
    }

    void setters() {

        location.setText(addr);

        name.setText(person.getInfo("name"));
        job.setText(person.getInfo("job"));
        party.setText("(" + person.getInfo("name") + ")");

        String urlString = person.getInfo("imageUrl");
        if (urlString.length() > 0) {
            Log.d(TAG, "url: " + urlString);
            Picasso.get().load(urlString).error(R.drawable.brokenimage).into(portrait);
        }
        else {
            portrait.setImageResource(R.drawable.missing);
        }

        doBoxes(0,0);

    }

    void doBoxes(int i, int j) { // i is which info, j is which box we are on
        if (i > 3 || j > 3) {
            return;
        }

        String s = "";

        switch (i) {
            case 0:
                if (person.getInfo("address").length() > 0) {
                    s = "address";
                }
            case 1:
                if (person.getInfo("phone").length() > 0) {
                    s = "phone";
                }
            case 2:
                if (person.getInfo("email").length() > 0) {
                    s = "email";
                }
            case 3:
                if (person.getInfo("website").length() > 0) {
                    s = "website";
                }
        }

        if (s.length() == 0) {
            doBoxes(i + 1, j);
            return;
        }

        String tempBox = person.getInfo(s);
        String tempLabel = s.substring(0,1).toUpperCase() + s.substring(1) + ":";

        switch (j) {
            case 0:
                label1.setText(tempLabel);
                box1.setText(tempBox);
            case 1:
                label2.setText(tempLabel);
                box2.setText(tempBox);
            case 2:
                label3.setText(tempLabel);
                box3.setText(tempBox);
            case 3:
                label4.setText(tempLabel);
                box4.setText(tempBox);
        }
        doBoxes(i+1, j+1);

    }

    @Override
    public void onClick(View view) {

    }
}

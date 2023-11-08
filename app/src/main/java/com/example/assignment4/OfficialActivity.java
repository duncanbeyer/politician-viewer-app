package com.example.assignment4;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.text.util.Linkify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


public class OfficialActivity  extends AppCompatActivity implements View.OnClickListener{
    Toolbar toolbar;
    private static final String TAG = "OfficialActivity";
    ConstraintLayout loc_layout;
    String addr;
    TextView location, name, job, party, label1, box1, label2, box2, label3, box3, label4, box4;
    ImageView portrait, partyLogo, twitterLogo, youtubeLogo, facebookLogo;
    String ytLink, xLink, fbLink;
    Person person;
    boolean photo = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);



        loc_layout = findViewById(R.id.loc_layout);
        loc_layout.setBackgroundColor(getResources().getColor(R.color.prim_purple));
        location = findViewById(R.id.location);



        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.toolbar_title);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setBackgroundColor(getResources().getColor(R.color.toolbar_purple));
        setSupportActionBar(toolbar);

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
        party.setText("(" + person.getInfo("party") + ")");

        ytLink = person.getInfo("yt");
        xLink = person.getInfo("x");
        fbLink = person.getInfo("fb");

        doImages();

        doBoxes(0,0);

    }

    void doImages() {

        View view = this.getWindow().getDecorView();
        if (person.getInfo("party").equals("Democratic Party")) {
            view.setBackgroundColor(getResources().getColor(R.color.dem_blue));
            partyLogo.setImageResource(R.drawable.dem_logo);
            partyLogo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {openLink(v,
                        "https://democrats.org");}
            });
        }
        else if (person.getInfo("party").equals("Republican Party")) {
            view.setBackgroundColor(getResources().getColor(R.color.rep_red));
            partyLogo.setImageResource(R.drawable.rep_logo);
            partyLogo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {openLink(v,
                        "https://www.gop.com");}
            });
        }

        String urlString = person.getInfo("imageUrl");
        if (urlString.length() > 0) {
            Log.d(TAG, "url: " + urlString);
            Picasso.get().load(urlString).error(R.drawable.brokenimage).into(portrait);
            portrait.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openPhoto(v);
                }
            });
        }
        else {
            portrait.setImageResource(R.drawable.missing);
        }



        if (fbLink.length() > 0) {
            facebookLogo.setImageResource(R.drawable.facebook);
            facebookLogo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {openLink(v,
                        ("https://www.facebook.com/alabamapublicserivcecommission" + fbLink));}
            });
        }
        if (ytLink.length() > 0) {
            youtubeLogo.setImageResource(R.drawable.youtube);
            youtubeLogo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {openLink(v,
                        ("https://www.youtube.com/" + ytLink));}
            });
        }
        if (xLink.length() > 0) {
            twitterLogo.setImageResource(R.drawable.twitter);
            twitterLogo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {openLink(v,
                        ("https://www.twitter.com/" + xLink));}
            });
        }

    }

    void doBoxes(int i, int j) { // i is which info, j is which box we are on
        if (i > 3 || j > 3) {
            doChannels(j);
            return;
        }

        String s = "";

        switch (i) {
            case 0:
                if (person.getInfo("address").length() > 0) {
                    Log.d(TAG,"s = address");
                    s = "address";
                }
                break;
            case 1:
                if (person.getInfo("phone").length() > 0) {
                    Log.d(TAG,"s = phone");
                    s = "phone";
                }
                break;

            case 2:
                if (person.getInfo("email").length() > 0) {
                    Log.d(TAG,"s = email");
                    s = "email";
                }
                break;
            case 3:
                if (person.getInfo("website").length() > 0) {
                    Log.d(TAG,"s = website");
                    s = "website";
                }
                break;
        }

        if (s.length() == 0) {
            doBoxes(i + 1, j);
            return;
        }

        String tempBox = person.getInfo(s);
        String tempLabel = s.substring(0,1).toUpperCase() + s.substring(1) + ":";

        switch (j) {
            case 0:
                Log.d(TAG,"Setting 1");
                label1.setText(tempLabel);
                box1.setText(tempBox);
                Linkify.addLinks(box1, Linkify.ALL);
                box1.setLinkTextColor(getResources().getColor(R.color.white));
                break;
            case 1:
                Log.d(TAG,"Setting 2");
                label2.setText(tempLabel);
                box2.setText(tempBox);
                Linkify.addLinks(box2, Linkify.ALL);
                box2.setLinkTextColor(getResources().getColor(R.color.white));
                break;
            case 2:
                Log.d(TAG,"Setting 3");
                label3.setText(tempLabel);
                box3.setText(tempBox);
                Linkify.addLinks(box3, Linkify.ALL);
                box3.setLinkTextColor(getResources().getColor(R.color.white));
                break;
            case 3:
                Log.d(TAG,"Setting 4");
                label4.setText(tempLabel);
                box4.setText(tempBox);
                Linkify.addLinks(box4, Linkify.ALL);
                box4.setLinkTextColor(getResources().getColor(R.color.white));
                break;
        }
        doBoxes(i+1, j+1);

    }

    void doChannels(int j) {

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            return;
        }

        ConstraintLayout constraintLayout = findViewById(R.id.landscape_official);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);

        Log.d(TAG,"in doChannels j is " + j);

        switch (j) {
            case 1:
                constraintSet.connect(R.id.youtube_logo,ConstraintSet.TOP,R.id.box1,ConstraintSet.BOTTOM,20);
                constraintSet.connect(R.id.twitter_logo,ConstraintSet.TOP,R.id.box1,ConstraintSet.BOTTOM,20);
                constraintSet.connect(R.id.facebook_logo,ConstraintSet.TOP,R.id.box1,ConstraintSet.BOTTOM,20);
                break;
            case 2:
                constraintSet.connect(R.id.youtube_logo,ConstraintSet.TOP,R.id.box2,ConstraintSet.BOTTOM,20);
                constraintSet.connect(R.id.twitter_logo,ConstraintSet.TOP,R.id.box2,ConstraintSet.BOTTOM,20);
                constraintSet.connect(R.id.facebook_logo,ConstraintSet.TOP,R.id.box2,ConstraintSet.BOTTOM,20);
                break;
            case 3:
                constraintSet.connect(R.id.youtube_logo,ConstraintSet.TOP,R.id.box3,ConstraintSet.BOTTOM,20);
                constraintSet.connect(R.id.twitter_logo,ConstraintSet.TOP,R.id.box3,ConstraintSet.BOTTOM,20);
                constraintSet.connect(R.id.facebook_logo,ConstraintSet.TOP,R.id.box3,ConstraintSet.BOTTOM,20);
                break;
            case 4:
                constraintSet.connect(R.id.youtube_logo,ConstraintSet.TOP,R.id.box4,ConstraintSet.BOTTOM,20);
                constraintSet.connect(R.id.twitter_logo,ConstraintSet.TOP,R.id.box4,ConstraintSet.BOTTOM,20);
                constraintSet.connect(R.id.facebook_logo,ConstraintSet.TOP,R.id.box4,ConstraintSet.BOTTOM,20);
                break;
        }

        constraintSet.applyTo(constraintLayout);
    }

    void openPhoto(View v) {

        Log.d(TAG,portrait.getDrawable().toString());
        Intent intent = new Intent(this, PhotoActivity.class);
        intent.putExtra("person", person);
        intent.putExtra("addr", addr);
        startActivity(intent);

    }

    void openLink(View v, String s) {

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(s));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
        else {
            Log.d(TAG,"NULL when opening copright link.");
        }
    }



    @Override
    public void onClick(View view) {

    }
}

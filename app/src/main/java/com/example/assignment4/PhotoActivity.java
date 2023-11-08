package com.example.assignment4;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.squareup.picasso.Picasso;

public class PhotoActivity  extends AppCompatActivity {

    private static final String TAG = "PhotoActivity";
    TextView location, name, job;
    ConstraintLayout loc_layout;
    Toolbar toolbar;
    ImageView portrait, partyLogo;
    Person person;
    String addr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

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
        portrait = findViewById(R.id.portrait);
        location = findViewById(R.id.location);
        partyLogo = findViewById(R.id.party_logo);

        person = getIntent().getParcelableExtra("person");
        addr = getIntent().getStringExtra("addr");

        location.setText(addr);
        name.setText(person.getInfo("name"));
        job.setText(person.getInfo("job"));

        Picasso.get().load(person.getInfo("imageUrl")).error(R.drawable.brokenimage).into(portrait);

        View view = this.getWindow().getDecorView();
        if (person.getInfo("party").equals("Democratic Party")) {
            view.setBackgroundColor(getResources().getColor(R.color.dem_blue));
            partyLogo.setImageResource(R.drawable.dem_logo);
        }
        else if (person.getInfo("party").equals("Republican Party")) {
            view.setBackgroundColor(getResources().getColor(R.color.rep_red));
            partyLogo.setImageResource(R.drawable.rep_logo);
        }

    }
}

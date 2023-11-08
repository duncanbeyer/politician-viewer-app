package com.example.assignment4;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {
    private static final String TAG = "CopyrightActivity";
    ImageView background;
    TextView box1, box2, box3, box4, box5;
    Toolbar aboutToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        aboutToolbar = findViewById(R.id.toolbar);
        aboutToolbar.setTitle(R.string.toolbar_title);
        aboutToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        aboutToolbar.setBackgroundColor(getResources().getColor(R.color.toolbar_purple));

        setSupportActionBar(aboutToolbar);

        box1 = findViewById(R.id.box1);
        box2 = findViewById(R.id.box2);
        box3 = findViewById(R.id.box3);
        box4 = findViewById(R.id.box4);
        box5 = findViewById(R.id.box5);

        SpannableString content = new SpannableString(getString(R.string.box3));
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        box3.setText(content);

        box3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = "https://developers.google.com/civic-information/";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(s));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
                else {
                    Log.d(TAG,"NULL when opening copright link.");
                }
            }
        });

        }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.about_toolbar, menu);
        return true;
    }

    void returnMain(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }


}


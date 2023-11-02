package com.example.assignment4;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PersonHolder  extends RecyclerView.ViewHolder{

    TextView title;
    ImageView pic;
    TextView info;


    public PersonHolder(@NonNull View itemView) {
        super(itemView);

        title = itemView.findViewById(R.id.thumbnail_title);
        info = itemView.findViewById(R.id.thumbnail_info);
        pic = itemView.findViewById(R.id.thumbnail_image);

    }
}

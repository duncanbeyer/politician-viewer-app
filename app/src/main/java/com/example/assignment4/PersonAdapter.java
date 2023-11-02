package com.example.assignment4;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


public class PersonAdapter  extends RecyclerView.Adapter<PersonHolder> {

    MainActivity act;
    ArrayList<Person> persons;
    int num = 0;
    private static final String TAG = "PersonAdapter";
    public PersonAdapter(ArrayList<Person> ph, MainActivity ma) {
        act = ma;
        persons = ph;
    }


    @NonNull
    @Override
    public PersonHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.thumbnail_view, parent, false);
        itemView.setOnClickListener(act);

        return new PersonHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonHolder holder, int position) {

        Person person = persons.get(position);

        holder.title.setText(person.getInfo("job"));
        Log.d(TAG, person.getInfo("name"));
        holder.info.setText(person.getInfo("name") + " (" + person.getInfo("party") + ")");
        String urlString = person.getInfo("imageUrl");

        if (urlString.length() > 0) {
            Log.d(TAG, "url: " + urlString);
            Picasso.get().load(urlString).into(holder.pic, new Callback() {
                @Override
                public void onSuccess() {
                    Log.d(TAG,"success");
                }

                @Override
                public void onError(Exception e) {
                    Log.e(TAG, "onError: " + e);
                }
            });
        }
        else {
            holder.pic.setImageResource(R.drawable.missing);
        }
    }

    @Override
    public int getItemCount() {return persons.size();}

}

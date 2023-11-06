package com.example.assignment4;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;

public class Person implements Parcelable  {
    private static final String TAG = "Person";
String  name, job, party, address, phone, email, website, imageUrl, fb, yt, x;



    public Person(String role, JSONObject data) {


        job = role;
        Log.d(TAG,data.toString());

        try {
            name = data.getString("name");
        } catch (Exception e) {}
        try {
            party = data.getString("party");
        } catch (Exception e) {party = "Unknown";}
        try {
            phone = data.getJSONArray("phones").getString(0);
        } catch (Exception e) {phone = "";}
        try {
            website = data.getJSONArray("urls").getString(0);
        } catch (Exception e) {website = "";}
        try {
            email = data.getJSONArray("emails").getString(0);
        } catch (Exception e) {email = "";}
        try {
            imageUrl = data.getString("photoUrl");
            Log.d(TAG,"just set imageUrl to " + imageUrl);
        } catch (Exception e) {imageUrl = "";}

        address = "";
        try {
            JSONArray address_data = data.getJSONArray("address");
            address += address_data.getJSONObject(0).getString("line1");
            address += ", " + address_data.getJSONObject(0).getString("line2");
            address +=  ", " + address_data.getJSONObject(0).getString("line3");
        } catch (Exception e) {}

        try {
            JSONArray address_data = data.getJSONArray("address");
            address += " " + address_data.getJSONObject(0).getString("city");
            address += ", " + address_data.getJSONObject(0).getString("state");
            address += " " + address_data.getJSONObject(0).getString("zip");
        } catch (Exception j) {}

        fb = "";
        yt = "";
        x = "";
        try {
            JSONArray channels = data.getJSONArray("channels");
            JSONObject temp;
            for (int i = 0;i < channels.length();i++) {
                temp = channels.getJSONObject(i);
                if (temp.getString("type").equals("Facebook")) {
                    fb = temp.getString("id");
                }
                else if (temp.getString("type").equals("Twitter")) {
                    x = temp.getString("id");
                }
                else {
                    yt = temp.getString("id");
                }
            }
        } catch (Exception j) {
            Log.d(TAG,"Exception setting channels");
        }
    }

    protected Person(Parcel in) {
        name = in.readString();
        job = in.readString();
        party = in.readString();
        address = in.readString();
        phone = in.readString();
        email = in.readString();
        website = in.readString();
        imageUrl = in.readString();
        fb = in.readString();
        yt = in.readString();
        x = in.readString();
    }

    public static final Creator<Person> CREATOR = new Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

    public String getInfo(String s) {

        switch (s) {
            case "name":
                return name;
            case "job":
                return job;
            case "party":
                return party;
            case "address":
                return address;
            case "phone":
                return phone;
            case "email":
                return email;
            case "website":
                return website;
            case "imageUrl":
                return imageUrl;
            case "yt":
                return yt;
            case "fb":
                return fb;
            case "x":
                return x;
        }

        return null;
    }



    void printPerson() {
        Log.d(TAG, name);
        Log.d(TAG, job);
        Log.d(TAG, party);
        Log.d(TAG, address);
        Log.d(TAG, phone);
        Log.d(TAG, email);
        Log.d(TAG, website);
        Log.d(TAG, imageUrl);
        Log.d(TAG,yt);
        Log.d(TAG,fb);
        Log.d(TAG,x);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(job);
        parcel.writeString(party);
        parcel.writeString(address);
        parcel.writeString(phone);
        parcel.writeString(email);
        parcel.writeString(website);
        parcel.writeString(imageUrl);
        parcel.writeString(fb);
        parcel.writeString(yt);
        parcel.writeString(x);
    }
}

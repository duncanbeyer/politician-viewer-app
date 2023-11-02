package com.example.assignment4;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

public class Person {
    private static final String TAG = "Person";
String  name, job, party, address, phone, email, website, imageUrl;



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
    }

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
    }

}

package com.inbrain.example;

import android.app.Application;

import com.inbrain.sdk.InBrain;

/**
 * Created by Alex Kravchenko on 28/07/2019.
 */
public class MyApplication extends Application {
    private static final String API_CLIENT_ID = "9c367c28-c8a4-498d-bf22-1f3682fc73aa"; // your client id obtained by your account manager
    private static final String API_SECRET = "90MB8WyMZyYykgs0TaR21SqCcCZz3YTTXio9FoN5o5NJ6+svp3Q2tO8pvM9CjbskCaLAog0msmVTcIigKPQw4A=="; // your client secret obtained by your account manager

    @Override
    public void onCreate() {
        super.onCreate();
        boolean isS2S = false;
        InBrain.getInstance().setInBrain(this, API_CLIENT_ID, API_SECRET, isS2S);

        //optional
//        String sessionID = "1f3682fc73aa";
//        HashMap<String, String> dataOptions = new HashMap<>();
//        dataOptions.put("gender", "female");
//        dataOptions.put("age", "26");
//        InBrain.getInstance().setInBrainValuesFor(sessionID, dataOptions);

        //optional
        applyUiCustomization();

        //optional
//        InBrain.getInstance().setLanguage("en-us");
    }

    private void applyUiCustomization() {
        InBrain.getInstance().setToolbarTitle(getString(R.string.app_name)); // set title

        boolean useResourceId = false;
        if (useResourceId) {
            InBrain.getInstance().setToolbarColorResId(R.color.background); // set toolbar color with status bar
            InBrain.getInstance().setTitleTextColorResId(android.R.color.white); // set toolbar text & icon color
        } else {
            InBrain.getInstance().setToolbarColor(getResources().getColor(R.color.colorAccent));
            InBrain.getInstance().setTitleTextColor(getResources().getColor(android.R.color.white));
        }
    }
}

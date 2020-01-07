package com.inbrain.example;

import android.app.Application;

import com.inbrain.sdk.InBrain;

/**
 * Created by Alex Kravchenko on 28/07/2019.
 */
public class MyApplication extends Application {
    private static final String CLIENT_ID = "9c367c28-c8a4-498d-bf22-1f3682fc73aa"; // your client id obtained by your account manager
    private static final String CLIENT_SECRET = "90MB8WyMZyYykgs0TaR21SqCcCZz3YTTXio9FoN5o5NJ6+svp3Q2tO8pvM9CjbskCaLAog0msmVTcIigKPQw4A=="; // your client secret obtained by your account manager

    @Override
    public void onCreate() {
        super.onCreate();
        InBrain.getInstance().init(this, CLIENT_ID, CLIENT_SECRET);
    }
}

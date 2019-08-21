package com.inbrain.example;

import android.app.Application;

import com.inbrain.sdk.InBrain;

/**
 * Created by Alex Kravchenko on 28/07/2019.
 */
public class MyApplication extends Application {
    private static final String CLIENT_ID = "external-web-client"; // your client id obtained by your account manager
    private static final String CLIENT_SECRET = "l3!9hrl*olsdfliw#4uJO*f^j4ow8"; // your client secret obtained by your account manager

    @Override
    public void onCreate() {
        super.onCreate();
        InBrain.getInstance().init(this, CLIENT_ID, CLIENT_SECRET);
    }
}

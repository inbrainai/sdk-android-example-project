package com.inbrain.example;

import android.app.Application;

import com.inbrain.sdk.InBrain;
import com.inbrain.sdk.callback.InBrainCallback;
import com.inbrain.sdk.model.Reward;

import java.util.List;

/**
 * Created by Alex Kravchenko on 28/07/2019.
 */
public class MyApplication extends Application {
    private static final String CLIENT_ID = "external-web-client"; // your client id obtained by your account manager
    private static final String CLIENT_SECRET = "l3!9hrl*olsdfliw#4uJO*f^j4ow8"; // your client secret obtained by your account manager

    @Override
    public void onCreate() {
        super.onCreate();
        InBrain.getInstance().init(this, CLIENT_ID, CLIENT_SECRET, new InBrainCallback() {
            @Override
            public void onClosed() {
            }

            @Override
            public boolean handleRewards(List<Reward> rewards) {
                // Here you can add universal handler for rewards, for example you can immediately
                // add to user balance and send a broadcast intent to update UI:
                // LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(new Intent(REWARDS_RECEIVED)));
                return false;
            }
        });
    }
}

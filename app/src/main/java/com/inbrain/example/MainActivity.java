package com.inbrain.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.inbrain.sdk.InBrain;
import com.inbrain.sdk.callback.GetRewardsCallback;
import com.inbrain.sdk.callback.InBrainCallback;
import com.inbrain.sdk.model.Reward;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements InBrainCallback {
    private static final String CLIENT_ID = "external-web-client"; // your client id obtained by your account manager
    private static final String CLIENT_SECRET = "l3!9hrl*olsdfliw#4uJO*f^j4ow8"; // your client secret obtained by your account manager
    private static final String APP_USER_ID = "1234-1234-1234-1234"; // your user id
    private float balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InBrain.getInstance().init(this, CLIENT_ID, CLIENT_SECRET, this);
        InBrain.getInstance().setAppUserId(APP_USER_ID);
        getRewards();
    }

    /**
     * Shows ad
     */
    public void showSurveys(View view) {
        InBrain.getInstance().showSurveys(this);
    }

    /**
     * Calls when InBrain Ad Activity is finished
     */
    @Override
    public void onAdClosed() {
        Log.d("MainActivity", "Ad closed");
        // nice place to execute getRewards(), however it will be called automatically with delay
    }

    /**
     * Notifies your application about new rewards.
     * You need to confirm receipt after processing rewards in your application.
     *
     * @param rewards new rewards
     * @return false in case manual confirm, true - will be confirmed by SDK
     */
    @Override
    public boolean handleRewards(List<Reward> rewards) {
        Log.d("MainActivity", "onRewardReceived");
        processRewards(rewards);
        return chooseHowToConfirmRewards(rewards);
    }

    /**
     * Handles new rewards. It may be updating used balance of notifying your API about new rewards.
     *
     * @param rewards
     */
    private void processRewards(List<Reward> rewards) {
        for (Reward reward : rewards) {
            balance += reward.amount;
        }
        TextView balanceTv = findViewById(R.id.balanceTv);
        balanceTv.setText(String.valueOf(balance));
    }

    /**
     * Requests rewards manually.
     *
     * @see InBrainCallback
     */
    private void getRewards() {
        InBrain.getInstance().getRewards(new GetRewardsCallback() {
            /**
             *
             * @param rewards
             * @return false in case manual confirm, true - will be confirmed by SDK
             */
            @Override
            public boolean handleRewards(List<Reward> rewards) {
                Log.d("MainActivity", "Received rewards:" + Arrays.toString(rewards.toArray()));
                processRewards(rewards);
                return chooseHowToConfirmRewards(rewards);
            }

            @Override
            public void onFailToLoadRewards(int errorCode) {
                if (errorCode == GetRewardsCallback.ERROR_CODE_UNKNOWN) {
                    Log.e("MainActivity", "onFailToLoadRewards with unknown error");
                }
            }
        });
    }

    /**
     * Randomly chooses how to confirm reward
     *
     * @param rewards rewards to confirm
     * @return false in case manual confirm, true - will be confirmed by SDK
     */
    private boolean chooseHowToConfirmRewards(List<Reward> rewards) {
        if (new Random().nextInt(10) % 2 == 0) {
            return true;
        } else {
            confirmRewards(rewards);
            return false;
        }
    }

    /**
     * Confirms rewards manually.
     *
     * @param list list of rewards which need to be confirmed
     */
    private void confirmRewards(List<Reward> list) {
        InBrain.getInstance().confirmRewards(list);
    }
}

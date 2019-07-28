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

public class MainActivity extends AppCompatActivity {
    private static final String APP_USER_ID = "1234-1234-1234-1234"; // your user id

    private float balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InBrain.getInstance().setAppUserId(APP_USER_ID);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getRewards();
    }

    public void showSurveys(View view) {
        InBrain.getInstance().showSurveys(this);
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
            @Override
            public boolean handleRewards(List<Reward> rewards) {
                Log.d("MainActivity", "Received rewards:" + Arrays.toString(rewards.toArray()));
                processRewards(rewards);
                return true; // rewards are always handled successfully
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
     * Confirms rewards manually.
     *
     * @param list list of rewards which need to be confirmed
     */
    private void confirmRewards(List<Reward> list) {
        InBrain.getInstance().confirmRewards(list);
    }
}

package com.inbrain.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.inbrain.sdk.InBrain;
import com.inbrain.sdk.callback.ConfirmRewardsCallback;
import com.inbrain.sdk.callback.GetRewardsCallback;
import com.inbrain.sdk.callback.InBrainCallback;
import com.inbrain.sdk.callback.ReceivedRewardsListener;
import com.inbrain.sdk.model.Reward;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements InBrainCallback {
    private static final String CLIENT_SECRET = "l3!9hrl*olsdfliw#4uJO*f^j4ow8"; // your client secret obtained by your account manager
    private static final String APP_USER_ID = "1234-1234-1234-1234"; // your user id
    private float balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InBrain.init(this, "external-web-client", CLIENT_SECRET, this);
        InBrain.setAppUserId(APP_USER_ID);
        getRewards();
    }

    /**
     * Shows ad
     */
    public void showSurveys(View view) {
        InBrain.showSurveys(this);
    }

    /**
     * Calls when InBrain Ad Activity is finished
     */
    @Override
    public void onAdClosed() {
        Log.d("MainActivity", "Ad closed");
    }

    /**
     * Method which is used for notifying your application about available rewards
     *
     * @param rewards  new rewards
     * @param callback callback which is need to be called after processing rewards to confirm them.
     */
    @Override
    public void onRewardReceived(List<Reward> rewards, ReceivedRewardsListener callback) {
        processRewards(rewards);
        callback.confirmRewardsReceived(rewards);
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
     * Request rewards manually. Not recommended. You need to receive new rewards in onRewardReceived method.
     */
    private void getRewards() {
        InBrain.getRewards(new GetRewardsCallback() {
            @Override
            public void onGetRewards(List<Reward> rewards, ReceivedRewardsListener confirmRewardsCallback) {
                Log.d("MainActivity", "Received rewards:" + Arrays.toString(rewards.toArray()));
                processRewards(rewards);
                if (new Random().nextInt(10) % 2 == 0) {
                    confirmRewardsCallback.confirmRewardsReceived(rewards);
                } else {
                    confirmRewards(rewards);
                }
            }

            @Override
            public void onFailToLoadRewards(Throwable t) {
                Log.e("MainActivity", "onFailToLoadRewards:" + t.toString());
            }
        });
    }

    /**
     * Confirm rewards manually. Not recommended. You better confirm rewards using ReceivedRewardsListener.confirmRewardsReceived()
     * while requesting rewards
     */
    private void confirmRewards(List<Reward> list) {
        InBrain.confirmRewards(list, new ConfirmRewardsCallback() {
            @Override
            public void onSuccessfullyConfirmRewards() {
                Log.d("MainActivity", "onSuccessfullyConfirmRewards");
            }

            @Override
            public void onFailToConfirmRewards(Throwable t) {
                Log.e("MainActivity", "onFailToConfirmRewards:" + t.toString());
            }
        });
    }
}

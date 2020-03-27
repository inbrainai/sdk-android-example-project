package com.inbrain.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.inbrain.sdk.InBrain;
import com.inbrain.sdk.callback.GetRewardsCallback;
import com.inbrain.sdk.callback.InBrainCallback;
import com.inbrain.sdk.callback.StartSurveysCallback;
import com.inbrain.sdk.model.Reward;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final String PREFERENCE_BALANCE = "Balance";

    private float balance;
    private TextView balanceText;
    private InBrainCallback callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        balance = getPreferences(MODE_PRIVATE).getFloat(PREFERENCE_BALANCE, 0);

        balanceText = findViewById(R.id.balanceTv);
        balanceText.setText(String.valueOf(balance));

        InBrain.getInstance().setAppUserId("1234-1234-1234-1234"); // Custom app user id goes here!
        callback = new InBrainCallback() {
            @Override
            public void onClosed() {
                // inBrain screen is closed & user get back to your application
            }

            @Override
            public boolean handleRewards(List<Reward> rewards) {
                // Here you can add to user balance and update UI:
                return false;
            }
        };
        InBrain.getInstance().addCallback(callback);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getRewards();
    }

    @Override
    protected void onDestroy() {
        InBrain.getInstance().removeCallback(callback); // unsubscribe from events
        super.onDestroy();
    }

    public void showSurveys(View view) {
        InBrain.getInstance().showSurveys(this, new StartSurveysCallback() {
            @Override
            public void onSuccess() {
                Log.d("MainActivity", "Successfully started InBrain");
            }

            @Override
            public void onFail(String message) {
                Log.e("MainActivity", "Failed to start inBrain:" + message);
                Toast.makeText(MainActivity.this, // show some message or dialog to user
                        "Sorry, InBrain isn't supported on your device",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Handles new rewards. It may be updating used balance of notifying your API about new rewards.
     *
     * @param rewards
     */
    private void processRewards(List<Reward> rewards) {
        float total = 0;
        for (Reward reward : rewards) {
            total += reward.amount;
        }
        balance += total;
        getPreferences(MODE_PRIVATE).edit().putFloat(PREFERENCE_BALANCE, balance).apply();
        balanceText.setText(String.valueOf(balance));
        if (rewards.isEmpty()) {
            Toast.makeText(MainActivity.this,
                    "You have no new rewards!",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(MainActivity.this,
                    String.format(Locale.getDefault(), "You have received %d new rewards for a total of %.1f %s!", rewards.size(), total, rewards.get(0).currency),
                    Toast.LENGTH_LONG).show();
        }
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
            public void onFailToLoadRewards(Throwable t) {
                Log.e("MainActivity", "onFailToLoadRewards:" + t);
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

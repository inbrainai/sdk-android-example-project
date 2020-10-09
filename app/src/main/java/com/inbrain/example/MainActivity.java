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
import com.inbrain.sdk.callback.SurveysAvailableCallback;
import com.inbrain.sdk.model.Reward;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final String PREFERENCE_BALANCE = "Balance";
    private static final String API_CLIENT_ID = "{clientIDhere}"; // your client id obtained by your account manager
    private static final String API_SECRET = "{clientSecrethere}"; // your client secret obtained by your account manager

    private float balance;
    private TextView balanceText;
    private InBrainCallback callback = new InBrainCallback() {
        @Override
        public void surveysClosed() {
            // inBrain screen is closed & user get back to your application
        }

        @Override
        public void surveysClosedFromPage() {
            // inBrain screen is closed from web page & user get back to your application
        }

        @Override
        public boolean didReceiveInBrainRewards(List<Reward> rewards) {
            // pay attention, this method can be called during SDK usage while your activity is in 'onStop' state
            Log.d("MainActivity", "Received rewards in new rewards callback:" + Arrays.toString(rewards.toArray()));
            return true; // in case true rewards will be confirmed automatically
            // in case return false you need to confirm it manually using confirmRewards method
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        balance = getPreferences(MODE_PRIVATE).getFloat(PREFERENCE_BALANCE, 0);

        balanceText = findViewById(R.id.balanceTv);
        balanceText.setText(String.valueOf(balance));
        initInBrain();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getRewards();
    }

    @Override
    protected void onDestroy() {
        InBrain.getInstance().removeCallback(callback); // unsubscribe from events & rewards update
        super.onDestroy();
    }

    private void initInBrain() {
        boolean isS2S = false;
        String userID = "1234-1234-1234-1234"; // Custom app user id goes here!
        InBrain.getInstance().setInBrain(this, API_CLIENT_ID, API_SECRET, isS2S, userID);
        InBrain.getInstance().addCallback(callback); // subscribe to events and new rewards

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

        // optional for obtaining device id for testing
//        String deviceId = InBrain.getInstance().getDeviceId();
//        Log.d("MainActivity", "deviceId:" + deviceId);

        InBrain.getInstance().areSurveysAvailable(this, new SurveysAvailableCallback() {
            @Override
            public void onSurveysAvailable(final boolean available) {
                Log.d("MainActivity", "Surveys available:" + available);
            }
        });
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
                return true; // in case true rewards will be confirmed automatically
                // in case return false you need to confirm it manually using confirmRewards method
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

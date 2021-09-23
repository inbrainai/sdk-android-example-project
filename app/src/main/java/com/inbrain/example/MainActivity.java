package com.inbrain.example;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.inbrain.sdk.InBrain;
import com.inbrain.sdk.callback.GetRewardsCallback;
import com.inbrain.sdk.callback.InBrainCallback;
import com.inbrain.sdk.callback.StartSurveysCallback;
import com.inbrain.sdk.config.StatusBarConfig;
import com.inbrain.sdk.config.ToolBarConfig;
import com.inbrain.sdk.model.Reward;
import com.inbrain.sdk.model.Survey;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final String PREFERENCE_BALANCE = "Balance";
    private static final String API_CLIENT_ID = "{clientIDhere}"; // your client id obtained by your account manager
    private static final String API_SECRET = "{clientSecrethere}"; // your client secret obtained by your account manager
    private static final String PLACEMENT_ID = "{placementIDhere}"; // a specific placementId on which you wanna get/show surveys based

    private float balance;
    private TextView balanceText;
    private final InBrainCallback callback = new InBrainCallback() {
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
    private List<Survey> nativeSurveys;
    private List<Survey> nativeSurveysInPlace;

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

        // request native surveys after user comes back from inbrain, it may be updated
        InBrain.getInstance().getNativeSurveys(surveyList -> {
            nativeSurveys = surveyList;
            Log.d("MainActivity", "Native surveys available count:" + surveyList.size());
        });

        // You can also pass in an optional parameter "placementId" to get a location-basic filtered list of native surveys
        InBrain.getInstance().getNativeSurveys(PLACEMENT_ID, surveyList -> {
            nativeSurveysInPlace = surveyList;
            Log.d("MainActivity", "Native surveys available count in this place:" + surveyList.size());
        });
    }

    @Override
    protected void onDestroy() {
        InBrain.getInstance().removeCallback(callback); // unsubscribe from events & rewards update
        super.onDestroy();
    }

    private void initInBrain() {
        boolean isS2S = false;
        String userID = "{userIDhere}"; // Custom app user id goes here!
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

        InBrain.getInstance().areSurveysAvailable(this, available -> Log.d("MainActivity", "Surveys available:" + available));
    }

    private void applyUiCustomization() {
        ToolBarConfig toolBarConfig = new ToolBarConfig();
        toolBarConfig.setTitle(getString(R.string.app_name)); // set title

        boolean useResourceId = false;
        if (useResourceId) {
            toolBarConfig.setToolbarColorResId(R.color.colorAccent) // set toolbar color with status bar
                    .setBackButtonColorResId(android.R.color.white) // set icon color
                    .setTitleColorResId(android.R.color.white); //  set toolbar text
        } else {
            toolBarConfig.setToolbarColor(getResources().getColor(R.color.colorAccent))
                    .setBackButtonColor(getResources().getColor(android.R.color.white))
                    .setTitleColor(getResources().getColor(android.R.color.white));
        }
        toolBarConfig.setElevationEnabled(false);
        InBrain.getInstance().setToolbarConfig(toolBarConfig);

        StatusBarConfig statusBarConfig = new StatusBarConfig();
        if (useResourceId) {
            statusBarConfig.setStatusBarColorResId(android.R.color.white)
                    .setStatusBarIconsLight(false);
        } else {
            statusBarConfig.setStatusBarColor(getResources().getColor(android.R.color.white))
                    .setStatusBarIconsLight(false);
        }
        InBrain.getInstance().setStatusBarConfig(statusBarConfig);
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

    public void showNativeSurveys(View view) {
        if (nativeSurveys == null || nativeSurveys.isEmpty()) {
            Toast.makeText(MainActivity.this,
                    "Sorry, no native surveys",
                    Toast.LENGTH_LONG).show();
            return;
        }
        findViewById(R.id.show_native_surveys_button).setVisibility(View.GONE);
        RecyclerView recyclerView = findViewById(R.id.native_surveys_recycler_view);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        NativeSurveysAdapter adapter = new NativeSurveysAdapter(this::showNativeSurvey, nativeSurveys /* nativeSurveysInPlace */);
        recyclerView.setAdapter(adapter);
    }

    private void showNativeSurvey(String surveyId) {
        InBrain.getInstance().showNativeSurveyWith(this, surveyId, new StartSurveysCallback() {
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

        // You can also pass in a specific placement_id to the Survey Wall like below
//        InBrain.getInstance().showNativeSurveyWith(this, surveyId, PLACEMENT_ID, new StartSurveysCallback() {
//            @Override
//            public void onSuccess() {
//                Log.d("MainActivity", "Successfully started InBrain, placement_id : " + PLACEMENT_ID);
//            }
//
//            @Override
//            public void onFail(String message) {
//                Log.e("MainActivity", "Failed to start inBrain:" + message);
//                Toast.makeText(MainActivity.this, // show some message or dialog to user
//                        "Sorry, InBrain isn't supported on your device",
//                        Toast.LENGTH_LONG).show();
//            }
//        });
    }

    /**
     * Handles new rewards. It may be updating used balance of notifying your API about new rewards.
     *
     * @param rewards a list of rewards to be processed
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

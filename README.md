#  inBrain Android SDK ![API](https://img.shields.io/badge/API-16%2B-brightgreen.svg?style=flat) 
## Integration
Integration is easiest with gradle dependency. Just add the following implementation line into dependencies section of your build.gradle file (app module).
```groovy
dependencies {  
    implementation 'com.github.inbrainai:sdk-android:0.1.6'  
}
```
## Configure inBrain SDK
First of all you need to initialize the SDK using the following code in the Application class:
```
public void onCreate() {
    super.onCreate();
    InBrain.getInstance().init(this, CLIENT_ID, CLIENT_SECRET);
}
```

Here `CLIENT_ID` is your client ID obtained from your account manager, `CLIENT_SECRET` is your client secret obtained from your account manager. This should be done just once.

## Set user ID
`InBrain.getInstance().setAppUserId(USER_ID);` where `USER_ID` is the unique identifier of the current user. This should be done when you already know how to identify the current user. Usually that happens in the main activity of the app, after sign-up/sign-in process.

## Presenting the survey wall
In order to open inBrain survey wall, execute the following call:
```
InBrain.getInstance().showSurveys(activity);
```
This will open the survey wall in new activity. inBrain SDK will handle everything else. It will return control to last opened activity of your app after user leaves the survey wall.

## InBrainCallback
The callback here is a reward handling callback which is purely optional. You can add it in `InBrain.getInstance().addCallback(callback);` or ignore it and just get rewards manually whenever you need throughout the app. However if you want a single point where you could add virtual currency to user balance and update UI, the addCallback is your choice.

```
InBrainCallback callback = new InBrainCallback() {
        @Override
        public void onClosed() {
        // inBrain screen is closed & user get back to your application
        }

        // Notifies your application about new rewards.
        @Override  
        public boolean handleRewards(List<Reward> rewards) {
            // Process received rewards here
            return true; // if processed successfully, false otherwise
        }  
    };
```
Received rewards will be passed to `handleRewards(List<Reward> rewards)` method of your callback.
`handleRewards` method of the callback should return true if received rewards were processed successfully (added to user's balance), false otherwise. If you return false, the same rewards will be passed to you again later so you could retry the operation.

Rewards that have been processed need to be confirmed with InBrain so it would not pass it back to the app again and again. You have two options to confirm rewards:

 * Simple synchronous method: just `return true` from `handleRewards` in the callback. This will confirm rewards instantly.
 * Advanced asynchronous method: `return false` from `handleRewards` method. Later, after you have processed the rewards, make a call to `confirmRewards` method. The call will look like this: `InBrain.getInstance().confirmRewards(list)`, where `list` is the list of rewards you want to confirm. All unconfirmed rewards will be passed again to `handleRewards` on subsequent calls.
 
 **If you use advanced method, make sure to confirm received rewards to avoid duplicate rewards!**

`onClosed` method is called when InBrain screen is closed & user returns back to your app.

In order to unsubscribe from inBrain events, you need to call `InBrain.getInstance().removeCallback(callback);`. You better do it in `Activity` `onDestroy()` method.

```
    @Override
    protected void onDestroy() {
        InBrain.getInstance().removeCallback(callback); // unsubscribe from events
        super.onDestroy();
    }
```

 **Don't forget to remove callback, otherwise it may call memory leak!**

## Request rewards manually
Once in a while new rewards will be passed to your app through `handleRewards` calls of your `InBrainCallback` instances. This mechanism is very reliable and it is usually enough for most apps. However if you want to check for new rewards at a certain time, there is a special method which invokes manual check:
```java
InBrain.getInstance().getRewards(new GetRewardsCallback() {  
    @Override  
    public boolean handleRewards(List<Reward> rewards) {  
        // Process rewards
        return true; // or return false and confirm your rewards manualy using InBrain.getInstance().confirmRewards(rewards);  
    }  
  
    @Override  
    public void onFailToLoadRewards(int errorCode) {  
        // Handle the fail
    }  
});
```

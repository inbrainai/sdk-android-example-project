#  inBrain Android SDK ![API](https://img.shields.io/badge/API-16%2B-brightgreen.svg?style=flat) 
## Integration
Integration is easiest with gradle dependency. In order to do that you need to add two pieces of code to your project. First goes into root build.gradle file. You need to add jitpack.io repository into list of repositories for all projects like this:
```groovy
allprojects {
    repositories {
        // other repositories here
        maven { url 'https://jitpack.io' }
    }
}
```
After that you just need to add the actual SDK dependency into build.gradle of the app module:
```groovy
dependencies {  
    // other dependencies here
    implementation 'com.github.inbrainai:sdk-android:0.1.19'  
}
```
That is all! After re-syncing the project from gradle files you will be able to start using inBrain SDK.

## Configure inBrain SDK
First of all you need to initialize the SDK using the following code in the Application class:
```
public void onCreate() {
    super.onCreate();
    InBrain.getInstance().init(this, CLIENT_ID, CLIENT_SECRET);
}
```

Here `CLIENT_ID` is your client ID obtained from your account manager, `CLIENT_SECRET` is your client secret obtained from your account manager. This should be done just once.

If you have optional parameter `SESSION_UID`, you can set it using `setSessionUid` method:
```
InBrain.getInstance().setSessionUid(SESSION_UID);
```

If you have optional data points, you can set it using `setDataPoints` method:
```
InBrain.getInstance().setDataPoints(DATA_POINTS);
```
Where `DATA_POINTS` is `HashMap<String, String>` field. Please check sample for more info.

## Set user ID
`InBrain.getInstance().setAppUserId(USER_ID);` where `USER_ID` is the unique identifier of the current user. This should be done when you already know how to identify the current user. Usually that happens in the main activity of the app, after sign-up/sign-in process.

## Presenting the survey wall
Minimum supported system WebView version for surveys is `51.0.2704.90`, it is default for Android 7.0, however older devices may have system update which updates WebView.
In order to open inBrain survey wall, execute the following call:
```
InBrain.getInstance().showSurveys(activity, new StartSurveysCallback() {
    @Override
    public void onSuccess() {
		// inBrain is successfully started
    }

    @Override
    public void onFail(String message) {
		// failed to start inBrain
    }
});
```
This will open the survey wall in new activity. inBrain SDK will handle everything else. It will return control to last opened activity of your app after user leaves the survey wall.

## InBrainCallback
The callback here is a reward handling callback which is purely optional. You can add it using `addCallback` method. It can be done in `onResume` method of an activity. It is not necessary to add a callback to handle rewards, you can just get rewards manually whenever you need throughout the app using `getRewards` method (see below). However if you want a single point where you could add virtual currency to user balance and update UI, `addCallback` is your choice.

```
InBrainCallback callback = new InBrainCallback() {
        @Override
        public void onClosed() {
        // inBrain screen is closed & user get back to your application
        }
		
	@Override
        public void onClosedFromPage() {
        // inBrain screen is closed from web page & user get back to your application
        }

        // Notifies your application about new rewards.
        @Override  
        public boolean handleRewards(List<Reward> rewards) {
            // Process received rewards here
            return true; // if processed successfully, false otherwise
        }  
    };
```
Received rewards will be passed to `handleRewards(List<Reward> rewards)` method of the callback. Rewards that have been processed need to be confirmed with inBrain so it would not pass it back to the app repeatedly. You have two options to confirm rewards:

 * Simple synchronous method: just `return true` from `handleRewards` in the callback. This will confirm rewards instantly.
 * Advanced asynchronous method: `return false` from `handleRewards` method. Later, after you have processed the rewards, make a call to `confirmRewards` method. The call will look like this: `InBrain.getInstance().confirmRewards(list)`, where `list` is the list of rewards you want to confirm. All unconfirmed rewards will be passed again to `handleRewards` on subsequent calls.
 
 **If you use advanced method, make sure to confirm received rewards to avoid duplicate rewards!**

`onClosed` method is called when InBrain screen is closed & user returns back to your app.

In order to unsubscribe from inBrain events, you need to call `InBrain.getInstance().removeCallback(callback)`. If you added a callback in `onResume`, you should remove you callback in `onPause` method of an activity like this:

```
    @Override
    protected void onPause() {
        InBrain.getInstance().removeCallback(callback); // unsubscribe from events
        super.onPause();
    }
```

 **Don't forget to remove callback, otherwise it may call memory leak!**

## Request rewards manually
If you want to force check for new rewards at a certain time, there is a special method which invokes manual rewards check:
```java
InBrain.getInstance().getRewards(new GetRewardsCallback() {  
    @Override  
    public boolean handleRewards(List<Reward> rewards) {  
        // Process rewards
        return true; // or return false and confirm your rewards manualy using InBrain.getInstance().confirmRewards(rewards);  
    }  
  
    @Override  
    public void onFailToLoadRewards(Throwable t) {  
        // Handle the fail, where t is an throwable that might help to investigate the error.
    }  
});
```

## UI customiztion
1. Toolbar and status bar color

```java
InBrain.getInstance().setToolbarColor(getResources().getColor(R.color.your_color));
```
Also you may pass color's resource id:
```java
InBrain.getInstance().setToolbarColorResId(R.color.your_color); 
```
2. Title text's & icon's color

```java
InBrain.getInstance().setTitleTextColor(getResources().getColor(R.color.your_color));
```
Also you may pass color's resource id:
```java
InBrain.getInstance().setTitleTextColorResId(R.color.your_color); 
```
3. Title

```java
InBrain.getInstance().setToolbarTitle(getString(R.string.app_name));
```
If you want to leave toolbar's text empty, just pass empty `String` to it.
```java
InBrain.getInstance().setToolbarTitle("");
```
## Language
By default, device's locale's language will be used. If you want to change it, you need to call
```java
InBrain.getInstance().setLanguage("en-fr");
```
Accepted language format: `"en-us"`, `"en-fr"` ...

## Staging mode
For debugging purposes you may enable staging servers. You can make it by setting
```java
InBrain.getInstance().setStagingMode(true);
```
right after `init()` method. Please don't forget to change your credentials!

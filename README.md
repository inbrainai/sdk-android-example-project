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
    implementation 'com.github.inbrainai:sdk-android:1.0.17'  
}
```
That is all! After re-syncing the project from gradle files you will be able to start using inBrain SDK.

## Configure inBrain SDK
First of all you need to initialize the SDK using the following code in the Activity class:
```
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    boolean isS2S = false;
    InBrain.getInstance().setInBrain(this, API_CLIENT_ID, API_SECRET, isS2S, USER_ID);
}
```

Here `API_CLIENT_ID` is your client ID obtained from your account manager, `API_SECRET` is your client secret obtained from your account manager. This should be done just once.
Based on your app's architecture, whether the rewards will be delivered via in-app callback or Server-to-Server (S2S) callback. This can be configured using `isS2S` flag.
`USER_ID` is the unique identifier of the current user.
 
If you have optional parameter `SESSION_ID` & `DATA_OPTIONS`, you can set it using `setInBrainValuesFor` method:
```
InBrain.getInstance().setInBrainValuesFor(SESSION_ID, DATA_OPTIONS);
```

Where `DATA_OPTIONS` is `HashMap<String, String>` field. Please check sample for more info.

# Usage

## Presenting the Regular surveys
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

## Native surveys

There a few steps to use InBrain Native Surveys:
1) Fetch Native Surveys using **InBrain.getInstance().getNativeSurveys(callback)** or **InBrain.getInstance().getNativeSurveys(placementId, callback)** function;
2) Receive Native Surveys using **nativeSurveysReceived(List<Survey> surveyList)** function of `GetNativeSurveysCallback` and show them to the user;
3) Once user choosed some survey - present InBrain WebView using **InBrain.getInstance().showNativeSurveyWith(context, surveyId, new StartSurveysCallback())** or **InBrain.getInstance().showNativeSurveyWith(context, surveyId, placementId, new StartSurveysCallback())** function.

**Please, note:** SDK provides new portion of Native Surveys after user completed some of Native Surveys, received before.

## InBrainCallback
The callback here is a sdk events & reward handling callback which is purely optional. You can add it using `addCallback` method. It can be done in `onCreate` method of an activity.

```
InBrainCallback callback = new InBrainCallback() {
        @Override
        public void surveysClosed() {
        // inBrain screen is closed & user get back to your application
        }
		
	@Override
        public void surveysClosedFromPage() {
        // inBrain screen is closed from web page & user get back to your application
        }

        // Notifies your application about new rewards.
        @Override  
        public boolean didReceiveInBrainRewards(List<Reward> rewards) {
            // Process received rewards here
            return true; // if processed successfully, false otherwise
        }  
    };
```
Received rewards will be passed to `didReceiveInBrainRewards(List<Reward> rewards)` method of the callback. Rewards that have been processed need to be confirmed with inBrain so it would not pass it back to the app repeatedly. You have two options to confirm rewards:

 * Simple synchronous method: just `return true` from `didReceiveInBrainRewards` in the callback. This will confirm rewards instantly.
 * Advanced asynchronous method: `return false` from `didReceiveInBrainRewards` method. Later, after you have processed the rewards, make a call to `confirmRewards` method. The call will look like this: `InBrain.getInstance().confirmRewards(list)`, where `list` is the list of rewards you want to confirm. All unconfirmed rewards will be passed again to `didReceiveInBrainRewards` on subsequent calls.
 
 **If you use advanced method, make sure to confirm received rewards to avoid duplicate rewards!**

`surveysClosed` method is called when InBrain screen is closed & user returns back to your app.

In order to unsubscribe from inBrain events, you need to call `InBrain.getInstance().removeCallback(callback)`. If you added a callback in `onCreate`, you should remove you callback in `onDestroy` method of an activity like this:

```
    @Override
    protected void onDestroy() {
        InBrain.getInstance().removeCallback(callback); // unsubscribe from events
        super.onDestroy();
    }
```

 **Don't forget to remove callback, otherwise it may cause a memory leak!**

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
**1. Toolbar:**

```java
ToolBarConfig toolBarConfig = new ToolBarConfig();
```
Set toolbar color:

```java
toolBarConfig.setToolbarColor(getResources().getColor(R.color.your_color));
```
Also you may pass color's resource id:
```java
toolBarConfig.setToolbarColorResId(R.color.your_color); 
```
Title:

```java
toolBarConfig.setToolbarTitle(getString(R.string.app_name));
```
If you want to leave toolbar's text empty, just pass empty `String` to it.
```java
toolBarConfig.setToolbarTitle("");
```
Title text's color:

```java
toolBarConfig.setTitleColor(getResources().getColor(R.color.your_color));
```
Also you may pass color's resource id:
```java
toolBarConfig.setTitleColorResId(R.color.your_color); 
```

Back icon's color:

```java
toolBarConfig.setBackButtonColor(getResources().getColor(R.color.your_color));
```
Also you may pass color's resource id:
```java
toolBarConfig.setBackButtonColorResId(R.color.your_color); 
```

If you want to enable elevation for toolbar, use:
```java
toolBarConfig.setElevationEnabled(true);
```

Finally, set `toolBarConfig` to inBrain:
```java
InBrain.getInstance().setToolbarConfig(toolBarConfig);
```

**2. Status bar:**

```java
StatusBarConfig statusBarConfig = new StatusBarConfig();
```

Set status bar color:

```java
statusBarConfig.setStatusBarColor(getResources().getColor(R.color.your_color));
```
Also you may pass color's resource id:
```java
statusBarConfig.setStatusBarColorResId(R.color.your_color); 
```

By default, status bar icons' color will be white. If you need to use black status bar icons:
```java
statusBarConfig.setStatusBarIconsLight(false);
```

## Language
By default, device's locale's language will be used. If you want to change it, you need to call
```java
InBrain.getInstance().setLanguage("en-us");
```
Accepted languages: `"en-us"`, `"fr-fr"`, `"en-gb"`, `"en-ca"`, `"en-au"`, `"en-in"`

## Getting device id
In order to add your device for test, you need to obtain it's id. You can do it by calling after `init()` method
```java
String deviceId = InBrain.getInstance().getDeviceId();
```

## Check for surveys availability
You can check if surveys are available for user. You can do it by calling after `init()` method
```java
InBrain.getInstance().areSurveysAvailable(this, new SurveysAvailableCallback() {
	@Override
	public void onSurveysAvailable(final boolean available) {
		Log.d("MainActivity", "Surveys available:" + available);
	}
});
```

[CHANGELOG](https://github.com/inbrainai/sdk-android-example-project/blob/master/CHANGELOG.md)

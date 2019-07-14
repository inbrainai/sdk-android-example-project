#  inBrain-Android ![API](https://img.shields.io/badge/API-16%2B-brightgreen.svg?style=flat) 
## Download
```groovy
dependencies {  
    implementation 'com.github.inbrainai:sdk-android:0.1.4'  
}
```
## Configure your SDK:
Before showing inBrain screen, you need to initialize it by:
`InBrain.getInstance().init(this, CLIENT_ID, CLIENT_SECRET, callback);`
where:
`CLIENT_ID` - Your client ID obtained by your account manager
`CLIENT_SECRET` - Your client secret obtained by your account manager
You also needed to initialize `callback` or make your `Activity` extends `InBrainCallback`, in this case you should use `InBrain.getInstance().init(this, CLIENT_ID, CLIENT_SECRET, this);`
```java
InBrainCallback callback = new InBrainCallback() {  
    @Override  
  public void onAdClosed() {  
  
    }  
    
  //Notifies your application about new rewards.
    @Override  
  public boolean handleRewards(List<Reward> rewards) {
		// process rewards  
        return true;  
    }  
};
```
## Presenting the InBrain: 
To present the inBrain Screen, execute 
 `InBrain.getInstance().showSurveys(this);`
 This will launch screen with inBrain.
## InBrainCallback
Received rewards will be passed to `handleRewards(List<Reward> rewards)` method.
Rewards that have been processed need to be conﬁrmed with InBrain. You have 2 options to  do this:
 * `return true;` inside `handleRewards` method - this will confirm rewards automatically.
 * Confirm rewards manually using `InBrain.getInstance().confirmRewards(list);`, in this case you need to `return false;` inside `handleRewards` method.
 
 `onAdClosed`- is called, when InBrain screen is  closed & user gets back to your app.
## Request rewards manually
Rewards will be passed automatically to your `callback`, but in case you want to request received rewards for user manually, you should execute:
```java
InBrain.getInstance().getRewards(new GetRewardsCallback() {  
    @Override  
  public boolean handleRewards(List<Reward> rewards) {  
        // process rewards
        // return true; or confirm your rewards manualy using InBrain.getInstance().confirmRewards(rewards);  
  }  
  
    @Override  
  public void onFailToLoadRewards(int errorCode) {  
        if (errorCode == GetRewardsCallback.ERROR_CODE_UNKNOWN) {  
        }  
    }  
});
```
**Don't forget to confirm received rewards to avoid duplicated rewards!**

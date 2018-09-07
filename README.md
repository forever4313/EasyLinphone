# EasyLinphone
EasyLinphone make it easy to import Linphone Android SDK to your project.

[中文文档](https://github.com/forever4313/EasyLinphone/blob/master/README_CN.md)

## Import
Download the EasyLinphone aar at [Latest release](https://github.com/xcy396/EasyLinphone/releases), and the Linphone Android SDK at [Linphone offical website](http://www.linphone.org/technical-corner/liblinphone/downloads). 

Then
copy EasyLinPhone aar and Linphone aar to your project's `libs` folder, and import them in your app buid.gradle as below:

```groovy
android {
	...
	repositories {
	    flatDir {
	        dirs 'libs' //this way we can find the .aar file in libs folder
	    }
	}
}

dependencies {
	...
	compile(name:'liblinphone-android-sdk', ext:'aar')
	compile(name:'easylinphone-release', ext:'aar')
}
```

## Usage
### 1. Init LinphoneService

```java
// Start service
EasyLinphone.startService(mContext);
// Add callback
EasyLinphone.addCallback(new RegistrationCallback() {
    @Override
    public void registrationOk() {
        super.registrationOk();
        // do something
    }

    @Override
    public void registrationFailed() {
        super.registrationFailed();
        // do something
    }
}, new PhoneCallback() {
    @Override
    public void incomingCall(LinphoneCall linphoneCall) {
        super.incomingCall(linphoneCall);
        // do something
    }

    @Override
    public void callConnected() {
        super.callConnected();
        // do something
    }

    @Override
    public void callEnd() {
        super.callEnd();
        // do something
    }
});
```

You can add registrationCallback and phoneCallback in different place in your project, this depending on your logic.

### 2. Login

```java
// Configure sip account
EasyLinphone.setAccount("1003", "123456", "192.168.9.60");
// Register to sip server
EasyLinphone.login();
```

### 3. Manage the voice call

```java
// Make a call
EasyLinphone.callTo("1001");
// Hang up the current call
EasyLinphone.hangUp();
// Answer the current call
EasyLinphone.acceptCall();
// Toggle the mute function
EasyLinphone.toggleMicro(!EasyLinphone.getLC().isMicMuted());
// Toggle the handsfree function
EasyLinphone.toggleSpeaker(!EasyLinphone.getLC().isSpeakerEnabled());
```

### 4. Manage the video call

Your video call activity or fragment may like below:

```java
public class VideoActivity extends AppCompatActivity {
    @BindView(R.id.video_rendering) SurfaceView mRenderingView;
    @BindView(R.id.video_preview) SurfaceView mPreviewView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        ButterKnife.bind(this);
        EasyLinphone.setAndroidVideoWindow(new SurfaceView[]{mRenderingView}, new SurfaceView[]{mPreviewView});
    }

    @Override
    protected void onResume() {
        super.onResume();
        EasyLinphone.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        EasyLinphone.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EasyLinphone.onDestroy();
    }

    @OnClick(R.id.video_hang)
    public void hang() {
        EasyLinphone.hangUp();
        finish();
    }
}
```

That's all, Enjoy it!

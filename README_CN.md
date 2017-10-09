# EasyLinphone
EasyLinphone 可以帮助你在项目中很轻松的使用 Linphone Android SDK。

[English document](https://github.com/xcy396/EasyLinphone/blob/master/README.md)

## 导入
* 在 [Release](https://github.com/xcy396/EasyLinphone/releases) 页面下载最新的 EasyLinphone aar 包
* 在 [Linphone 官网](http://www.linphone.org/technical-corner/liblinphone/downloads) 下载最新的 LinPhone Android aar 包
* 将刚才下载的两个 aar 包放到项目 app 的 libs 文件夹下，在 app 的 build.gradle 添加以下引用：

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

## 使用
### 1. 初始化 LinphoneService

```java
// 开启服务
EasyLinphone.startService(mContext);
// 添加登录状态回调和通话回调
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

可以根据实际情况，在不同的地方分别添加登录状态回调和通话状态回调。

### 2. 登录

```java
// 配置 Sip 账户信息
EasyLinphone.setAccount("1003", "123456", "192.168.9.60");
// 注册到 Sip 服务器
EasyLinphone.login();
```

### 3. 管理音频通话

```java
// 呼叫指定号码
EasyLinphone.callTo("1001");
// 挂断当前通话
EasyLinphone.hangUp();
// 接听当前来电
EasyLinphone.acceptCall();
// 切换静音
EasyLinphone.toggleMicro(!EasyLinphone.getLC().isMicMuted());
// 切换免提
EasyLinphone.toggleSpeaker(!EasyLinphone.getLC().isSpeakerEnabled());
```

### 4. 管理视频通话

视频通话示例如下：

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

可以看到，通过本库可以很简单的使用 Linphone Android SDK。代码质量不高，还有很多疵漏之处，还请大家多多指教。
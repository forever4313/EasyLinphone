package com.xuchongyang.easyphone;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.xuchongyang.easyphone.callback.PhoneCallback;
import com.xuchongyang.easyphone.callback.RegistrationCallback;

import org.linphone.core.LinphoneAddress;
import org.linphone.core.LinphoneAuthInfo;
import org.linphone.core.LinphoneCall;
import org.linphone.core.LinphoneCallStats;
import org.linphone.core.LinphoneChatMessage;
import org.linphone.core.LinphoneChatRoom;
import org.linphone.core.LinphoneContent;
import org.linphone.core.LinphoneCore;
import org.linphone.core.LinphoneCoreFactoryImpl;
import org.linphone.core.LinphoneCoreListener;
import org.linphone.core.LinphoneEvent;
import org.linphone.core.LinphoneFriend;
import org.linphone.core.LinphoneFriendList;
import org.linphone.core.LinphoneInfoMessage;
import org.linphone.core.LinphoneProxyConfig;
import org.linphone.core.PublishState;
import org.linphone.core.SubscriptionState;

import java.nio.ByteBuffer;


/**
 * Created by Mark Xu on 17/3/13.
 * LinphoneService
 */

public class LinphoneService extends Service implements LinphoneCoreListener {
    private static final String TAG = "LinphoneService";
    private PendingIntent mKeepAlivePendingIntent;
    private static LinphoneService instance;
    private static PhoneCallback sPhoneCallback;
    private static RegistrationCallback sRegistrationCallback;
//    private PowerManager.WakeLock mWakeLock;

    public static boolean isReady() {
        return instance != null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LinphoneCoreFactoryImpl.instance();
        LinphoneManager.createAndStart(LinphoneService.this);
        instance = this;
        Intent intent = new Intent(this, KeepAliveHandler.class);
        mKeepAlivePendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        ((AlarmManager)this.getSystemService(Context.ALARM_SERVICE)).setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + 60000, 60000, mKeepAlivePendingIntent);

//        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
//        mWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Linphone");
//        mWakeLock.acquire();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "LinphoneService onDestroy execute");
        removeAllCallback();
        LinphoneManager.getLc().destroy();
        LinphoneManager.destroy();
//        if (mWakeLock != null) {
//            mWakeLock.release();
//            mWakeLock = null;
//        }
        ((AlarmManager)this.getSystemService(Context.ALARM_SERVICE)).cancel(mKeepAlivePendingIntent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static void addPhoneCallback(PhoneCallback phoneCallback) {
        if (sPhoneCallback == null) {
            sPhoneCallback = phoneCallback;
        }
    }

    public static void removePhoneCallback() {
        if (sPhoneCallback != null) {
            sPhoneCallback = null;
        }
    }

    public static void addRegistrationCallback(RegistrationCallback registrationCallback) {
        if (sRegistrationCallback == null) {
            sRegistrationCallback = registrationCallback;
        }
    }

    public static void removeRegistrationCallback() {
        if (sRegistrationCallback != null) {
            sRegistrationCallback = null;
        }
    }

    public void removeAllCallback() {
        removePhoneCallback();
        removeRegistrationCallback();
    }

    @Override
    public void registrationState(LinphoneCore linphoneCore, LinphoneProxyConfig linphoneProxyConfig,
                                  LinphoneCore.RegistrationState registrationState, String s) {
        String state = registrationState.toString();
        if (sRegistrationCallback != null && state.equals(LinphoneCore.RegistrationState.RegistrationNone.toString())) {
            sRegistrationCallback.registrationNone();
        } else if (state.equals(LinphoneCore.RegistrationState.RegistrationProgress.toString())) {
            sRegistrationCallback.registrationProgress();
        } else if (state.equals(LinphoneCore.RegistrationState.RegistrationOk.toString())) {
            sRegistrationCallback.registrationOk();
        } else if (state.equals(LinphoneCore.RegistrationState.RegistrationCleared.toString())) {
            sRegistrationCallback.registrationCleared();
        } else if (state.equals(LinphoneCore.RegistrationState.RegistrationFailed.toString())) {
            sRegistrationCallback.registrationFailed();
        }
     }

    @Override
    public void callState(final LinphoneCore linphoneCore, final LinphoneCall linphoneCall, LinphoneCall.State state, String s) {
        Log.e(TAG, "callState: " + state.toString());
        if (state == LinphoneCall.State.IncomingReceived && sPhoneCallback != null) {
            sPhoneCallback.incomingCall(linphoneCall);
        }

        if (state == LinphoneCall.State.OutgoingInit && sPhoneCallback != null) {
            sPhoneCallback.outgoingInit();
        }

        if (state == LinphoneCall.State.Connected && sPhoneCallback != null) {
            sPhoneCallback.callConnected();
        }

        if (state == LinphoneCall.State.Error && sPhoneCallback != null) {
            sPhoneCallback.error();
        }

        if (state == LinphoneCall.State.CallEnd && sPhoneCallback != null) {
            sPhoneCallback.callEnd();
        }

        if (state == LinphoneCall.State.CallReleased && sPhoneCallback != null) {
            sPhoneCallback.callReleased();
        }
    }

    /**
     * 呼叫指定频道
     * @param channel 频道
     */
    private void callThroughMobile(String channel) {
//        mChannel = channel;
//        if (LinphoneManager.getLc().isIncall()) {
//            LinphoneUtils.getInstance().hangUp();
//            MediaUtils.stop();
//        }
//        SPUtils.save(this, "channel", mChannel);
//        callNowChannel();
    }

    /**
     * 呼叫当前所在频道
     */
    private void callNowChannel() {
//        if (!LinphoneManager.getLc().isIncall()) {
//            if (!mChannel.equals("")) {
//                PhoneBean phone = new PhoneBean();
//                phone.setUserName(mChannel);
//                phone.setHost("115.159.84.73");
//                LinphoneUtils.getInstance().startSingleCallingTo(phone);
//            }
//        }
    }

    @Override
    public void authInfoRequested(LinphoneCore linphoneCore, String s, String s1, String s2) {

    }

    @Override
    public void authenticationRequested(LinphoneCore linphoneCore, LinphoneAuthInfo linphoneAuthInfo, LinphoneCore.AuthMethod authMethod) {

    }

    @Override
    public void callStatsUpdated(LinphoneCore linphoneCore, LinphoneCall linphoneCall, LinphoneCallStats linphoneCallStats) {

    }

    @Override
    public void newSubscriptionRequest(LinphoneCore linphoneCore, LinphoneFriend linphoneFriend, String s) {

    }

    @Override
    public void notifyPresenceReceived(LinphoneCore linphoneCore, LinphoneFriend linphoneFriend) {

    }

    @Override
    public void dtmfReceived(LinphoneCore linphoneCore, LinphoneCall linphoneCall, int i) {

    }

    @Override
    public void notifyReceived(LinphoneCore linphoneCore, LinphoneCall linphoneCall, LinphoneAddress linphoneAddress, byte[] bytes) {

    }

    @Override
    public void transferState(LinphoneCore linphoneCore, LinphoneCall linphoneCall, LinphoneCall.State state) {

    }

    @Override
    public void infoReceived(LinphoneCore linphoneCore, LinphoneCall linphoneCall, LinphoneInfoMessage linphoneInfoMessage) {

    }

    @Override
    public void subscriptionStateChanged(LinphoneCore linphoneCore, LinphoneEvent linphoneEvent, SubscriptionState subscriptionState) {

    }

    @Override
    public void publishStateChanged(LinphoneCore linphoneCore, LinphoneEvent linphoneEvent, PublishState publishState) {

    }

    @Override
    public void show(LinphoneCore linphoneCore) {

    }

    @Override
    public void displayStatus(LinphoneCore linphoneCore, String s) {

    }

    @Override
    public void displayMessage(LinphoneCore linphoneCore, String s) {

    }

    @Override
    public void displayWarning(LinphoneCore linphoneCore, String s) {

    }

    @Override
    public void fileTransferProgressIndication(LinphoneCore linphoneCore, LinphoneChatMessage linphoneChatMessage, LinphoneContent linphoneContent, int i) {

    }

    @Override
    public void fileTransferRecv(LinphoneCore linphoneCore, LinphoneChatMessage linphoneChatMessage, LinphoneContent linphoneContent, byte[] bytes, int i) {

    }

    @Override
    public int fileTransferSend(LinphoneCore linphoneCore, LinphoneChatMessage linphoneChatMessage, LinphoneContent linphoneContent, ByteBuffer byteBuffer, int i) {
        return 0;
    }

    @Override
    public void callEncryptionChanged(LinphoneCore linphoneCore, LinphoneCall linphoneCall, boolean b, String s) {

    }

    @Override
    public void isComposingReceived(LinphoneCore linphoneCore, LinphoneChatRoom linphoneChatRoom) {

    }

    @Override
    public void ecCalibrationStatus(LinphoneCore linphoneCore, LinphoneCore.EcCalibratorStatus ecCalibratorStatus, int i, Object o) {

    }

    @Override
    public void globalState(LinphoneCore linphoneCore, LinphoneCore.GlobalState globalState, String s) {

    }

    @Override
    public void uploadProgressIndication(LinphoneCore linphoneCore, int i, int i1) {

    }

    @Override
    public void uploadStateChanged(LinphoneCore linphoneCore, LinphoneCore.LogCollectionUploadState logCollectionUploadState, String s) {

    }

    @Override
    public void friendListCreated(LinphoneCore linphoneCore, LinphoneFriendList linphoneFriendList) {

    }

    @Override
    public void friendListRemoved(LinphoneCore linphoneCore, LinphoneFriendList linphoneFriendList) {

    }

    @Override
    public void networkReachableChanged(LinphoneCore linphoneCore, boolean b) {

    }

    @Override
    public void messageReceived(LinphoneCore linphoneCore, LinphoneChatRoom linphoneChatRoom, LinphoneChatMessage linphoneChatMessage) {

    }

    @Override
    public void messageReceivedUnableToDecrypted(LinphoneCore linphoneCore, LinphoneChatRoom linphoneChatRoom, LinphoneChatMessage linphoneChatMessage) {

    }

    @Override
    public void notifyReceived(LinphoneCore linphoneCore, LinphoneEvent linphoneEvent, String s, LinphoneContent linphoneContent) {

    }

    @Override
    public void configuringStatus(LinphoneCore linphoneCore, LinphoneCore.RemoteProvisioningState remoteProvisioningState, String s) {

    }
}

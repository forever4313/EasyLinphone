package com.xuchongyang.easyphone;

import android.content.Context;
import android.content.Intent;

import com.xuchongyang.easyphone.callback.PhoneCallback;
import com.xuchongyang.easyphone.callback.RegistrationCallback;

import org.linphone.core.LinphoneCore;
import org.linphone.core.LinphoneCoreException;

/**
 * Created by Mark Xu on 2017/9/20.
 * Site: http://xuchongyang.com
 */

public class EasyLinphone {
    private static ServiceWaitThread mServiceWaitThread;
    private static String mUsername, mPassword, mServerIP;

    /**
     * 开启服务
     * @param context 上下文
     */
    public static void startService(Context context) {
        if (!LinphoneService.isReady()) {
            context.startService(new Intent(Intent.ACTION_MAIN).setClass(context, LinphoneService.class));
        }
    }

    /**
     * 设置 sip 账户信息
     * @param username sip 账户
     * @param password 密码
     * @param serverIP sip 服务器
     */
    public static void setAccount(String username, String password, String serverIP) {
        mUsername = username;
        mPassword = password;
        mServerIP = serverIP;
    }

    /**
     * 添加注册状态、通话状态回调
     * @param phoneCallback 通话回调
     * @param registrationCallback 注册状态回调
     */
    public static void addCallback(RegistrationCallback registrationCallback,
                                   PhoneCallback phoneCallback) {
        if (LinphoneService.isReady()) {
            LinphoneService.addRegistrationCallback(registrationCallback);
            LinphoneService.addPhoneCallback(phoneCallback);
        } else {
            mServiceWaitThread = new ServiceWaitThread(registrationCallback, phoneCallback);
            mServiceWaitThread.start();
        }
    }

    /**
     * 登录到 SIP 服务器
     */
    public static void login() {
        if (LinphoneService.isReady()) {
            loginToServer();
        } else {
            throw new RuntimeException("LinphoneService is not ready");
        }
    }

    /**
     * 呼叫指定号码
     * @param num 呼叫号码
     */
    public static void callTo(String num) {
        if (!LinphoneService.isReady() || !LinphoneManager.isInstanceiated()) {
            return;
        }
        if (!num.equals("")) {
            PhoneBean phone = new PhoneBean();
            phone.setUserName(num);
            phone.setHost(mServerIP);
            LinphoneUtils.getInstance().startSingleCallingTo(phone);
        }
    }

    /**
     * 接听来电
     */
    public static void acceptCall() {
        try {
            LinphoneManager.getLc().acceptCall(LinphoneManager.getLc().getCurrentCall());
        } catch (LinphoneCoreException e) {
            e.printStackTrace();
        }
    }

    /**
     * 挂断当前通话
     */
    public static void hangUp() {
        LinphoneUtils.getInstance().hangUp();
    }

    /**
     * 切换静音
     * @param isMicMuted 是否静音
     */
    public static void toggleMicro(boolean isMicMuted) {
        LinphoneUtils.getInstance().toggleMicro(isMicMuted);
    }

    /**
     * 切换免提
     * @param isSpeakerEnabled 是否免提
     */
    public static void toggleSpeaker(boolean isSpeakerEnabled) {
        LinphoneUtils.getInstance().toggleSpeaker(isSpeakerEnabled);
    }

    private static class ServiceWaitThread extends Thread {
        private PhoneCallback mPhoneCallback;
        private RegistrationCallback mRegistrationCallback;

        ServiceWaitThread(RegistrationCallback registrationCallback, PhoneCallback phoneCallback) {
            mRegistrationCallback = registrationCallback;
            mPhoneCallback = phoneCallback;
        }

        @Override
        public void run() {
            super.run();
            while (!LinphoneService.isReady()) {
                try {
                    sleep(80);
                } catch (InterruptedException e) {
                    throw new RuntimeException("waiting thread sleep() has been interrupted");
                }
            }
            LinphoneService.addPhoneCallback(mPhoneCallback);
            LinphoneService.addRegistrationCallback(mRegistrationCallback);
            mServiceWaitThread = null;
        }
    }

    /**
     * 登录 SIP 服务器
     */
    private static void loginToServer() {
        try {
            if (mUsername == null || mPassword == null || mServerIP == null) {
                throw new RuntimeException("The account is not configure.");
            }
            LinphoneUtils.getInstance().registerUserAuth(mUsername, mPassword, mServerIP);
        } catch (LinphoneCoreException e) {
            e.printStackTrace();
        }
    }

    public static LinphoneCore getLC() {
        return LinphoneManager.getLc();
    }
}

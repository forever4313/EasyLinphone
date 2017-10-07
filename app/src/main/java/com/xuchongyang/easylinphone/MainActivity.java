package com.xuchongyang.easylinphone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xuchongyang.easyphone.EasyLinphone;
import com.xuchongyang.easyphone.callback.PhoneCallback;

import org.linphone.core.LinphoneCall;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @BindView(R.id.dial_num) EditText mDialNum;
    @BindView(R.id.hang_up) Button mHangUp;
    @BindView(R.id.accept_call) Button mCallIn;
    @BindView(R.id.toggle_speaker) Button mToggleSpeaker;
    @BindView(R.id.toggle_mute) Button mToggleMute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        EasyLinphone.addCallback(null, new PhoneCallback() {
            @Override
            public void incomingCall(LinphoneCall linphoneCall) {
                super.incomingCall(linphoneCall);
                Log.e(TAG, "incomingCall: ");
                // 开启铃声免提
                EasyLinphone.toggleSpeaker(true);
                mCallIn.setVisibility(View.VISIBLE);
                mHangUp.setVisibility(View.VISIBLE);
            }

            @Override
            public void outgoingInit() {
                super.outgoingInit();
                mHangUp.setVisibility(View.VISIBLE);
            }

            @Override
            public void callConnected() {
                super.callConnected();
                Log.e(TAG, "callConnected: ");
                // 视频通话默认免提，语音通话默认非免提
                EasyLinphone.toggleSpeaker(EasyLinphone.getVideoEnabled());
                // 所有通话默认非静音
                EasyLinphone.toggleMicro(false);
                mCallIn.setVisibility(View.GONE);
                mToggleSpeaker.setVisibility(View.VISIBLE);
                mToggleMute.setVisibility(View.VISIBLE);
            }

            @Override
            public void callEnd() {
                super.callEnd();
                Log.e(TAG, "callEnd: ");
                mCallIn.setVisibility(View.GONE);
                mHangUp.setVisibility(View.GONE);
                mToggleMute.setVisibility(View.GONE);
                mToggleSpeaker.setVisibility(View.GONE);
            }
        });
    }

    @OnClick(R.id.audio_call)
    public void audioCall() {
        String dialNum = mDialNum.getText().toString();
        EasyLinphone.callTo(dialNum, false);
    }

    @OnClick(R.id.video_call)
    public void videoCall() {
        String dialNum = mDialNum.getText().toString();
        EasyLinphone.callTo(dialNum, true);
        startActivity(new Intent(MainActivity.this, VideoActivity.class));
    }

    @OnClick(R.id.hang_up)
    public void hangUp() {
        EasyLinphone.hangUp();
    }

    @OnClick(R.id.accept_call)
    public void acceptCall() {
        EasyLinphone.acceptCall();
        if (EasyLinphone.getVideoEnabled()) {
            startActivity(new Intent(MainActivity.this, VideoActivity.class));
        }
    }

    @OnClick(R.id.toggle_mute)
    public void toggleMute() {
        EasyLinphone.toggleMicro(!EasyLinphone.getLC().isMicMuted());
    }

    @OnClick(R.id.toggle_speaker)
    public void toggleSpeaker() {
        EasyLinphone.toggleSpeaker(!EasyLinphone.getLC().isSpeakerEnabled());
    }

}

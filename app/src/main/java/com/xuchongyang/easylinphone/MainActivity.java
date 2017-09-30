package com.xuchongyang.easylinphone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
                mCallIn.setVisibility(View.VISIBLE);
            }

            @Override
            public void callConnected() {
                super.callConnected();
                Log.e(TAG, "callConnected: ");
                mCallIn.setVisibility(View.GONE);
                mToggleSpeaker.setVisibility(View.VISIBLE);
                mToggleMute.setVisibility(View.VISIBLE);
            }

            @Override
            public void callEnd() {
                super.callEnd();
                Log.e(TAG, "callEnd: ");
                mToggleMute.setVisibility(View.GONE);
                mToggleSpeaker.setVisibility(View.GONE);
            }
        });
    }

    @OnClick(R.id.call)
    public void callTo() {
        String dialNum = mDialNum.getText().toString();
        EasyLinphone.callTo(dialNum);
    }

    @OnClick(R.id.hang_up)
    public void hangUp() {
        EasyLinphone.hangUp();
    }

    @OnClick(R.id.accept_call)
    public void acceptCall() {
        EasyLinphone.acceptCall();
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

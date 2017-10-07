package com.xuchongyang.easylinphone;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceView;

import com.xuchongyang.easyphone.EasyLinphone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @OnClick(R.id.video_mute)
    public void mute() {
        EasyLinphone.toggleMicro(!EasyLinphone.getLC().isMicMuted());
    }

    @OnClick(R.id.video_speaker)
    public void speaker() {
        EasyLinphone.toggleSpeaker(!EasyLinphone.getLC().isSpeakerEnabled());
    }
}
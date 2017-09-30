package com.xuchongyang.easylinphone;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;

import com.xuchongyang.easyphone.EasyLinphone;
import com.xuchongyang.easyphone.linphone.LinphoneManager;

import org.linphone.mediastream.video.AndroidVideoWindowImpl;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VideoActivity extends AppCompatActivity {
    @BindView(R.id.video_rendering) SurfaceView mRenderingView;
    @BindView(R.id.video_preview) SurfaceView mPreviewView;

    private AndroidVideoWindowImpl mAndroidVideoWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        ButterKnife.bind(this);

        fixZOrder(mRenderingView, mPreviewView);
        mAndroidVideoWindow = new AndroidVideoWindowImpl(mRenderingView, mPreviewView, new AndroidVideoWindowImpl.VideoWindowListener() {
            @Override
            public void onVideoRenderingSurfaceReady(AndroidVideoWindowImpl androidVideoWindow, SurfaceView surfaceView) {
                EasyLinphone.setVideoWindow(androidVideoWindow);
                mRenderingView = surfaceView;
            }

            @Override
            public void onVideoRenderingSurfaceDestroyed(AndroidVideoWindowImpl androidVideoWindow) {
                EasyLinphone.removeVideoWindow();
            }

            @Override
            public void onVideoPreviewSurfaceReady(AndroidVideoWindowImpl androidVideoWindow, SurfaceView surfaceView) {
                mPreviewView = surfaceView;
                EasyLinphone.setPreviewWindow(mPreviewView);
            }

            @Override
            public void onVideoPreviewSurfaceDestroyed(AndroidVideoWindowImpl androidVideoWindow) {
                EasyLinphone.removePreviewWindow();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mRenderingView != null) {
            ((GLSurfaceView) mRenderingView).onResume();
        }

        if (mAndroidVideoWindow != null) {
            synchronized (mAndroidVideoWindow) {
                LinphoneManager.getLc().setVideoWindow(mAndroidVideoWindow);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAndroidVideoWindow != null) {
            synchronized (mAndroidVideoWindow) {
                LinphoneManager.getLc().setVideoWindow(null);
            }
        }

        if (mRenderingView != null) {
            ((GLSurfaceView) mRenderingView).onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPreviewView = null;
        mRenderingView = null;

        if (mAndroidVideoWindow != null) {
            mAndroidVideoWindow.release();
            mAndroidVideoWindow = null;
        }
    }

    @OnClick(R.id.video_hang)
    public void hang() {
        EasyLinphone.hangUp();
        finish();
    }

    private void fixZOrder(SurfaceView video, SurfaceView preview) {
        video.setZOrderOnTop(false);
        preview.setZOrderOnTop(true);
        preview.setZOrderMediaOverlay(true); // Needed to be able to display control layout over
    }
}

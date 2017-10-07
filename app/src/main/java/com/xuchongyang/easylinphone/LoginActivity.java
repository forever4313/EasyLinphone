package com.xuchongyang.easylinphone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.xuchongyang.easyphone.EasyLinphone;
import com.xuchongyang.easyphone.callback.RegistrationCallback;
import com.xuchongyang.easyphone.service.LinphoneService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    @BindView(R.id.sip_account) EditText mAccount;
    @BindView(R.id.sip_password) EditText mPassword;
    @BindView(R.id.sip_server) EditText mServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        if (!LinphoneService.isReady()) {
            EasyLinphone.startService(this);
            EasyLinphone.addCallback(new RegistrationCallback() {
                @Override
                public void registrationOk() {
                    super.registrationOk();
                    Log.e(TAG, "registrationOk: ");
                    Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                    goToMainActivity();
                }

                @Override
                public void registrationFailed() {
                    super.registrationFailed();
                    Log.e(TAG, "registrationFailed: ");
                    Toast.makeText(LoginActivity.this, "登录失败！", Toast.LENGTH_SHORT).show();
                }
            }, null);
        } else {
            goToMainActivity();
        }
    }

    @OnClick(R.id.press_login)
    public void login() {
        String account = mAccount.getText().toString();
        String password = mPassword.getText().toString();
        String serverIP = mServer.getText().toString();
        EasyLinphone.setAccount(account, password, serverIP);
        EasyLinphone.login();
    }

    private void goToMainActivity() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }
}

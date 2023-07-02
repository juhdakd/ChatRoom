package com.example.chatsip;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.chatroom.R;
import com.example.chatsip.InCallActivity;
import com.example.chatsip.APP;
import com.example.chatsip.BaseActivity;
import com.example.chatsip.OutCallActivity;

import org.linphone.core.Call;
import org.linphone.core.Core;
import org.linphone.core.CoreListener;
import org.linphone.core.CoreListenerStub;

import butterknife.OnClick;

public class ChatSIP extends BaseActivity {
    private static final int PERMISSION_REQUEST_CODE = 1;
    private String targetSip = null; // 目标用户的SIP账号
    //这是自动监听来电情况 导入Linphone官方库 coreListener
    private CoreListener coreListener = new CoreListenerStub() {
        @Override
        public void onCallStateChanged(Core core, Call call, Call.State state, String message) {
            super.onCallStateChanged(core, call, state, message);
            //接到来电自动跳转接电话页面
            if (state == Call.State.IncomingReceived) {
                // 自动接听来电并跳转到接听页面
                Intent intent = new Intent(ChatSIP.this, InCallActivity.class);
                intent.putExtra("sip", targetSip);
                startActivity(intent);
            }
        }
    };

    @Override
    public int getLayoutID() {
        return R.layout.choose_sip;
    }

    @Override
    public void initView() {
        setTitle("好友: " + targetSip);
    }

    @Override
    public void initData() {
        targetSip = getIntent().getStringExtra("targetSipAccount");
    }

    @Override
    public void initialize() {
        requestPermissions();
        Core core = APP.app.getCore();
        if (core != null) {
            core.addListener(coreListener);
        } else {
            Log.e("ChatSIP", "Core instance is null.");
        }
    }
    //询问权限获取情况 获取语音和视频权限
    private void requestPermissions() {
        String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
        boolean allPermissionsGranted = true;

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                allPermissionsGranted = false;
                break;
            }
        }

        if (!allPermissionsGranted) {
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean allPermissionsGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }

            if (!allPermissionsGranted) {
                // 处理权限请求被拒绝的情况
                // ...
            }
        }
    }

    @OnClick(R.id.btnVoiceCall)
    public void startVoiceCall() {
        Intent intent = new Intent(ChatSIP.this, OutCallActivity.class);
        intent.putExtra("sip", targetSip);
        intent.putExtra("type", "audio");
        startActivity(intent);
    }

    @OnClick(R.id.btnVideoCall)
    public void startVideoCall() {
        Intent intent = new Intent(ChatSIP.this, OutCallActivity.class);
        intent.putExtra("sip", targetSip);
        intent.putExtra("type", "video");
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Core core = APP.app.getCore();
        if (core != null) {
            core.removeListener(coreListener);
        }
    }
}

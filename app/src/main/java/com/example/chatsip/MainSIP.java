package com.example.chatsip;

import static com.example.chatsip.APP.core;

import android.content.Intent;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import org.linphone.core.Account;
import org.linphone.core.AccountParams;
import org.linphone.core.Address;
import org.linphone.core.AuthInfo;
import org.linphone.core.Core;
import org.linphone.core.CoreListenerStub;
import org.linphone.core.Factory;
import org.linphone.core.RegistrationState;
import org.linphone.core.TransportType;

import com.example.chatroom.R;
import com.example.chatsip.APP;
import com.example.chatsip.BaseActivity;
import com.example.chatsip.SipUser;

import butterknife.BindView;
import butterknife.OnClick;

public class MainSIP extends BaseActivity {
    public final String TAG = "MainSIP";
    @BindView(R.id.accountInput)
    public EditText accountInput;
    @BindView(R.id.passwordInput)
    public EditText passwordInput;
    @BindView(R.id.sipInput)
    EditText sipInput;

    @Override
    public int getLayoutID() {
        return R.layout.activity_sip;
    }

    /*初始化视图*/
    @Override
    public void initView() {
        setTitle("ChatNote");
        TextView titleTextView = findViewById(R.id.tv_title1);
        titleTextView.setTextSize(50);
        titleTextView.setTextColor(getResources().getColor(R.color.black));
        titleTextView.setText("ChatNote");
    }

    /*初始化数据*/
    @Override
    public void initData() {
        /*初始化Linphone core*/
        Factory factory = Factory.instance();
        factory.setDebugMode(true, "Hello Linphone");
        Core core = factory.createCore(null, null, this);
        APP.core = core;
        Log.d(TAG, "linphone core version: " + core.getVersion());
    }

    @Override
    public void initialize() {
    }

    //监听函数 监听开始聊天按钮
    @OnClick(R.id.startChatBtn)
    public void onLoginBtn() {
        try {
            /*拿到填写的用户名和密码信息*/
            String pwd = passwordInput.getText().toString();// 密码
            String username = accountInput.getText().toString();// 用户名
            /*获取SIP域*/
            String domain = SipUser.domain;
            /*获取传输协议*/
            TransportType transportType = TransportType.Udp;
            /*用户信息，账户信息等*/
            AuthInfo authInfo = Factory.instance().createAuthInfo(username, null, pwd, null, null, domain, null);
            AccountParams accountParams = APP.getInstance().getCore().createAccountParams();
            /*设置user sip account【用户的sip账号】*/
            Address identify = Factory.instance().createAddress("sip:" + username + "@" + domain);// todo
            accountParams.setIdentityAddress(identify);
            /*设置服务器的sip域*/
            Address address = Factory.instance().createAddress("sip:" + domain);
            address.setTransport(transportType);
            accountParams.setServerAddress(address);
            accountParams.setRegisterEnabled(true);
            /*初始化并启动core*/
            Account account = core.createAccount(accountParams);
            core.addAuthInfo(authInfo);
            core.addAccount(account);
            core.setDefaultAccount(account);
            core.addListener(new CoreListenerM());
            account.addListener((a, state, message) -> {
                Log.d(TAG, "onLoginBtn: account state changed......");
            });
            core.start();
        } catch (Exception e) {
            Toast.makeText(this, "请输入SIP账号和密码~", Toast.LENGTH_SHORT);
        }
    }

    private class CoreListenerM extends CoreListenerStub {
        @Override
        public void onAccountRegistrationStateChanged(@NonNull Core core, @NonNull Account account, RegistrationState state, @NonNull String message) {
            super.onAccountRegistrationStateChanged(core, account, state, message);
            if (state == RegistrationState.Ok) {
                Log.d(TAG, "onAccountRegistrationStateChanged: 登录成功");
                //登录成功跳转
                /*界面跳转*/
                Intent intent = new Intent(MainSIP.this, ChatSIP.class);
                intent.putExtra("targetSipAccount", sipInput.getText().toString());
                startActivity(intent);
            }
        }
    }
    //开始聊天
    @OnClick(R.id.startChatBtn)
    public void startChat() {
        /*获取好友SIP账号*/
        String targetSipAccount = sipInput.getText().toString();
        if (targetSipAccount == null || targetSipAccount.equals("")) {
            Toast.makeText(this, "请输入好友sip账号", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d(TAG, "startChat: " + targetSipAccount);
        //获取好友SIP账号 获取成功后，进入ChatSIP
        Intent intent = new Intent(MainSIP.this, ChatSIP.class);
        intent.putExtra("targetSipAccount", targetSipAccount);
        startActivity(intent);
    }
}

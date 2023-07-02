package com.example.chatsip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;

import com.example.chatnote.MainNote;
import com.example.chatsip.APP;

import android.util.Log;
import android.view.View;
import android.widget.*;

import org.linphone.core.*;

import com.example.chatroom.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
//接到来电
public class InCallActivity extends AppCompatActivity {

    @BindView(R.id.remote_address)
    EditText remoteAddressEditText;
    @BindView(R.id.call_status)
    TextView callStatusTextView;
    @BindView(R.id.answer)
    Button answerButton;
    @BindView(R.id.hang_up)
    Button hangUpButton;
    @BindView(R.id.mute_mic)
    Button muteMicButton;
    @BindView(R.id.toggle_speaker)
    Button toggleSpeakerButton;
    @BindView(R.id.note_button)
    Button noteButton;

    private Core core = null;
    //监听器
    private CoreListener coreListener = new CoreListenerStub() {
        @Override
        public void onCallStateChanged(@NonNull Core core, @NonNull Call call, Call.State state, @NonNull String message) {
            super.onCallStateChanged(core, call, state, message);
            updateCallStatus(state, message);
            if (state == Call.State.IncomingReceived) {
                enableCallButtons(false);
                remoteAddressEditText.setText(call.getRemoteAddress().asStringUriOnly());
                CallParams callParams = core.createCallParams(call);
                callParams.setVideoEnabled(call.getRemoteParams().isVideoEnabled());
                call.acceptUpdate(callParams);
            } else if (state == Call.State.Connected) {
                enableCallButtons(true);
                Log.d("IncomingCallActivity", "Call connected.");
            } else if (state == Call.State.Released) {
                Log.d("IncomingCallActivity", "Call released.");
                finish();
            } else {
                CallParams callParams = core.createCallParams(call);
                callParams.setVideoEnabled(call.getRemoteParams().isVideoEnabled());
                call.acceptUpdate(callParams);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.call_out);
        ButterKnife.bind(this);
        core = APP.app.getCore();
        core.setNativeVideoWindowId(findViewById(R.id.remote_video_surface_));
        core.setNativePreviewWindowId(findViewById(R.id.local_preview_video_surface_));
        core.setVideoCaptureEnabled(true);
        core.setVideoDisplayEnabled(true);
        core.getVideoActivationPolicy().setAutomaticallyAccept(true);
        core.addListener(coreListener);
        hideButtonsBeforeAnswer();

        String userName = ""; // 替换为您的账号名字
        remoteAddressEditText.setText(userName);
    }
    //撤销监听器
    @Override
    protected void onDestroy() {
        super.onDestroy();
        core.removeListener(coreListener);
    }
    //更新页面的信息
    private void updateCallStatus(final Call.State state, final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                callStatusTextView.setText(message);
            }
        });
    }

    private void enableCallButtons(final boolean enable) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                answerButton.setVisibility(enable ? View.GONE : View.VISIBLE);
                hangUpButton.setEnabled(enable);
                muteMicButton.setEnabled(enable);
                toggleSpeakerButton.setEnabled(enable);
                noteButton.setVisibility(enable ? View.VISIBLE : View.GONE);
            }
        });
    }

    private void hideButtonsBeforeAnswer() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                answerButton.setVisibility(View.VISIBLE);
                hangUpButton.setVisibility(View.GONE);
                muteMicButton.setVisibility(View.GONE);
                toggleSpeakerButton.setVisibility(View.GONE);
                noteButton.setVisibility(View.GONE);
            }
        });
    }

    @OnClick(R.id.answer)
    public void onAnswerClicked() {
        if (core.getCurrentCall() != null) {
            Log.d("IncomingCallActivity", "Answering call.");
            core.getCurrentCall().accept();
            enableCallButtons(true);
            answerButton.setVisibility(View.GONE);
            hangUpButton.setVisibility(View.VISIBLE);
            muteMicButton.setVisibility(View.VISIBLE);
            toggleSpeakerButton.setVisibility(View.VISIBLE);
            noteButton.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.hang_up)
    public void onHangUpClicked() {
        if (core.getCurrentCall() != null) {
            core.getCurrentCall().terminate();
        }
        finish();
    }

    @OnClick(R.id.mute_mic)
    public void muteMic(View view) {
        core.setMicEnabled(!core.isMicEnabled());
    }
    //扬声器
    @OnClick(R.id.toggle_speaker)
    public void onToggleSpeakerClicked() {
        if (core != null) {
            AudioDevice currentAudioDevice = core.getCurrentCall().getOutputAudioDevice();
            boolean isSpeakerEnabled = currentAudioDevice.getType() == AudioDevice.Type.Speaker;
            for (AudioDevice audioDevice : core.getAudioDevices()) {
                if (isSpeakerEnabled && audioDevice.getType() == AudioDevice.Type.Earpiece) {
                    core.getCurrentCall().setOutputAudioDevice(audioDevice);
                    break;
                } else if (!isSpeakerEnabled && audioDevice.getType() == AudioDevice.Type.Speaker) {
                    core.getCurrentCall().setOutputAudioDevice(audioDevice);
                    break;
                }
            }
            isSpeakerEnabled = !isSpeakerEnabled;
            toggleSpeakerButton.setText(isSpeakerEnabled ? "关闭扬声器" : "打开扬声器");
        }
    }

    public void openNoteActivity(View view) {
        Intent intent = new Intent(InCallActivity.this, MainNote.class);
        startActivity(intent);
    }
}

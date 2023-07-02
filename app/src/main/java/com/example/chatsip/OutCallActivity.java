package com.example.chatsip;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatnote.MainNote;
import com.example.chatroom.R;

import org.linphone.core.Call;
import org.linphone.core.CallParams;
import org.linphone.core.Core;
import org.linphone.core.CoreListenerStub;
import org.linphone.core.Factory;
import org.linphone.core.MediaEncryption;
import org.linphone.core.Address;

public class OutCallActivity extends AppCompatActivity {
    private TextView callStatusTextView;
    private Button hangUpButton;
    private Button noteButton;

    private Core core;
    private MCoreListener mCoreListener;

    private boolean isCallPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.call_in);

        callStatusTextView = findViewById(R.id.call_status);
        hangUpButton = findViewById(R.id.hang_up);
        noteButton = findViewById(R.id.btnOpenNoteActivity);

        core = APP.getInstance().getCore();
        mCoreListener = new MCoreListener();

        core.setNativeVideoWindowId(findViewById(R.id.remote_video_surface));
        core.setNativePreviewWindowId(findViewById(R.id.local_preview_video_surface));
        core.setVideoCaptureEnabled(true);
        core.setVideoDisplayEnabled(true);
        core.getVideoActivationPolicy().setAutomaticallyAccept(true);
        core.addListener(mCoreListener);

        outgoingCall();

        hangUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hangUp(v);
            }
        });

        noteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNoteActivity(v);
            }
        });
    }

    private void outgoingCall() {
        String remoteSipUri = "sip:" + getIntent().getStringExtra("sip") + "@sip.linphone.org";
        Address remoteAddress = Factory.instance().createAddress(remoteSipUri);
        if (remoteAddress == null)
            return;
        CallParams params = core.createCallParams(null);
        if (params == null)
            return;
        params.setMediaEncryption(MediaEncryption.None);
        params.setVideoEnabled(getIntent().getStringExtra("type").equals("video"));
        core.inviteAddressWithParams(remoteAddress, params);
    }

    public void hangUp(View view) {
        if (core.getCallsNb() == 0)
            return;
        Call call;
        if (core.getCurrentCall() != null)
            call = core.getCurrentCall();
        else
            call = core.getCalls()[0];
        if (call == null) {
            return;
        }
        call.terminate();
        finish();
    }

    public void openNoteActivity(View view) {
        Intent intent = new Intent(OutCallActivity.this, MainNote.class);
        startActivity(intent);
    }

   private class MCoreListener extends CoreListenerStub {
        @Override
        public void onCallStateChanged(@NonNull Core core, @NonNull Call call, Call.State state, @NonNull String message) {
            super.onCallStateChanged(core, call, state, message);
            callStatusTextView.setText(message);

            if (state == Call.State.OutgoingInit) {
                hangUpButton.setVisibility(View.VISIBLE);
                noteButton.setVisibility(View.GONE);
                hangUpButton.setEnabled(true);
            } else if (state == Call.State.StreamsRunning) {
                hangUpButton.setVisibility(View.VISIBLE);
                noteButton.setVisibility(View.VISIBLE);
                hangUpButton.setEnabled(true);
                noteButton.setEnabled(true); // 设置按钮可用状态
            } else if (state == Call.State.Paused) {
                hangUpButton.setEnabled(true);
                noteButton.setEnabled(true); // 设置按钮可用状态
            } else if (state == Call.State.Released) {
                finish();
            } else {
                hangUpButton.setEnabled(true);
                noteButton.setEnabled(false); // 设置按钮不可用状态
            }
        }
    }
}

package com.example;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chatnote.MainNote;
import com.example.chatsip.MainSIP;
import com.example.chatroom.MainSocket;
import com.example.chatroom.R;

public class MainPage extends AppCompatActivity {
    Button socketButton;
    Button sipButton;
    Button noteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        socketButton = findViewById(R.id.socketButton);
        sipButton = findViewById(R.id.sipButton);
        noteButton = findViewById(R.id.noteButton);

        socketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainPage.this, MainSocket.class);
                startActivity(intent);
            }
        });

        sipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainPage.this, MainSIP.class);
                startActivity(intent);
            }
        });

        noteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainPage.this, MainNote.class);
                startActivity(intent);
            }
        });
    }
}

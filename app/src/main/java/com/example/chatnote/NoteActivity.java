package com.example.chatnote;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.chatroom.R;

public class NoteActivity extends AppCompatActivity {
    private TextView textViewNoteTitle;
    private TextView textViewNoteContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note);

        textViewNoteTitle = findViewById(R.id.textViewNoteTitle);
        textViewNoteContent = findViewById(R.id.textViewNoteContent);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");

        textViewNoteTitle.setText(title);
        textViewNoteContent.setText(content);
    }
}

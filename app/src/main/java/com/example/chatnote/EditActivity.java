package com.example.chatnote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chatroom.R;

public class EditActivity extends AppCompatActivity {
    private EditText editTextTitle;
    private EditText editTextContent;
    private Button buttonSave;
    private Button buttonDelete;

    private int memoPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextContent = findViewById(R.id.editTextContent);
        buttonSave = findViewById(R.id.buttonSave);
        buttonDelete = findViewById(R.id.buttonDelete);

        // 获取传递的备忘录位置
        memoPosition = getIntent().getIntExtra("position", -1);

        // 获取传递的备忘录标题和内容
        String memoTitle = getIntent().getStringExtra("title");
        String memoContent = getIntent().getStringExtra("content");

        // 在编辑界面中显示备忘录的标题和内容
        editTextTitle.setText(memoTitle);
        editTextContent.setText(memoContent);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteNote();
            }
        });
    }

    private void saveNote() {
        String title = editTextTitle.getText().toString();
        String content = editTextContent.getText().toString();

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "Please enter title and content", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent resultIntent = new Intent();
        resultIntent.putExtra("position", memoPosition);
        resultIntent.putExtra("title", title);
        resultIntent.putExtra("content", content);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void deleteNote() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("position", memoPosition);
        setResult(RESULT_CANCELED, resultIntent);
        finish();
    }
}

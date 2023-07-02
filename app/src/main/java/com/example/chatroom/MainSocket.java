package com.example.chatroom;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatnote.MainNote;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainSocket extends AppCompatActivity {

    private Button login;
    private Button quit;
    private EditText et_name;
    private EditText et_ip;
    private EditText et_port;
    private TextView my_name;
    private Button btnOpenNoteActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 隐藏原有的标题栏
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        // 获取id,连接控件
        login = findViewById(R.id.login);
        quit = findViewById(R.id.quit);
        et_name = findViewById(R.id.et_name);
        et_ip = findViewById(R.id.et_ip);
        et_port = findViewById(R.id.et_port);
        btnOpenNoteActivity = findViewById(R.id.btnOpenNoteActivity);

        // login点击事件
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 获取一个视图View对象里的字符串
                String name = et_name.getText().toString();
                // 如果没输入名字
                if("".equals(name)){
                    Toast.makeText(MainSocket.this, "请输入用户名！", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(MainSocket.this, ChoosePicture.class);
                    // 向下一活动传递信息
                    intent.putExtra("name", et_name.getText().toString());
                    intent.putExtra("ip", et_ip.getText().toString());
                    intent.putExtra("port", et_port.getText().toString());

                    try {
                        startActivity(intent);
                    } catch (Exception e) {
                        System.out.println("开启失败");
                        finish();
                    }
                }
            }
        });

        // quit点击事件
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 用AlertDialog显示一个退出提示框
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainSocket.this);
                dialog.setTitle("关闭提示");
                dialog.setMessage("确定退出登录？");
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        System.exit(0);
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                dialog.show();
            }
        });

        // btnOpenNoteActivity点击事件
        btnOpenNoteActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainSocket.this, MainNote.class);
                startActivity(intent);
            }
        });
    }
}

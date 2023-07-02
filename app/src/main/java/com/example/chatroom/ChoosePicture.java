package com.example.chatroom;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class ChoosePicture extends AppCompatActivity implements View.OnClickListener{

    private String et_name;
    private String et_ip;
    private String et_port;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_picture);
        // 隐藏原有的标题栏
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        System.out.println("启动2");
        //启动活动，并获取传递过来的信息
        Intent intent = getIntent();
        //传入键值得到数据
        et_name = intent.getStringExtra("name");
        et_ip = intent.getStringExtra("ip");
        et_port = intent.getStringExtra("port");
        Button button1 = (Button)findViewById(R.id.one);
        Button button2 = (Button)findViewById(R.id.two);
        Button button3 = (Button)findViewById(R.id.three);
        Button button4 = (Button)findViewById(R.id.four);
        Button button5 = (Button)findViewById(R.id.five);
        Button button6 = (Button)findViewById(R.id.six);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        //向下一活动传递信息
        Intent intent = new Intent(ChoosePicture.this,ChatRoom.class);
        Log.i("ChoosePicture", et_name.toString());
        intent.putExtra("name",et_name.toString());
        intent.putExtra("ip",et_ip.toString());
        intent.putExtra("port",et_port.toString());
        switch(view.getId()){
            case R.id.one:
                intent.putExtra("imageId",R.drawable.one);
                break;
            case R.id.two:
                intent.putExtra("imageId",R.drawable.two);
                break;
            case R.id.three:
                intent.putExtra("imageId",R.drawable.three);
                break;
            case R.id.four:
                intent.putExtra("imageId",R.drawable.four);
                break;
            case R.id.five:
                intent.putExtra("imageId",R.drawable.five);
                break;
            case R.id.six:
                intent.putExtra("imageId",R.drawable.six);
                break;
        }
        startActivity(intent);
    }

}

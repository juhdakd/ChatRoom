package com.example.chatsip;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutID());
        ButterKnife.bind(this);
        initData();
        initView();
        initialize();
    }
    public void hideToolBar(){
        getSupportActionBar().hide();
    }
    public abstract int getLayoutID();
    public abstract void initView();// 初始化视图
    public abstract void initData();// 初始化数据
    public abstract void initialize();// 其他初始化
}

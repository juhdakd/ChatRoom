package com.example.chatsip;

import android.app.Application;

import org.linphone.core.Core;

public class APP extends Application {
    public static APP app;
    public static Core core;

    public static APP getInstance() {
        return app;
    }

    public static Core getCore() {
        return core;
    }

    public static void setCore(Core core) {
        APP.core = core;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }
}

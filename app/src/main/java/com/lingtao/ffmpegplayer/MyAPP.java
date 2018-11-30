package com.lingtao.ffmpegplayer;

import android.app.Application;
import android.util.Log;

import com.lingtao.core.app.ProjectInit;


public class MyAPP extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        ProjectInit.init(this).withApiHost("http://japi.juhe.cn/joke/").configure();
    }
}

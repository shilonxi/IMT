package com.example.administrator.imt;

import android.app.Application;

import com.netease.nimlib.sdk.NIMClient;

public class NimApplication extends Application
{
    public void onCreate()
    {
        // SDK初始化（启动后台服务，若已经存在用户登录信息， SDK 将完成自动登录）
        NIMClient.init(this,null,null);
    }
}

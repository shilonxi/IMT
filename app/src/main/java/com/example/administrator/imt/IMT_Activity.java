package com.example.administrator.imt;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.List;

public class IMT_Activity extends Activity
{
    private EditText input;
    private Button send;
    private TextView receive;
    //建立变量
    private Observer<List<IMMessage>> incomingMessageObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imt_layout);
        input=findViewById(R.id.input);
        send=findViewById(R.id.send);
        receive=findViewById(R.id.receive);
        //获取实例
        doLogin();
        //手动登陆
        messageReceive();
        //接收消息
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
                //发送消息
            }
        });
        //监听
    }

    public void doLogin()
    {
        LoginInfo info=new LoginInfo("fxd","19950303");
        //登录信息不变
        RequestCallback<LoginInfo> callback=
                new RequestCallback<LoginInfo>() {
                    @Override
                    public void onSuccess(LoginInfo param) {
                        Toast.makeText(IMT_Activity.this,"登录成功",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailed(int code) {
                        Toast.makeText(IMT_Activity.this,"登录失败",Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onException(Throwable exception) {
                        Toast.makeText(IMT_Activity.this,exception.toString(),Toast.LENGTH_LONG).show();
                    }
                };
        NIMClient.getService(AuthService.class).login(info).setCallback(callback);
        //发送请求
    }

    private void sendMessage()
    {
        String account="nanashi";
        //聊天对象的 ID
        SessionTypeEnum sessionType=SessionTypeEnum.P2P;
        //单聊类型
        String text=input.getText().toString();
        // 创建一个文本消息
        IMMessage textMessage=MessageBuilder.createTextMessage(account,sessionType,text);
        NIMClient.getService(MsgService.class).sendMessage(textMessage,false);
        //发送给对方
    }

    private void messageReceive()
    {
        incomingMessageObserver=new Observer<List<IMMessage>>() {
            @Override
            public void onEvent(List<IMMessage> messages) {
                IMMessage imMessage=messages.get(0);
                String messageStr=imMessage.getContent();
                receive.append(messageStr+"\n");
                //处理新收到的消息
            }
        };
        NIMClient.getService(MsgServiceObserve.class).observeReceiveMessage(incomingMessageObserver,true);
        //注册消息接收观察者
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        NIMClient.getService(MsgServiceObserve.class).observeReceiveMessage(incomingMessageObserver,false);
        //注销消息接收观察者
    }

}

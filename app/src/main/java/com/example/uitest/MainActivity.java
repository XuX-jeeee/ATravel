package com.example.uitest;

import static com.xuexiang.xui.utils.StatusBarUtils.getNavigationBarHeight;

import static java.util.TimeZone.SHORT;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.telecom.Call;
import android.telephony.SmsManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.uitest.object.Listitem;
import com.example.uitest.utils.StringUtil;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.xuexiang.xui.widget.toast.XToast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {
    public static boolean Call = false;
    private static final int SEND_SMS = 100;
    @BindView(R.id.tl_commen)
    CommonTabLayout tlCommen;
    @BindView(R.id.flContent)
    FrameLayout flContent;


    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private String[] mTitles = {"主页", "便签", "分析", "我的"};


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white));//设置状态栏颜色
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//实现状态栏图标和文字颜色为暗色
        }
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        Handler handle = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                //toSendSMS(MainActivity.this,Me.user.getRelativesPhone(),"您的朋友/家人/亲戚在"+Home.nowAddress+"驾驶过程中检测到异常情况，需要及时的帮助！");
                startCall();
            }

        };
        class TimeThread extends Thread {
            @Override
            public void run() {
                while (true) {
                    while (Call) {
                        Message msg = new Message();
                        handle.sendMessage(msg);
                        Call = false;
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        TimeThread thread = new TimeThread();
        new Thread(thread).start(); //开启线程
        ButterKnife.bind(this);
        initTab();
    }

    public void initTab() {
        for (String mTitle : mTitles) {
            if("主页".equals(mTitle)){
                //后面两个值是选中图标和未选中(R.drawable.xxx)不要图标就填0
                mTabEntities.add(new TabEntity(mTitle, R.drawable.home_blue, R.drawable.home));
                mFragments.add(new Home());
            }else if("便签".equals(mTitle)){
                mTabEntities.add(new TabEntity(mTitle, R.drawable.pen_blue, R.drawable.memorandum));
                mFragments.add(new Memorandum());
            }else if("分析".equals(mTitle)){
                mTabEntities.add(new TabEntity(mTitle, R.drawable.analysis_blue, R.drawable.chart));
                mFragments.add(new DriverReport());
            }else if("我的".equals(mTitle)){
                mTabEntities.add(new TabEntity(mTitle, R.drawable.me_blue, R.drawable.me));
                mFragments.add(new Me());
            }
        }
        tlCommen.setTabData(mTabEntities, this, R.id.flContent, mFragments);
    }



    public void Call()
    {
        try {
            Intent intent = new Intent (Intent.ACTION_CALL);
            intent.setData(Uri.parse( "tel:"+Me.user.getRelativesPhone()));
            startActivity(intent) ;
            requestPermission();
        } catch (SecurityException e) {
            e. printStackTrace( );
        }
    }

    public void startCall(){
            if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.
                    permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{ Manifest.permission.CALL_PHONE },1);
            }else{
                Call();
            }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                }
                break;
            case SEND_SMS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendSMSS();
                } else {
                    // Permission Denied
                    XToast.error(MainActivity.this, "请求限权失败").show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    private void requestPermission() {
        //判断Android版本是否大于23
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SEND_SMS);
                return;
            } else {
                sendSMSS();
                //已有权限
            }
        } else {
            //API 版本在23以下
        }
    }

    //发送短信
    private void sendSMSS() {
        String content = "绑定您为紧急联系人的朋友/亲戚/家人目前在"+Home.nowAddress+"检车到有异常情况，请及时给予帮助！";
        String phone = Me.user.getRelativesPhone();
        if (!StringUtil.isEmpty(content) && !StringUtil.isEmpty(phone)) {
            SmsManager manager = SmsManager.getDefault();
            ArrayList<String> strings = manager.divideMessage(content);
            for (int i = 0; i < strings.size(); i++) {
                manager.sendTextMessage(phone, null, content, null, null);
            }
            XToast.success(MainActivity.this,"发送成功！").show();
        } else {
            XToast.success(MainActivity.this,"手机号或内容不能为空！").show();
            return;
        }

    }


}
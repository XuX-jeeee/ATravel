package com.example.uitest.me;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.uitest.R;
import com.hjq.bar.TitleBar;


public class WeekReport extends AppCompatActivity {
    //private CardView meItem1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_test);
        WebView webView = findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDefaultTextEncodingName("utf-8");

        //解决网页不显示的代码
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        Intent intent = getIntent();

        WebSettings wSet = webView.getSettings();
        //设置载入页面自适应手机屏幕，居中显示
        wSet.setUseWideViewPort(true);
        wSet.setLoadWithOverviewMode(true);
        wSet.setAllowFileAccess(true);
        wSet.setAllowContentAccess(true);
        wSet.setJavaScriptEnabled(true);
//设置支持缩放
        wSet.setBuiltInZoomControls(true);
// 打开本地sd卡内的index.html文件
        webView.loadUrl("file:///android_asset/健康周报.html");
        TitleBar back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /**
         * 全透状态栏
         */

        if (Build.VERSION.SDK_INT >= 21) {//21表示5.0
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= 19) {//19表示4.4
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //虚拟键盘也透明
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        /*
        meItem1=findViewById(R.id.me_item_1);
        ScaleAnimationBySpringWayOne.onScaleAnimationBySpringWayOneMiddle(meItem1);
        meItem1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScaleAnimationBySpringWayOne.onScaleAnimationBySpringWayOneMiddle(v.findViewById(v.getId()));
            }
        });
         */

    }
}
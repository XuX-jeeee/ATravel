package com.example.uitest;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import me.wangyuwei.loadingview.LoadingView;

public class Loading extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading); //设置布局
        LoadingView loadingView=(LoadingView)findViewById(R.id.loading_view);
        loadingView.start();
        loadingView.setExternalRadius(40);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingView.stop();Loading.this.finish();
            }
        }, 100);
    }
}

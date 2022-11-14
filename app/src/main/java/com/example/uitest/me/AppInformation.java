package com.example.uitest.me;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.uitest.R;
import com.example.uitest.appLogin;
import com.example.uitest.tools.ScaleAnimationBySpringWayOne;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;

public class AppInformation extends AppCompatActivity implements View.OnClickListener {
    private CardView meItem1;
    private CardView meItem2;
    private CardView meItem3;
    private CardView meItem4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_information);
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
        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        /**
         * 全透状态栏
         */


        meItem1=findViewById(R.id.me_item_1);
        meItem2=findViewById(R.id.me_item_2);
        meItem3=findViewById(R.id.me_item_3);
        meItem4=findViewById(R.id.me_item_4);
        ScaleAnimationBySpringWayOne.onScaleAnimationBySpringWayOneMiddle(meItem1);
        ScaleAnimationBySpringWayOne.onScaleAnimationBySpringWayOneMiddle(meItem2);
        ScaleAnimationBySpringWayOne.onScaleAnimationBySpringWayOneMiddle(meItem3);
        ScaleAnimationBySpringWayOne.onScaleAnimationBySpringWayOneMiddle(meItem4);
        meItem1.setOnClickListener(this);
        meItem2.setOnClickListener(this);
        meItem3.setOnClickListener(this);
        meItem4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        ScaleAnimationBySpringWayOne.onScaleAnimationBySpringWayOneMiddle(v.findViewById(v.getId()));
        switch (v.getId()){
            case R.id.me_item_2:
                intent.setClass(AppInformation.this,UpdateHistory.class);
                startActivity(intent);
                break;
            case R.id.me_item_3:
                intent.setClass(AppInformation.this,Privacy.class);
                startActivity(intent);
                break;
            case R.id.me_item_4:
                intent.setClass(AppInformation.this,Developer.class);
                startActivity(intent);
                break;
        }

    }
}
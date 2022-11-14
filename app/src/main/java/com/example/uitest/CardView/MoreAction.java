package com.example.uitest.CardView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.uitest.DriverReport;
import com.example.uitest.Me;
import com.example.uitest.R;
import com.example.uitest.tools.ScaleAnimationBySpringWayOne;
import com.xuexiang.xui.widget.progress.HorizontalProgressView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MoreAction extends AppCompatActivity {
    private ImageView back;
    private CardView cardView1;

    private static HorizontalProgressView progressView1;
    private static HorizontalProgressView progressView2;
    private static HorizontalProgressView progressView3;
    private static HorizontalProgressView progressView4;
    private static HorizontalProgressView progressView5;
    private static TextView textView1;
    private static TextView textView2;
    private static TextView textView3;
    private static TextView textView4;
    private static TextView textView5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_action);
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



        progressView1=findViewById(R.id.progress_1);
        progressView2=findViewById(R.id.progress_2);
        progressView3=findViewById(R.id.progress_3);
        progressView4=findViewById(R.id.progress_4);
        progressView5=findViewById(R.id.progress_5);
        textView1=findViewById(R.id.progress_1_times);
        textView2=findViewById(R.id.progress_2_times);
        textView3=findViewById(R.id.progress_3_times);
        textView4=findViewById(R.id.progress_4_times);
        textView5=findViewById(R.id.progress_5_times);

        progressView1.startProgressAnimation();
        progressView2.startProgressAnimation();
        progressView3.startProgressAnimation();
        progressView4.startProgressAnimation();
        progressView5.startProgressAnimation();


        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        cardView1=findViewById(R.id.item_1);
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScaleAnimationBySpringWayOne.onScaleAnimationBySpringWayOneMiddle(cardView1);
            }
        });
        List <Integer>list=new ArrayList<>();
        for(int i=0;i<5;i++){
            list.add(1);
        }
        Handler handle = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                list.set(0, DriverReport.operatingTheRadio);
                list.set(1,DriverReport.drinking);
                list.set(2,DriverReport.reachingBehindAndTurningHead);
                list.set(3,DriverReport.touchingHairAndMakingUp);
                list.set(4,DriverReport.nodding);
                textView1.setText(String.valueOf(DriverReport.operatingTheRadio));
                textView2.setText(String.valueOf(DriverReport.drinking));
                textView3.setText(String.valueOf(DriverReport.reachingBehindAndTurningHead));
                textView4.setText(String.valueOf(DriverReport.touchingHairAndMakingUp));
                textView5.setText(String.valueOf(DriverReport.nodding));
                Integer max = Collections.max(list);
                if(max!=0){
                    progressView1.setEndProgress(DriverReport.operatingTheRadio*100/max);
                    progressView2.setEndProgress(DriverReport.drinking*100/max);
                    progressView3.setEndProgress(DriverReport.reachingBehindAndTurningHead*100/max);
                    progressView4.setEndProgress(DriverReport.touchingHairAndMakingUp*100/max);
                    progressView5.setEndProgress(DriverReport.nodding*100/max);
                }
                progressView1.startProgressAnimation();
                progressView2.startProgressAnimation();
                progressView3.startProgressAnimation();
                progressView4.startProgressAnimation();
                progressView5.startProgressAnimation();
            }

        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                    Message msg = new Message();
                    handle.sendMessage(msg);
            }
        }).start();
    }


}
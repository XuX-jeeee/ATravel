package com.example.uitest.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.uitest.Me;
import com.example.uitest.R;
import com.example.uitest.news.News_1;
import com.example.uitest.tools.ScaleAnimationBySpringWayOne;
import com.example.uitest.utils.AnimationUtils;
import com.example.uitest.utils.ChartUtils;
import com.example.uitest.utils.ShowUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BloodPressure extends AppCompatActivity{
    private TextView tvDate;
    private ImageView ivDate;
    private LineChart chart;
    private LineChart chart2;
    private ImageView back;
    private ImageView url;
    private CardView cardView1;
    private CardView cardView2;
    private List dataHistory;
    private List dataHistory2;

    private static final String[] dates = new String[]{"今日", "本周", "本月"};
    private List<String> dateList = Arrays.asList(dates);


    Thread getData = new Thread(new Runnable() {
        @Override
        public void run() {
            if(Me.user.getActionTimeWeekly(Me.user.getAccount(),"1")!=null){
                try {
                    dataHistory=new ArrayList();
                    JSONArray data=Me.user.getHealthInfo(Me.user.getAccount(),"2");
                    for(int i=0;i<data.length();i++){
                        dataHistory.add(new Entry(i,Integer.valueOf(data.getInt(i))));
                    }
                    Message msg = new Message();
                    handle.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    Message msg = new Message();
                    dataHistory2=new ArrayList();
                    JSONArray data=Me.user.getHealthInfo(Me.user.getAccount(),"1");
                    for(int i=0;i<data.length();i++){
                        dataHistory2.add(new Entry(i,Integer.valueOf(data.getInt(i))));
                    }
                    handle1.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


        }
    });
    Handler handle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            initView();
        }

    };
    Handler handle1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            initView2();
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_pressure);
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

        getData.start();

        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        url=findViewById(R.id.url);
        url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(BloodPressure.this, News_1.class);
                startActivity(intent);
            }
        });
        cardView1=findViewById(R.id.item_1);
        cardView2=findViewById(R.id.item_2);
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScaleAnimationBySpringWayOne.onScaleAnimationBySpringWayOneMiddle(cardView1);
            }
        });
        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScaleAnimationBySpringWayOne.onScaleAnimationBySpringWayOneMiddle(cardView2);
            }
        });
    }

    private void initView() {
        tvDate = (TextView) findViewById(R.id.tv_date);
        ivDate = (ImageView) findViewById(R.id.iv_date);
        chart = (LineChart) findViewById(R.id.chart);
        chart2=(LineChart)findViewById(R.id.chart1);

        ChartUtils.initChart(chart);
        ChartUtils.notifyDataSetChanged(chart, getData(true), ChartUtils.dayValue);
    }

    private void initView2() {
        tvDate = (TextView) findViewById(R.id.tv_date);
        ivDate = (ImageView) findViewById(R.id.iv_date);
        chart = (LineChart) findViewById(R.id.chart);
        chart2=(LineChart)findViewById(R.id.chart1);

        ChartUtils.initChart(chart2);
        ChartUtils.notifyDataSetChanged(chart2, getData(false), ChartUtils.dayValue);
    }


    private List<Entry> getData(boolean selected) {
        if (selected)
            return dataHistory;
        else
            return dataHistory2;
    }

}
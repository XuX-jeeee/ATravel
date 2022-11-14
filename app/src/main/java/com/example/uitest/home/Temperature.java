package com.example.uitest.home;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.uitest.Me;
import com.example.uitest.R;
import com.example.uitest.tools.ScaleAnimationBySpringWayOne;
import com.example.uitest.utils.AnimationUtils;
import com.example.uitest.utils.ChartUtils;
import com.example.uitest.utils.ShowUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 主页
 * Created by yangle on 2016/11/25.
 * <p>
 * Website：http://www.yangle.tech
 * GitHub：https://github.com/alidili
 * CSDN：http://blog.csdn.net/kong_gu_you_lan
 * JianShu：http://www.jianshu.com/u/34ece31cd6eb
 */

public class Temperature extends AppCompatActivity{
    private CardView cardView1;
    private CardView cardView2;
    private TextView tvDate;
    private ImageView ivDate;
    private LineChart chart;
    private ImageView back;
    private List dataHistory;
    Handler handle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            initView();
        }

    };
    Thread getData = new Thread(new Runnable() {
        @Override
        public void run() {
            if(Me.user.getActionTimeWeekly(Me.user.getAccount(),"0")!=null){
                try {
                    dataHistory=new ArrayList();
                    JSONArray data=Me.user.getHealthInfo(Me.user.getAccount(),"0");
                    for(int i=0;i<data.length();i++){
                        dataHistory.add(new Entry(i,Integer.valueOf(data.getInt(i))));
                    }
                    Message msg = new Message();
                    handle.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }
    });
    private static final String[] dates = new String[]{"今日", "本周", "本月"};
    private List<String> dateList = Arrays.asList(dates);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart1);

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
        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getData.start();

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


        ChartUtils.initChart(chart);
        ChartUtils.notifyDataSetChanged(chart, getData(), ChartUtils.dayValue);
    }


    private List<Entry> getData() {
        return dataHistory;
    }
}

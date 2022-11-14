package com.example.uitest.CardView;

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
import com.example.uitest.tools.ScaleAnimationBySpringWayOne;
import com.example.uitest.utils.AnimationUtils;
import com.example.uitest.utils.ChartUtils;
import com.example.uitest.utils.ShowUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActionSix extends AppCompatActivity implements View.OnClickListener{
    private TextView tvDate;
    private ImageView ivDate;
    private LineChart chart;
    private ImageView back;
    private CardView cardView1;
    public static boolean flag=false;
    private final JSONObject[] dayData = new JSONObject[1];
    private JSONArray weekData = new JSONArray();
    public static List<String> xValues=new ArrayList<>();
    private List dayDataList=new ArrayList();
    private List weekDataList=new ArrayList();
    private Entry[] entries=new Entry[7];

    private static final String[] dates = new String[]{ "天", "周"};
    private List<String> dateList = Arrays.asList(dates);
    Thread getData = new Thread(new Runnable() {
        @Override
        public void run() {
            System.out.println(getData.getName());
            if(Me.user.getActionTimeThisWeek(Me.user.getAccount(),"smoke")!=null){
                System.out.println("1516111111111111111111111111111111");
                try {
                    dayData[0] =Me.user.getActionTimeThisWeek(Me.user.getAccount(),"smoke");
                    System.out.println("1111111111111111111");
                    System.out.println(dayData[0].toString());
                    System.out.println(dayData[0].getInt("Sun"));
                    entries[0]=new Entry(0, dayData[0].getInt("Sun"));
                    entries[1]=new Entry(1, dayData[0].getInt("Mon"));
                    entries[2]=new Entry(2, dayData[0].getInt("Tues"));
                    entries[3]=new Entry(3, dayData[0].getInt("Wed"));
                    entries[4]=new Entry(4, dayData[0].getInt("Thur"));
                    entries[5]=new Entry(5, dayData[0].getInt("Fri"));
                    entries[6]=new Entry(6, dayData[0].getInt("Sat"));
                    dayDataList.set(0,entries[0]);
                    dayDataList.set(1,entries[1]);
                    dayDataList.set(2,entries[2]);
                    dayDataList.set(3,entries[3]);
                    dayDataList.set(4,entries[4]);
                    dayDataList.set(5,entries[5]);
                    dayDataList.set(6,entries[6]);
                    System.out.println(dayDataList.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if(Me.user.getActionTimeWeekly(Me.user.getAccount(),"smoke")!=null){
                try {
                    xValues=new ArrayList<>();
                    weekData=Me.user.getActionTimeWeekly(Me.user.getAccount(),"smoke");
                    System.out.println("week.length"+weekData.length());
                    for(int i=0;i<7;i++){
                        System.out.println(i);
                        xValues.add("i");

                        if (i<weekData.length())
                        {
                            System.out.println(weekData.get(i).toString());
                            xValues.set(i,"第"+weekData.get(i).toString().split(":")[0]+"周");
                            weekDataList.add(new Entry(i, Integer.parseInt(weekData.get(i).toString().split(":")[1])));
                        }
                        else{
                            weekDataList.add(new Entry(i, 0));
                        }

                    }
                    System.out.println(weekDataList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            Message msg = new Message();
            handle.sendMessage(msg);
        }
    });
    Handler handle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            initView();
            super.handleMessage(msg);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_six);
        for(int i=0;i<7;i++)
        {
            dayDataList.add(i,0);
        }
        getData.start();
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
        tvDate = (TextView) findViewById(R.id.tv_date);
        ivDate = (ImageView) findViewById(R.id.iv_date);
        chart = (LineChart) findViewById(R.id.chart);
        tvDate.setOnClickListener(this);
        ivDate.setOnClickListener(this);

        cardView1=findViewById(R.id.item_1);
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScaleAnimationBySpringWayOne.onScaleAnimationBySpringWayOneMiddle(cardView1);
            }
        });
    }

    private void initView() {
        ChartUtils.initChart(chart);
        ChartUtils.notifyDataSetChanged(chart, getData(0), ChartUtils.weekValue);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_date:
            case R.id.iv_date:

                String data = tvDate.getText().toString();
                if (!ShowUtils.isPopupWindowShowing()) {
                    AnimationUtils.startModeSelectAnimation(ivDate, true);
                    ShowUtils.showPopupWindow(this, tvDate, 90, 111, dateList,
                            new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view,
                                                        int position, long id) {
                                    ShowUtils.updatePopupWindow(position);
                                    AnimationUtils.startModeSelectAnimation(ivDate, false);
                                    ShowUtils.popupWindowDismiss();
                                    tvDate.setText(dateList.get(position));
                                    // 更新图表
                                    ChartUtils.notifyDataSetChanged(chart, getData(position), position);
                                }
                            });
                } else {
                    AnimationUtils.startModeSelectAnimation(ivDate, false);
                    ShowUtils.popupWindowDismiss();
                }
                if (dateList.get(0).equals(data)) {
                    ShowUtils.updatePopupWindow(1);
                } else if (dateList.get(1).equals(data)) {
                    ShowUtils.updatePopupWindow(2);
                }
                break;

            default:
                break;
        }
    }

    private List<Entry> getData(int position) {
        if(position==0&&dayDataList.size()!=0) {
            System.out.println("use1111111111111\n");
            flag=false;
            return dayDataList;
        }
        else if((position==1&&weekDataList.size()!=0)) {
            System.out.println("use22222222222222222\n");
            return weekDataList;
        }
        else {
            System.out.println("use333333333333333\n");
            List<Entry> values = new ArrayList<>();
            values.add(new Entry(1, (float) Math.random()*100));
            values.add(new Entry(2, (float) Math.random()*100));
            values.add(new Entry(3, (float) Math.random()*100));
            values.add(new Entry(4, (float) Math.random()*100));
            values.add(new Entry(5, (float) Math.random()*100));
            values.add(new Entry(6, (float) Math.random()*100));
            values.add(new Entry(7, (float) Math.random()*100));
            return values;
        }
    }
}
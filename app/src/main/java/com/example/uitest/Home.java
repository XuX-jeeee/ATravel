package com.example.uitest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.uitest.home.BloodPressure;
import com.example.uitest.home.HeartRate;
import com.example.uitest.home.Oximetry;
import com.example.uitest.home.Temperature;
import com.example.uitest.news.News_4;
import com.example.uitest.object.Listitem;
import com.example.uitest.object.User;
import com.example.uitest.tools.ScaleAnimationBySpringWayOne;
import com.xuexiang.xui.widget.progress.CircleProgressView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Home extends Fragment implements View.OnClickListener, com.example.uitest.object.Me {
    private CardView meItem1;
    private CardView meItem2;
    private CardView meItem3;
    private CardView meItem4;
    private CardView meItem5;
    private TextView time;
    private ImageView url;
    private TextView hello;
    private CircleProgressView circleProgressView;
    private JSONObject data;
    private String temperature;
    private String heartBeat;
    public static String bloodPressureHigh;
    private String bloodPressureLow;
    private String oximetry;
    private String drivingTime;
    private String location;
    private String weather;
    private String temperatures;
    private TextView temperatureView;
    private TextView heartBeatView;
    private TextView bloodPressureHighView;
    private TextView bloodPressureLowView;
    private TextView oximetryView;
    private TextView drivingTimeView;
    private TextView locationView;
    private TextView temperaturesView;
    public static String nowAddress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        meItem1 = view.findViewById(R.id.home_cardView_1);
        meItem2 = view.findViewById(R.id.home_cardView_2);
        meItem3 = view.findViewById(R.id.home_cardView_3);
        meItem4 = view.findViewById(R.id.home_cardView_4);
        meItem5 = view.findViewById(R.id.home_cardview);
        hello = view.findViewById(R.id.hello);
        ScaleAnimationBySpringWayOne.onScaleAnimationBySpringWayOneMiddle(meItem1);
        ScaleAnimationBySpringWayOne.onScaleAnimationBySpringWayOneMiddle(meItem2);
        ScaleAnimationBySpringWayOne.onScaleAnimationBySpringWayOneMiddle(meItem3);
        ScaleAnimationBySpringWayOne.onScaleAnimationBySpringWayOneMiddle(meItem4);
        ScaleAnimationBySpringWayOne.onScaleAnimationBySpringWayOneMiddle(meItem5);
        meItem1.setOnClickListener(this);
        meItem2.setOnClickListener(this);
        meItem3.setOnClickListener(this);
        meItem4.setOnClickListener(this);
        meItem5.setOnClickListener(this);
        time = view.findViewById(R.id.now_temp);

        temperatureView=view.findViewById(R.id.temperature);
        heartBeatView=view.findViewById(R.id.heart_rate);
        bloodPressureHighView=view.findViewById(R.id.top_blood_pressure);
        bloodPressureLowView=view.findViewById(R.id.bottom_blood_pressure);
        oximetryView=view.findViewById(R.id.blood_oxygen);
        temperaturesView=view.findViewById(R.id.now_temp);
        locationView=view.findViewById(R.id.location);
        drivingTimeView=view.findViewById(R.id.driving_time_text);

        circleProgressView=view.findViewById(R.id.driving_time_progress);
        circleProgressView.setEndProgress(0);
        Handler handle = new Handler() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                time.setText(new Listitem("null").getTime());
                hello.setText(When() +"好，"+ Me.user.getUsername()+"!");
                temperatureView.setText(temperature+"℃");
                heartBeatView.setText(heartBeat+"次/分");
                bloodPressureHighView.setText("上压"+bloodPressureHigh+"mmHg");
                if (Integer.parseInt(bloodPressureHigh)>200) {
                    try {
                        Thread.sleep(2000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    MainActivity.Call=true;
                }

                bloodPressureLowView.setText("下压"+bloodPressureLow+"mmHg");
                oximetryView.setText(oximetry+"HbO₂");
                temperaturesView.setText("18℃");
                nowAddress=location;
                locationView.setText(location);
                drivingTimeView.setText("");
                System.out.println("111111111111111\n"+drivingTime);
                if (drivingTime!=null){
                    int dt=Integer.parseInt(drivingTime);
                    if(dt<=240){
                        int hours = (int) Math.floor(dt / 60);
                        int minute = dt % 60;
                        drivingTimeView.setText(hours+"小时"+minute+"分钟");
                        System.out.println(dt);
                        System.out.println("progress:"+dt*100/240);
                        circleProgressView.setEndProgress(dt*100/240);
                        circleProgressView.startProgressAnimation();
                    }else{
                        drivingTimeView.setText("您已持续驾驶时长超过4小时，请适当停车休息一下！");
                        circleProgressView.setEndProgress(100);
                        circleProgressView.setEndColor(R.color.app_color_theme_1);
                        circleProgressView.setStartColor(R.color.app_color_theme_1);
                        circleProgressView.startProgressAnimation();
                    }
                }
            }

        };



/*
        JSONObject allHealthInfo = Me.user.getAllHealthInfo(Me.user.getAccount());
        try {
            JSONObject data =allHealthInfo.getJSONObject("data");
            data.getString("");

            System.out.println(allHealthInfo.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
 */


        url = view.findViewById(R.id.url);
        url.setOnClickListener(this::onClick);

        class TimeThread extends Thread {
            @Override
            public void run() {
                while (true) {
                    try {
                        System.out.println(Me.user.getAccount());
                        data=User.getAllHealthInfo(Me.user.getAccount());
                        System.out.println(data);
                        temperature= data.getString("temperature");
                        heartBeat= data.getString("heartBeat");
                        bloodPressureHigh= data.getString("bloodPressureHigh");
                        bloodPressureLow= data.getString("bloodPressureLow");
                        oximetry= data.getString("oximetry");
                        drivingTime= data.getString("drivingTime");
                        location= data.getString("location");
                        weather= data.getString("weather");
                        temperatures= data.getString("temperatures");
                        /*
                                                heartBeat=data.getString("heartBeat");
                        bloodPressureHigh=data.getString("bloodPressureHigh");
                        bloodPressureLow=data.getString("bloodPressureLow");
                        oximetry=data.getString("oximetry");
                        drivingTime=data.getString("drivingTime");
                        location=data.getString("location");
                        weather=data.getString("weather");
                        temperatures=data.getString("temperatures");
                         */
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Message msg = new Message();
                    handle.sendMessage(msg);
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }finally {
                        if(temperature.equals("0"))
                            temperature="没有数据";
                        if(heartBeat.equals("0"))
                            heartBeat="没有数据";
                        if(bloodPressureHigh.equals("0"))
                            bloodPressureHigh="没有数据";
                        if(bloodPressureLow.equals("0"))
                            bloodPressureLow="没有数据";
                        if(oximetry.equals("0"))
                            oximetry="没有数据";
                        if(drivingTime.equals("0"))
                            drivingTime="没有数据";
                        if(location.equals("0"))
                            location="没有数据";
                    }
                }
            }
        }
        TimeThread thread = new TimeThread();
        new Thread(thread).start(); //开启线程
    }

    public static String When() {
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("HH");
        String str = df.format(date);
        int a = Integer.parseInt(str);
        if (a > 6 && a <= 12) {
            return "早上";
        }
        if (a > 12 && a <= 13) {
            return "中午";
        }
        if (a > 13 && a <= 18) {
            return "下午";
        }else{
            return "晚上";
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        ScaleAnimationBySpringWayOne.onScaleAnimationBySpringWayOneMiddle(v.findViewById(v.getId()));
        switch (v.getId()) {
            case R.id.home_cardView_1:
                intent = new Intent();
                intent.setClass(getContext(), Temperature.class);
                startActivity(intent);
                break;
            case R.id.home_cardView_2:
                intent = new Intent();
                intent.setClass(getContext(), HeartRate.class);
                startActivity(intent);
                break;
            case R.id.home_cardView_3:
                intent = new Intent();
                intent.setClass(getContext(), BloodPressure.class);
                startActivity(intent);
                break;
            case R.id.home_cardView_4:
                intent = new Intent();
                intent.setClass(getContext(), Oximetry.class);
                startActivity(intent);
                break;
            case R.id.url:
                intent = new Intent();
                intent.setClass(getContext(), News_4.class);
                startActivity(intent);
        }
    }

}

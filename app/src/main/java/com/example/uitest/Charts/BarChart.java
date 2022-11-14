package com.example.uitest.Charts;


import static com.example.uitest.DriverReport.barChangeData;
import static com.example.uitest.DriverReport.cardViewChangeDate;
import static com.github.mikephil.charting.utils.ColorTemplate.VORDIPLOM_COLORS;
import static com.github.mikephil.charting.utils.ColorTemplate.rgb;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.uitest.CardView.MoreAction;
import com.example.uitest.DriverReport;
import com.example.uitest.Me;
import com.example.uitest.R;
import com.example.uitest.object.User;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.xuexiang.xui.widget.progress.HorizontalProgressView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BarChart extends Fragment {
    public static int callUp;
    public static int closeEyes;
    public static int noSafetyBelts;
    public static int other;
    public static int playTelephone;
    public static int smoke;
    public static int yawn;
    private View mView;
    public Handler BarChartHandler;
    Thread changeData = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                while (barChangeData) {
                    barChangeData = false;
                    Message msg = new Message();
                    BarChartHandler.sendMessage(msg);
                }
            }
        }
    });


    private static final int[] VORDIPLOM_COLORS = {
            Color.rgb(192, 255, 140), Color.rgb(255, 247, 140), Color.rgb(255, 208, 140),
            Color.rgb(140, 234, 255), Color.rgb(255, 140, 157), rgb("#2ecc71"),rgb("#50a387")};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.barchart, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject data = Me.user.getActionsTimes(Me.user.getAccount(), DriverReport.selectDate);
                try {
                    closeEyes = data.getInt("closeEyes");
                    yawn = data.getInt("yawn");
                    playTelephone = data.getInt("playTelephone");
                    callUp = data.getInt("callUp");
                    noSafetyBelts = data.getInt("noSafetyBelts");
                    smoke = data.getInt("smoke");
                    other = data.getInt("other");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                BarChartHandler.sendMessage(msg);
            }
        }).start();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BarChartHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                try {
                    System.out.println("barchange");
                    super.handleMessage(msg);
                    com.github.mikephil.charting.charts.BarChart barChart=(com.github.mikephil.charting.charts.BarChart)view.findViewById(R.id.abstracted_chart);
                    initBarChart(barChart); //初始化一个柱状图
                    barChart.getAxisLeft().setDrawAxisLine(false);
                    barChart.animateXY(2500, 2500);
                    barChart.getDescription().setEnabled(false);
                    barChart.setPinchZoom(true);//设置按比例放缩柱状图
                    barChart.setData(setBarData()); //给柱状图添加数据
                    barChart.invalidate(); //让柱状图填充数据后刷新
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        };
        mView=view;
        if(!changeData.isAlive()){
            changeData.start();
        }
    }

    public BarData setBarData() {
        List<BarEntry> entries = new ArrayList<>(); //定义一个数据容器
        //生成随机数数据
        entries.add(new BarEntry(0, DriverReport.closeEyes));
        entries.add(new BarEntry(1, DriverReport.yawn));
        entries.add(new BarEntry(2, DriverReport.playTelephone));
        entries.add(new BarEntry(3, DriverReport.callUp));
        entries.add(new BarEntry(4, DriverReport.noSafetyBelts));
        entries.add(new BarEntry(5, DriverReport.smoke));
        entries.add(new BarEntry(6, DriverReport.other));
        BarDataSet barDataSet = new BarDataSet(entries,null);
        barDataSet.setColors(VORDIPLOM_COLORS);
        BarData barData = new BarData(barDataSet);
        barDataSet.setHighLightAlpha(37);
        return barData; //返回可用于柱状图的数据
    }

    public com.github.mikephil.charting.charts.BarChart initBarChart(com.github.mikephil.charting.charts.BarChart barChart) {
        String []values={"闭眼","打哈欠","玩手机","通话","未系安全带","吸烟","其他"};
        barChart.getLegend().setEnabled(false);
        barChart.setDrawBarShadow(false); // 设置每条柱子的阴影不显示
        barChart.setDrawValueAboveBar(true); // 设置每条柱子的数值显示
        XAxis xAxis = barChart.getXAxis(); // 获取柱状图的x轴
        xAxis.setDrawGridLines(true);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return values[(int)value];
            }
        });
        YAxis yAxisLeft = barChart.getAxisLeft(); // 获取柱状图左侧的y轴
        YAxis yAxisRight = barChart.getAxisRight(); // 获取柱状图右侧的y轴
        setAxis(xAxis, yAxisLeft, yAxisRight); //调用方法设置柱状图的轴线
        barChart.setBorderWidth(3f);
        barChart.setBorderColor(getResources().getColor(android.R.color.holo_blue_light));


        return barChart;
    }

    public void setAxis(XAxis xAxis, YAxis leftAxis, YAxis rightAxis) {
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // 这里设置x轴在柱状图底部显示
        xAxis.setDrawAxisLine(true); //设置x轴的轴线显示
        xAxis.setDrawGridLines(false);//设置x轴的表格线不显示
        xAxis.setEnabled(true); // 设置x轴显示
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);//设置最小间隔，防止当放大时，出现重复标签。



        leftAxis.setAxisMinimum(0); //设置y轴从0刻度开始
        leftAxis.setDrawGridLines(false); // 这里设置左侧y轴不显示表格线
        leftAxis.setDrawAxisLine(true); // 这里设置左侧y轴显示轴线
        leftAxis.setAxisLineWidth(1); //设置y轴宽度
        leftAxis.setEnabled(true); //设置左侧的y轴显示

        rightAxis.setAxisMinimum(0); //设置y轴从0刻度开始
        rightAxis.setDrawGridLines(false);// 这里设置右侧y轴不显示表格线
        rightAxis.setDrawAxisLine(true); // 这里设置右侧y轴显示轴线
        rightAxis.setAxisLineWidth(1); //设置右侧y轴宽度
        rightAxis.setEnabled(true); //设置右侧的y轴显示

    }


}

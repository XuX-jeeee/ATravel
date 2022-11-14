package com.example.uitest.Charts;

import static com.example.uitest.DriverReport.cardViewChangeDate;
import static com.example.uitest.DriverReport.pieChangeData;
import static com.github.mikephil.charting.utils.ColorTemplate.rgb;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.uitest.DriverReport;
import com.example.uitest.Me;
import com.example.uitest.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;
import com.example.uitest.object.User;

import org.json.JSONObject;

public class PieChart extends Fragment {
    public static int callUp;
    public static int closeEyes;
    public static int noSafetyBelts;
    public static int other;
    public static int playTelephone;
    public static int smoke;
    public static int yawn;
    public static com.github.mikephil.charting.charts.PieChart mPieChart;
    private View mView;
    public Handler PieChartHandle;
    Thread changeData = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                while (pieChangeData) {
                    pieChangeData = false;
                    Message msg = new Message();
                    PieChartHandle.sendMessage(msg);
                }
            }
        }
    });

    private static final int[] VORDIPLOM_COLORS = {
            Color.rgb(192, 255, 140), Color.rgb(255, 247, 140), Color.rgb(255, 208, 140),
            Color.rgb(140, 234, 255), Color.rgb(255, 140, 157), rgb("#2ecc71"),rgb("#50a387")};


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
                PieChartHandle.sendMessage(msg);
            }
        }).start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.piechart, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        PieChartHandle = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                System.out.println("piechange");
                init(view);
                ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
                entries.add(new PieEntry(DriverReport.closeEyes, "闭眼"));
                entries.add(new PieEntry(DriverReport.yawn, "打哈欠"));
                entries.add(new PieEntry(DriverReport.playTelephone, "玩手机"));
                entries.add(new PieEntry(DriverReport.callUp, "通话"));
                entries.add(new PieEntry(DriverReport.noSafetyBelts, "未系安全带"));
                entries.add(new PieEntry(DriverReport.smoke, "吸烟"));
                entries.add(new PieEntry(DriverReport.other, "其他"));
                //设置数据
                setData(entries);
                //默认动画
                mPieChart.animateY(1400, Easing.EaseInOutQuad);

                //设置图例
                Legend l = mPieChart.getLegend();
                //设置显示的位置，低版本用的是setPosition();
                l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
                //设置是否显示图例
                l.setDrawInside(false);
                l.setEnabled(true);

                // 输入图例标签样式
                mPieChart.setEntryLabelColor(Color.BLUE);
                mPieChart.setEntryLabelTextSize(18f);
                mPieChart.getData().notifyDataChanged();
                mPieChart.notifyDataSetChanged();
                mPieChart.invalidate();
            }

        };
        mView=view;
        if(!changeData.isAlive()){
            changeData.start();
        }
    }


    public void init(View view){
        mPieChart = (com.github.mikephil.charting.charts.PieChart) view.findViewById(R.id.mPieChart);
        mPieChart.setUsePercentValues(true); //设置是否使用百分值,默认不显示
        mPieChart.getDescription().setEnabled(false);
        mPieChart.setDragDecelerationFrictionCoef(0.95f);
        mPieChart.setRotationEnabled(true);

        //是否设置中心文字
        mPieChart.setDrawCenterText(true);
        //绘制中间文字
        SpannableString sp = new SpannableString("");
        mPieChart.setCenterText(sp);
        mPieChart.setExtraOffsets(20.f, 0.f, 20.f, 0.f);

        //设置是否为实心图，以及空心时 中间的颜色
        mPieChart.setDrawHoleEnabled(true);
        mPieChart.setHoleColor(Color.WHITE);

        //设置圆环信息
        mPieChart.setTransparentCircleColor(Color.WHITE);//设置透明环颜色
        mPieChart.setTransparentCircleAlpha(110);//设置透明环的透明度
        mPieChart.setHoleRadius(55f);//设置内圆的大小
        mPieChart.setTransparentCircleRadius(60f);//设置透明环的大小

        mPieChart.setRotationAngle(0);
        // 触摸旋转
        mPieChart.setRotationEnabled(true);
        //选中变大
        mPieChart.setHighlightPerTapEnabled(true);
        mPieChart.setDrawSliceText(false);
        //模拟数据

    }

    //设置数据
    private void setData(ArrayList<PieEntry> entries) {
        PieDataSet dataSet = new PieDataSet(entries, "");
        //设置个饼状图之间的距离
        dataSet.setSliceSpace(0f);
        //颜色的集合，按照存放的顺序显示

        dataSet.setColors(VORDIPLOM_COLORS);

        //设置折线
        dataSet.setValueLinePart1OffsetPercentage(80.f);

        //设置线的长度
        dataSet.setValueLinePart1Length(0.3f);
        dataSet.setValueLinePart2Length(0.3f);
        dataSet.setValueLineColor(R.color.green_500);
        //设置文字和数据图外显示
        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData data = new PieData(dataSet);
        dataSet.getValueTextColor(R.color.green_500);


        data.setValueTextSize(14f);
        data.setValueTextColor(R.color.green_500);

        //文字的颜色
        mPieChart.setData(data);
        data.setDrawValues(true);
        //百分比设置
        data.setValueFormatter(new PercentFormatter(mPieChart));
        // 撤销所有的亮点
        mPieChart.highlightValues(null);
        mPieChart.invalidate();
        mPieChart.notifyDataSetChanged();
    }

}

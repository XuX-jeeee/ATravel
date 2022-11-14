package com.example.uitest.utils;


import android.graphics.Color;

import com.example.uitest.CardView.ActionOne;
import com.example.uitest.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.Calendar;
import java.util.List;

/**
 * 图表工具
 * Created by yangle on 2016/11/29.
 */
public class ChartUtils {

    public static int dayValue = 3;
    public static int weekValue = 0;

    /**
     * 初始化图表
     *
     * @param chart 原始图表
     * @return 初始化后的图表
     */
    public static LineChart initChart(LineChart chart) {
        // 不显示数据描述
        chart.getDescription().setEnabled(false);
        // 没有数据的时候，显示“暂无数据”
        chart.setNoDataText("暂无数据");
        // 不显示表格颜色
        chart.setDrawGridBackground(false);
        // 不可以缩放
        chart.setScaleEnabled(true);
        // 不显示y轴右边的值
        chart.getAxisRight().setEnabled(false);
        // 不显示图例
        Legend legend = chart.getLegend();
        legend.setEnabled(false);
        // 向左偏移15dp，抵消y轴向右偏移的30dp
        chart.setExtraLeftOffset(-15);

        XAxis xAxis = chart.getXAxis();
        // 不显示x轴
        xAxis.setDrawAxisLine(false);
        // 设置x轴数据的位置
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(12);
        xAxis.setGridColor(Color.parseColor("#30FFFFFF"));
        // 设置x轴数据偏移量
        xAxis.setYOffset(-12);

        YAxis yAxis = chart.getAxisLeft();
        // 不显示y轴
        yAxis.setDrawAxisLine(false);
        // 设置y轴数据的位置
        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        // 不从y轴发出横向直线
        yAxis.setDrawGridLines(false);
        yAxis.setTextSize(12);
        // 设置y轴数据偏移量
        yAxis.setXOffset(30);
        yAxis.setYOffset(-3);
        yAxis.setAxisMinimum(0);

        //Matrix matrix = new Matrix();
        // x轴缩放1.5倍
        //matrix.postScale(1.5f, 1f);
        // 在图表动画显示之前进行缩放
        //chart.getViewPortHandler().refresh(matrix, chart, false);
        // x轴执行动画
        //chart.animateX(2000);
        chart.invalidate();
        return chart;
    }

    /**
     * 设置图表数据
     *
     * @param chart  图表
     * @param values 数据
     */
    public static void setChartData(LineChart chart, List<Entry> values) {
        LineDataSet lineDataSet;
        try {
            if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
                lineDataSet = (LineDataSet) chart.getData().getDataSetByIndex(0);
                lineDataSet.setValues(values);
                System.out.println(chart.getData().toString());
                chart.getData().notifyDataChanged();
                chart.notifyDataSetChanged();
            } else {
                lineDataSet = new LineDataSet(values, "");
                // 设置曲线颜色
                lineDataSet.setColor(Color.parseColor("#51ACFA"));
                // 设置平滑曲线
                lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                // 不显示坐标点的小圆点
                lineDataSet.setDrawCircles(false);
                // 不显示坐标点的数据
                lineDataSet.setDrawValues(false);
                // 不显示定位线
                lineDataSet.setHighlightEnabled(false);

                LineData data = new LineData(lineDataSet);
                chart.setData(data);
                chart.invalidate();

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 更新图表
     *
     * @param chart     图表
     * @param values    数据
     * @param valueType 数据类型
     */
    public static void notifyDataSetChanged(LineChart chart, List<Entry> values,final int valueType) {
        String[] dayValues = new String[7];
        long currentTime = System.currentTimeMillis();
        for (int i = 6; i >= 0; i--) {
            dayValues[i] = TimeUtils.dateToString(currentTime, TimeUtils.dateFormat_day);
            currentTime -= (2 * 60 * 1000);
            String[] week = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
            String[] monthValues = new String[7];
            for (int j = 6; j >= 0; j--) {
                monthValues[j] = TimeUtils.dateToString(currentTime, TimeUtils.dateFormat_month);
                currentTime -= (4 * 24 * 60 * 60 * 1000);
            }
            chart.getXAxis().setValueFormatter(new ValueFormatter() {
                @Override
                public String getAxisLabel(float value, AxisBase axis) {
                    if (valueType==0)
                        return week[(int) value];
                    else if(valueType==1)
                        return ActionOne.xValues.get((int)value);
                    else
                        return dayValues[(int) value];

                }
            });
            chart.invalidate();
            setChartData(chart, values);
        }

        /**
         * x轴数据处理
         *
         * @param valueType 数据类型
         * @return x轴数据
         */
    }
}


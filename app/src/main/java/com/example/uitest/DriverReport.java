package com.example.uitest;


import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.uitest.Charts.BarChart;
import com.example.uitest.Charts.CardView;
import com.example.uitest.Charts.HorizontalProgress;
import com.example.uitest.Charts.PieChart;
import com.example.uitest.object.Listitem;
import com.example.uitest.object.User;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.xuexiang.xui.widget.toast.XToast;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import me.zhouzhuo.zzhorizontalcalenderview.ZzHorizontalCalenderView;

public class DriverReport extends Fragment {
    private static ViewPager viewPager;
    private static SmartTabLayout smartTabLayout;
    private FragmentPagerItemAdapter adapter;
    public static String selectDate;
    public static boolean flag1=true;
    public static boolean flag2=true;
    public static boolean flag3=true;
    public static int callUp;
    public static int closeEyes;
    public static int drinking;
    public static int noSafetyBelts;
    public static int nodding;
    public static int other;
    public static int playTelephone;
    public static int touchingHairAndMakingUp;
    public static int reachingBehindAndTurningHead;
    public static int smoke;
    public static int operatingTheRadio;
    public static int yawn;

    private static boolean changeDate=true;
    public static boolean cardViewChangeDate=false;
    public static boolean barChangeData=false;
    public static boolean pieChangeData=false;
    private int position;

    Thread changeData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.driver_report, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ZzHorizontalCalenderView mView = (ZzHorizontalCalenderView) view.findViewById(R.id.zz_horizontal_calender_view);
        viewPager=view.findViewById(R.id.viewpager);
        smartTabLayout=view.findViewById(R.id.viewpagertab);
        User.init();
        User.setActionDate();
        ButterKnife.bind(getActivity());
        adapter = new FragmentPagerItemAdapter(
                getActivity().getSupportFragmentManager(), FragmentPagerItems.with(getActivity())
                .add("分析", CardView.class)
                .add("条形图", BarChart.class)
                .add("饼图", PieChart.class)
                .create());
        viewPager.setAdapter(adapter);
        smartTabLayout.setViewPager(viewPager);
        //动态设置各种属性值：
        mView.setShowPickDialog(true);
        mView.setUnitColorResId(android.R.color.holo_green_dark);
        mView.setDayTextColorNormalResId(R.color.black);
        mView.setTodayPointColor(Color.YELLOW);
        mView.setMonthTextColor(Color.RED);
        mView.setYearTextColor(Color.BLUE);
        selectDate=mView.getSelectedDate();
        changeData = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    while (changeDate) {
                        changeDate=false;
                        System.out.println("CardView Done!");
                        System.out.println(DriverReport.selectDate);
                        JSONObject data = Me.user.getActionsTimes(Me.user.getAccount(), DriverReport.selectDate);
                        try {
                            closeEyes = data.getInt("closeEyes");
                            yawn = data.getInt("yawn");
                            playTelephone = data.getInt("playTelephone");
                            callUp = data.getInt("callUp");
                            noSafetyBelts = data.getInt("noSafetyBelts");
                            smoke = data.getInt("smoke");
                            other = data.getInt("other");
                            touchingHairAndMakingUp = data.getInt("touchingHairAndMakingUp");
                            reachingBehindAndTurningHead = data.getInt("reachingBehindAndTurningHead");
                            nodding = data.getInt("nodding");
                            drinking = data.getInt("drinking");
                            operatingTheRadio = data.getInt("operatingTheRadio");

                            cardViewChangeDate=true;
                            barChangeData=true;
                            pieChangeData=true;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        if(!changeData.isAlive())
            changeData.start();
        //日点击监听
        mView.setOnDaySelectedListener(new ZzHorizontalCalenderView.OnDaySelectedListener() {
            @Override
            public void onSelected(boolean hasChanged, int year, int month, int day, int week) {
                changeDate=true;
                selectDate=mView.getSelectedDate();
            }
        });
        //年月点击监听
        mView.setOnYearMonthClickListener(new ZzHorizontalCalenderView.OnYearMonthClickListener() {
            @Override
            public void onYearClick(int selectedYear, int selectedMonth) {
                changeDate=true;
                selectDate=mView.getSelectedDate();
            }

            @Override
            public void onMonthClick(int selectedYear, int selectedMonth) {
                changeDate=true;
                selectDate=mView.getSelectedDate();
            }
        });
    }
}
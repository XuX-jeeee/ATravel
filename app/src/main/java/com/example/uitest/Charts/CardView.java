package com.example.uitest.Charts;

import static com.example.uitest.DriverReport.cardViewChangeDate;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.uitest.CardView.ActionFive;
import com.example.uitest.CardView.ActionFour;
import com.example.uitest.CardView.ActionOne;
import com.example.uitest.CardView.ActionSix;
import com.example.uitest.CardView.ActionThree;
import com.example.uitest.CardView.ActionTwo;
import com.example.uitest.CardView.MoreAction;
import com.example.uitest.DriverReport;
import com.example.uitest.Me;
import com.example.uitest.R;
import com.example.uitest.object.Listitem;
import com.example.uitest.tools.ScaleAnimationBySpringWayOne;
import com.example.uitest.tools.SpringScaleInterpolator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CardView extends Fragment implements View.OnClickListener {
    private List<Integer> actionsTime=new ArrayList();
    private androidx.cardview.widget.CardView c1;
    private androidx.cardview.widget.CardView c2;
    private androidx.cardview.widget.CardView c3;
    private androidx.cardview.widget.CardView c4;
    private androidx.cardview.widget.CardView c5;
    private androidx.cardview.widget.CardView c6;
    private androidx.cardview.widget.CardView c7;
    private TextView closeEyesTimes;
    private TextView yawnTimes;
    private TextView playTelephoneTimes;
    private TextView callUpTimes;
    private TextView touchingHairAndMakingUpTimes;
    private TextView noSafetyBeltsTimes;
    private TextView smokeTimes;
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



    public Handler CardViewHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            System.out.println("The datas of CardView has been change");
            closeEyesTimes.setText(String.valueOf(closeEyes));
            yawnTimes.setText(String.valueOf(yawn));
            playTelephoneTimes.setText(String.valueOf(playTelephone));
            callUpTimes.setText(String.valueOf(callUp));
            smokeTimes.setText(String.valueOf(smoke));
            noSafetyBeltsTimes.setText(String.valueOf(noSafetyBelts));
        }

    };
    Thread changeData = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                while (cardViewChangeDate) {
                    cardViewChangeDate = false;
                    Message msg = new Message();
                    CardViewHandler.sendMessage(msg);
                }
            }
        }
    });
    Thread refreshData = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true){
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
                DriverReport.barChangeData=true;
                DriverReport.pieChangeData=true;
                Message msg = new Message();
                CardViewHandler.sendMessage(msg);
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    });

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
                    touchingHairAndMakingUp = data.getInt("touchingHairAndMakingUp");
                    reachingBehindAndTurningHead = data.getInt("reachingBehindAndTurningHead");
                    nodding = data.getInt("nodding");
                    drinking = data.getInt("drinking");
                    operatingTheRadio = data.getInt("operatingTheRadio");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                CardViewHandler.sendMessage(msg);
            }
        }).start();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(!changeData.isAlive()){
            changeData.start();
        }
        return inflater.inflate(R.layout.cardview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        c1=(androidx.cardview.widget.CardView)view.findViewById(R.id.card1);
        c2=(androidx.cardview.widget.CardView)view.findViewById(R.id.card2);
        c3=(androidx.cardview.widget.CardView)view.findViewById(R.id.card3);
        c4=(androidx.cardview.widget.CardView)view.findViewById(R.id.card4);
        c5=(androidx.cardview.widget.CardView)view.findViewById(R.id.card5);
        c6=(androidx.cardview.widget.CardView)view.findViewById(R.id.card6);
        c7=(androidx.cardview.widget.CardView)view.findViewById(R.id.more);
        ScaleAnimationBySpringWayOne.onScaleAnimationBySpringWayOne(c1);
        ScaleAnimationBySpringWayOne.onScaleAnimationBySpringWayOne(c2);
        ScaleAnimationBySpringWayOne.onScaleAnimationBySpringWayOne(c3);
        ScaleAnimationBySpringWayOne.onScaleAnimationBySpringWayOne(c4);
        ScaleAnimationBySpringWayOne.onScaleAnimationBySpringWayOne(c5);
        ScaleAnimationBySpringWayOne.onScaleAnimationBySpringWayOne(c6);
        ScaleAnimationBySpringWayOne.onScaleAnimationBySpringWayOne(c7);
        c1.setOnClickListener(this);
        c2.setOnClickListener(this);
        c3.setOnClickListener(this);
        c4.setOnClickListener(this);
        c5.setOnClickListener(this);
        c6.setOnClickListener(this);
        c7.setOnClickListener(this);
        callUpTimes=view.findViewById(R.id.call_up_times);
        closeEyesTimes=view.findViewById(R.id.close_eyes_times);
        playTelephoneTimes=view.findViewById(R.id.play_telephone_times);
        smokeTimes=view.findViewById(R.id.smoke_times);
        noSafetyBeltsTimes=view.findViewById(R.id.no_safety_belts_times);
        yawnTimes=view.findViewById(R.id.yawn_times);
        if(!refreshData.isAlive()){
            refreshData.start();
        }
    }


    @Override
    public void onClick(View v) {
        Intent intent=new Intent();
        ScaleAnimationBySpringWayOne.onScaleAnimationBySpringWayOne(v.findViewById(v.getId()));
        switch (v.getId()){
            case R.id.card1:
                intent.setClass(getContext(), ActionOne.class);
                startActivity(intent);
                break;
            case R.id.card2:
                intent.setClass(getContext(), ActionTwo.class);
                startActivity(intent);
                break;
            case R.id.card3:
                intent.setClass(getContext(), ActionThree.class);
                startActivity(intent);
                break;
            case R.id.card4:;
                intent.setClass(getContext(), ActionFour.class);
                startActivity(intent);
                break;
            case R.id.card5:
                intent.setClass(getContext(), ActionFive.class);
                startActivity(intent);
                break;
            case R.id.card6:
                intent.setClass(getContext(), ActionSix.class);
                startActivity(intent);
                break;
            case R.id.more:


                intent.setClass(getContext(), MoreAction.class);
                startActivity(intent);
                break;
        }
    }

    public void getData(JSONObject data) throws JSONException {
        actionsTime.add(data.getInt("closeEyes"));
        actionsTime.add(data.getInt("yawn"));
        actionsTime.add(data.getInt("playTelephone"));
        actionsTime.add(data.getInt("callUp"));
        actionsTime.add(data.getInt("noSafetyBelts"));
        actionsTime.add(data.getInt("smoke"));
        actionsTime.add(data.getInt("other"));
        System.out.println(actionsTime.toString());
    }
}

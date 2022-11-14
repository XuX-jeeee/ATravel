package com.example.uitest.me;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.uitest.R;
import com.example.uitest.object.Me;
import com.example.uitest.tools.ScaleAnimationBySpringWayOne;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView;

public class PersonalInformation extends AppCompatActivity implements View.OnClickListener, Me {
    private SuperTextView superTextView1;
    private SuperTextView superTextView2;
    private SuperTextView superTextView3;
    private SuperTextView superTextView4;
    private SuperTextView superTextView5;
    private SuperTextView superTextView6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);

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
        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        superTextView1 = findViewById(R.id.change_personal_information_btn);
        superTextView2 = findViewById(R.id.info_age);
        superTextView3 = findViewById(R.id.info_gender);
        superTextView4 = findViewById(R.id.info_location);
        superTextView5 = findViewById(R.id.info_name);
        superTextView6 = findViewById(R.id.info_relatives_phone);
        superTextView2.setRightString(Me.user.getAge());
        superTextView3.setRightString(Me.user.getGender());
        superTextView4.setRightString(Me.user.getAddress());
        superTextView5.setRightString(Me.user.getUsername());
        superTextView6.setRightString(Me.user.getRelativesPhone());
        superTextView1.setOnClickListener(this);
        superTextView2.setOnClickListener(this);
        superTextView3.setOnClickListener(this);
        superTextView4.setOnClickListener(this);
        superTextView5.setOnClickListener(this);
        superTextView6.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        ScaleAnimationBySpringWayOne.onScaleAnimationBySpringWayOneMiddle(v.findViewById(v.getId()));
        if(v.getId()==R.id.change_personal_information_btn){
            Intent intent = new Intent();
            intent.setClass(this,ChangeInformation.class);
            intent.putExtra("info_age",superTextView2.getRightString());
            intent.putExtra("info_gender",superTextView3.getRightString());
            intent.putExtra("info_location",superTextView4.getRightString());
            intent.putExtra("info_name",superTextView5.getRightString());
            startActivity(intent);
            finish();
        }
    }
}
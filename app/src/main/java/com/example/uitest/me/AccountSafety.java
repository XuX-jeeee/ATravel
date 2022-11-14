package com.example.uitest.me;

import static com.baidu.aip.asrwakeup3.core.inputstream.InFileStream.getContext;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.uitest.MainActivity;
import com.example.uitest.R;
import com.example.uitest.object.Me;
import com.example.uitest.tools.ScaleAnimationBySpringWayOne;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.xuexiang.xui.widget.dialog.DialogLoader;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView;
import com.xuexiang.xui.widget.toast.XToast;

public class AccountSafety extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_safety);

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

        CardView superTextView1 = findViewById(R.id.change_safety_phone);
        CardView superTextView2 = findViewById(R.id.change_password);
        CardView superTextView3 = findViewById(R.id.call);
        superTextView1.setOnClickListener(this);
        superTextView2.setOnClickListener(this);
        superTextView3.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        Intent intent=new Intent();
        ScaleAnimationBySpringWayOne.onScaleAnimationBySpringWayOneMiddle(v.findViewById(v.getId()));
        switch (v.getId()){
            case R.id.change_safety_phone:
                intent.setClass(AccountSafety.this,ChangeRelativesPhone.class);
                startActivity(intent);
                break;
            case R.id.change_password:
                intent.setClass(AccountSafety.this,ChangePassword.class);
                startActivity(intent);
                break;
            case R.id.call:
                if(Me.user.getRelativesPhone()!=null){
                    MainActivity.Call=true;
                }else{
                    XToast.normal(this,"您没有绑定紧急联系人电话哦！").show();
                }
                break;
        }
    }
}
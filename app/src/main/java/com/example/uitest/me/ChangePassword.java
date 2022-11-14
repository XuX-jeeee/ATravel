package com.example.uitest.me;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.example.uitest.R;
import com.example.uitest.Reigister;
import com.example.uitest.object.Me;
import com.example.uitest.object.User;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.xuexiang.xui.widget.popupwindow.ViewTooltip;

public class ChangePassword extends AppCompatActivity implements Me {
    private TitleBar titleBar;
    private EditText oldpd;
    private EditText password1;
    private EditText password2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

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
        oldpd=findViewById(R.id.old_pd);
        password1=findViewById(R.id.passwd);
        password2=findViewById(R.id.passwd2);
        titleBar=findViewById(R.id.back);
        titleBar.setOnTitleBarListener(new OnTitleBarListener() {
            @Override
            public void onLeftClick(TitleBar titleBar) {
                finish();
            }
            @Override
            public void onRightClick(TitleBar titleBar) {
                if(oldpd.getText().toString().equals(Me.user.getPassword())){
                    if(password1.getText().toString().equals("")){
                        ViewTooltip
                                .on(password1)
                                .color(Color.BLACK)
                                .position(ViewTooltip.Position.BOTTOM)
                                .text("密码输入为空")
                                .padding(10,10,10,10)
                                .clickToHide(true)
                                .autoHide(true, 2000)
                                .animation(new ViewTooltip.FadeTooltipAnimation(500))
                                .show();
                    }else if (password2.getText().toString().equals("")){
                        ViewTooltip
                                .on(password2)
                                .color(Color.BLACK)
                                .padding(10,10,10,10)
                                .position(ViewTooltip.Position.BOTTOM)
                                .text("密码输入为空")
                                .clickToHide(true)
                                .autoHide(true, 2000)
                                .animation(new ViewTooltip.FadeTooltipAnimation(500))
                                .show();
                    }else {
                        if(password1.getText().toString().equals(password2.getText().toString())){

                            changePassword(password1.getText().toString());
                            Me.user.setPassword(password1.getText().toString());
                        }else {
                            ViewTooltip
                                    .on(password2)
                                    .color(Color.BLACK)
                                    .padding(10,10,10,10)
                                    .position(ViewTooltip.Position.BOTTOM)
                                    .text("两次密码输入不一致")
                                    .clickToHide(true)
                                    .autoHide(true, 2000)
                                    .animation(new ViewTooltip.FadeTooltipAnimation(500))
                                    .show();
                        }
                    }
                }else{
                    ViewTooltip
                            .on(oldpd)
                            .color(Color.BLACK)
                            .padding(10,10,10,10)
                            .position(ViewTooltip.Position.BOTTOM)
                            .text("密码输入不正确")
                            .clickToHide(true)
                            .autoHide(true, 2000)
                            .animation(new ViewTooltip.FadeTooltipAnimation(500))
                            .show();
                }
            }

        });
    }

    public void changePassword(String password){
        new Thread(new Runnable() {
            @Override
            public void run() {
                User.changePassword(Me.user.getAccount(),oldpd.getText().toString(),password);
            }
        }).start();
    }
}
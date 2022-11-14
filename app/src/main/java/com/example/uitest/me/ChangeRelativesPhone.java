package com.example.uitest.me;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.baidu.aip.asrwakeup3.core.inputstream.InFileStream.getContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.uitest.R;
import com.example.uitest.Reigister;
import com.example.uitest.object.Me;
import com.example.uitest.object.User;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.jpeng.jptabbar.DensityUtils;
import com.xuexiang.xui.widget.popupwindow.ViewTooltip;
import com.xuexiang.xui.widget.popupwindow.popup.XUIPopup;

public class ChangeRelativesPhone extends AppCompatActivity implements Me {
    private TitleBar titleBar;
    private EditText relativesPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_relatives_phone);
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
        relativesPhone=findViewById(R.id.relatives_phone);
        titleBar=findViewById(R.id.back);
        titleBar.setOnTitleBarListener(new OnTitleBarListener() {

            @Override
            public void onLeftClick(TitleBar titleBar) {
                finish();
            }

            @Override
            public void onRightClick(TitleBar titleBar) {
                if(Reigister.isPhoneNumber(relativesPhone.getText().toString())){
                    if(relativesPhone.getText().toString().equals(Me.user.getAccount()))
                    {
                        ViewTooltip
                                .on(relativesPhone)
                                .color(Color.BLACK)
                                .position(ViewTooltip.Position.BOTTOM)
                                .text("紧急联系人电话不能与自己相同")
                                .clickToHide(true)
                                .autoHide(true, 2000)
                                .animation(new ViewTooltip.FadeTooltipAnimation(500))
                                .show();
                    }else{
                        String rp=relativesPhone.getText().toString();
                        Me.user.setRelativesPhone(rp);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                User.changeRelativesPhone(Me.user.getAccount(),rp);
                            }
                        }).start();
                        finish();
                    }
                }else{
                    ViewTooltip
                            .on(relativesPhone)
                            .color(Color.BLACK)
                            .position(ViewTooltip.Position.BOTTOM)
                            .text("手机号格式输入错误")
                            .clickToHide(true)
                            .autoHide(true, 2000)
                            .animation(new ViewTooltip.FadeTooltipAnimation(500))
                            .show();
                }
            }

        });


    }
}
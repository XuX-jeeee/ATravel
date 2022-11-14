package com.example.uitest.me;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.uitest.R;
import com.example.uitest.object.Me;
import com.example.uitest.object.User;
import com.github.mikephil.charting.data.Entry;
import com.xuexiang.xui.widget.toast.XToast;

public class ChangeInformation extends AppCompatActivity implements Me {
    private EditText editText1;
    private EditText editText2;
    private EditText editText3;
    private EditText editText4;
    private ImageView back;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_information);
        String nickname = (String) getIntent().getExtras().get("info_name");
        String gender = (String) getIntent().getExtras().get("info_gender");
        String location = (String) getIntent().getExtras().get("info_location");
        String age = (String) getIntent().getExtras().get("info_age");
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
        editText1=findViewById(R.id.item_1);
        editText2=findViewById(R.id.item_2);
        editText3=findViewById(R.id.item_3);
        editText4=findViewById(R.id.item_4);
        editText1.setText(nickname);
        editText2.setText(gender);
        editText3.setText(age);
        editText4.setText(location);

        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent();
                intent.setClass(ChangeInformation.this,PersonalInformation.class);
                startActivity(intent);
                finish();
            }
        });

        textView=findViewById(R.id.confirm);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickname = editText1.getText().toString();
                String gender = editText2.getText().toString();
                String age =editText3.getText().toString();
                String address = editText4.getText().toString();
                if(gender.equals("男")||gender.equals("女")){
                    Me.user.setUsername(nickname);
                    Me.user.setGender(gender);
                    Me.user.setAge(age);
                    Me.user.setAddress(address);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            User.changeInfo(Me.user.getAccount(),nickname,gender,age,address);
                        }
                    }).start();
                    Intent intent =new Intent();
                    intent.setClass(ChangeInformation.this,PersonalInformation.class);
                    startActivity(intent);
                    finish();
                }else{
                    XToast.error(ChangeInformation.this,"性别输入错误，请重新输入！");
                }
            }
        });
    }
}
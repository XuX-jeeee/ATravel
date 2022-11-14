package com.example.uitest;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.example.uitest.object.Listitem;
import com.example.uitest.object.Me;
import com.example.uitest.object.User;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.xuexiang.xui.widget.popupwindow.ViewTooltip;

public class ChangeMemorandum extends AppCompatActivity {
    private TitleBar titleBar;
    private EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_memorandum);
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
        editText=findViewById(R.id.change_memorandum);
        titleBar=findViewById(R.id.back);
        Intent intent = getIntent();
        String ct=intent.getStringExtra("context");
        String n=intent.getStringExtra("n");
        System.out.println(ct);
        editText.setText(ct);
        titleBar.setOnTitleBarListener(new OnTitleBarListener() {

            @Override
            public void onLeftClick(TitleBar titleBar) {
                finish();
            }

            @Override
            public void onRightClick(TitleBar titleBar) {
                /*
                                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("n="+n);
                        User.editMemorandum(Me.user.getAccount(),n,editText.getText().toString());

                    }
                }).start();
                 */
                Listitem listitem = (Listitem) Memorandum.adapter.getItem(Integer.parseInt(n));
                listitem.setContent(editText.getText().toString());
                Memorandum.adapter.notifyDataSetChanged();
                finish();
            }
        });
    }
}
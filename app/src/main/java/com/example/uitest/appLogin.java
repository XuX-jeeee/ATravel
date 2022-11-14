package com.example.uitest;

import static android.widget.Button.OnClickListener;

import static com.baidu.aip.asrwakeup3.core.inputstream.InFileStream.getContext;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.uitest.object.Me;
import com.example.uitest.object.User;
import com.example.uitest.tools.ScaleAnimationBySpringWayOne;
import com.xuexiang.xui.widget.toast.XToast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class appLogin extends AppCompatActivity implements Me {
    //声明组件变量
    private EditText weixinNumber;
    private EditText password;
    //自定义的一个Hander消息机制
    private ImageView visible;
    private String account;
    private String nickname;
    private String gender;
    private String age;
    private String address;
    private String relativesPhone;
    JSONObject data;
    private MyHander myhander = new MyHander();
    boolean flag=true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout); //设置布局
        /* 隐藏自带标题*/
        weixinNumber = (EditText) this.findViewById(R.id.username);
        password = (EditText) this.findViewById(R.id.password);
        visible=findViewById(R.id.visible);
        visible.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag){
                    visible.setImageResource(R.drawable.visible);
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    flag=false;
                }else{
                    visible.setImageResource(R.drawable.unvisible);
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    flag=true;
                }
            }
        });
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN //全屏显示
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR; //因为背景为浅色所以将状态栏字体设置为黑色
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        TextView register = (TextView) findViewById(R.id.register);
        initViews();  // 初始化布局元素
        /*获取注册activity传过来的微信号*/
        Intent intent = getIntent();
        String number = intent.getStringExtra("weixin_number");
        //把传过来的值显示在登录布局上
        weixinNumber.setText(number);

        TextView login =(TextView)findViewById(R.id.login);
        login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ScaleAnimationBySpringWayOne.onScaleAnimationBySpringWayOneMiddle(login);
                //创建一个进度条的activity,通过AndroidMainfest.xml文件声明为对话框，这样activity就不会覆盖当前的activity
                Intent intent = new Intent();
                intent.setClass(appLogin.this, Loading.class);
                startActivity(intent);
                // 开一个线程完成网络请求操作
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                            httpUrlConnPost(appLogin.this.weixinNumber.getText().toString() + "",
                                    password.getText().toString() + "");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        register.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(appLogin.this, com.example.uitest.Reigister.class);
                startActivity(intent);
            }
        });
    }



    @SuppressLint("NewApi")
    public void initViews() {
        // 得到所有的组件

    }

    // 发送请求的主要方法
    public void httpUrlConnPost(String number, String password) {
        HttpURLConnection urlConnection = null;
        URL url;
        try {
            // 请求的URL地地址
            url = new URL(
                    "http://124.221.124.7:5000/login");

            urlConnection = (HttpURLConnection) url.openConnection();// 打开http连接
            urlConnection.setConnectTimeout(3000);// 连接的超时时间
            urlConnection.setUseCaches(false);// 不使用缓存
            // urlConnection.setFollowRedirects(false);是static函数，作用于所有的URLConnection对象。
            urlConnection.setInstanceFollowRedirects(true);// 是成员函数，仅作用于当前函数,设置这个连接是否可以被重定向
            urlConnection.setReadTimeout(3000);// 响应的超时时间
            urlConnection.setDoInput(true);// 设置这个连接是否可以写入数据
            urlConnection.setDoOutput(true);// 设置这个连接是否可以输出数据
            urlConnection.setRequestMethod("POST");// 设置请求的方式
            urlConnection.setRequestProperty("Content-Type",
                    "application/json;charset=UTF-8");// 设置消息的类型
            urlConnection.connect();// 连接，从上述至此的配置必须要在connect之前完成，实际上它只是建立了一个与服务器的TCP连接
            JSONObject json = new JSONObject();// 创建json对象
            json.put("account", URLEncoder.encode(number, "UTF-8"));// 使用URLEncoder.encode对特殊和不可见字符进行编码
            json.put("password", URLEncoder.encode(password, "UTF-8"));// 把数据put进json对象中
            String jsonstr = json.toString();// 把JSON对象按JSON的编码格式转换为字符串
            // ------------字符流写入数据------------
            OutputStream out = urlConnection.getOutputStream();// 输出流，用来发送请求，http请求实际上直到这个函数里面才正式发送出去
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));// 创建字符流对象并用高效缓冲流包装它，便获得最高的效率,发送的是字符串推荐用字符流，其它数据就用字节流
            bw.write(jsonstr);// 把json字符串写入缓冲区中
            bw.flush();// 刷新缓冲区，把数据发送出去，这步很重要
            out.close();
            bw.close();// 使用完关闭
            Log.i("aa", urlConnection.getResponseCode() + "");
            //以下判斷是否訪問成功，如果返回的状态码是200则说明访问成功
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {// 得到服务端的返回码是否连接成功
                // ------------字符流读取服务端返回的数据------------
                InputStream in = urlConnection.getInputStream();
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(in));
                String str = null;
                StringBuffer buffer = new StringBuffer();
                while ((str = br.readLine()) != null) {// BufferedReader特有功能，一次读取一行数据
                    buffer.append(str);
                }
                in.close();
                br.close();
                JSONObject rjson = new JSONObject(buffer.toString());
                Log.i("aa", "rjson=" + rjson);// rjson={"json":true}
                String result = rjson.getString("code");// 从rjson对象中得到key值为"json"的数据，这里服务端返回的是一个boolean类型的数据
                data=rjson.getJSONObject("data");
                account = data.getString("account");
                nickname = URLDecoder.decode(data.getString("nickname"),"UTF-8");
                gender = data.getString("gender");
                if(gender.equals("male")) gender = "男";else gender = "女";
                age = data.getString("age");
                address = URLDecoder.decode(data.getString("address"),"UTF-8");
                relativesPhone = data.getString("relativesPhone");

                System.out.println("json:===" + result);
                //如果服务器端返回的是true，则说明登录成功，否则登录失败
                if (result.equals("true")) {// 判断结果是否正确
                    //在Android中http请求，必须放到线程中去作请求，但是在线程中不可以直接修改UI，只能通过hander机制来完成对UI的操作
                    myhander.sendEmptyMessage(1);
                    Log.i("用户：", "登录成功");
                } else {
                    myhander.sendEmptyMessage(2);
                    System.out.println("222222222222222");
                    Log.i("用户：", "登录失败");
                }
            } else {
                myhander.sendEmptyMessage(2);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("aa", e.toString());
            System.out.println("11111111111111111");
            myhander.sendEmptyMessage(2);
        } finally {
            urlConnection.disconnect();// 使用完关闭TCP连接，释放资源
        }
    }

    // 在Android中不可以在线程中直接修改UI，只能借助Handler机制来完成对UI的操作
    class MyHander extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //判断hander的内容是什么，如果是1则说明登录成功，如果是2说明登录失败
            switch (msg.what) {
                case 1:
                    Log.i("aa", msg.what + "");
                    //提示
                    XToast.success(appLogin.this, "登录成功!").show();
                    //通过Intent跳转到微信首页，把账号号传过去
                    Intent intent = new Intent();
                    Me.user.setAccount(account);
                    Me.user.setUsername(nickname);
                    Me.user.setGender(gender);
                    Me.user.setAddress(address);
                    Me.user.setAge(age);
                    Me.user.setRelativesPhone(relativesPhone);
                    Me.user.setPassword(password.getText().toString());
                    intent.setClass(appLogin.this,
                            com.example.uitest.MainActivity.class);
                    startActivity(intent);
                    appLogin.this.finish(); //结束当前actitivy
                    break;
                case 2:
                    Log.i("aa", msg.what + "");
                    //对话框
                    XToast.error(appLogin.this, "用户名或密码错误，请重新填写").show();
                    break;

            }
        }
    }

    //返回按钮处理事件
    public void login_activity_back(View v) {
        /*跳转到微信启动页*/
        Intent intent = new Intent();
        intent.setClass(appLogin.this, MainActivity.class);
        startActivity(intent);
        appLogin.this.finish(); //结束当前activity
    }

    public void getUserData(){

    }
}

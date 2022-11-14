package com.example.uitest;

import static com.example.uitest.R.drawable.add;
import static com.example.uitest.R.id.add_image;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;
import com.example.uitest.object.Listitem;
import com.example.uitest.object.Me;
import com.example.uitest.object.User;
import com.example.uitest.tools.ASRresponse;
import com.example.uitest.tools.ScaleAnimationBySpringWayOne;
import com.google.gson.Gson;
import com.xuexiang.xui.widget.imageview.RadiusImageView;
import com.baidu.aip.asrwakeup3.core.mini.ActivityMiniRecog;
import com.xuexiang.xui.widget.toast.XToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import kotlin.collections.IntIterator;

public class Memorandum extends Fragment implements View.OnClickListener, EventListener {
    private EventManager asr;//语音识别核心库
    private boolean flag=false;
    TextView date;
    TextView time;
    TextView ct;
    ImageView delete;
    ImageView exit;

    private RadiusImageView imageView;
    public static ArrayAdapter adapter;
    private CardView addbtn;
    private ListView listView;
    private List<Listitem> list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.memorandum, null);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initPermission();

        listView=view.findViewById(R.id.Listview);
        addbtn=view.findViewById(R.id.addBtn);
        imageView=view.findViewById(add_image);
        ScaleAnimationBySpringWayOne.onScaleAnimationBySpringWayOne(imageView);
        ScaleAnimationBySpringWayOne.onScaleAnimationBySpringWayOne(addbtn);

        adapter=new MyArrayAdapter(getContext(),R.layout.listview,list);
        listView.setAdapter(adapter);

        imageView.setOnClickListener(this);
        addbtn.setOnClickListener(this);


        //初始化EventManager对象
        asr = EventManagerFactory.create(getContext(), "asr");
        //注册自己的输出事件类
        asr.registerListener(this::onEvent); //  EventListener 中 onEvent方法
        /*
                new Thread(new Runnable() {
            @Override
            public void run() {
                JSONArray data = User.getMemorandum(Me.user.getAccount());
                if(data!=null){
                    System.out.println(data.toString());
                    for(int i=0;i<data.length();i++) {
                        try {
                            JSONObject e= data.getJSONObject(i);
                            adapter.add(new Listitem(e.getString("date"),e.getString("time"),e.getString("note")));
                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                        }
                    }
                }
            }
        }).start();
         */
    }

    @Override
    public void onClick(View v) {
        ScaleAnimationBySpringWayOne.onScaleAnimationBySpringWayOne(imageView);
        ScaleAnimationBySpringWayOne.onScaleAnimationBySpringWayOne(addbtn);
        if(!flag){
            asr.send(SpeechConstant.ASR_START, "{}", null, 0, 0);
            XToast.info(getContext(), "开始识别").show();
            imageView.setImageDrawable(null);
            flag=true;
        }else{
            asr.send(SpeechConstant.ASR_STOP, "{}", null, 0, 0);
            imageView.setImageDrawable(getResources().getDrawable(add));
            XToast.success(getContext(), "识别结束").show();
            flag=false;
        }
    }


    /**
     * 自定义输出事件类 EventListener 回调方法
     */
    @Override
    public void onEvent(String name, String params, byte[] data, int offset, int length) {

        if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL)) {
            // 识别相关的结果都在这里
            if (params == null || params.isEmpty()) {
                XToast.error(getContext(), "没听到呢！请再说一次").show();
                return;
            }
            if (params.contains("\"final_result\"")) {
                Gson gson = new Gson();
                ASRresponse asRresponse = gson.fromJson(params, ASRresponse.class);//数据解析转实体bean
                if(asRresponse == null) return;
                //从日志中，得出Best_result的值才是需要的，但是后面跟了一个中文输入法下的逗号，
                if(asRresponse.getBest_result().contains("，")){//包含逗号  则将逗号替换为空格，这个地方还会问题，还可以进一步做出来，你知道吗？
                    System.out.println("1111111111111");
                    System.out.println(new Listitem(asRresponse.getBest_result().replace('，',' ').trim()));
                    adapter.add(new Listitem(asRresponse.getBest_result().replace('，',' ').trim()));
                    /*
                                        new Thread(new Runnable() {
                        @Override
                        public void run() {
                            User.addMemorandum(Me.user.getAccount(),time.getText().toString(),date.getText().toString(),asRresponse.getBest_result().replace('，',' ').trim());
                        }
                    }).start();
                     */
                }else {//不包含
                    System.out.println("2222222222222");
                    System.out.println(asRresponse.getBest_result().trim());
                    adapter.add(new Listitem(asRresponse.getBest_result().trim()));
                    /*
                                        new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //User.addMemorandum(Me.user.getAccount(),time.getText().toString(),date.getText().toString(),asRresponse.getBest_result().trim());
                        }
                    }).start();
                     */

                }
            }
        }

    }

    private class MyArrayAdapter extends ArrayAdapter {
        private List listItems;
        private int resourceId;
        private LayoutInflater inflater;

        public List getListItems(){
            return listItems;
        }

        public MyArrayAdapter(@NonNull Context context, int resource, @NonNull List objects) {
            super(context, resource, objects);
            inflater=LayoutInflater.from(context);
            listItems=objects;
            resourceId=resource;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Listitem listitem = (Listitem) getItem(position);
            View view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            date=view.findViewById(R.id.Memorandum_date);
            time=view.findViewById(R.id.time);
            delete=view.findViewById(R.id.delete);
            exit=view.findViewById(R.id.edit);
            ct=view.findViewById(R.id.context);
            date.setText(listitem.getDate());
            time.setText(listitem.getTime());
            ct.setText(listitem.getContent());
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listItems.remove(position);
                    adapter.notifyDataSetChanged();
/*
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            User.deleteMemorandum(Me.user.getAccount(),String.valueOf(position));
                        }
                    }).start();
 */
                }
            });
            exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= new Intent();
                    intent.setClass(Memorandum.this.getView().getContext(), ChangeMemorandum.class);
                    intent.putExtra("context",ct.getText().toString());
                    intent.putExtra("n",String.valueOf(position));
                    startActivity(intent);
                }
            });
            return view;
        }
    }


    /**
     * android 6.0 以上需要动态申请权限
     */
    private void initPermission() {
        String permissions[] = {Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        ArrayList<String> toApplyList = new ArrayList<String>();

        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(getContext(), perm)) {
                toApplyList.add(perm);
            }
        }
        String tmpList[] = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), toApplyList.toArray(tmpList), 123);
        }

    }



    /**
     * 权限申请回调，可以作进一步处理
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 此处为android 6.0以上动态授权的回调，用户自行实现。
    }

    /**
     * 初始化控件
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        //发送取消事件
        asr.send(SpeechConstant.ASR_CANCEL, "{}", null, 0, 0);
        //退出事件管理器
        // 必须与registerListener成对出现，否则可能造成内存泄露
        asr.unregisterListener(this);
    }




}
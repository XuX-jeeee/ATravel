package com.example.uitest;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static com.example.uitest.Home.When;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.uitest.me.AccountSafety;
import com.example.uitest.me.AppInformation;
import com.example.uitest.me.PersonalInformation;
import com.example.uitest.me.Settings;
import com.example.uitest.me.WeekReport;
import com.example.uitest.object.User;
import com.example.uitest.tools.ScaleAnimationBySpringWayOne;
import com.example.uitest.utils.PhotoQUtil;
import com.example.uitest.utils.SPUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xuexiang.xui.widget.imageview.RadiusImageView;
import com.xuexiang.xui.widget.toast.XToast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Me extends Fragment implements View.OnClickListener, com.example.uitest.object.Me {
    private CardView meItem1;
    private CardView meItem2;
    private CardView meItem3;
    private CardView meItem4;
    private CardView meItem5;
    private CardView meItem6;
    private ImageView idCard;
    private TextView nickname;
    private TextView id;
    private TextView hello;
    private RadiusImageView avatar;

    //存储拍完照后的图片
    private File outputImagePath;
    //启动相机标识
    public static final int TAKE_PHOTO = 1;

    //是否拥有权限
    private boolean hasPermissions = false;
    //底部弹窗
    private BottomSheetDialog bottomSheetDialog;
    //弹窗视图
    private View bottomView;

    private Uri imageUri;
    public static final int CHOOSE_PHOTO = 2;
    private PopupWindow pop;
    //权限请求
    private RxPermissions rxPermissions;
    String imagePath = null;
    private RequestOptions requestOptions = RequestOptions.circleCropTransform()
            .diskCacheStrategy(DiskCacheStrategy.NONE)//不做磁盘缓存
            .skipMemoryCache(true);//不做内存缓存


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // 使用布局加载器加载
        View view = inflater.inflate(R.layout.me, null);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        meItem1 = view.findViewById(R.id.me_item_1);
        meItem2 = view.findViewById(R.id.me_item_2);
        meItem3 = view.findViewById(R.id.me_item_3);
        meItem4 = view.findViewById(R.id.me_item_4);
        meItem5 = view.findViewById(R.id.me_item_5);
        meItem6 = view.findViewById(R.id.me_item_6);
        ScaleAnimationBySpringWayOne.onScaleAnimationBySpringWayOneMiddle(meItem1);
        ScaleAnimationBySpringWayOne.onScaleAnimationBySpringWayOneMiddle(meItem2);
        ScaleAnimationBySpringWayOne.onScaleAnimationBySpringWayOneMiddle(meItem3);
        ScaleAnimationBySpringWayOne.onScaleAnimationBySpringWayOneMiddle(meItem4);
        ScaleAnimationBySpringWayOne.onScaleAnimationBySpringWayOneMiddle(meItem5);
        ScaleAnimationBySpringWayOne.onScaleAnimationBySpringWayOneMiddle(meItem6);
        meItem1.setOnClickListener(this);
        meItem2.setOnClickListener(this);
        meItem3.setOnClickListener(this);
        meItem4.setOnClickListener(this);
        meItem5.setOnClickListener(this);
        meItem6.setOnClickListener(this);
        avatar = view.findViewById(R.id.avatar);
        avatar.setOnClickListener(this);
        nickname = view.findViewById(R.id.nickname);
        id = view.findViewById(R.id.account);
        nickname.setText(Me.user.getUsername());
        id.setText(Me.user.getAccount());
        hello = view.findViewById(R.id.hello);
        Handler handle = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                hello.setText(When() + "好，" + Me.user.getUsername() + "!");
                nickname.setText(Me.user.getUsername());
            }

        };
        class TimeThread extends Thread {

            @Override
            public void run() {
                    while (true) {

                        Message msg = new Message();
                        handle.sendMessage(msg);
                        try {
                            Thread.sleep(30000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                    }
                }
            }
        }
        TimeThread thread = new TimeThread();
        new Thread(thread).start(); //开启线程

        idCard = view.findViewById(R.id.change_personal_information);
        idCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getContext(),
                        PersonalInformation.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        ScaleAnimationBySpringWayOne.onScaleAnimationBySpringWayOneMiddle(v.findViewById(v.getId()));
        switch (v.getId()) {
            case R.id.me_item_2:
                intent = new Intent();
                intent.setClass(getContext(),
                        AccountSafety.class);
                startActivity(intent);
                break;
            case R.id.me_item_3:
                intent = new Intent();
                intent.setClass(getContext(),
                        WeekReport.class);
                startActivity(intent);
                break;
            case R.id.me_item_4:
                intent = new Intent();
                intent.setClass(getContext(),
                        AppInformation.class);
                startActivity(intent);
                break;
            case R.id.me_item_5:
                intent = new Intent();
                intent.setClass(getContext(),
                        appLogin.class);
                startActivity(intent);
                getActivity().finish();
                Me.user.deleteData();
                break;
            case R.id.me_item_6:
                intent = new Intent();
                intent.setClass(getContext(),
                        Settings.class);
                startActivity(intent);
                break;
            case R.id.avatar:
                showPop();
                break;
        }
    }

    private void showPop() {
        View bottomView = View.inflate(getActivity(), R.layout.dialog_bottom, null);
        TextView mAlbum = bottomView.findViewById(R.id.tv_album);
        TextView mCamera = bottomView.findViewById(R.id.tv_camera);
        TextView mCancel = bottomView.findViewById(R.id.tv_cancel);

        pop = new PopupWindow(bottomView, -1, -2);
        pop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pop.setOutsideTouchable(true);
        pop.setFocusable(true);
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.5f;
        getActivity().getWindow().setAttributes(lp);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1f;
                getActivity().getWindow().setAttributes(lp);
            }
        });
        pop.setAnimationStyle(R.style.main_menu_photo_anim);
        pop.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.tv_album:
                        //相册
                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            //相册中的照片都是存储在SD卡上的，需要申请运行时权限，WRITE_EXTERNAL_STORAGE是危险权限，表示同时授予程序对SD卡的读和写的能力
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                        } else {
                            Intent intent = new Intent(Intent.ACTION_PICK, null);
                            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                            startActivityForResult(intent, 2);
                        }
                        break;
                    case R.id.tv_camera:
                        //拍照
                        break;
                    case R.id.tv_cancel:
                        //取消
                        closePopupWindow();
                        break;
                }
                closePopupWindow();
            }
        };
        mCamera.setOnClickListener(clickListener);
        mAlbum.setOnClickListener(clickListener);

        mCancel.setOnClickListener(clickListener);
    }

    public void closePopupWindow() {
        if (pop != null && pop.isShowing()) {
            pop.dismiss();
            pop = null;
        }
    }

   /*
    public void startSelect(View view) {
        if(ContextCompat.checkSelfPermission(getContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getContext(),Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            PhotoQUtil.getInstance().dispatchSelectPictureIntent(getActivity());
        }else{
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA},123);
        }
    }

    public void startCamera(View view) {
        PhotoQUtil.getInstance().dispatchTakeLargePictureIntent(getActivity());
    }

    public void filePath(View view) {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PhotoQUtil.REQUEST_CAMERA_THUMB_CAPTURE && resultCode == RESULT_OK) {      //获取拍照的缩略图
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            avatar.setImageBitmap(imageBitmap);
        } else if (requestCode == PhotoQUtil.REQUEST_CAMERA_CAPTURE && resultCode == RESULT_OK) {
            if (!TextUtils.isEmpty(PhotoQUtil.getInstance().currentPhotoPath)) {
                PhotoQUtil.getInstance().setPic(avatar, true);
            }
        } else if (requestCode == PhotoQUtil.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            String imagePath = PhotoQUtil.getInstance().parseApiImagePath(getContext(), data);
            if (!TextUtils.isEmpty(imagePath)) {
                avatar.setImageBitmap(BitmapFactory.decodeFile(imagePath));
            }
        }
    }

    */
}
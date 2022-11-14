package com.example.uitest.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * create by hb
 */
public class PhotoQUtil {
    private static final String TAG = "PhotoQUtil";
    private static PhotoQUtil mInstance;
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int REQUEST_CAMERA_CAPTURE = 2;
    public static final int REQUEST_CAMERA_THUMB_CAPTURE = 3;  //拍照获取缩略图
    String currentPhotoPath;

    public static PhotoQUtil getInstance(){
        if(mInstance == null){
            mInstance = new PhotoQUtil();
        }
        return mInstance;
    }

    /**
     * 拍照
     * @param activity
     * 返回是缩略图
     */
    public void dispatchTakePictureIntent(Activity activity){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if(intent.resolveActivity(activity.getPackageManager()) != null){
//
//        }
        activity.startActivityForResult(intent,REQUEST_CAMERA_THUMB_CAPTURE);
    }

    /**
     * 拍照
     * 返回大图,直接返回目录
     */
    public void dispatchTakeLargePictureIntent(Activity activity){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if(takePictureIntent.resolveActivity(activity.getPackageManager()) != null){  //官方添加这个判断,但是在华为android11 此方法为空,不知道为啥
//
//        }
        try {
            File photoFile = null;
            photoFile = createImageFile(activity);
            if(photoFile != null){
                Uri photoURI = FileProvider.getUriForFile(activity,
                        "com.hb.myphoto.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                activity.startActivityForResult(takePictureIntent,REQUEST_CAMERA_CAPTURE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File createImageFile(Context context) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    /**
     * 选择照片
     */
    public void dispatchSelectPictureIntent(Activity activity){
        Log.i(TAG,"选择照片");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        activity.startActivityForResult(intent,REQUEST_IMAGE_CAPTURE);
    }

    public String parseApiImagePath(Context context,Intent intent){
        String result = "";
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            result = handleImageOnKitkat(context,intent);
        }else{
            result = handleImageBeforeKitkat(intent);
        }
        Log.i(TAG,"选择照片路径:" + result);
        return result;
    }

    /**
     * 获取大于19版本的图片
     * @param data
     */
    @TargetApi(19)
    private String handleImageOnKitkat(Context context,Intent data){
        String imagePath = "";
        Uri uri = data.getData();
        if(DocumentsContract.isDocumentUri(context,uri)){
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1];
                //解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(context,uri,selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath = getImagePath(context,contentUri,null);
            }
        }else if("content".equalsIgnoreCase(uri.getScheme())){
            imagePath = getImagePath(context,uri,null);
        }else if("file".equalsIgnoreCase(uri.getScheme())){
            imagePath = uri.getPath();
        }
        return imagePath;
    }

    private String handleImageBeforeKitkat(Intent data){
        String imagePath = "";
        Uri uri = data.getData();
        imagePath = uri.getPath();
        return imagePath;
    }

    @SuppressLint("Range")
    public String getImagePath(Context context, Uri uri, String selection){
        String path = null;
        /**
         * 通过uri和seletion来获取真实的图片路径
         */
        Cursor cursor = context.getContentResolver().query(uri,null,selection,null,null);
        if(cursor != null){
            if(cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    /*
     * 将照片添加到图库
     * */
    private void galleryAddPic(Context context){
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        if(TextUtils.isEmpty(currentPhotoPath)){
            File f = new File(currentPhotoPath);
            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            context.sendBroadcast(mediaScanIntent);
        }
    }

    public void setPic(ImageView imageView,boolean isGallery){
        if(isGallery){
            galleryAddPic(imageView.getContext());
        }
        if(!TextUtils.isEmpty(currentPhotoPath)){
            int targetW = imageView.getWidth();
            int targetH = imageView.getHeight();
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;

            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;
            int scaleFactor = Math.min(photoW/targetW,photoH/targetH);
            Log.i(TAG,"photoW:" + photoW + "-photoH:" + photoH + "-scaleFactor:" + scaleFactor);

            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;
            Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath,bmOptions);
            imageView.setImageBitmap(bitmap);
        }
    }

    public void clear() {
        currentPhotoPath = "";
    }
}

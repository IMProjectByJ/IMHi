package com.example.star.imhi.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.example.star.imhi.BaseActivity;

/**
 * Created by d c on 2018/1/5.
 */

public class Picture extends BaseActivity {
    public static final int CHOOSE_PHOTO = 2;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    //public Intent run(Context context){
        if (ContextCompat.checkSelfPermission(Picture.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Picture.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            openAblum();
        }
    }
    private void openAblum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
        //return intent;

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAblum();
                } else {
                    Toast.makeText(this, "没有权限啦～～", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CHOOSE_PHOTO:
                    if (Build.VERSION.SDK_INT >= 19) {
//                        4.4及以上系统，选取相册中的图片不再返回图片真实的uri
                        handleImageOnKitKat(data);
                    } else {
                        handleImagebeforeKitKat(data);
                    }
                    break;
            }
        }
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
//        如果uri是document类型，就通过读取document id进行处理
        if (DocumentsContract.isDocumentUri(this, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
//            如果Uri的Authority是media格式的话，document id还需要进行解析，然后通过字符串
//            分割方式取出后半部分才能得到真正的数字id
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads,documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads.public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
//            如果是content类型的uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
//            如果是file类型，直接获取图片路径
            imagePath = uri.getPath();
        }
        displayImage(imagePath);
    }


    private void handleImagebeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);

    }


    /**
     * 获取图片真实路径
     *
     * @param uri
     * @param selection
     * @return
     */
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (null != cursor) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    /**
     * 根据图片路径显示图片
     *
     * @param imagePath
     */
    private void displayImage(String imagePath) {
        if (null != imagePath) {
            Intent intent = new Intent(Picture.this,ShowPicture.class);
            intent.putExtra("path",imagePath);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "获取图片失败", Toast.LENGTH_LONG).show();
        }
    }
}

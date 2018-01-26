package com.example.star.imhi.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.example.star.imhi.BaseActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by d c on 2018/1/5.
 */

public class Camera extends BaseActivity implements Serializable {
    public static final int TAKE_PHOTO = 1;
    public Uri imageuri;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        File outputimage = new File(getExternalCacheDir(),"output_image.jpg");
        try {
            if(outputimage.exists()){
                outputimage.delete();
            }
            outputimage.createNewFile();
        }catch (IOException e){
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >=24){
            imageuri = FileProvider.getUriForFile(Camera.this,"com.dc.tx.fileprovider.IMAGE_CAPTURE",outputimage);
        }else {
            imageuri = Uri.fromFile(outputimage);
        }
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageuri);
        startActivityForResult(intent,TAKE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/cameraphoto/";
                    String sdStatus = Environment.getExternalStorageState();
                    if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
                        Log.i("TestFile", "SD card is not avaiable/writeable right now.");
                        return;
                    }
                    Calendar now = new GregorianCalendar();
                    SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
                    String name = simpleDate.format(now.getTime());
                    Bitmap bitmap = null;
                    try {
                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageuri));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    String fileName =dir+name+ ".jpg";
                    try {
                        File filedir = new File(dir);
                        if(!filedir.exists())
                        {
                            filedir.mkdirs();
                        }
                        File file = new File(fileName);
                        FileOutputStream b = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);
                        b.flush();
                        b.close();
                        Uri uri = Uri.fromFile(file);
                        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,uri));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(Camera.this,ShowPicture.class);
                    intent.putExtra("path",fileName);
                    startActivity(intent);
                    finish();
                }
                break;
        }
    }

}

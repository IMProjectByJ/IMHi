package com.example.star.imhi.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.star.imhi.R;
import com.example.star.imhi.Utils.FileOperateService;
import com.example.star.imhi.Utils.ImageUtil;
import com.example.star.imhi.Utils.OkHttpUtils;
import com.example.star.imhi.personally.Personal_information;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Createqun extends AppCompatActivity implements View.OnClickListener{
    private LinearLayout createqun_back;
    private ImageView qun_image;
    private TextView qun_name;
    private Button qun_commit;
    private SharedPreferences sp;
    private static final String PREFERENCE_NAME = "userInfo";
    private String token;
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;
    protected static Uri tempUri;
    private String imagePath;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createqun);
        createqun_back = (LinearLayout) findViewById(R.id.cretequn_back);
        qun_image = (ImageView) findViewById(R.id.qun_image);
        qun_name = (TextView) findViewById(R.id.qun_name);
        qun_commit = (Button) findViewById(R.id.qun_commit);
        createqun_back.setOnClickListener(this);
        qun_image.setOnClickListener(this);
        qun_name.setOnClickListener(this);
        qun_commit.setOnClickListener(this);
        sp = getSharedPreferences(PREFERENCE_NAME, Activity.MODE_PRIVATE);
        token = sp.getString("token", "");
        userId = sp.getString("userId", "");
        Log.e("token", token);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cretequn_back:
                finish();
                break;
            case R.id.qun_image:
                Toast.makeText(Createqun.this, "群头像", Toast.LENGTH_SHORT).show();
                showChoosePicDialog();
                break;
            case R.id.qun_name:
                Toast.makeText(Createqun.this, "群名字", Toast.LENGTH_SHORT).show();
                break;
            case R.id.qun_commit:
                Toast.makeText(Createqun.this, "提交", Toast.LENGTH_SHORT).show();
                creatQun();
                break;
            default:
                break;
        }

    }

    public  void creatQun(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = OkHttpUtils.getOkHttpClientInstance();
                Request request;
                String url = getString(R.string.postUrl) + "api/groupchat/createGroup";

                Log.e("qunName", qun_name.getText().toString());
                Log.e("imagePath", imagePath);

                request = FileOperateService.createQun(token, qun_name.getText().toString(), qun_name.getText().toString() + ".jpg", imagePath, url);

                try {
                    Response response = okHttpClient.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e("createQun result:", responseData);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void showChoosePicDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置头像");
        String[] items = {"选择本地照片", "拍照"};
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                switch (which) {
                    case CHOOSE_PICTURE://选择本地照片
                        Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        openAlbumIntent.setType("image/*");
                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                        break;
                    case TAKE_PICTURE://拍照
                        takePicture();
                        break;
                }
            }
        });
        builder.create().show();
    }

    private void takePicture() {
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= 23) {
            // 需要申请动态权限
            int check = ContextCompat.checkSelfPermission(this, permissions[0]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (check != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
        Intent openCameraIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
//        File file = new File(Environment
//                .getExternalStorageDirectory().getAbsolutePath() + "/test/" + user.getPhoneNum(), "image.jpg");
        File file = new File(Environment
                .getExternalStorageDirectory().getAbsolutePath() + "/test/" + qun_name.getText().toString()+".jpg");
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= 24) {
            openCameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            tempUri = FileProvider.getUriForFile(Createqun.this, "com.lt.uploadpicdemo.fileProvider", file);
        } else {
            tempUri = Uri.fromFile(new File(Environment
                    .getExternalStorageDirectory(), "image.jpg"));
        }
        // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // 如果返回码是可以用的
            switch (requestCode) {
                case TAKE_PICTURE:
                    startPhotoZoom(tempUri); // 开始对图片进行裁剪处理
                    break;
                case CHOOSE_PICTURE:
                    startPhotoZoom(data.getData()); // 开始对图片进行裁剪处理
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
            }
        }
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    protected void startPhotoZoom(Uri uri) {
        if (uri == null) {
            Log.i("tag", "The uri is not exist.");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param
     */
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            photo = ImageUtil.toRoundBitmap(photo); // 这个时候的图片已经被处理成圆形的了
            qun_image.setImageBitmap(photo);
            imagePath = saveImageToGallery(Createqun.this, photo);

//            uploadPic(photo);
        }
    }

    public String saveImageToGallery(Context context, Bitmap bitmap) {
        //首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "Boohee");
        if (!appDir.exists()){
            appDir.mkdir();
        }
        String fileName = userId + ".jpg";
        File file = new File(appDir, fileName);

        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //将文件插入系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }

        //最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://"+file.getAbsolutePath())));
        return file.getAbsolutePath();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        } else {
            // 没有获取 到权限，从新请求，或者关闭app
            Toast.makeText(this, "需要存储权限", Toast.LENGTH_SHORT).show();
        }
    }

}

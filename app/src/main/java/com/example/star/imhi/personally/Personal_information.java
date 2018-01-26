package com.example.star.imhi.personally;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.star.imhi.DAO.pojo.User;
import com.example.star.imhi.R;
import com.example.star.imhi.Utils.FileOperateService;
import com.example.star.imhi.Utils.FileUtils;
import com.example.star.imhi.Utils.ImageUtil;
import com.example.star.imhi.Utils.OkHttpUtils;
import com.google.gson.Gson;

import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Personal_information extends AppCompatActivity {

    private EditText personal_name, person_words;
    private TextView uID;
    private LinearLayout more;
    private Integer iD;
    private User user;
    private Button btn_back;
    private Button btn_finsh;
    private LinearLayout current_touch;
    private ImageView iv_personal_icon;
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;
    protected static Uri tempUri;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    private static final String PREFERENCE_NAME = "userInfo";
    private String  token;
    public static final MediaType JSON=MediaType.parse("application/json; charset=utf-8");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);

        init();

        current_touch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                personal_name.setCursorVisible(false);
                person_words.setCursorVisible(false);
            }
        });

        iv_personal_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChoosePicDialog();
            }
        });

        clickPerson_name();
        clickPerson_words();
        clickMore();
        titleButton();


    }

    private void init() {
        personal_name = (EditText) findViewById(R.id.personal_name);
        more = (LinearLayout) findViewById(R.id.personal_more);
        uID = (TextView) findViewById(R.id.uID);
        iD = Integer.parseInt(uID.getText().toString());
        person_words = (EditText) findViewById(R.id.personal_words);
        btn_back = (Button) findViewById(R.id.back);
        btn_finsh = (Button) findViewById(R.id.finsh);
        current_touch = (LinearLayout) findViewById(R.id.current_touch);
        iv_personal_icon = (ImageView) findViewById(R.id.iv_personal_icon);
        sp = getSharedPreferences(PREFERENCE_NAME, Activity.MODE_PRIVATE);
        editor = sp.edit();
        token = sp.getString("token", "");
        Log.e("token",token);
        Log.e("-------token:", token+"is null?");

        Intent intent = getIntent();
        user = new Gson().fromJson(intent.getStringExtra("person_user"), User.class);
        Log.e("USER:", user.toString());

        personal_name.setText(user.getNikname());
        person_words.setText(user.getMotto());
        uID.setText(user.getUserId().toString());

        Bitmap bm = BitmapFactory.decodeFile(sp.getString(user.getUserId().toString(),""));

        if (bm != null) {
            Log.e("-----------Bitmap:", "bitmap is not null" );
            iv_personal_icon.setImageBitmap(bm);
        } else {
            Log.e("-----------Bitmap:", "bitmap is null" );

            String responseUrl = getString(R.string.postUrl)+"/api/imageOperate/imageDownload";
            responseServert(responseUrl);
        }



    }

    public void responseServert(final String Url){
        new Thread(new Runnable() {
            @Override
            public void run() {
                FileUtils fileUtils = new FileUtils();
                OkHttpClient okHttpClient = OkHttpUtils.getOkHttpClientInstance();
                Log.e("-------OkHttpClient:", "run1: ");
                Request request = FileOperateService.DownloadFileRequest(101, Url, token);
                Log.e("-----OkHttpClient:", "run2: ");
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    File file = fileUtils.createFileInSDCard("user"+user.getPhoneNum(), user.getPhoneNum());
                    File file1 = FileOperateService.saveFile(response, user.getPhoneNum()+".jpg", "user"+user.getPhoneNum());

                    if (file1 == null) {
                        Log.e("file1?", "file1 is null" );
                        //Log.e("-------file1:", file1.getAbsolutePath());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                iv_personal_icon.setImageResource(R.drawable.man);
                            }
                        });
                    } else {
                        Log.e("-------file1:", file1.getAbsolutePath());
                        final Bitmap bm1 = BitmapFactory.decodeFile(file1.getAbsolutePath());//                    Log.e("--------ResponseBody:", file );


                        if (bm1 == null) {
                            Log.e("-----------Bitmap1111:?", "Bitmap is null" );
                            //Log.e("-----------Bitmap1111:", bm1.toString());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    iv_personal_icon.setImageResource(R.drawable.man);
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    iv_personal_icon.setImageBitmap(bm1);
                                }
                            });
                        }
                    }

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
                .getExternalStorageDirectory().getAbsolutePath() + "/test/" + user.getPhoneNum()+".jpg");
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= 24) {
            openCameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            tempUri = FileProvider.getUriForFile(Personal_information.this, "com.lt.uploadpicdemo.fileProvider", file);
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
            iv_personal_icon.setImageBitmap(photo);

            uploadPic(photo);
        }
    }

    private void uploadPic(Bitmap bitmap) {
        // 上传至服务器
        // ... 可以在这里把Bitmap转换成file，然后得到file的url，做文件上传操作
        // 注意这里得到的图片已经是圆形图片了
        // bitmap是没有做个圆形处理的，但已经被裁剪了
        Log.e("in token----",token);

//        String imagePath = ImageUtil.savePhoto(bitmap, Environment
//                 .getExternalStorageDirectory().getAbsolutePath(), user.getPhoneNum());
//
//        if(imagePath != null) {
            // 拿着imagePath上传了
            // ...
            String serverUrl = getString(R.string.postUrl)+"/api/imageOperate/imageUpload";
            String imagePath = saveImageToGallery(Personal_information.this, bitmap);
            Log.e("imagePath", imagePath + "");
            editor.putString(user.getUserId().toString(), imagePath);
            editor.putString(user.getPhoneNum(), imagePath);
            editor.commit();

            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
            Intent intent = new Intent("com.imagePath.LOCAL_BROADCAST");
            intent.putExtra("imgData",imagePath);
            localBroadcastManager.sendBroadcast(intent);


//            Intent intent = new Intent("com.example.broadcasttest.MY_BROADCAST");
//            intent.putExtra("imgData", imagePath);
//            sendBroadcast(intent);

            requestServert(imagePath, serverUrl,token);
//        }
    }

    public void requestServert(final String imagePath, final String serverUrl, final String token){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e("OkHttpClient","OkHttpClient");
                OkHttpClient okHttpClient = OkHttpUtils.getOkHttpClientInstance();
                Request request = FileOperateService.uploadFileRequest(user.getPhoneNum()+".jpg", imagePath, user.getUserId(), 101, serverUrl, token);
                Response response = null;
                try {
                    response = okHttpClient.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e("responseData", responseData);
                    org.json.JSONObject jsonObject = new org.json.JSONObject(responseData);

                    if (jsonObject.optString("err") != null || !jsonObject.optString("err").equals("")) {
                        Log.e("上传--：", jsonObject.optString("err"));
                    } else if (jsonObject.optString("success") != null || !jsonObject.optString("success").equals("")) {
                        Log.e("上传--：", "成功");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public String saveImageToGallery(Context context, Bitmap bitmap) {
        //首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "Boohee");
        if (!appDir.exists()){
            appDir.mkdir();
        }
        String fileName = user.getPhoneNum() + ".jpg";
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
    public void titleButton(){
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_finsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("destory:", "destory");
                Log.e("birthday:", user.getBirth());
                Log.e("user:", user.toString() );

                user.setNikname(personal_name.getText().toString());
                user.setMotto(person_words.getText().toString());
                user.setAge(0);
                user.setGender("男");

//                user.setHeadUrl("");

                Log.e("user:", user.toString() );
                final String json = new Gson().toJson(user).toString();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

//                            Log.e("JSON TEST!!!!!:", json);
//                            OkHttpClient okHttpClient = new OkHttpClient();
//
//                            Request request = new Request.Builder().get().url(getString(R.string.postUrl)+"api/user/information/"+json).build();
//                            //Request request = new Request.Builder().get().url(getString(R.string.postUrl)+"api/user/information/"+"TEST").build();
//                            Response response = okHttpClient.newCall(request).execute();

                            //申明给服务端传递一个json串
                            //创建一个OkHttpClient对象
                            OkHttpClient okHttpClient = new OkHttpClient();
                            //创建一个RequestBody(参数1：数据类型 参数2传递的json串)
                            //json为String类型的json数据
                            RequestBody requestBody = RequestBody.create(JSON, json);
                            Log.e("JSON:" , json);
                            //创建一个请求对象
                            Request request = new Request.Builder()
                                    .url(getString(R.string.postUrl) + "api/user/information_post")
                                    .post(requestBody)
                                    .build();
                            //发送请求获取响应
                            try {
                                Response response=okHttpClient.newCall(request).execute();
                                //判断请求是否成功
                                if(response.isSuccessful()){
                                    //打印服务端返回结果
                                    Log.e("Success", "success!!!!!!!!!" );

                                } else {
                                    Log.e("Failed", "fail!!!!!!!!!!!!!");
                                }


                                LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(Personal_information.this);
                                Intent intent = new Intent("com.imagePath.LOCAL_BROADCAST");
                                intent.putExtra("userName",user.getNikname());
                                localBroadcastManager.sendBroadcast(intent);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            //String responseData = response.body().string();

                           // Log.e("personInformation:", responseData);
                            finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }).start();
            }
        });
    }

    private void clickMore() {
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestServert(user.getUserId().toString());
            }
        });
    }

    private void clickPerson_words() {
        person_words.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEND || actionId == EditorInfo.IME_ACTION_DONE ||
                        (keyEvent != null && KeyEvent.KEYCODE_ENTER == keyEvent.getKeyCode())) {
                    String motto = person_words.getText().toString();
                    Log.e("ENter:", motto);
                    user.setMotto(motto);
                    person_words.setCursorVisible(false);
                }

                return false;
            }
        });

        person_words.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                person_words.setCursorVisible(true);
                return false;
            }
        });
    }


    private void clickPerson_name() {
        personal_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

                if (actionId == EditorInfo.IME_ACTION_SEND || actionId == EditorInfo.IME_ACTION_DONE ||
                        (keyEvent != null && KeyEvent.KEYCODE_ENTER == keyEvent.getKeyCode())) {
                    String name = personal_name.getText().toString();
                    Log.e("ENter:", name);
                    user.setNikname(name);
                    Log.e("ENter:", user.getNikname());
                    personal_name.setCursorVisible(false);
                }

                return false;
                //return keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER;
            }
        });

        personal_name.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                personal_name.setCursorVisible(true);
                return false;
            }
        });
    }

    public void requestServert(final String uid){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request;
                request = new Request.Builder().get().url(getString(R.string.postUrl) + "api/user/details/" + uid).build();

                try {
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();

                    Log.e("responseData:", responseData);
                    org.json.JSONObject jsonObject = new org.json.JSONObject(responseData);

                    if (jsonObject.optString("err") == "") {
                        Gson gson  = new Gson();
                        user = gson.fromJson(responseData,User.class);
                        Log.e("details:",user.toString() );
                    }

                    Intent intent1 = new Intent(Personal_information.this, PersonalMoreActivity.class);
                    Gson gson = new Gson();
                    intent1.putExtra("person_user",gson.toJson(user) );
                    Log.e("intent1:", gson.toJson(user).toString() );
                    startActivity(intent1);

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}

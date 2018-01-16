package com.example.star.imhi.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.star.imhi.BaseActivity;
import com.example.star.imhi.DAO.pojo.ChatModel;
import com.example.star.imhi.DAO.pojo.ItemModel;
import com.example.star.imhi.R;
import com.example.star.imhi.TestData;
import com.example.star.imhi.adapter.ChatAdapter;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements View.OnClickListener{
    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
    public Uri imageuri;
    private RecyclerView recyclerView;
    private ChatAdapter adapter;
    private EditText et;
    private TextView tvSend;
    private String content;
    private ImageView camera,picture,file;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainchat);

        camera=(ImageView) findViewById(R.id.camera);
        picture=(ImageView) findViewById(R.id.picture);
        file=(ImageView) findViewById(R.id.file);
        camera.setOnClickListener(this);
        picture.setOnClickListener(this);
        file.setOnClickListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.recylerView);
        et = (EditText) findViewById(R.id.et);
        tvSend = (TextView) findViewById(R.id.tvSend);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter = new ChatAdapter());
        adapter.replaceAll(TestData.getTestAdData());
        initData();
    }


//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode){
//            case 10:
//                if (resultCode == RESULT_OK) {
//                    if (data != null) {
//                        byte [] bis =data.getByteArrayExtra("picture");
//                        Bitmap bitmap= BitmapFactory.decodeByteArray(bis,0,bis.length);
//                        Intent intent=new Intent(MainActivity.this,ShowPicture.class);
//                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                        bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
//                        byte [] bitmapbyte = baos.toByteArray();
//                        intent.putExtra("picture",bitmapbyte);
//                       // imageView.setImageBitmap(bitmap);
//                        startActivity(intent);
//                    }
//                }
//        }
//    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.camera:
//                File outputimage = new File(getExternalCacheDir(),"output_image.jpg");
//                try {
//                    if(outputimage.exists()){
//                        outputimage.delete();
//                    }
//                    outputimage.createNewFile();
//                }catch (IOException e){
//                    e.printStackTrace();
//                }
//                if (Build.VERSION.SDK_INT >=24){
//                    imageuri = FileProvider.getUriForFile(MainActivity.this,"com.dc.tx.fileprovider.IMAGE_CAPTURE",outputimage);
//                }else {
//                    imageuri = Uri.fromFile(outputimage);
//                }
//                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageuri);
//                startActivityForResult(intent,TAKE_PHOTO);
//        startActivity(intent);
              //  Toast.makeText(MainActivity.this, "camera", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(MainActivity.this,Camera.class);
                startActivity(intent);
               //Camera camera=new Camera();
                //Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                //startActivity(camera.run(MainActivity.this));
                break;
            case R.id.picture:
                Intent intent2=new Intent(MainActivity.this,Picture.class);
                startActivity(intent2);
//                Picture picture=new Picture();
//                startActivity(picture.run(MainActivity.this));
                break;
            case R.id.file:
                Intent intent1 = new Intent(MainActivity.this,ShowPicture.class);
                startActivity(intent1);
                break;
            default:
                break;
        }

    }

    private void initData() {
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                content = s.toString().trim();
            }
        });

        tvSend.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                ArrayList<ItemModel> data = new ArrayList<>();
                ChatModel model = new ChatModel();
                model.setIcon("http://img.my.csdn.net/uploads/201508/05/1438760758_6667.jpg");
                model.setContent(content);
                data.add(new ItemModel(ItemModel.CHAT_B, model));
                adapter.addAll(data);
                et.setText("");
                hideKeyBorad(et);
            }
        });

    }
    private void hideKeyBorad(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        }
    }


}

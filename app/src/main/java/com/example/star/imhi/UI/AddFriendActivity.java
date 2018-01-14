package com.example.star.imhi.UI;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.star.imhi.DAO.pojo.User;
import com.example.star.imhi.R;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AddFriendActivity extends AppCompatActivity {
    User user = null;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        setActionBar();

        editText = (EditText) findViewById(R.id.search_msg);
        setOnEditorActionListener(editText);
        setSearchMessage();
    }

    public void setSearchMessage(){
        TextView textView = (TextView) findViewById(R.id.search_person);
        ImageView imageView = (ImageView) findViewById(R.id.search_person_head_img);

        if (user != null){
            //ImageView
            textView.setText(user.getNikname());

            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.search);
            linearLayout.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(AddFriendActivity.this, DetailsActivity.class);
                    intent.putExtra("search_user", new Gson().toJson(user));
                    startActivity(intent);
                }
            });
        } else {
                imageView.setImageURI(Uri.parse("http://pic4.nipic.com/20091217/3885730_124701000519_2.jpg"));
                textView.setText("");
        }
    }

    public void setOnEditorActionListener(final EditText editText){
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    String aimsPhoneNumber = editText.getText().toString();
                    requestServert(aimsPhoneNumber);
                }
                return false;
            }
        });
    }

    public void setActionBar(){
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.hide();
        }
        Button btn_back = (Button) findViewById(R.id.back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void requestServert(final String aimsPthoneNumber){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url("http://172.20.10.10:8080/api/user/add_search/" + aimsPthoneNumber).build();
                try {
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e("responseData:", responseData);
                    JSONObject jsonObject = new JSONObject(responseData);

                    if (jsonObject.optString("err") == "") {
                        Gson gson  = new Gson();
                        user = gson.fromJson(jsonObject.getString("add_search"),User.class);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}

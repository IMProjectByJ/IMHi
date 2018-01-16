package com.example.star.imhi.UI;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.Authenticator;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.star.imhi.DAO.pojo.User;
import com.example.star.imhi.MyApplication;
import com.example.star.imhi.R;
import com.example.star.imhi.Utils.AccountValidatorUtil;
import com.example.star.imhi.Utils.MD5Util;
import com.example.star.imhi.activity.StartActivity;
import com.example.star.imhi.database.MyDatabaseHelper;
import com.example.star.imhi.mina.MyService;
import com.example.star.imhi.mina.Protocol;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;

import net.sf.json.JSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via mobile/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {


    /*
        正则表达式：验证用户名，匹配手机号
    */

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "18020536089:liuyunxing", "13260905153:liuyunxing"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mMobileView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private TextView tforgetPasswdView;
    private TextView tRegView;

    //yuyisummer private  MyDatabaseHelper dbHelper;
    private  MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mMobileView = (AutoCompleteTextView) findViewById(R.id.mobile);
        populateAutoComplete();
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mMobileSignInButton = (Button) findViewById(R.id.mobile_sign_in_button);
        mMobileSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tforgetPasswdView = findViewById(R.id.forgetPassword);
        tRegView = findViewById(R.id.regBySms);
// 跳转到注册界面
        tRegView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        //yuyisummer 1.15创建数据库操作，测试
        dbHelper = new MyDatabaseHelper(this,"FriendsStore.db",null,1);
        dbHelper.getWritableDatabase();
        //yuyisummer 1.16创建Stetho
        sqlinit();

    }

    // 获取权限
    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mMobileView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid Mobile, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mMobileView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String Mobile = mMobileView.getText().toString();
        final String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid Mobile address.
        if (TextUtils.isEmpty(Mobile)) {
            mMobileView.setError(getString(R.string.error_field_required));
            focusView = mMobileView;
            cancel = true;
        } else if (!isMobileValid(Mobile)) {
            mMobileView.setError(getString(R.string.error_invalid_mobile));
            focusView = mMobileView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {

            //加密
            final String md5password = MD5Util.encrypt(mPasswordView.getText().toString());
            Log.e("md5passwd", md5password);
            String strMobile = mMobileView.getText().toString();
            final OkHttpClient okHttpClient = new OkHttpClient().newBuilder().build();
            Request request;
            if (Mobile.length() == 11)
                request = new Request.Builder().get().url(getString(R.string.postUrl) + "api/user/" + Mobile + "/" + md5password).build();
            else
                request = new Request.Builder().get().url(getString(R.string.postUrl) + "api/user/id/" + Mobile + "/" + md5password).build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException { // 获取返回值

                    User user = null;
                    if (response.isSuccessful()) {
                        String strJson = response.body().string();
                        if (strJson != null) {
                            // 用户不正确

                            try {
                                JSONObject jsonObject = new JSONObject(strJson);
                                if (jsonObject.optString("err") != "") {
                                    if (jsonObject.get("err").equals("账户不存在")) {
                                        user = null;
                                    }

                                    if (jsonObject.optString("err") != "" && jsonObject.get("err").equals("密码错误")) {
                                        user = new User();
                                    }
                                } else {
                                    Gson gson = new Gson();
                                    user = gson.fromJson(strJson, User.class);
                                    SharedPreferences share = getSharedPreferences("userInfo", Activity.MODE_WORLD_READABLE);// 私有方式获取
                                    SharedPreferences.Editor edit = share.edit();
                                    edit.putString("userId", user.getUserId() + "");
                                    edit.putString("userPassword", user.getUserPassword()); // 用户密码
                                    edit.commit();
                                    // 作为token认证

                                }
                            } catch (Exception e) {

                            }
                        }

                    }

                    final User finalUser = user;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (finalUser != null && finalUser.getUserId() != null) {
                                //  Toast.makeText(getApplicationContext(), "你好 " + finalUser.getNikname(), Toast.LENGTH_SHORT).show();
                                // Toast.makeText(getApplicationContext(), "登录成功！", Toast.LENGTH_SHORT).show();
                                // Intent intent = new Intent(LoginActivity.this, HomepageActivity.class);

                                //yuyiummser 2018.1.15

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        OkHttpClient client = new OkHttpClient();
                                        String user_id = "10001";
                                        Request request;
                                        request = new Request.Builder().url(getString(R.string.postUrl)+"api/user/find_friend_list/" + user_id).build();
                                        try {
                                            Response response = client.newCall(request).execute();
                                            String responseData = response.body().string();
                                            JSONObject json = new JSONObject(responseData);
                                            String user_str = json.getString("retval");
                                            System.out.println(user_str);

//                                            Protocol protocol = gson.fromJson(message.toString(), Protocol.class);
//                                            Map<String,Integer> content = (Map<String, Integer>) protocol.getTextcontent();
//                                              yuyisummer 测试直接转换json->list
                                            //Gson gson = new Gson();
                                           // List<User> list =  new ArrayList<User>();
                                            //JSONArray jsonArray = JSONArray.

                                            SQLiteDatabase db = dbHelper.getWritableDatabase();
                                            JSONArray arr = new JSONArray(user_str);
                                            for(int i = 0 ;i<arr.length();i++){
                                                JSONObject temp = (JSONObject) arr.get(i);
                                                int userid = temp.getInt("userId");
                                                 String phone_num = temp.getString("phoneNum");
                                                 String nikname = temp.getString("nikname");
                                                System.out.println(userid+" "+phone_num+" "+nikname+" ");
                                                //进行数据库个人简单信息的插入
                                                ContentValues values = new ContentValues();
                                                values.put("user_id",userid);
                                                values.put("phone_num",phone_num);
                                                values.put("nikname",nikname);
                                              if( db.query("Friends",null,"user_id=?", new String[]{user_id},null,null,null) == null)
                                              {
                                                  values.put("user_id",userid);
                                                  db.insert("Friends",null,values);
                                              } else {
                                                  db.update("Friends", values, "user_id=?", new String[]{String.valueOf(userid)});
                                              }


                                            }
                                           // Log.e("LoginActivity",list.toString());

                                            //这里应该做一步
                                            Log.e("LoginAcitivty", responseData);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).start();











                                Intent intent = new Intent(LoginActivity.this, StartActivity.class);
                                //  intent.putExtra("loginUser", new Gson().toJson(finalUser));
                                intent.putExtra("loginUser", String.valueOf(finalUser.getUserId()));
                                //传入登录时间，最简单的一种
                                // Date dt = new Date();
                                //  intent.putExtra("login_date",dt.toLocaleString());
                                //传入token，测试
                                intent.putExtra("token", "假装有");
                                //进行socket通信
                                /*
                                Intent in = new Intent(LoginActivity.this, MyService.class);
                                Log.e("LoginActivity", "进行MyService");
                                startService(in);
                                */
                                startActivity(intent);
                                finish();
                            } else if (finalUser == null) {
                                // 用户不存在
                                mMobileView.requestFocus();
                                mMobileView.setError("用户不存在");
                            } else {// 密码错误
                                mPasswordView.requestFocus();
                                mPasswordView.setError("密码错误");
                            }
                        }
                    });

                }
            });
        }
    }

    private boolean isMobileValid(String mobile) {
        //TODO: Replace this with your own logic
        return AccountValidatorUtil.isMobile(mobile) || (mobile.length() >= 0 && mobile.length() <= 7);
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return AccountValidatorUtil.isPassword(password);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only Mobile addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Phone
                .CONTENT_ITEM_TYPE},

                // Show primary Mobile addresses first. Note that there won't be
                // a primary Mobile address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> Mobiles = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Mobiles.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addMobilesToAutoComplete(Mobiles);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addMobilesToAutoComplete(List<String> MobileAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, MobileAddressCollection);

        mMobileView.setAdapter(adapter);
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mMobile;
        private final String mPassword;

        UserLoginTask(String Mobile, String password) {
            mMobile = Mobile;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mMobile)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(true);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }

        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(true);
        }
    }
    //yuyisummer 1.16测试数据库查看
    private void sqlinit(){
        Stetho.initializeWithDefaults(this);
        new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
    }
}


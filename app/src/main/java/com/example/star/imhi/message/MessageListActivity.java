package com.example.star.imhi.message;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.star.imhi.DAO.pojo.DefaultUser;
import com.example.star.imhi.DAO.pojo.MyMessage;
import com.example.star.imhi.R;
import com.example.star.imhi.Utils.Chating;
import com.example.star.imhi.database.MyDatabaseHelper;
import com.example.star.imhi.mina.SessionManager;
import com.example.star.imhi.view.ChatView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.jiguang.imui.chatinput.ChatInputView;
import cn.jiguang.imui.chatinput.listener.OnCameraCallbackListener;
import cn.jiguang.imui.chatinput.listener.OnClickEditTextListener;
import cn.jiguang.imui.chatinput.listener.OnMenuClickListener;
import cn.jiguang.imui.chatinput.listener.RecordVoiceListener;
import cn.jiguang.imui.chatinput.model.FileItem;
import cn.jiguang.imui.chatinput.model.VideoItem;
import cn.jiguang.imui.commons.ImageLoader;
import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.messages.MsgListAdapter;
import cn.jiguang.imui.messages.ViewHolderController;
import cn.jiguang.imui.messages.ptr.PtrHandler;
import cn.jiguang.imui.messages.ptr.PullToRefreshLayout;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


public class MessageListActivity extends Activity implements View.OnTouchListener,
        EasyPermissions.PermissionCallbacks, SensorEventListener {

    private final static String TAG = "MessageListActivity";
    private final int RC_RECORD_VOICE = 0x0001;
    private final int RC_CAMERA = 0x0002;
    private final int RC_PHOTO = 0x0003;

    private ChatView mChatView;
    private MsgListAdapter<MyMessage> mAdapter;
    private List<MyMessage> mData;

    private InputMethodManager mImm;
    private Window mWindow;
    private HeadsetDetectReceiver mReceiver;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private PowerManager mPowerManager;
    private PowerManager.WakeLock mWakeLock;
    private DefaultUser user1;
    private DefaultUser user2;
    private static  int pos = 0;// just for test
    private MyDatabaseHelper dbHelper;
    Chating chating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);
        dbHelper = new MyDatabaseHelper(this, "FriendsStore.db", null, 1);
//user2是别人
        user2 = (DefaultUser) getIntent().getSerializableExtra("user1");
//        SharedPreferences share = getSharedPreferences("userInfo", Context.MODE_PRIVATE);// 私有方式获取
//        SharedPreferences.Editor edit = share.edit();
//        edit.putString("chating", user2.getId()); // 用户密码
//        edit.commit();
       chating = new Chating();
        chating.setChating(Integer.parseInt(user2.getId()));


        //user2 = (DefaultUser) getIntent().getSerializableExtra("user2");
        SharedPreferences preferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String usernikname = preferences.getString("usernikname", null);
        String userId = preferences.getString("userId", null);

        // DefaultUser(String id, String displayName, String avatar, int type)
        //user1是自己
        user1 = new DefaultUser(userId,usernikname,"R.drawable.aurora_menuitem_emoji",1);
        this.mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); // jian shi shuru fa?
        mWindow = getWindow();
        registerProximitySensorListener();
        mChatView = (ChatView) findViewById(R.id.chat_view);
        mChatView.initModule();
        mChatView.setTitle(user2.getDisplayName());//设置标题栏文字，如果是跟好友聊天返回好友名称，群聊返回群名称
        mData = getMessages();
        initMsgAdapter();
        mReceiver = new HeadsetDetectReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_HEADSET_PLUG);
        //yuyisummer
        intentFilter.addAction("com.bs.showMsg");

        registerReceiver(mReceiver, intentFilter);
        mChatView.setOnTouchListener(this);
        mChatView.setMenuClickListener(new OnMenuClickListener() { //菜单按键接口
            @Override
            public boolean onSendTextMessage(CharSequence input) { //按发送键发送数据接口
                if (input.length() == 0) {
                    return false;
                }
                MyMessage message = new MyMessage(input.toString(), IMessage.MessageType.SEND_TEXT.ordinal()); //消息类
                message.setUserInfo( user1);
                Date date = new Date();
                message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(date));
                mAdapter.addToStart(message, true);
                /*
                  在此添发送信息的操作和数据库操作
                 */

                /*                // -------------just  for test

                message = new MyMessage(input.toString(), IMessage.MessageType.RECEIVE_TEXT.ordinal());
                message.setUserInfo(user2);
                message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
                mAdapter.addToStart(message, true);*/
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("from",user1.getId());
                    jsonObject.put("to",user2.getId());
                    jsonObject.put("message_type","2");
                    jsonObject.put("text_type","1");
                    jsonObject.put("textcontent",input.toString());
                    jsonObject.put("date",new SimpleDateFormat("HH:mm", Locale.getDefault()).format(date));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                SessionManager.getInstance().writeMag(jsonObject);
                return true;
            }

            @Override
            public void onSendFiles(List<FileItem> list) {  //当点击发送文件,我们这边只做发送一个文件,如果是多个文件那么就多次发送
                if (list == null || list.isEmpty()) {
                    return;
                }

                MyMessage message;
                for (FileItem item : list) {
                    if (item.getType() == FileItem.Type.Image) {
                        message = new MyMessage(null, IMessage.MessageType.SEND_IMAGE.ordinal());

                    } else if (item.getType() == FileItem.Type.Video) {
                        message = new MyMessage(null, IMessage.MessageType.SEND_VIDEO.ordinal());
                        message.setDuration(((VideoItem) item).getDuration());

                    } else {
                        throw new RuntimeException("Invalid FileItem type. Must be Type.Image or Type.Video");
                    }

                    message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
                    message.setMediaFilePath(item.getFilePath());
                    message.setUserInfo(user1);

                    final MyMessage fMsg = message;
                    //在此处添加http发送请求
                    //-------------------------------
                       /*
                  在此添发送信息的操作和数据库操作
                 */
                    MessageListActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.addToStart(fMsg, true);
                        }
                    });
                }
            }

            @Override
            public boolean switchToMicrophoneMode() {
                scrollToBottom();
                String[] perms = new String[]{
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                };

                if (!EasyPermissions.hasPermissions(MessageListActivity.this, perms)) {
                    EasyPermissions.requestPermissions(MessageListActivity.this,
                            getResources().getString(R.string.rationale_record_voice),
                            RC_RECORD_VOICE, perms);
                }
                return true;
            }

            @Override
            public boolean switchToGalleryMode() {  //
                scrollToBottom();
                String[] perms = new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE
                };

                if (!EasyPermissions.hasPermissions(MessageListActivity.this, perms)) {
                    EasyPermissions.requestPermissions(MessageListActivity.this,
                            getResources().getString(R.string.rationale_photo),
                            RC_PHOTO, perms);
                }
                return true;
            }

            @Override
            public boolean switchToCameraMode() {
                scrollToBottom();
                String[] perms = new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO
                };

                if (!EasyPermissions.hasPermissions(MessageListActivity.this, perms)) {
                    EasyPermissions.requestPermissions(MessageListActivity.this,
                            getResources().getString(R.string.rationale_camera),
                            RC_CAMERA, perms);
                } else {
                    File rootDir = getFilesDir();
                    String fileDir = rootDir.getAbsolutePath() + "/photo";
                    mChatView.setCameraCaptureFile(fileDir, new SimpleDateFormat("yyyy-MM-dd-hhmmss",
                            Locale.getDefault()).format(new Date()));
                }
                return true;
            }

            @Override
            public boolean switchToEmojiMode() {
                scrollToBottom();
                return true;
            }
        });

        mChatView.setRecordVoiceListener(new RecordVoiceListener() {
            @Override
            public void onStartRecord() {
                // set voice file path, after recording, audio file will save here
                String path = Environment.getExternalStorageDirectory().getPath() +"/IMHi"+ user1.getId()+"/voice"; //音频消息放入用户的个人文件夹
                File destDir = new File(path);
                if (!destDir.exists()) {
                    destDir.mkdirs();
                }
                mChatView.setRecordVoiceFile(destDir.getPath(), DateFormat.format("yyyy-MM-dd-hhmmss",
                        Calendar.getInstance(Locale.CHINA)) + "");
            }

            @Override
            public void onFinishRecord(File voiceFile, int duration) {
                MyMessage message = new MyMessage(null, IMessage.MessageType.SEND_VOICE.ordinal());
                message.setUserInfo(user1);
                message.setMediaFilePath(voiceFile.getPath());
                message.setDuration(duration);
                message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
                mAdapter.addToStart(message, true);
            }

            @Override
            public void onCancelRecord() {

            }
        });

        mChatView.setOnCameraCallbackListener(new OnCameraCallbackListener() {
            @Override
            public void onTakePictureCompleted(String photoPath) {
                final MyMessage message = new MyMessage(null, IMessage.MessageType.SEND_IMAGE.ordinal());
                message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
                message.setMediaFilePath(photoPath);
                message.setUserInfo(user1);
                MessageListActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.addToStart(message, true);
                    }
                });
            }

            @Override
            public void onStartVideoRecord() {

            }

            @Override
            public void onFinishVideoRecord(String videoPath) {

            }

            @Override
            public void onCancelVideoRecord() {

            }
        });

        mChatView.setOnTouchEditTextListener(new OnClickEditTextListener() {
            @Override
            public void onTouchEditText() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mChatView.getMessageListView().smoothScrollToPosition(0);
                    }
                }, 100);

            }
        });

        mChatView.getSelectAlbumBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MessageListActivity.this, "OnClick select album button",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registerProximitySensorListener() {
        try {
            mPowerManager = (PowerManager) getSystemService(POWER_SERVICE);
            mWakeLock = mPowerManager.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, TAG);
            mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
            mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        try {
            if (audioManager.isBluetoothA2dpOn() || audioManager.isWiredHeadsetOn()) {
                return;
            }
            if (mAdapter.getMediaPlayer().isPlaying()) {
                float distance = event.values[0];
                if (distance >= mSensor.getMaximumRange()) {
                    mAdapter.setAudioPlayByEarPhone(0);
                    setScreenOn();
                } else {
                    mAdapter.setAudioPlayByEarPhone(2);
                    ViewHolderController.getInstance().replayVoice();
                    setScreenOff();
                }
            } else {
                if (mWakeLock != null && mWakeLock.isHeld()) {
                    mWakeLock.release();
                    mWakeLock = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setScreenOn() {
        if (mWakeLock != null) {
            mWakeLock.setReferenceCounted(false);
            mWakeLock.release();
            mWakeLock = null;
        }
    }

    private void setScreenOff() {
        if (mWakeLock == null) {
            mWakeLock = mPowerManager.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, TAG);
        }
        mWakeLock.acquire();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private class HeadsetDetectReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
                if (intent.hasExtra("state")) {
                    int state = intent.getIntExtra("state", 0);
                    mAdapter.setAudioPlayByEarPhone(state);
                }
            } else if(intent.getAction().equals("com.bs.showMsg")){

                //public void updateOrAddMessage(String oldId, MESSAGE newMessage, boolean scrollToBottom)
//                MyMessage message = new MyMessage(input.toString(), IMessage.MessageType.SEND_TEXT.ordinal()); //消息类
//                message.setUserInfo( user1);
//                message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));

                try {

                    JSONObject jsonObject = new JSONObject(intent.getStringExtra("textcontent"));
                    Log.e("messagelistac",jsonObject.toString());
                    MyMessage myMessage = new MyMessage(jsonObject.getString("textContent"),IMessage.MessageType.RECEIVE_TEXT.ordinal());
                   myMessage.setUserInfo(user2);
                    mAdapter.updateOrAddMessage(jsonObject.getString("messageId"),myMessage,true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }
/*
  拉取消息展示到界面中,初始化界面
 */
    private List<MyMessage> getMessages() {


        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor;
        String from = user1.getId();
        String to = user2.getId();
        cursor = db.query("history_message", null, "(user_from_id=? and to_id=?) or (user_from_id=? and to_id=?) and message_type=? ", new String[]{to,from,from,to,"2"}, null, null, null);

        cursor.moveToFirst();
        Log.e("message 427",cursor.getString(0));
        Log.e("message 427",cursor.getString(1));
//        Log.e("message 427",cursor.getString(2));
        Log.e("message 427",cursor.getString(cursor.getCount()));

        List<MyMessage> list = new ArrayList<>();

        cursor.moveToFirst();
            if(cursor.getCount() != 0){
                do{

                    MyMessage message;
                    String user_from_id = cursor.getString(cursor.getColumnIndex("user_from_id"));
                    String textcontent = cursor.getString(cursor.getColumnIndex("text_content"));
                    if (user_from_id.equals(to)) {
                        message = new MyMessage(textcontent, IMessage.MessageType.RECEIVE_TEXT.ordinal());
                        message.setUserInfo(user2);
                    } else {
                        message = new MyMessage(textcontent, IMessage.MessageType.SEND_TEXT.ordinal());
                        message.setUserInfo(user1);
                    }
                    message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
                    list.add(message);

                }while (cursor.moveToNext());


            }

        Collections.reverse(list);



        Resources res = getResources();
        return list;   //下降顺序
    }

    private void initMsgAdapter() {
        ImageLoader imageLoader = new ImageLoader() {
            @Override
            public void loadAvatarImage(ImageView avatarImageView, String string) {
                // You can use other image load libraries.
                if (string.contains("R.drawable")) {
                    Integer resId = getResources().getIdentifier(string.replace("R.drawable.", ""),
                            "drawable", getPackageName());

                    avatarImageView.setImageResource(resId);
                } else {
                    Glide.with(getApplicationContext())
                            .load(string)
                            .placeholder(R.drawable.aurora_headicon_default)
                            .into(avatarImageView);
                }
            }

            @Override
            public void loadImage(ImageView imageView, String string) {
                // You can use other image load libraries.
                Glide.with(getApplicationContext())
                        .load(string)
                        .fitCenter()
                        .placeholder(R.drawable.aurora_picture_not_found)
                        .override(400, Target.SIZE_ORIGINAL)
                        .into(imageView);
            }
        };

        // Use default layout
        MsgListAdapter.HoldersConfig holdersConfig = new MsgListAdapter.HoldersConfig();
        mAdapter = new MsgListAdapter<>("0", holdersConfig, imageLoader);
        // If you want to customise your layout, try to create custom ViewHolder:
        // holdersConfig.setSenderTxtMsg(CustomViewHolder.class, layoutRes);
        // holdersConfig.setReceiverTxtMsg(CustomViewHolder.class, layoutRes);
        // CustomViewHolder must extends ViewHolders defined in MsgListAdapter.
        // Current ViewHolders are TxtViewHolder, VoiceViewHolder.

        mAdapter.setOnMsgClickListener(new MsgListAdapter.OnMsgClickListener<MyMessage>() {
            @Override
            public void onMessageClick(MyMessage message) {
                // do something
                if (message.getType() == IMessage.MessageType.RECEIVE_VIDEO.ordinal()
                        || message.getType() == IMessage.MessageType.SEND_VIDEO.ordinal()) {
                    if (!TextUtils.isEmpty(message.getMediaFilePath())) {
                        Intent intent = new Intent(MessageListActivity.this, VideoActivity.class);
                        intent.putExtra(VideoActivity.VIDEO_PATH, message.getMediaFilePath());
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            getApplicationContext().getString(R.string.message_click_hint),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        mAdapter.setMsgLongClickListener(new MsgListAdapter.OnMsgLongClickListener<MyMessage>() {
            @Override
            public void onMessageLongClick(MyMessage message) {
                Toast.makeText(getApplicationContext(),
                        getApplicationContext().getString(R.string.message_long_click_hint),
                        Toast.LENGTH_SHORT).show();
                // do something
            }
        });

        mAdapter.setOnAvatarClickListener(new MsgListAdapter.OnAvatarClickListener<MyMessage>() {
            @Override
            public void onAvatarClick(MyMessage message) {
                DefaultUser userInfo = (DefaultUser) message.getFromUser();
                Toast.makeText(getApplicationContext(),
                        getApplicationContext().getString(R.string.avatar_click_hint),
                        Toast.LENGTH_SHORT).show();
                // do something
            }
        });

        mAdapter.setMsgStatusViewClickListener(new MsgListAdapter.OnMsgStatusViewClickListener<MyMessage>() {
            @Override
            public void onStatusViewClick(MyMessage message) {
                // message status view click, resend or download here
            }
        });

//        MyMessage message = new MyMessage("Hello World", IMessage.MessageType.RECEIVE_TEXT.ordinal());
//        message.setUserInfo(user2);
      //  mAdapter.addToStart(message, true);
        MyMessage eventMsg = new MyMessage("above is history", IMessage.MessageType.EVENT.ordinal());
        mAdapter.addToStart(eventMsg, true);
        mAdapter.addToEnd(mData); //添加初始化消息
        PullToRefreshLayout layout = mChatView.getPtrLayout();
        /*
        以下是下拉刷新的处理
         */
        layout.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PullToRefreshLayout layout) {
                Log.i("MessageListActivity", "Loading next page");
                //加载
                loadNextPage();
            }
        });


        mChatView.setAdapter(mAdapter);
        mAdapter.getLayoutManager().scrollToPosition(0);
    }

    /*
      拉取历史消息
     */
    private void loadNextPage() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /*
                  从数据库拉取信息 //  界面应该记住当前展示的消息的历史id,并从本地数据库拉取
                 */
                List<MyMessage> list = new ArrayList<>();
                Resources res = getResources();
                String[] messages = {"1","2","1","2","21","211","211","12","2","2","2","2","2","2","2","2","2","2","2","2","2"};
                for(int i=0;i<messages.length;i++){
                    messages[i]="message:" + (messages.length-i);

                }

                if (pos == messages.length-1) {
                    MyMessage message= new MyMessage("没有更多消息", IMessage.MessageType.EVENT.ordinal());
                    pos++;
                    list.add(message);
                    mAdapter.addToEnd(list);
                    return;
                }
                for (int i = pos+1; i < messages.length; i++) {
                    MyMessage message;
                    if (i % 2 == 0) {
                        message = new MyMessage(messages[i], IMessage.MessageType.RECEIVE_TEXT.ordinal());
                        message.setUserInfo(user2);
                    } else {
                        message = new MyMessage(messages[i], IMessage.MessageType.SEND_TEXT.ordinal());
                        message.setUserInfo(user1);
                    }
                    message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
                    list.add(message);
                    if (i-pos == 4) {
                        pos = i;
                        break;
                    }

                    /*
                      实现了每次加载4条消息
                     */
                }
            //    Collections.reverse(list); //倒序排列
                mAdapter.addToEnd(list);
                mChatView.getPtrLayout().refreshComplete();
            }
        }, 1500);
    }

    private void scrollToBottom() {
        mAdapter.getLayoutManager().scrollToPosition(0);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                ChatInputView chatInputView = mChatView.getChatInputView();

                if (view.getId() == chatInputView.getInputView().getId()) {
                    scrollToBottom();
                    if (chatInputView.getMenuState() == View.VISIBLE
                            && !chatInputView.getSoftInputState()) {
                        chatInputView.dismissMenuAndResetSoftMode();
                        return false;
                    } else {
                        return false;
                    }
                }
                if (chatInputView.getMenuState() == View.VISIBLE) {
                    chatInputView.dismissMenuLayout();
                }
                try {
                    View v = getCurrentFocus();
                    if (mImm != null && v != null) {
                        mImm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        mWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                        view.clearFocus();
                        chatInputView.setSoftInputState(false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case MotionEvent.ACTION_UP:
                view.performClick();
                break;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
        mSensorManager.unregisterListener(this);
        chating.setChating(0);
    }
}

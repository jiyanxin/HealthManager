package com.example.jiyanxin.loginui.others;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.jiyanxin.loginui.BaseActivity;
import com.example.jiyanxin.loginui.R;
import com.example.jiyanxin.loginui.Service.LongRunningService;
import com.example.jiyanxin.loginui.viewitem.TopTitle;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by JIYANXIN on 2017/4/5.
 */

public class ActivityMain extends BaseActivity {

    private TextView text;
    SharedPreferences pref;

    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    update();
                    break;
            }
            super.handleMessage(msg);
        }
        void update() {
            pref.getInt("time",0);
            SharedPreferences.Editor editor = pref.edit();
            if(!pref.contains("time")){
                editor.putInt("time",0);
            }
            if(!pref.contains("healthSum")){
                editor.putInt("healthSum",0);
            }
            if(!pref.contains("states")){
                editor.putInt("states",0);
            }
            if(!pref.contains("heartbeats")){
                editor.putInt("heartbeats",0);
            }
            if(!pref.contains("temperature")){
                editor.putFloat("temperature",0f);
            }
            if(!pref.contains("oxygenblood")){
                editor.putFloat("oxygenblood",0);
            }
            if(!pref.contains("sports")){
                editor.putInt("sports",0);
            }

            editor.commit();

            text.setText(" "+pref.getInt("time",0)+" "+pref.getInt("healthSum",0)+" "+pref.getInt("states",0)
                    +" "+pref.getInt("heartbeats",0)+ " "+pref.getFloat("temperature",0)+" "+pref.getFloat("oxygenblood",0)
                    +" "+pref.getInt("sports",0));
            //  text.setText("22355");
        }
    };

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter filter = new IntentFilter();
        filter.addAction(TopTitle.BROADCAST_ACTION_EXIT);
        registerReceiver(mBroadcastReceiver,filter);

        text = (TextView)findViewById(R.id.test);
        pref = PreferenceManager.getDefaultSharedPreferences(this);

    }

    @Override
    protected void onStart(){
        super.onStart();
        timer.schedule(task, 0, 1000 * 1); //启动timer
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(TopTitle.BROADCAST_ACTION_EXIT)){//发来关闭action的广播
                finish();
            }
        }
    };

    Timer timer = new Timer();
    TimerTask task = new TimerTask() {
        public void run() {
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        }
    };

    @Override
    protected void onDestroy() {
        if (timer != null) {// 停止timer
            timer.cancel();
            timer = null;
        }
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }
}

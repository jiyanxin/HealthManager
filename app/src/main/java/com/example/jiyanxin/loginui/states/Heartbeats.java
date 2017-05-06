package com.example.jiyanxin.loginui.states;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.TextView;

import com.example.jiyanxin.loginui.PublicData;
import com.example.jiyanxin.loginui.R;
import com.example.jiyanxin.loginui.homepage.HealthSumService;
import com.example.jiyanxin.loginui.viewitem.Analyse;
import com.example.jiyanxin.loginui.viewitem.Bottom;
import com.example.jiyanxin.loginui.viewitem.LineGraphicView;
import com.example.jiyanxin.loginui.viewitem.TopTitle;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by JIYANXIN on 2017/4/21.
 */

public class Heartbeats extends Activity {

    private LineGraphicView healthHeartbeats;
    private ArrayList<Double> yListHealthbeats;
    private ArrayList<String> xRawDatasHealthbeats;

    private Analyse heartbeatsAnalyse;

    private TextView heartbeatText;

    private TopStates topStates;
    private Bottom bottom;

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
            if(!pref.contains("heartbeats")){
                editor.putInt("heartbeats",0);
            }

            editor.commit();
            heartbeatText.setText(" "+pref.getInt("heartbeats",0)+" ");

            healthHeartbeats.setData(PublicData.yListHeartbeats,PublicData.xRawDatasHeartbeats,50, 150, 25);
            healthHeartbeats.invalidate();

            heartbeatsAnalyse.setMaxValue(""+PublicData.maxHeartbeats);
            heartbeatsAnalyse.setMinValue(""+PublicData.minHeartbeats);
            heartbeatsAnalyse.setArgValue(""+PublicData.argHeartbeats);
        }
    };

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_heartbeats);
        findView();
    }

    @Override
    protected void onStart(){
        super.onStart();
//        timer.schedule(task, 0, 1000 * 1); //启动timer
    }

    public void findView(){
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        timer.schedule(task, 0, 1000 * 1); //启动timer

        topStates = (TopStates) findViewById(R.id.top_heartbeats);
        topStates.activity_from = Heartbeats.this;
        topStates.heartbeats.setBackgroundColor(Color.parseColor("#FF02CAE1"));

        bottom = (Bottom)findViewById(R.id.bottom_heartbeats);
        bottom.activity_from = Heartbeats.this;
        bottom.states.setBackgroundColor(Color.parseColor("#FF02CAE1"));

        IntentFilter filter = new IntentFilter();
        filter.addAction(TopTitle.BROADCAST_ACTION_EXIT);
        registerReceiver(mBroadcastReceiver,filter);

        healthHeartbeats = (LineGraphicView) findViewById(R.id.lineViewHeartbeats);
        heartbeatsAnalyse = (Analyse)findViewById(R.id.heartbeat_analyse);

        heartbeatText = (TextView)findViewById(R.id.heartbeatsText);

        initLineGraphicViewHeartbeats();


        heartbeatsAnalyse.argValue.setText("75");
        heartbeatsAnalyse.minValue.setText("65");
        heartbeatsAnalyse.maxValue.setText("90");
    }

    public void initLineGraphicViewHeartbeats(){
        /****************初始化设置******************/
        healthHeartbeats.setMstyle(LineGraphicView.Linestyle.Line);
        healthHeartbeats.setmPaintColor(getResources().getColor(R.color.red));
        healthHeartbeats.setmPaintColorX(getResources().getColor(R.color.color_f2f2f2));
        healthHeartbeats.setmPaintColorY(getResources().getColor(R.color.color_999999));
        healthHeartbeats.setmPaintWidth(5f,8f,3f);
        healthHeartbeats.setCircleSize(LineGraphicView.CircleSize.Mid);
        /*****************输入y坐标************************/

        if(PublicData.yListHeartbeats.size()< PublicData.NyHeartbeats){
            PublicData.yListHeartbeats.clear();
            for(int  i=0;i< PublicData.NyHeartbeats;i++){
                PublicData.yListHeartbeats.add(75d);
            }
        }

        if(PublicData.xRawDatasHeartbeats.size()< PublicData.NxHeartbeats){
            PublicData.xRawDatasHeartbeats.clear();
            for(int  i=0;i< PublicData.NxHeartbeats;i++){
                PublicData.xRawDatasHeartbeats.add("10:10");
            }
        }
        healthHeartbeats.setData(PublicData.yListHeartbeats, PublicData.xRawDatasHeartbeats,50, 150, 25);
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

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

public class OxygenBlood extends Activity {
    private LineGraphicView healthBlood;
    private ArrayList<Double> yListBlood;
    private ArrayList<String> xRawDatasBlood;
    private Analyse bloodAnalyse;

    private TextView oxygenBloodText;
    private TextView oxygenStates;

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
            if(!pref.contains("oxygenblood")){
                editor.putFloat("oxygenblood",0);
            }

            editor.commit();
            oxygenBloodText.setText(" "+pref.getFloat("oxygenblood",0)+" ");
            oxygenStates.setText(PublicData.tempOxygenBlood_states);


            healthBlood.setData(PublicData.yListOxygenBlood,PublicData.xRawDatasOxygenBlood,90,100, 2);
            healthBlood.invalidate();

            bloodAnalyse.setMaxValue(""+PublicData.maxOxygenBlood);
            bloodAnalyse.setMinValue(""+PublicData.minOxygenBlood);
            bloodAnalyse.setArgValue(""+PublicData.argOxygenBlood);
        }
    };

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_oxygenblood);
        findView();
    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    public void findView(){
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        timer.schedule(task, 0, 1000 * 1); //启动timer

        topStates = (TopStates) findViewById(R.id.top_oxygen);
        topStates.activity_from = OxygenBlood.this;
        topStates.oxygenBlood.setBackgroundColor(Color.parseColor("#FF02CAE1"));

        bottom = (Bottom)findViewById(R.id.bottom_oxygen);
        bottom.activity_from = OxygenBlood.this;
        bottom.states.setBackgroundColor(Color.parseColor("#FF02CAE1"));

        IntentFilter filter = new IntentFilter();
        filter.addAction(TopTitle.BROADCAST_ACTION_EXIT);
        registerReceiver(mBroadcastReceiver,filter);

        healthBlood = (LineGraphicView)findViewById(R.id.lineViewBlood);
        bloodAnalyse = (Analyse)findViewById(R.id.blood_analyse);

        oxygenBloodText = (TextView)findViewById(R.id.oxygenbloodText);
        oxygenStates = (TextView)findViewById(R.id.oxygen_states);

        initLineGraphicViewBlood();


        bloodAnalyse.argValue.setText("75");
        bloodAnalyse.minValue.setText("65");
        bloodAnalyse.maxValue.setText("90");
    }

    public void initLineGraphicViewBlood(){
        /****************初始化设置******************/
        healthBlood.setMstyle(LineGraphicView.Linestyle.Line);
        healthBlood.setmPaintColor(getResources().getColor(R.color.orangered));
        healthBlood.setmPaintColorX(getResources().getColor(R.color.color_f2f2f2));
        healthBlood.setmPaintColorY(getResources().getColor(R.color.color_999999));
        healthBlood.setmPaintWidth(5f,8f,3f);
        healthBlood.setCircleSize(LineGraphicView.CircleSize.Mid);
        /*****************输入y坐标************************/

        if(PublicData.yListOxygenBlood.size()< PublicData.NyOxygenBlood){
            PublicData.yListOxygenBlood.clear();
            for(int  i=0;i< PublicData.NyOxygenBlood;i++){
                PublicData.yListOxygenBlood.add(97d);
            }
        }

        if(PublicData.xRawDatasOxygenBlood.size()< PublicData.NxOxygenBlood){
            PublicData.xRawDatasOxygenBlood.clear();
            for(int  i=0;i< PublicData.NxOxygenBlood;i++){
                PublicData.xRawDatasOxygenBlood.add("10:10");
            }
        }
        healthBlood.setData(PublicData.yListOxygenBlood, PublicData.xRawDatasOxygenBlood,90, 100, 2);
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

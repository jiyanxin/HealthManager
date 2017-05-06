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

public class Temperature extends Activity {
    private LineGraphicView healthTemperature;
    private ArrayList<Double> yListTemperature;
    private ArrayList<String> xRawDatasTemperature;
    private Analyse temperatureAnalyse;
    private TextView temperatureText;

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
            if(!pref.contains("temperature")){
                editor.putFloat("temperature",0f);
            }

            editor.commit();
            temperatureText.setText(" "+pref.getFloat("temperature",0)+" ");

            healthTemperature.setData(PublicData.yListTemperature,PublicData.xRawDatasTemperature,35, 42, 1);
            healthTemperature.invalidate();

            temperatureAnalyse.setMaxValue(""+PublicData.maxTemperature);
            temperatureAnalyse.setMinValue(""+PublicData.minTemperature);
            temperatureAnalyse.setArgValue(""+PublicData.argTemperature);

        }
    };

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_temperature);
        findView();
    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    public void findView(){
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        timer.schedule(task, 0, 1000 * 1); //启动timer

        topStates = (TopStates) findViewById(R.id.top_temperature);
        topStates.activity_from = Temperature.this;
        topStates.temperature.setBackgroundColor(Color.parseColor("#FF02CAE1"));

        bottom = (Bottom)findViewById(R.id.bottom_temperature);
        bottom.activity_from = Temperature.this;
        bottom.states.setBackgroundColor(Color.parseColor("#FF02CAE1"));

        IntentFilter filter = new IntentFilter();
        filter.addAction(TopTitle.BROADCAST_ACTION_EXIT);
        registerReceiver(mBroadcastReceiver,filter);

        healthTemperature = (LineGraphicView)findViewById(R.id.lineViewTemperature);
        temperatureAnalyse = (Analyse)findViewById(R.id.temperature_analyse);

        temperatureText = (TextView)findViewById(R.id.temperatureText);

        initLineGraphicViewTemperature();


        temperatureAnalyse.argValue.setText("75");
        temperatureAnalyse.minValue.setText("65");
        temperatureAnalyse.maxValue.setText("90");

    }


    public void initLineGraphicViewTemperature(){
        /****************初始化设置******************/
        healthTemperature.setMstyle(LineGraphicView.Linestyle.Curve);
        healthTemperature.setmPaintColor(getResources().getColor(R.color.lightyellow));
        healthTemperature.setmPaintColorX(getResources().getColor(R.color.color_f2f2f2));
        healthTemperature.setmPaintColorY(getResources().getColor(R.color.color_999999));
        healthTemperature.setmPaintWidth(5f,8f,3f);
        healthTemperature.setCircleSize(LineGraphicView.CircleSize.Mid);
        /*****************输入y坐标************************/

        if(PublicData.yListTemperature.size()< PublicData.NyTemperature){
            PublicData.yListTemperature.clear();
            for(int  i=0;i< PublicData.NyTemperature;i++){
                PublicData.yListTemperature.add(39d);
            }
        }

        if(PublicData.xRawDatasTemperature.size()< PublicData.NxTemperature){
            PublicData.xRawDatasTemperature.clear();
            for(int  i=0;i< PublicData.NxTemperature;i++){
                PublicData.xRawDatasTemperature.add("10:10");
            }
        }
        healthTemperature.setData(PublicData.yListTemperature, PublicData.xRawDatasTemperature,35, 42, 1);
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

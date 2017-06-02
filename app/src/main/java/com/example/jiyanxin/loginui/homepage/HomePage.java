package com.example.jiyanxin.loginui.homepage;

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
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.jiyanxin.loginui.PublicData;
import com.example.jiyanxin.loginui.Statistics;
import com.example.jiyanxin.loginui.viewitem.Analyse;
import com.example.jiyanxin.loginui.viewitem.LineGraphicView;
import com.example.jiyanxin.loginui.R;
import com.example.jiyanxin.loginui.viewitem.Bottom;
import com.example.jiyanxin.loginui.viewitem.TopTitle;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by JIYANXIN on 2017/4/10.
 */

public class HomePage extends Activity implements View.OnClickListener{


    private TopTitle topTitle;
    private Bottom bottom;

    private CircleHomePage circleHomePage;
    private TextView suggestion;

    private LineGraphicView healthSum;
    private ArrayList<Double> yList;
    private ArrayList<String> xRawDatas;

    private Analyse homeAnalyse;
    private Statistics statistics;
    private double max,min,arg;
    private float healthSumValue;

    SharedPreferences pref;

    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    update();
                    //count();
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
            }if(!pref.contains("array_y_HealthSum")){
                editor.putString("array_y_HealthSum","40#");
            }

            editor.commit();

//            healthSumValue = (float)pref.getInt("healthSum",0);
//            circleHomePage.setProgressSync(healthSumValue);
            circleHomePage.setProgressSync(PublicData.tempHealthSum);

            if(""==PublicData.heartSuggestion && ""==PublicData.temperatureSuggestion && ""==PublicData.oxygenSuggestion){
                suggestion.setText("身体很健康，继续保持！");
            }else{
                suggestion.setText(PublicData.heartSuggestion+"\n"+PublicData.temperatureSuggestion+"\n"+PublicData.oxygenSuggestion);
            }

//            suggestion.setText(PublicData.heartScore+" "+PublicData.temperatureScore+" "+PublicData.oxygenScore);

            healthSum.setData(PublicData.yListHealthSum,PublicData.xRawDatasHealthSum,60, 100, 10);
            healthSum.invalidate();

            homeAnalyse.setMaxValue(""+PublicData.maxHealthSum);
            homeAnalyse.setMinValue(""+PublicData.minHealthSum);
            homeAnalyse.setArgValue(""+PublicData.argHealthSum);

        }
    };

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.home_page);
        findView();
    }

    public void findView(){

        topTitle = (TopTitle)findViewById(R.id.top_homepage);
        topTitle.activity_from=HomePage.this;
        topTitle.title.setText("首页");

        bottom = (Bottom)findViewById(R.id.bottom_homepage);
        bottom.activity_from=HomePage.this;
        bottom.homePage.setBackgroundColor(Color.parseColor("#FF02CAE1"));

        IntentFilter filter = new IntentFilter();
        filter.addAction(TopTitle.BROADCAST_ACTION_EXIT);
        registerReceiver(mBroadcastReceiver,filter);


        healthSum = (LineGraphicView) findViewById(R.id.lineViewSum);
        homeAnalyse = (Analyse)findViewById(R.id.home_analyse);

        suggestion = (TextView)findViewById(R.id.suggestion);

        circleHomePage = (CircleHomePage)findViewById(R.id.circle_homepage);
        circleHomePage.setMax(100);
        circleHomePage.setProgressSync(healthSumValue);

        initLineGraphicView();

        pref = PreferenceManager.getDefaultSharedPreferences(this);

    }

    @Override
    protected void onStart(){
        super.onStart();
        timer.schedule(task, 0, 1000 * 1); //启动timer
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            default:
                break;
        }
    }



    public void initLineGraphicView(){
        /*****************初始化设置*******************/
        healthSum.setMstyle(LineGraphicView.Linestyle.Curve);
        healthSum.setmPaintColor(getResources().getColor(R.color.blue));
        healthSum.setmPaintColorX(getResources().getColor(R.color.color_f2f2f2));
        healthSum.setmPaintColorY(getResources().getColor(R.color.color_999999));
        healthSum.setmPaintWidth(5f,8f,3f);
        healthSum.setCircleSize(LineGraphicView.CircleSize.Mid);

        if(PublicData.yListHealthSum.size()< PublicData.NyHealthSum){
            PublicData.yListHealthSum.clear();
            for(int  i=0;i< PublicData.NyHealthSum;i++){
                PublicData.yListHealthSum.add(75d);
            }
        }

        if(PublicData.xRawDatasHealthSum.size()< PublicData.NxHealthSum){
            PublicData.xRawDatasHealthSum.clear();
            for(int  i=0;i< PublicData.NxHealthSum;i++){
                PublicData.xRawDatasHealthSum.add("10:10");
            }
        }

        healthSum.setData(PublicData.yListHealthSum,PublicData.xRawDatasHealthSum,60, 100, 10);
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

    public void count(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 2;
                handler.sendMessage(message);
            }
        }).start();
    }

}

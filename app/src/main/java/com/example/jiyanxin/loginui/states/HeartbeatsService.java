package com.example.jiyanxin.loginui.states;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.jiyanxin.loginui.PublicData;
import com.example.jiyanxin.loginui.Service.AlarmReceiver;
import com.example.jiyanxin.loginui.Statistics;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Created by JIYANXIN on 2017/4/23.
 */

public class HeartbeatsService extends Service {
    private int time=0;
    private int result=0;
    SharedPreferences pref;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        pref = PreferenceManager.getDefaultSharedPreferences(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                UpdataHeartbeats();
                Log.d("LongRunningService", "executed at " + new Date().toString());
            }
        }).start();

        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 10 * 1000; // 这是一小时的毫秒数
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this, HeartbeatsAlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }

    public void UpdataHeartbeats() {

        /**********更新Y数组*********/
        Statistics statistics = new Statistics();
        int i = 0, flag = 0;
        Random random = new Random();
        DecimalFormat df = new java.text.DecimalFormat("###");

        while (flag == 0) {
            double r = random.nextDouble();
            if (0.050 < r && r < 0.150) {
                flag = 1;
                String number = df.format(r * 1000);
                double num = Double.parseDouble(number);

                if (PublicData.yListHeartbeats.size() < PublicData.NyHeartbeats) {
                    PublicData.yListHeartbeats.clear();
                    for (i = 0; i < PublicData.NyHeartbeats; i++) {
                        PublicData.yListHeartbeats.add(75d);
                    }
                } else if (PublicData.yListHeartbeats.size() == PublicData.NyHeartbeats) {
                    PublicData.yListHeartbeats.remove(0);
                    PublicData.yListHeartbeats.add(num);
                }
            }
        }
        /***************更新X数组****************/
        PublicData.tempHeartbeats++;
        if (PublicData.NxyHeartbeats == PublicData.tempHeartbeats) {
            Calendar currentTime = Calendar.getInstance();
            currentTime.getTime();
            currentTime.get(currentTime.AM_PM);
            String temp = currentTime.get(currentTime.HOUR) + ":" + currentTime.get(currentTime.MINUTE) + ":" + currentTime.get(currentTime.SECOND);
            if (PublicData.xRawDatasHeartbeats.size() < PublicData.NxHeartbeats) {
                PublicData.xRawDatasHeartbeats.clear();
                for (i = 0; i < PublicData.NxHeartbeats; i++) {
                    PublicData.xRawDatasHeartbeats.add("10:10");
                }
            } else if (PublicData.xRawDatasHeartbeats.size() == PublicData.NxHeartbeats) {
                PublicData.xRawDatasHeartbeats.remove(0);
                PublicData.xRawDatasHeartbeats.add(temp);
            }
            PublicData.tempHeartbeats = 0;
        }
//        else if (PublicData.NxyHeartbeats == PublicData.tempHeartbeats){
//            PublicData.tempHeartbeats = 0;
//        }
        /********************计算数据***********************/
        PublicData.argHeartbeats = statistics.AverageDouble(PublicData.yListHeartbeats);
        String number = df.format(PublicData.argHeartbeats);
        PublicData.argHeartbeats = Double.parseDouble(number);

        PublicData.maxHeartbeats= statistics.getMaxDouble(PublicData.yListHeartbeats);
        PublicData.minHeartbeats = statistics.getMinDouble(PublicData.yListHeartbeats);
        /****************心率*****************/
        double heartbeats;
        heartbeats = PublicData.yListHeartbeats.get(PublicData.NyHeartbeats-1) ;
        if(heartbeats<=60){
            PublicData.tempHeartbeats_states = "窦性心动过缓";
            PublicData.heartSuggestion = "心跳较慢，及时检查身体；";
        }else if(heartbeats>60 && heartbeats<=95){
            PublicData.tempHeartbeats_states = "正常";
            PublicData.heartSuggestion = "";
        }else if(heartbeats>95 && heartbeats<=160){
            PublicData.tempHeartbeats_states = "窦性心动过速";
            PublicData.heartSuggestion = "心跳较快，避免剧烈运动；";
        }else{
            PublicData.tempHeartbeats_states = "阵发性心动过速";
            PublicData.heartSuggestion = "心跳过快，呼叫紧急电话；";
        }
        if(heartbeats<75){
            PublicData.heartScore = (75-heartbeats)/35*20+20;
        }else{
            PublicData.heartScore = (heartbeats-75)/125*20+20;
        }
    }
    
}

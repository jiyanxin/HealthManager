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

public class OxygenBloodService extends Service {
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
                UpdataOxygenBlood();
                Log.d("LongRunningService", "executed at " + new Date().toString());
            }
        }).start();

        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 10 * 1000; // 这是一小时的毫秒数
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this, OxygenBloodAlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }

    public void UpdataOxygenBlood() {

        /**********更新Y数组*********/
        Statistics statistics = new Statistics();
        int i = 0, flag = 0;
        Random random = new Random();
        DecimalFormat df = new java.text.DecimalFormat("##.##");

        while (flag == 0) {
            double r = random.nextDouble();
            if (0.90 < r && r < 1.00) {
                flag = 1;
                String number = df.format(r * 100);
                double num = Double.parseDouble(number);

                if (PublicData.yListOxygenBlood.size() < PublicData.NyOxygenBlood) {
                    PublicData.yListOxygenBlood.clear();
                    for (i = 0; i < PublicData.NyOxygenBlood; i++) {
                        PublicData.yListOxygenBlood.add(97d);
                    }
                } else if (PublicData.yListOxygenBlood.size() == PublicData.NyOxygenBlood) {
                    PublicData.yListOxygenBlood.remove(0);
                    PublicData.yListOxygenBlood.add(num);
                }
            }
        }
        /***************更新X数组****************/
        PublicData.tempOxygenBlood++;
        if (PublicData.NxyOxygenBlood == PublicData.tempOxygenBlood){
            Calendar currentTime = Calendar.getInstance();
            currentTime.getTime();
            currentTime.get(currentTime.AM_PM);
            String temp = currentTime.get(currentTime.HOUR) + ":" + currentTime.get(currentTime.MINUTE) + ":" + currentTime.get(currentTime.SECOND);
            if (PublicData.xRawDatasOxygenBlood.size() < PublicData.NxOxygenBlood) {
                PublicData.xRawDatasOxygenBlood.clear();
                for (i = 0; i < PublicData.NxOxygenBlood; i++) {
                    PublicData.xRawDatasOxygenBlood.add("10:10");
                }
            } else if (PublicData.xRawDatasOxygenBlood.size() == PublicData.NxOxygenBlood) {
                PublicData.xRawDatasOxygenBlood.remove(0);
                PublicData.xRawDatasOxygenBlood.add(temp);
            }
            PublicData.tempOxygenBlood = 0;
        }
//        else if (PublicData.NxyOxygenBlood == PublicData.tempOxygenBlood){
//            PublicData.tempOxygenBlood = 0;
//        }
        /********************计算数据***********************/
        PublicData.argOxygenBlood = statistics.AverageDouble(PublicData.yListOxygenBlood);
        String number = df.format(PublicData.argOxygenBlood);
        PublicData.argOxygenBlood = Double.parseDouble(number);

        PublicData.maxOxygenBlood= statistics.getMaxDouble(PublicData.yListOxygenBlood);
        PublicData.minOxygenBlood = statistics.getMinDouble(PublicData.yListOxygenBlood);
        /****************血氧饱和度*****************/
        double oxygenBlood;
        oxygenBlood = PublicData.yListOxygenBlood.get(PublicData.NyOxygenBlood-1);
        if(oxygenBlood<=90){
            PublicData.tempOxygenBlood_states = "血氧过低";
            PublicData.oxygenSuggestion = "血氧过低，呼叫救助。";
        }else if(oxygenBlood>90 && oxygenBlood<=94){
            PublicData.tempOxygenBlood_states = "供氧不足";
            PublicData.oxygenSuggestion = "血氧不足，如果您在室内，请保持空气流通。";
        }else{
            PublicData.tempOxygenBlood_states = "正常";
            PublicData.oxygenSuggestion = "";
        }
        if(oxygenBlood<97){
            PublicData.oxygenScore = (97-oxygenBlood)/12*15+15;
        }else{
            PublicData.oxygenScore = (oxygenBlood-97)/3*15+15;
        }
    }
}

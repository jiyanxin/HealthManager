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

public class TemperatureService extends Service {
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
                UpdataTemperature();
                Log.d("LongRunningService", "executed at " + new Date().toString());
            }
        }).start();

        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 5 * 1000; // 这是一小时的毫秒数
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this, TemperatureAlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }

    public void UpdataTemperature() {

        /**********更新Y数组*********/
        Statistics statistics = new Statistics();
        int i = 0, flag = 0;
        Random random = new Random();
        DecimalFormat df = new java.text.DecimalFormat("##.##");

        while (flag == 0) {
            double r = random.nextDouble();
            if (0.35 < r && r < 0.420) {
                flag = 1;
                String number = df.format(r * 100);
                double num = Double.parseDouble(number);

                if (PublicData.yListTemperature.size() < PublicData.NyTemperature) {
                    PublicData.yListTemperature.clear();
                    for (i = 0; i < PublicData.NyTemperature; i++) {
                        PublicData.yListTemperature.add(39d);
                    }
                } else if (PublicData.yListTemperature.size() == PublicData.NyTemperature) {
                    PublicData.yListTemperature.remove(0);
                    PublicData.yListTemperature.add(num);
                }
            }
        }
        /***************更新X数组****************/
        PublicData.tempTemperature++;
        if (PublicData.NxyTemperature == PublicData.tempTemperature){
            Calendar currentTime = Calendar.getInstance();
            currentTime.getTime();
            currentTime.get(currentTime.AM_PM);
            String temp = currentTime.get(currentTime.HOUR) + ":" + currentTime.get(currentTime.MINUTE) + ":" + currentTime.get(currentTime.SECOND);
            if (PublicData.xRawDatasTemperature.size() < PublicData.NxTemperature) {
                PublicData.xRawDatasTemperature.clear();
                for (i = 0; i < PublicData.NxTemperature; i++) {
                    PublicData.xRawDatasTemperature.add("10:10");
                }
            } else if (PublicData.xRawDatasTemperature.size() == PublicData.NxTemperature) {
                PublicData.xRawDatasTemperature.remove(0);
                PublicData.xRawDatasTemperature.add(temp);
            }
            PublicData.tempTemperature = 0;
        }
//        else if (PublicData.NxyTemperature == PublicData.tempTemperature){
//            PublicData.tempTemperature = 0;
//        }
        /********************计算数据***********************/
        PublicData.argTemperature = statistics.AverageDouble(PublicData.yListTemperature);
        String number = df.format(PublicData.argTemperature);
        PublicData.argTemperature = Double.parseDouble(number);

        PublicData.maxTemperature= statistics.getMaxDouble(PublicData.yListTemperature);
        PublicData.minTemperature = statistics.getMinDouble(PublicData.yListTemperature);
    }
}

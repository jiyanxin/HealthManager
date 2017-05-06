package com.example.jiyanxin.loginui.homepage;

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

public class HealthSumService extends Service {
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
                UpdataHealthSum();
                Log.d("LongRunningService", "executed at " + new Date().toString());
            }
        }).start();

        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 5 * 1000; // 这是一小时的毫秒数
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this, HealthSumAlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }

    public void UpdataHealthSum() {

        /**********更新Y数组*********/
        Statistics statistics = new Statistics();
        int i = 0, flag = 0;
        Random random = new Random();
        DecimalFormat df = new java.text.DecimalFormat("###");

        while (flag == 0) {
            double r = random.nextDouble();
            if (0.60 < r && r < 1.0) {
                flag = 1;
                String number = df.format(r * 100);
                double num = Double.parseDouble(number);

                if (PublicData.yListHealthSum.size() < PublicData.NyHealthSum) {
                    PublicData.yListHealthSum.clear();
                    for (i = 0; i < PublicData.NyHealthSum; i++) {
                        PublicData.yListHealthSum.add(75d);
                    }
                } else if (PublicData.yListHealthSum.size() == PublicData.NyHealthSum) {
                    PublicData.yListHealthSum.remove(0);
                    PublicData.yListHealthSum.add(num);
                }
            }
        }
        /***************更新X数组****************/
        PublicData.tempHealthSum++;
        if (PublicData.NxyHealthSum == PublicData.tempHealthSum) {
            Calendar currentTime = Calendar.getInstance();
            currentTime.getTime();
            currentTime.get(currentTime.AM_PM);
            String temp = currentTime.get(currentTime.HOUR) + ":" + currentTime.get(currentTime.MINUTE) + ":" + currentTime.get(currentTime.SECOND);
            if (PublicData.xRawDatasHealthSum.size() < PublicData.NxHealthSum) {
                PublicData.xRawDatasHealthSum.clear();
                for (i = 0; i < PublicData.NxHealthSum; i++) {
                    PublicData.xRawDatasHealthSum.add("10:10");
                }
            } else if (PublicData.xRawDatasHealthSum.size() == PublicData.NxHealthSum) {
                PublicData.xRawDatasHealthSum.remove(0);
                PublicData.xRawDatasHealthSum.add(temp);
            }
            PublicData.tempHealthSum = 0;
        }
//        else if (PublicData.NxyHealthSum == PublicData.tempHealthSum) {
//            PublicData.tempHealthSum = 0;
//        }
        /********************计算数据***********************/
        PublicData.argHealthSum = statistics.AverageDouble(PublicData.yListHealthSum);
        String number = df.format(PublicData.argHealthSum);
        PublicData.argHealthSum = Double.parseDouble(number);

        PublicData.maxHealthSum= statistics.getMaxDouble(PublicData.yListHealthSum);
        PublicData.minHealthSum = statistics.getMinDouble(PublicData.yListHealthSum);
    }
}

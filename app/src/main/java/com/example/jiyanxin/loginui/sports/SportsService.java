package com.example.jiyanxin.loginui.sports;

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

public class SportsService extends Service {
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
                UpdataSports();
                Log.d("LongRunningService", "executed at " + new Date().toString());
            }
        }).start();

        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 5 * 1000; // 这是一小时的毫秒数
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this, SportsAlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }

    public void UpdataSports() {

        /**********更新Y数组*********/
        Statistics statistics = new Statistics();
        int i = 0, flag = 0;
        Random random = new Random();
        DecimalFormat df = new java.text.DecimalFormat("##.###");

        while (flag == 0) {
            double r = random.nextDouble();
            if (0.02 < r && r < 0.16) {
                flag = 1;
                String number = df.format(r * 100);
                double num = Double.parseDouble(number);

                if (PublicData.yListSports.size() < PublicData.NySports) {
                    PublicData.yListSports.clear();
                    for (i = 0; i < PublicData.NySports; i++) {
                        PublicData.yListSports.add(3.458d);
                    }
                } else if (PublicData.yListSports.size() == PublicData.NySports) {
                    PublicData.yListSports.remove(0);
                    PublicData.yListSports.add(num);
                }
            }
        }
        /***************更新X数组****************/
        PublicData.tempSports++;
        if (PublicData.NxySports == PublicData.tempSports) {
            Calendar currentTime = Calendar.getInstance();
            currentTime.getTime();
            currentTime.get(currentTime.AM_PM);
            String temp = currentTime.get(currentTime.HOUR) + ":" + currentTime.get(currentTime.MINUTE) + ":" + currentTime.get(currentTime.SECOND);
            if (PublicData.xRawDatasSports.size() < PublicData.NxSports) {
                PublicData.xRawDatasSports.clear();
                for (i = 0; i < PublicData.NxSports; i++) {
                    PublicData.xRawDatasSports.add("10:10");
                }
            } else if (PublicData.xRawDatasSports.size() == PublicData.NxSports) {
                PublicData.xRawDatasSports.remove(0);
                PublicData.xRawDatasSports.add(temp);
            }
            PublicData.tempSports = 0;
        }
//        else if (PublicData.NxySports == PublicData.tempSports){
//            PublicData.tempSports = 0;
//        }
        /********************计算数据***********************/
        PublicData.argSports = statistics.AverageDouble(PublicData.yListSports);
        String number = df.format(PublicData.argSports);
        PublicData.argSports = Double.parseDouble(number);

        PublicData.maxSports= statistics.getMaxDouble(PublicData.yListSports);
        PublicData.minSports = statistics.getMinDouble(PublicData.yListSports);
    }
}

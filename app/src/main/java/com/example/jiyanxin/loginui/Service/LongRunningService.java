package com.example.jiyanxin.loginui.Service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.jiyanxin.loginui.HttpConnection;
import com.example.jiyanxin.loginui.PublicData;
import com.example.jiyanxin.loginui.Statistics;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import static com.example.jiyanxin.loginui.PublicData.NyHealthSum;

/**
 * Created by JIYANXIN on 2017/4/20.
 */

public class LongRunningService extends Service {
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
                NetConnection();
//                Analyze();
                Log.d("LongRunningService", "executed at " + new Date().toString());
            }
        }).start();

        SharedPreferences.Editor editor = pref.edit();
        if(!pref.contains("time")){
            editor.putInt("time",time);
        }else {
            time=time+1;
            editor.putInt("time", time);

        }
       // editor.putInt("healthSum",result);
        editor.commit();

        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 10 * 1000; // 这是一小时的毫秒数
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }

    public void NetConnection(){
        int healthSum = 0;
        int heartbeats = 0;
        double temperature = 0f;
        double oxygenblood = 0f;
        int sports=0;
        int states =0;
        SharedPreferences.Editor editor = pref.edit();
        if(!pref.contains("account")){
            editor.putString("account","***");
        }
        editor.commit();
        String username = pref.getString("account","");
        String params = "uid="+username;

        String urlstr = "http://120.25.207.101/requestSum.php";

        HttpConnection httpConnection = new HttpConnection();
        String result = httpConnection.HttpConnectionSendAndReveice(params,urlstr);

        try {
            JSONObject jsonObject = new JSONObject(result);
            healthSum = jsonObject.getInt("healthSum");//获取JSON数据中的states字段值
            heartbeats = jsonObject.getInt("heartbeats");
            temperature = jsonObject.getDouble("temperature");
            oxygenblood = jsonObject.getDouble("oxygenblood");
            sports = jsonObject.getInt("sports");
            states = jsonObject.getInt("states");
        } catch (Exception e) {
            Log.e("log_tag", "the Error parsing data" + e.toString());
        }
        editor.putInt("healthSum",healthSum);
        editor.putInt("heartbeats",heartbeats);
        editor.putFloat("temperature",(float)temperature);
        editor.putFloat("oxygenblood",(float)oxygenblood);
        editor.putInt("sports",sports);
        editor.putInt("states",states);
        editor.commit();
    }

    public void Analyze(){
        /****************心率*****************/
        if(PublicData.tempHeartbeats<=60){
            PublicData.tempHeartbeats_states = "窦性心动过缓";
        }else if(PublicData.tempHeartbeats>60 && PublicData.tempHeartbeats<=95){
            PublicData.tempHeartbeats_states = "正常";
        }else if(PublicData.tempHeartbeats>95 && PublicData.tempHeartbeats<=160){
            PublicData.tempHeartbeats_states = "窦性心动过速";
        }else{
            PublicData.tempHeartbeats_states = "阵发性心动过速";
        }
        /****************体温*****************/
        if(PublicData.tempTemperature<=36.2){
            PublicData.tempTemperature_states = "低温";
        }else if(PublicData.tempTemperature>36.3 && PublicData.tempTemperature<=37.2){
            PublicData.tempTemperature_states = "正常";
        }else if(PublicData.tempTemperature>37.2 && PublicData.tempTemperature<=38){
            PublicData.tempTemperature_states = "低热";
        }else if(PublicData.tempTemperature>38 && PublicData.tempTemperature<=39){
            PublicData.tempTemperature_states = "中度发热";
        }else{
            PublicData.tempTemperature_states = "高热";
        }
        /****************血氧饱和度*****************/
        if(PublicData.tempOxygenBlood<=90){
            PublicData.tempOxygenBlood_states = "血氧过低";
        }else if(PublicData.tempOxygenBlood>90 && PublicData.tempOxygenBlood<=94){
            PublicData.tempOxygenBlood_states = "供氧不足";
        }else{
            PublicData.tempOxygenBlood_states = "正常";
        }
        /****************健康综合*****************/

    }
//    public void UpdataHealthSum() {
//
//        /**********更新Y数组*********/
//        Statistics statistics = new Statistics();
//        int i = 0, flag = 0;
//        Random random = new Random();
//        DecimalFormat df = new java.text.DecimalFormat("##.##");
//
//        while (flag == 0) {
//            double r = random.nextDouble();
//            if (0.350 < r && r < 0.420) {
//                flag = 1;
//                String number = df.format(r * 100);
//                double num = Double.parseDouble(number);
//
//                if (PublicData.yListHealthSum.size() < PublicData.NyHealthSum) {
//                    PublicData.yListHealthSum.clear();
//                    for (i = 0; i < PublicData.NyHealthSum; i++) {
//                        PublicData.yListHealthSum.add(39d);
//                    }
//                } else if (PublicData.yListHealthSum.size() == PublicData.NyHealthSum) {
//                    PublicData.yListHealthSum.remove(0);
//                    PublicData.yListHealthSum.add(num);
//                }
//            }
//        }
//        /***************更新X数组****************/
//        PublicData.tempHealthSum++;
//        if (PublicData.NxyHealthSum == PublicData.tempHealthSum) {
//            Calendar currentTime = Calendar.getInstance();
//            currentTime.getTime();
//            currentTime.get(currentTime.AM_PM);
//            String temp = currentTime.get(currentTime.HOUR) + ":" + currentTime.get(currentTime.MINUTE) + ":" + currentTime.get(currentTime.SECOND);
//            if (PublicData.xRawDatasHealthSum.size() < PublicData.NxHealthSum) {
//                PublicData.xRawDatasHealthSum.clear();
//                for (i = 0; i < PublicData.NxHealthSum; i++) {
//                    PublicData.xRawDatasHealthSum.add("10:10");
//                }
//            } else if (PublicData.xRawDatasHealthSum.size() == PublicData.NxHealthSum) {
//                PublicData.xRawDatasHealthSum.remove(0);
//                PublicData.xRawDatasHealthSum.add(temp);
//            }
//            PublicData.tempHealthSum = 0;
//        }
//        /********************计算数据***********************/
//        statistics.SortDouble(PublicData.yListHealthSum);
//        PublicData.argHealthSum = statistics.AverageDouble(PublicData.yListHealthSum);
//        String number = df.format(PublicData.argHealthSum);
//        PublicData.argHealthSum = Double.parseDouble(number);
//        PublicData.maxHealthSum= statistics.getMaxDouble();
//        PublicData.minHealthSum = statistics.getMinDouble();
//    }
}

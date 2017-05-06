package com.example.jiyanxin.loginui.sports;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.jiyanxin.loginui.homepage.HealthSumService;

/**
 * Created by JIYANXIN on 2017/4/23.
 */

public class SportsAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, SportsService.class);
        context.startService(i);
    }
}

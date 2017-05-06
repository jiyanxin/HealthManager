package com.example.jiyanxin.loginui.homepage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by JIYANXIN on 2017/4/23.
 */

public class HealthSumAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, HealthSumService.class);
        context.startService(i);
    }
}

package com.competition.kakin.mtreatment.broadcast;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import com.competition.kakin.mtreatment.service.AlarmService2;
import com.competition.kakin.mtreatment.tool.CustomMethod;

/**
 * Created by kakin on 2016/8/15.
 */
public class CancelAlarmReceiver extends BroadcastReceiver {
    private int index;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    public CancelAlarmReceiver(){

    }
    public CancelAlarmReceiver(int index, AlarmManager alarmManager, PendingIntent pendingIntent){
        this.index = index;
        this.alarmManager = alarmManager;
        this.pendingIntent = pendingIntent;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        if (new CustomMethod().isServiceWork(context, "com.competition.kakin.mtreatment.AlarmService2")){
            Intent i = new Intent(context, AlarmService2.class);
            int position = i.getIntExtra("position", 0);
            //取消闹钟
            if (index == position){
                alarmManager.cancel(pendingIntent);
            }
        }
    }

}

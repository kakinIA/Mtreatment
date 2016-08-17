package com.competition.kakin.mtreatment.broadcast;

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
public class AlarmServiceReceiver extends BroadcastReceiver {
    private ServiceConnection conn;
    public AlarmServiceReceiver(ServiceConnection conn){
        this.conn = conn;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        if (new CustomMethod().isServiceWork(context, "com.competition.kakin.mtreatment.AlarmService2")){
            Intent i = new Intent(context, AlarmService2.class);
            context.unbindService(conn);
            context.stopService(i);
            Bundle b = intent.getExtras();
            i.putExtras(b);

        }
    }

}

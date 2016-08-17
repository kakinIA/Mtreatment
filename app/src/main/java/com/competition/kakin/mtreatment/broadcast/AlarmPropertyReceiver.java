package com.competition.kakin.mtreatment.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.ListView;

import com.competition.kakin.mtreatment.Adapter.MedAlarmAdapter;
import com.competition.kakin.mtreatment.AddAlarmProperty;
import com.competition.kakin.mtreatment.MedAlarmContent;
import com.competition.kakin.mtreatment.service.AlarmService2;
import com.competition.kakin.mtreatment.tool.AlarmInfo;
import com.competition.kakin.mtreatment.tool.CustomMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kakin on 2016/8/12.
 */
public class AlarmPropertyReceiver extends BroadcastReceiver{

    private Context mContext;
    private AddAlarmProperty alarmProperty;
    private List<MedAlarmContent> medAlarmContents;
    private MedAlarmAdapter medAlarmAdapter;
    private ListView listView;
    private ServiceConnection conn;
    public AlarmPropertyReceiver(Context mContext, AddAlarmProperty addAlarmProperty, List<MedAlarmContent> medAlarmContents, MedAlarmAdapter medAlarmAdapter, ListView listView, ServiceConnection conn){
        this.mContext = mContext;
        this.alarmProperty = addAlarmProperty;
        this.medAlarmContents = medAlarmContents;
        this.medAlarmAdapter = medAlarmAdapter;
        this.listView = listView;
        this.conn = conn;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle b = intent.getExtras();
        AddAlarmProperty addAlarmProperty = b.getParcelable("alarmProperty");
        alarmProperty = addAlarmProperty;
        if (alarmProperty != null && new CustomMethod().getTimebetWeen(alarmProperty.getAlarmTime()) >= 0) {

            AlarmInfo alarmInfo = new AlarmInfo(context);
            Boolean setAlarm = true;
            if (alarmInfo.getAlarm_Property() != null){
                ArrayList<AddAlarmProperty> addAlarmProperties = alarmInfo.getAlarm_Property();
                ArrayList<Boolean> setAlarms = alarmInfo.getIsSetAlarms();
                System.out.println("添加第二次之前" + setAlarms.size());
                addAlarmProperties.add(addAlarmProperty);
                setAlarms.add(setAlarm);
                alarmInfo.setAlarm_Property(addAlarmProperties);
                alarmInfo.setIsSetAlarms(setAlarms);
                ArrayList<Boolean> setAlarmlist = alarmInfo.getIsSetAlarms();
                System.out.println("setLalrmList是空的吗？？" + setAlarmlist.size());
            }else {
                ArrayList<AddAlarmProperty> addAlarmProperties = new ArrayList<>();
                ArrayList<Boolean> setAlarms = new ArrayList<>();
                System.out.println("添加第二次之前" + setAlarms.size());
                addAlarmProperties.add(addAlarmProperty);
                setAlarms.add(setAlarm);
                alarmInfo.setAlarm_Property(addAlarmProperties);
                alarmInfo.setIsSetAlarms(setAlarms);
                alarmInfo.setAlarm_Property(addAlarmProperties);
                ArrayList<Boolean> setAlarmlist = new AlarmInfo(context).getIsSetAlarms();
                System.out.println("setLalrmList是空的吗？？" + setAlarmlist.size());
            }
            Map<String, Integer> resTime = new HashMap<>();
            resTime.put("dayOfMonth", 0);
            resTime.put("hourOfDay", 0);
            resTime.put("minute", 0);
            MedAlarmContent medAlarmContent = new MedAlarmContent(alarmProperty.getName(), alarmProperty.getAlarmTime(),
                    alarmProperty.getDose(), alarmProperty.getWay(), resTime);
            medAlarmContents.add(medAlarmContent);
            medAlarmAdapter.notifyDataSetChanged();
            System.out.println(alarmProperty.getName() + alarmProperty.getAlarmTime() +
                    alarmProperty.getDose() + alarmProperty.getWay());
            System.out.println("服务是否已经开启：" + new CustomMethod().isServiceWork(context, "com.competition.kakin.mtreatment.service.AlarmService2"));
            if (new CustomMethod().isServiceWork(context, "com.competition.kakin.mtreatment.service.AlarmService2")) {
                Intent i = new Intent(context, AlarmService2.class);
                context.unbindService(conn);
                context.stopService(i);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("medAlarmContents", (ArrayList<? extends Parcelable>) medAlarmContents);
                i.putExtras(bundle);
                context.startService(i);
                context.bindService(i, conn, Context.BIND_AUTO_CREATE);
            }else {
                Intent i = new Intent(context, AlarmService2.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("medAlarmContents", (ArrayList<? extends Parcelable>) medAlarmContents);
                i.putExtras(bundle);
                context.startService(i);
                context.bindService(i, conn, Context.BIND_AUTO_CREATE);
            }
        }
    }

}

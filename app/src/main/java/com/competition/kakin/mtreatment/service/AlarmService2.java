package com.competition.kakin.mtreatment.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.competition.kakin.mtreatment.MedAlarmContent;
import com.competition.kakin.mtreatment.UI.Notifi.ClockActivity;
import com.competition.kakin.mtreatment.broadcast.CancelAlarmReceiver;
import com.competition.kakin.mtreatment.tool.AlarmInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kakin on 2016/8/13.
 */
public class AlarmService2 extends Service{
    ArrayList<MedAlarmContent> medAlarmContents;//content
    ArrayList<Map<String, Integer>> resTimes ;//剩余时间
    ArrayList<Boolean> setAlarms;//是否在倒计时
    CancelAlarmReceiver[]  cancelAlarmReceivers;
    private TimeBinder timeBinder = new TimeBinder();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return timeBinder;
    }
    public class TimeBinder extends Binder {
        public AlarmService2 getService(){
            return AlarmService2.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        setAlarms = new AlarmInfo(this).getIsSetAlarms();
        Bundle b;
        if (intent != null && intent.getExtras() != null){
            b = intent.getExtras();
            medAlarmContents = b.getParcelableArrayList("medAlarmContents");
        }
        resTimes = new ArrayList<>();
        cancelAlarmReceivers = new CancelAlarmReceiver[medAlarmContents.size()];
        for (int i = 0; i < medAlarmContents.size(); i++){
            final Map<String, Integer> theEndTime = medAlarmContents.get(i).getAlarmTime();
            final int p = i;
            resTimes.add(p, new HashMap<String, Integer>());
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            Intent clockIntent = new Intent(this, ClockActivity.class);
            clockIntent.putExtra("index", p);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, i, clockIntent, 0);
            int theAlarmMinute = theEndTime.get("minute");
            int theAlarmHour = theEndTime.get("hourOfDay");
            int theAlarmDay = theEndTime.get("dayOfMonth");
            int theAlarmMonth = theEndTime.get("monthOfYear");
            int theAlarmYear = theEndTime.get("year");
            Calendar alarmCalendar = Calendar.getInstance();
            alarmCalendar.set(Calendar.MINUTE, theAlarmMinute);
            alarmCalendar.set(Calendar.HOUR_OF_DAY, theAlarmHour);
            alarmCalendar.set(Calendar.DAY_OF_MONTH, theAlarmDay);
            alarmCalendar.set(Calendar.MONTH, theAlarmMonth - 1);
            alarmCalendar.set(Calendar.YEAR, theAlarmYear);
            final long alarmCalendarTime = alarmCalendar.getTimeInMillis();
            alarmManager.set(AlarmManager.RTC_WAKEUP, alarmCalendarTime, pendingIntent);
            cancelAlarmReceivers[i] = new CancelAlarmReceiver(i, alarmManager, pendingIntent);//接收广播时取消alarmManager
            IntentFilter filter = new IntentFilter("com.competition.kakin.mtreatment.broadcast.cancelalarmbroadcast");
            registerReceiver(cancelAlarmReceivers[i], filter);
            //倒计时
            if (setAlarms.get(p) == true){
                    new Thread(){
                        @Override
                        public void run() {
                            long betWeenTime = alarmCalendarTime - System.currentTimeMillis();
                            while (true){
                                long tolalarmDay = betWeenTime/1000/60/60/24;
                                long tolalarmHour = betWeenTime/1000/60/60;
                                long tolalarmMinute = betWeenTime/1000/60;
                                long tolalarmSecond = betWeenTime/1000;
                                int resAlarmDay = (int) tolalarmDay;
                                int resAlarmHour = (int) (tolalarmHour%24);
                                int resAlarmMinute = (int) (tolalarmMinute%60);
                                int resAlarmSecond = (int) (tolalarmSecond%60);
                                System.out.println("时间差："+resAlarmDay+"天"+resAlarmHour+"时"+resAlarmMinute+"分"+resAlarmSecond+"秒" + "--------线程" + Thread.currentThread().getId());
                                resTimes.get(p).put("dayOfMonth", resAlarmDay);
                                resTimes.get(p).put("hourOfDay", resAlarmHour);
                                resTimes.get(p).put("minute", resAlarmMinute + 1);
                                resTimes.get(p).put("second", resAlarmSecond);
                                if (showCallBack != null){
                                    showCallBack.onTimeShow(resTimes);
                                    showCallBack.setEndContent(medAlarmContents);
                                    if (betWeenTime < 0){
                                        System.out.println("betWeenTime等于零时");
                                        resTimes.get(p).put("dayOfMonth", 0);
                                        resTimes.get(p).put("hourOfDay", 0);
                                        resTimes.get(p).put("minute", 0);
                                        resTimes.get(p).put("second", 0);
                                        showCallBack.onTimeShow(resTimes);
                                        try {
                                            sleep(2000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();

                                        }
                                        resTimes.remove(p);
                                        medAlarmContents.remove(p);
                                        showCallBack.setEndContent(medAlarmContents);
                                        setAlarms.remove(p);
                                        new AlarmInfo(getBaseContext()).setIsSetAlarms(setAlarms);
                                        break;
                                    }

                                }
                                if (setAlarms.get(p)== false){
                                    break;
                                }
                                try {
                                    sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                betWeenTime = alarmCalendarTime - System.currentTimeMillis();
                            }

                        }
                    }.start();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }
    public interface ShowCallBack{
        void onTimeShow(ArrayList<Map<String, Integer>> resTimes);
        void setEndContent(ArrayList<MedAlarmContent> medAlarmContents);
    }
    private ShowCallBack showCallBack =null;

    public ShowCallBack getShowCallBack() {
        return showCallBack;
    }

    public void setShowCallBack(ShowCallBack showCallBack) {
        this.showCallBack = showCallBack;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("服务执行onDestroy");
        for (int i = 0; i < setAlarms.size(); i++){
            Boolean setAlarm = setAlarms.get(i);
            setAlarm = false;
            setAlarms.set(i, setAlarm);
        }
        for (int i = 0; i < cancelAlarmReceivers.length; i++){//所有接收取消闹钟的广播注销掉
            unregisterReceiver(cancelAlarmReceivers[i]);
        }
    }
}

package com.competition.kakin.mtreatment.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

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
        for (int i = 0; i < medAlarmContents.size(); i++){
            final Map<String, Integer> theEndTime = medAlarmContents.get(i).getAlarmTime();
            final int p = i;
            resTimes.add(p, new HashMap<String, Integer>());
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
                                medAlarmContents.get(p).setResTime(resTimes.get(p));
                                if (showCallBack != null){
                                    if (betWeenTime >= 0){
                                        showCallBack.UpdataContent(medAlarmContents);
                                    }else {
                                        System.out.println("betWeenTime等于零时");
                                        resTimes.get(p).put("dayOfMonth", 0);
                                        resTimes.get(p).put("hourOfDay", 0);
                                        resTimes.get(p).put("minute", 0);
                                        resTimes.get(p).put("second", 0);
                                        showCallBack.UpdataEndContent(medAlarmContents, p);
                                        Intent clockIntent = new Intent(AlarmService2.this, ClockActivity.class);
                                        clockIntent.putExtra("index", p);
                                        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                                        PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), p, clockIntent, 0);
                                        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmCalendarTime, pendingIntent);
                                        System.out.println("--------线程" + Thread.currentThread().getId() + "停止");
                                        break;
                                    }

                                }
                                if (setAlarms.get(p)== false){
                                    System.out.println("--------线程" + Thread.currentThread().getId() + "停止");
                                    break;
                                }
                                try {
                                    sleep(7*1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                betWeenTime = alarmCalendarTime - System.currentTimeMillis();
                            }

                        }
                    }.start();
            }
        }
        if (medAlarmContents.size() == 0){
            new Thread(){
                @Override
                public void run() {
                    for (int i = 0; i < 2; i ++){
                        try {
                            sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (showCallBack != null){
                            showCallBack.UpdataContent(medAlarmContents);
                        }
                    }
                }
            }.start();
        }
        return super.onStartCommand(intent, flags, startId);
    }
    public interface ShowCallBack{
        void UpdataContent(ArrayList<MedAlarmContent> medAlarmContents);
        void UpdataEndContent(ArrayList<MedAlarmContent> medAlarmContents, int position);
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

    }
}

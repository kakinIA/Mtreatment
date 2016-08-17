package com.competition.kakin.mtreatment.tool;

import android.app.ActivityManager;
import android.content.Context;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kakin on 2016/8/15.
 */
public class CustomMethod {
    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param mContext
     * @param serviceName
     *            是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    public boolean isServiceWork(Context mContext, String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(40);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }

    public long getTimebetWeen(int year, int month, int day, int hour, int minute){
        Calendar alarmCalendar = Calendar.getInstance();
        alarmCalendar.set(Calendar.MINUTE, minute);
        alarmCalendar.set(Calendar.HOUR_OF_DAY, hour);
        alarmCalendar.set(Calendar.DAY_OF_MONTH, day);
        alarmCalendar.set(Calendar.MONTH, month - 1);
        alarmCalendar.set(Calendar.YEAR, year);
        System.out.println("闹响时间：" + alarmCalendar.get(Calendar.YEAR)+alarmCalendar.get(Calendar.MONTH)
                +alarmCalendar.get(Calendar.DAY_OF_MONTH) + alarmCalendar.get(Calendar.HOUR_OF_DAY) + alarmCalendar.get(Calendar.MINUTE));
        long TimebetWeen = alarmCalendar.getTimeInMillis() - System.currentTimeMillis();
        return TimebetWeen;
    }
    public long getTimebetWeen(Map<String, Integer> targetTime){
        int minute = targetTime.get("minute");
        int hour = targetTime.get("hourOfDay");
        int day = targetTime.get("dayOfMonth");
        int month = targetTime.get("monthOfYear");
        int year = targetTime.get("year");
        Calendar alarmCalendar = Calendar.getInstance();
        alarmCalendar.set(Calendar.MINUTE, minute);
        alarmCalendar.set(Calendar.HOUR_OF_DAY, hour);
        alarmCalendar.set(Calendar.DAY_OF_MONTH, day);
        alarmCalendar.set(Calendar.MONTH, month - 1);
        alarmCalendar.set(Calendar.YEAR, year);
        System.out.println("闹响时间：" + alarmCalendar.get(Calendar.YEAR)+alarmCalendar.get(Calendar.MONTH)
                +alarmCalendar.get(Calendar.DAY_OF_MONTH) + alarmCalendar.get(Calendar.HOUR_OF_DAY) + alarmCalendar.get(Calendar.MINUTE));
        long TimebetWeen = alarmCalendar.getTimeInMillis() - System.currentTimeMillis();
        return TimebetWeen;
    }
    public Map<String, Integer> getResTime(Map<String, Integer> targetTime){
        int minute = targetTime.get("minute");
        int hour = targetTime.get("hourOfDay");
        int day = targetTime.get("dayOfMonth");
        int month = targetTime.get("monthOfYear");
        int year = targetTime.get("year");
        Calendar alarmCalendar = Calendar.getInstance();
        alarmCalendar.set(Calendar.MINUTE, minute);
        alarmCalendar.set(Calendar.HOUR_OF_DAY, hour);
        alarmCalendar.set(Calendar.DAY_OF_MONTH, day);
        alarmCalendar.set(Calendar.MONTH, month - 1);
        alarmCalendar.set(Calendar.YEAR, year);
        System.out.println("闹响时间：" + alarmCalendar.get(Calendar.YEAR)+alarmCalendar.get(Calendar.MONTH)
                +alarmCalendar.get(Calendar.DAY_OF_MONTH) + alarmCalendar.get(Calendar.HOUR_OF_DAY) + alarmCalendar.get(Calendar.MINUTE));
        long TimebetWeen = alarmCalendar.getTimeInMillis() - System.currentTimeMillis();
        long tolalarmDay = TimebetWeen/1000/60/60/24;
        long tolalarmHour = TimebetWeen/1000/60/60;
        long tolalarmMinute = TimebetWeen/1000/60;
        int resAlarmDay = (int) tolalarmDay;
        int resAlarmHour = (int) (tolalarmHour%24);
        int resAlarmMinute = (int) (tolalarmMinute%60);
        Map<String, Integer> resTime = new HashMap<>();
        resTime.put("dayOfMonth", resAlarmDay);
        resTime.put("hourOfDay", resAlarmHour);
        resTime.put("minute", resAlarmMinute);
        return resTime;
    }
}

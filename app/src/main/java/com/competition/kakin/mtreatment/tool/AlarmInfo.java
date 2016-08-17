package com.competition.kakin.mtreatment.tool;

import android.content.Context;
import android.content.SharedPreferences;

import com.competition.kakin.mtreatment.AddAlarmProperty;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by kakin on 2016/8/14.
 */
public class AlarmInfo {
    private Context mContext;
    private static  final String IS_SETALARM = "is_setAlarm";
    private static  final String ALARM_PROPERTY = "alarm_property";
    private static  final String DETECT_CHANGE = "detect_change";
    public AlarmInfo(){

    }
    public AlarmInfo(Context mContext){
        this.mContext = mContext;
    }

    /***
     * 设置是否倒计时，true为倒计时
     * @param value
     */
    public void setIsSetAlarms(ArrayList<Boolean> value){
        SharedPreferences sp = mContext.getSharedPreferences(IS_SETALARM, mContext.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        String is_set_alarm_gson = gson.toJson(value);
        editor.putString(IS_SETALARM, is_set_alarm_gson);
        editor.commit();
    }

    /**
     * 获得倒计时状态
     * @return
     */
    public ArrayList<Boolean> getIsSetAlarms(){
        SharedPreferences sp = mContext.getSharedPreferences(IS_SETALARM, mContext.MODE_PRIVATE);
        String is_set_alarm_gson = sp.getString(IS_SETALARM, "null");
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Boolean>>(){}.getType();
        ArrayList<Boolean> value = gson.fromJson(is_set_alarm_gson, type);
        return value;
    }

    /***
     * 保存已经设置好的闹钟属性
     * @param addAlarmProperties
     */
    public void setAlarm_Property(ArrayList<AddAlarmProperty> addAlarmProperties){
        SharedPreferences sp = mContext.getSharedPreferences(ALARM_PROPERTY, mContext.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        String alarm_property_gson = gson.toJson(addAlarmProperties);
        editor.putString(ALARM_PROPERTY, alarm_property_gson);
        editor.commit();
    }

    /***
     * 获得已经存储好的闹钟属性
     * @return
     */
    public ArrayList<AddAlarmProperty> getAlarm_Property(){
        SharedPreferences sp = mContext.getSharedPreferences(ALARM_PROPERTY, mContext.MODE_PRIVATE);
        String alarm_property_gson = sp.getString(ALARM_PROPERTY, "null");
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<AddAlarmProperty>>(){}.getType();
        ArrayList<AddAlarmProperty> addAlarmProperties = gson.fromJson(alarm_property_gson, type);
        return addAlarmProperties;
    }

    public void setCurrentAmount(int amount){
        SharedPreferences sp = mContext.getSharedPreferences(DETECT_CHANGE, mContext.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("CurrentAmount", amount);
        editor.commit();
    }
    public int getCurrentAmount(){
        SharedPreferences sp = mContext.getSharedPreferences(DETECT_CHANGE, mContext.MODE_PRIVATE);
        int currentAmount = sp.getInt("CurrentAmount", 0);
        return currentAmount;
    }

}

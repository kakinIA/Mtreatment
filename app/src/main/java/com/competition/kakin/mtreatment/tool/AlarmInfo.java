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
    private String IS_SETALARM = "is_setAlarm";
    private String ALARM_PROPERTY = "alarm_property";
    public AlarmInfo(){

    }
    public AlarmInfo(Context mContext){
        this.mContext = mContext;
    }
    public void setIsSetAlarms(ArrayList<Boolean> value){
        SharedPreferences sp = mContext.getSharedPreferences(IS_SETALARM, mContext.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        String is_set_alarm_gson = gson.toJson(value);
        editor.putString(IS_SETALARM, is_set_alarm_gson);
        editor.commit();
    }
    public ArrayList<Boolean> getIsSetAlarms(){
        SharedPreferences sp = mContext.getSharedPreferences(IS_SETALARM, mContext.MODE_PRIVATE);
        String is_set_alarm_gson = sp.getString(IS_SETALARM, "null");
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Boolean>>(){}.getType();
        ArrayList<Boolean> value = gson.fromJson(is_set_alarm_gson, type);
        return value;
    }

    public void setAlarm_Property(ArrayList<AddAlarmProperty> addAlarmProperties){
        SharedPreferences sp = mContext.getSharedPreferences(ALARM_PROPERTY, mContext.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        String alarm_property_gson = gson.toJson(addAlarmProperties);
        editor.putString(ALARM_PROPERTY, alarm_property_gson);
        editor.commit();
    }
    public ArrayList<AddAlarmProperty> getAlarm_Property(){
        SharedPreferences sp = mContext.getSharedPreferences(ALARM_PROPERTY, mContext.MODE_PRIVATE);
        String alarm_property_gson = sp.getString(ALARM_PROPERTY, "null");
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<AddAlarmProperty>>(){}.getType();
        ArrayList<AddAlarmProperty> addAlarmProperties = gson.fromJson(alarm_property_gson, type);
        return addAlarmProperties;
    }
}

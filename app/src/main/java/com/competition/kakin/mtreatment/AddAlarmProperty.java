package com.competition.kakin.mtreatment;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kakin on 2016/8/12.
 */
public class AddAlarmProperty implements Parcelable{
    private String name;
    private Map<String, Integer> alarmTime;
    private String way;
    private String dose;
    public AddAlarmProperty(){

    }
    public AddAlarmProperty(String name, Map<String, Integer> alarmTime, String way, String dose){
        this.name = name;
        this.alarmTime = alarmTime;
        this.way = way;
        this.dose = dose;
    }

    public String getName() {
        return name;
    }

    public Map<String, Integer> getAlarmTime() {
        return alarmTime;
    }

    public String getWay() {
        return way;
    }

    public String getDose() {
        return dose;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAlarmTime(Map<String, Integer> alarmTime) {
        this.alarmTime = alarmTime;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }

    public void setWay(String way) {
        this.way = way;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeMap(alarmTime);
        dest.writeString(way);
        dest.writeString(dose);
    }
    public static final Parcelable.Creator<AddAlarmProperty> CREATOR = new Parcelable.Creator<AddAlarmProperty>(){

        @Override
        public AddAlarmProperty createFromParcel(Parcel source) {
            AddAlarmProperty addAlarmProperty = new AddAlarmProperty();
            addAlarmProperty.name = source.readString();
            addAlarmProperty.alarmTime = source.readHashMap(HashMap.class.getClassLoader());
            addAlarmProperty.way = source.readString();
            addAlarmProperty.dose = source.readString();
            return  addAlarmProperty;
        }

        @Override
        public AddAlarmProperty[] newArray(int size) {
            return new AddAlarmProperty[0];
        }
    };
}

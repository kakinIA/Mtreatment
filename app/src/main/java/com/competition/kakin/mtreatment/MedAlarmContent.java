package com.competition.kakin.mtreatment;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kakin on 2016/8/9.
 */
public class MedAlarmContent implements Parcelable{
    private String name;
    private Map<String, Integer> alarmTime;
    private String dose;
    private String eatWay;
    private Map<String, Integer> resTime;
    public MedAlarmContent(){

    }
    public MedAlarmContent(String name, Map<String, Integer> alarmTime, String dose, String eatWay, Map<String, Integer> resTime){
        this.name = name;
        this.alarmTime = alarmTime;
        this.dose = dose;
        this.eatWay = eatWay;
        this.resTime = resTime;
    }

    public Map<String, Integer> getAlarmTime() {
        return alarmTime;
    }

    public Map<String, Integer> getResTime() {
        return resTime;
    }

    public String getDose() {
        return dose;
    }

    public String getEatWay() {
        return eatWay;
    }

    public String getName() {
        return name;
    }

    public void setAlarmTime(Map<String, Integer> alarmTime) {
        this.alarmTime = alarmTime;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }

    public void setEatWay(String eatWay) {
        this.eatWay = eatWay;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setResTime(Map<String, Integer> resTime) {
        this.resTime = resTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeMap(alarmTime);
        dest.writeString(dose);
        dest.writeString(eatWay);
        dest.writeMap(resTime);
    }
    public static final Parcelable.Creator<MedAlarmContent> CREATOR = new Parcelable.Creator<MedAlarmContent>(){

        @Override
        public MedAlarmContent createFromParcel(Parcel source) {
            MedAlarmContent medAlarmContent = new MedAlarmContent();
            medAlarmContent.name = source.readString();
            medAlarmContent.alarmTime = source.readHashMap(HashMap.class.getClassLoader());
            medAlarmContent.dose = source.readString();
            medAlarmContent.eatWay = source.readString();
            medAlarmContent.resTime = source.readHashMap(HashMap.class.getClassLoader());
            return medAlarmContent;
        }

        @Override
        public MedAlarmContent[] newArray(int size) {
            return new MedAlarmContent[0];
        }
    };
}

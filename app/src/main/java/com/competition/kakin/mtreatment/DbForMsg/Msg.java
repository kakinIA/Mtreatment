package com.competition.kakin.mtreatment.DbForMsg;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kakin on 2016/8/23.
 */
public class Msg implements Parcelable{
    private String doctorName;
    private String info;
    private int month;
    private int day;
    public Msg(){

    }
    public Msg(String doctorName, String info, int month, int day){
        this.doctorName = doctorName;
        this.info = info;
        this.month = month;
        this.day = day;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getInfo() {
        return info;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDay(int day) {
        this.day = day;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(doctorName);
        dest.writeString(info);
        dest.writeInt(month);
        dest.writeInt(day);
    }
    public static final Parcelable.Creator<Msg> CREATOR = new Parcelable.Creator<Msg>(){


        @Override
        public Msg createFromParcel(Parcel source) {
            Msg msg = new Msg();
            msg.doctorName = source.readString();
            msg.info = source.readString();
            msg.month = source.readInt();
            msg.day = source.readInt();
            return msg;
        }

        @Override
        public Msg[] newArray(int size) {
            return new Msg[0];
        }
    };
}

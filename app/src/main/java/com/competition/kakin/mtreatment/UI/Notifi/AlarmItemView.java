package com.competition.kakin.mtreatment.UI.Notifi;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.competition.kakin.mtreatment.MedAlarmContent;

import java.util.List;

/**
 * Created by kakin on 2016/8/10.
 */
public class AlarmItemView extends RelativeLayout {
    private boolean isBind = false;
    private boolean isServiceStart = false;
    private boolean isRegister = false;
    private List<MedAlarmContent> medAlarmContents;
    private int resId;
    private int position;
    private TextView alarmHour;
    private TextView alarmMinute;
    private TextView name;
    private TextView dose;
    private TextView eatWay;
    private TextView resTime;

    public AlarmItemView(Context context, AttributeSet attr, List<MedAlarmContent> medAlarmContents, int resId, int position) {
        super(context, attr);
        this.medAlarmContents = medAlarmContents;
        this.resId = resId;
        this.position = position;
    }

    public List<MedAlarmContent> getMedAlarmContents() {
        return medAlarmContents;
    }

    public TextView getAlarmHour() {
        return alarmHour;
    }

    public TextView getAlarmMinute() {
        return alarmMinute;
    }

    public TextView getDose() {
        return dose;
    }

    public TextView getEatWay() {
        return eatWay;
    }

    public TextView getName() {
        return name;
    }

    public TextView getResTime() {
        return resTime;
    }

    public void setMedAlarmContents(List<MedAlarmContent> medAlarmContents) {
        this.medAlarmContents = medAlarmContents;
    }

    public void setDose(TextView dose) {
        this.dose = dose;
    }

    public void setResTime(TextView resTime) {
        this.resTime = resTime;
    }

    public void setEatWay(TextView eatWay) {
        this.eatWay = eatWay;
    }

    public void setName(TextView name) {
        this.name = name;
    }

    public void setAlarmMinute(TextView alarmMinute) {
        this.alarmMinute = alarmMinute;
    }

    public void setAlarmHour(TextView alarmHour) {
        this.alarmHour = alarmHour;
    }

    public void setAlarmTime(){

    }
}

package com.competition.kakin.mtreatment.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.competition.kakin.mtreatment.MedAlarmContent;
import com.competition.kakin.mtreatment.R;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by kakin on 2016/8/9.
 */
public class MedAlarmAdapter extends ArrayAdapter<MedAlarmContent> {
    private int resId;
    public MedAlarmAdapter(Context context, int resource, List<MedAlarmContent> medAlarmContents) {
        super(context, resource, medAlarmContents);
        resId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MedAlarmContent medAlarmContent = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resId, null);
            viewHolder = new ViewHolder();
            viewHolder.alarmHour = (TextView) view.findViewById(R.id.alarm_hour);
            viewHolder.alarmMinute = (TextView) view.findViewById(R.id.alarm_minute);
            viewHolder.alarmYear = (TextView) view.findViewById(R.id.add_alarm_year);
            viewHolder.alarmMonth = (TextView) view.findViewById(R.id.add_alarm_month);
            viewHolder.alarmDay = (TextView) view.findViewById(R.id.add_alarm_day);
            viewHolder.name = (TextView) view.findViewById(R.id.med_name);
            viewHolder.dose = (TextView) view.findViewById(R.id.med_dose);
            viewHolder.eatWay = (TextView) view.findViewById(R.id.med_eatWay);
            viewHolder.resTime = (TextView) view.findViewById(R.id.resTime);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        DecimalFormat df = new DecimalFormat("00");
        viewHolder.alarmHour.setText(df.format(medAlarmContent.getAlarmTime().get("hourOfDay")));
        viewHolder.alarmMinute.setText(df.format(medAlarmContent.getAlarmTime().get("minute")));
        viewHolder.alarmYear.setText(medAlarmContent.getAlarmTime().get("year")+"");
        viewHolder.alarmMonth.setText(medAlarmContent.getAlarmTime().get("monthOfYear"+"")+"");
        viewHolder.alarmDay.setText(medAlarmContent.getAlarmTime().get("dayOfMonth")+"");
        viewHolder.name.setText(medAlarmContent.getName());
        viewHolder.dose.setText(medAlarmContent.getDose());
        viewHolder.eatWay.setText(medAlarmContent.getEatWay());
        if (medAlarmContent.getAlarmTime().get("dayOfMonth" ) != null) {
            if (medAlarmContent.getResTime().get("dayOfMonth") == 0) {
                viewHolder.resTime.setText(medAlarmContent.getResTime().get("hourOfDay") + "小时"
                        + medAlarmContent.getResTime().get("minute") + "分钟");
            } else {
                viewHolder.resTime.setText(medAlarmContent.getResTime().get("dayOfMonth") + "天"
                        + medAlarmContent.getResTime().get("hourOfDay") + "小时"
                        + medAlarmContent.getResTime().get("minute") + "分钟");
            }
        }
        return view;
    }
    public class ViewHolder{
        TextView alarmHour;
        TextView alarmMinute;
        TextView alarmYear;
        TextView alarmMonth;
        TextView alarmDay;
        TextView name;
        TextView dose;
        TextView eatWay;
        TextView resTime;
    }
}

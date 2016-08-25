package com.competition.kakin.mtreatment.tool;

import android.content.Context;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.competition.kakin.mtreatment.AddAlarmProperty;
import com.competition.kakin.mtreatment.DbForMsg.Record;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kakin on 2016/8/23.
 */
public class CatchMsg {
    private Context context;

    public CatchMsg(Context context){
        this.context = context;
    }
    /***
     * 捕抓信息中的本次病情
     */
    public Record getRecord(String fullMsg){
        String ill = "";
        String year = "";
        String month = "";
        String day = "";

        System.out.println("--------------------");
        System.out.println(fullMsg);
        System.out.println("---------------------");
        int startInIll = fullMsg.indexOf("本次病情") + 5;
        int endInIll = fullMsg.indexOf("。", startInIll);
        ill = fullMsg.substring(startInIll, endInIll);
        int startInY = endInIll + 1;
        int endInY = fullMsg.indexOf("年", startInY);
        System.out.println("开始是" + startInY + "结束是" + endInY);
        year = fullMsg.substring(startInY, endInY);
        int startInM = endInY + 1;
        int endInM = fullMsg.indexOf("月", startInY);
        month = fullMsg.substring(startInM, endInM);
        int startInD = endInM + 1;
        int endInD = fullMsg.indexOf("日", startInD);
        day = fullMsg.substring(startInD, endInD);

        System.out.println("捕抓病历中，病历为：" + ill +  "日期为"  + year + month + day);
        Record record = new Record(ill, year, month, day);
        return record;
    }
    public AddAlarmProperty getAlarm(String fullMsg){

        int startInname = fullMsg.indexOf("药物") + 3;
        int endInname = fullMsg.indexOf("。", startInname) ;
        String name = fullMsg.substring(startInname, endInname);
        int startInWay = fullMsg.indexOf("服用方式") + 5;
        int endInWay = fullMsg.indexOf("。", startInWay) ;
        String way = fullMsg.substring(startInWay, endInWay);
        int startInDose = fullMsg.indexOf("服用量") + 4;
        int endInDose = fullMsg.indexOf("。", startInDose) ;
        String dose = fullMsg.substring(startInDose, endInDose);
        System.out.println("药物为：" + name + "  方式为：" + way + "  用量为：" + dose);
        int alarmHour = 0;
        int hour = new Date().getHours();
        if (hour >0 && hour < 6){
            alarmHour = 7;
        }else if (hour >= 6 && hour < 10){
            alarmHour = 12;
        }else if (hour >= 10 && hour < 17){
            alarmHour = 19;
        }else if (hour >= 17 && hour <21){
            alarmHour = 22;
        }else {
            Toast.makeText(context, "今天已经超过吃药时间，请手动设置闹钟", Toast.LENGTH_SHORT).show();
            System.out.println("今天已经超过吃药时间，请手动设置闹钟");
        }
        Calendar currentDate = Calendar.getInstance();
        Map<String, Integer> alarmTime = new HashMap<String, Integer>();
        alarmTime.put("year", currentDate.get(Calendar.YEAR));
        alarmTime.put("monthOfYear", currentDate.get(Calendar.MONTH) + 1);
        alarmTime.put("dayOfMonth", currentDate.get(Calendar.DAY_OF_MONTH));
        if (alarmHour == 0){
            return null;
        }else {
            alarmTime.put("hourOfDay", alarmHour);
            alarmTime.put("minute", 30);
            AddAlarmProperty alarmProperty = new AddAlarmProperty(name, alarmTime, way, dose);
            return alarmProperty;
        }

    }


}

package com.competition.kakin.mtreatment.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.competition.kakin.mtreatment.MedAlarmContent;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by kakin on 2016/8/6.
 */
public class AlarmService extends Service {
    ArrayList<MedAlarmContent> medAlarmContents = new ArrayList<>();
//    Map<String, Integer> theEndTime;
    ArrayList<Map<String, Integer>> resTimes ;
//    Map<String, Integer> resTime;
    MediaPlayer mediaPlayer;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return timeBinder;
    }

    private TimeBinder timeBinder = new TimeBinder();
    public class TimeBinder extends Binder {

//        public void setEndTime(Map<String, Integer> endTime){
//            theEndTime = endTime;
//        }
        public AlarmService getService(){
            return AlarmService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle b;
        if (intent != null && intent.getExtras() != null){
            b = intent.getExtras();
            medAlarmContents = b.getParcelableArrayList("medAlarmContents");
        }
        resTimes = new ArrayList<>(medAlarmContents.size());
        for (int i = 0; i < medAlarmContents.size(); i++){
            final Map<String, Integer> theEndTime = medAlarmContents.get(i).getAlarmTime();
            final int p = i;
            resTimes.add(p, new HashMap<String, Integer>());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int theEndHour = theEndTime.get("hourOfDay");
                    int theEndMinute = theEndTime.get("minute");
                    System.out.println("theEndTime为：" + theEndHour + theEndMinute);
//                SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
//                String nowtime=format.format(new Date());
//                Log.d("LongRunService", "execute at" + nowtime);
                    SimpleDateFormat format = new SimpleDateFormat("ss");
                    Log.d("theSecond is :", format.format(new Date()));
                    SimpleDateFormat dtf = new SimpleDateFormat("hh:mm");
                    Date date = new Date();
                    int timeHour = date.getHours();
                    int timeMinute = date.getMinutes();
                    while (!(timeHour == theEndHour && timeMinute == theEndMinute)){
                        if (timeHour <= theEndHour && timeMinute <= theEndMinute){
                            int resHour = theEndHour - timeHour;
                            int resMinute = theEndMinute - timeMinute;
                            resTimes.get(p).put("dayOfMonth", 0);
                            resTimes.get(p).put("hourOfDay", resHour);
                            resTimes.get(p).put("minute", resMinute);
                            Log.d("坑爹时间：", date.getHours() +";" + date.getMinutes());
                            Log.d("-----------", resTimes.get(p).get("hourOfDay") + ";" + resTimes.get(p).get("minute"));
                        }else if (timeHour <= theEndHour && timeMinute > theEndMinute){
                            int resHour = theEndHour - timeHour - 1;
                            int resMinute = 60 - (timeMinute - theEndMinute);
                            resTimes.get(p).put("dayOfMonth", 0);
                            resTimes.get(p).put("hourOfDay", resHour);
                            resTimes.get(p).put("minute", resMinute);
                            Log.d("坑爹时间：", date.getHours() +";" + date.getMinutes());
                            Log.d("-----------", resTimes.get(p).get("hourOfDay") + ";" + resTimes.get(p).get("minute"));
                        }else if (timeHour > theEndHour && timeMinute <= theEndMinute){
                            int resHour = theEndHour + (24 - timeHour);
                            int resMinute = theEndMinute - timeMinute;
                            resTimes.get(p).put("dayOfMonth", 0);
                            resTimes.get(p).put("hourOfDay", resHour);
                            resTimes.get(p).put("minute", resMinute);
                            Log.d("坑爹时间：", date.getHours() +";" + date.getMinutes());
                            Log.d("-----------", resTimes.get(p).get("hourOfDay") + ";" + resTimes.get(p).get("minute"));
                        }else if (timeHour > theEndHour && timeMinute > theEndMinute){
                            int resHour = theEndHour + (24- timeHour) - 1;
                            int resMinute = 60 - (timeMinute - theEndMinute);
                            resTimes.get(p).put("dayOfMonth", 0);
                            resTimes.get(p).put("hourOfDay", resHour);
                            resTimes.get(p).put("minute", resMinute);
                            Log.d("坑爹时间：", date.getHours() +";" + date.getMinutes());
                            Log.d("-----------", resTimes.get(p).get("hourOfDay") + ";" + resTimes.get(p).get("minute"));
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        timeHour = new Date().getHours();
                        timeMinute = new Date().getMinutes();
                        if (showCallBack != null){
                            showCallBack.onTimeShow(resTimes);
                        }

                    }
                    if (showCallBack != null){
                        resTimes.get(p).put("dayOfMonth", 0);
                        resTimes.get(p).put("hourOfDay", 0);
                        resTimes.get(p).put("minute", 0);
                        showCallBack.onTimeShow(resTimes);
                    }
                    System.out.println("倒计时完成");
                    Notification notification = null;
                    NotificationManager manager = null;
                    if (manager ==null){
                        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    }

                    Notification.Builder builder = new Notification.Builder(getApplicationContext()).setTicker("It is time to have medicine").setSmallIcon(android.R.drawable.stat_notify_chat);
                    if (notification == null){
                        Uri sounduri = Uri.fromFile(new File("/storage/sdcard1/musics/Do_You_Wanna_Go.mp3"));
                        notification = builder.setContentIntent(null).setContentTitle("该吃药啦~").setContentText("吃药么么哒").setVibrate(new long[]{0,1000,0,1000}).build();
                        notification.defaults = Notification.DEFAULT_SOUND;
                        manager.notify(1, notification);
                    }

                    mediaPlayer = new MediaPlayer();
                    try {
                        mediaPlayer.setDataSource("/storage/sdcard1/musics/Do_You_Wanna_Go.mp3");
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//                    Looper.prepare();
//                    mediaPlayer.stop();
//                    new AlertDialog.Builder(getBaseContext()).setTitle("闹钟").setMessage("dasfdf").setPositiveButton("close alarm",
//                            new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    mediaPlayer.stop();
//                                }
//                            }).show();

                }
            }).start();
        }

//        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        int tagetTime = 1 * 1000;
//        long triggerAtTime = SystemClock.elapsedRealtime() + tagetTime;
//        Intent i = new Intent(this, AlarmReciver.class);
//        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
//        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }


    private ShowCallBack showCallBack = null;
    public void setShowCallBack(ShowCallBack showCallBack){
        this.showCallBack = showCallBack;
    }
    public ShowCallBack getShowCallBack(){
        return showCallBack;
    }

    public interface ShowCallBack{
        void onTimeShow(ArrayList<Map<String, Integer>> resTimes);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("LongRunService is", "停止");
    }
}

package com.competition.kakin.mtreatment.UI.Notifi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import com.competition.kakin.mtreatment.AddAlarmProperty;
import com.competition.kakin.mtreatment.R;
import com.competition.kakin.mtreatment.service.AlarmService2;
import com.competition.kakin.mtreatment.tool.AlarmInfo;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by kakin on 2016/8/11.
 */
public class ClockActivity extends Activity{
    private MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource("/storage/sdcard1/musics/Do_You_Wanna_Go.mp3");
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        new AlertDialog.Builder(ClockActivity.this).setTitle("闹钟").setMessage("dasfdf").setPositiveButton("close alarm",
        new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = getIntent();
                int p = i.getIntExtra("index", 0);
                System.out.println("被移除的是" + p);
                mediaPlayer.stop();
                ClockActivity.this.finish();
                AlarmInfo alarmInfo = new AlarmInfo(ClockActivity.this);
                ArrayList<AddAlarmProperty> addAlarmProperties = alarmInfo.getAlarm_Property();
                if (addAlarmProperties.size() > 0){
                    addAlarmProperties.remove(p);
                    alarmInfo.setAlarm_Property(addAlarmProperties);
                }
            }
        }).show();
    }
}

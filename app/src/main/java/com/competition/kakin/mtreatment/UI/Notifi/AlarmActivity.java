package com.competition.kakin.mtreatment.UI.Notifi;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.competition.kakin.mtreatment.AddAlarmProperty;
import com.competition.kakin.mtreatment.R;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by kakin on 2016/8/16.
 */
public class AlarmActivity extends Activity implements AppCompatCallback, TimePicker.OnTimeChangedListener, View.OnClickListener {

    private int mychoic = 0;
    private boolean canEdit = false;
    private int position;
    private Toolbar toolbar;
    private AppCompatDelegate delegate;
    private RelativeLayout dateSet;
    private RelativeLayout eatwaySet;
    private RelativeLayout doseSet;
    private TextView tvname;
    private TextView tvyear;
    private TextView tvmonth;
    private TextView tvday;
    private TextView tvway;
    private EditText etdose;
    private TimePicker tptime;

    private AddAlarmProperty alarmProperty = new AddAlarmProperty();
    Map<String, Integer> alarmTime = new HashMap<>();

    private AddAlarmProperty addAlarmProperty;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String way = msg.getData().getString("way").toString();
            tvway.setText(way);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        delegate = AppCompatDelegate.create(this, this);
        delegate.onCreate(savedInstanceState);
        delegate.setContentView(R.layout.activity_alarm);
        Intent intent = getIntent();
        addAlarmProperty = intent.getExtras().getParcelable("AlarmProperty");
        position = intent.getIntExtra("position", 0);
        initToolbar();
        initWidget();
        init_date();
        set_alarmProperty();
    }

    private void initToolbar(){
        toolbar = (Toolbar) findViewById(R.id.alarm_toolbar);
        delegate.setSupportActionBar(toolbar);
        toolbar.setTitle("添加提醒");
        toolbar.setTitleTextColor(getResources().getColor(R.color.text_white));
        delegate.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initWidget(){
        tvname = (TextView) findViewById(R.id.alarm_name);
        tvname.setText(addAlarmProperty.getName());
        tptime = (TimePicker) findViewById(R.id.tp_alarm_time);
        tptime.setIs24HourView(true);
        tptime.setCurrentHour(addAlarmProperty.getAlarmTime().get("hourOfDay"));
        tptime.setCurrentMinute(addAlarmProperty.getAlarmTime().get("minute"));
        tptime.setOnTimeChangedListener(this);
        tvyear = (TextView) findViewById(R.id.alarm_year);
        tvmonth = (TextView) findViewById(R.id.alarm_month);
        tvday = (TextView) findViewById(R.id.alarm_day);
        tvway = (TextView) findViewById(R.id.alarm_eatway_show);
        tvway.setText(addAlarmProperty.getWay());
        dateSet = (RelativeLayout) findViewById(R.id.alarm_date);
        dateSet.setOnClickListener(this);
        eatwaySet = (RelativeLayout) findViewById(R.id.alarm_eatway);
        eatwaySet.setOnClickListener(this);
        etdose = (EditText) findViewById(R.id.alarm_dose_show);
        etdose.setText(addAlarmProperty.getDose());
        etdose.setEnabled(canEdit);
        doseSet = (RelativeLayout) findViewById(R.id.alarm_dose);
        doseSet.setOnClickListener(this);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_alarm, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.alter :
                set_alarmProperty();
                Intent intent = new Intent();
                Bundle b = new Bundle();
                b.putParcelable("alarmProperty", alarmProperty);
                intent.putExtras(b);
                intent.putExtra("position", position);
                setResult(77, intent);
                finish();
                break;
            case R.id.delete:
                Intent deldata = new Intent();
                deldata.putExtra("position", position);
                setResult(88, deldata);
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSupportActionModeStarted(ActionMode mode) {

    }

    @Override
    public void onSupportActionModeFinished(ActionMode mode) {

    }

    @Nullable
    @Override
    public ActionMode onWindowStartingSupportActionMode(ActionMode.Callback callback) {
        return null;
    }

    /***
     * 初始化日期
     */
    private void init_date(){
        tvyear.setText(addAlarmProperty.getAlarmTime().get("year") + "");
        tvmonth.setText(addAlarmProperty.getAlarmTime().get("monthOfYear") + 1 + "");
        tvday.setText(addAlarmProperty.getAlarmTime().get("dayOfMonth") + "");

    }

    /***
     * 设置属性，通过intent传回
     */
    private void set_alarmProperty(){
        alarmTime.put("year", Integer.parseInt(tvyear.getText().toString()));
        alarmTime.put("monthOfYear", Integer.parseInt(tvmonth.getText().toString()));
        alarmTime.put("dayOfMonth", Integer.parseInt(tvday.getText().toString()));
        alarmTime.put("minute", tptime.getCurrentMinute());
        alarmTime.put("hourOfDay", tptime.getCurrentHour());

        alarmProperty.setName(tvname.getText().toString());
        alarmProperty.setAlarmTime(alarmTime);
        alarmProperty.setDose(etdose.getText().toString());
        alarmProperty.setWay(tvway.getText().toString());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.alarm_date:
                new DatePickerDialog(AlarmActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        tvyear.setText(year + "");
                        tvmonth.setText(monthOfYear + 1 + "");
                        tvday.setText(dayOfMonth + "");
                    }
                }, Integer.parseInt(tvyear.getText().toString()), Integer.parseInt(tvmonth.getText().toString()) - 1, Integer.parseInt(tvday.getText().toString())).show();
                break;

            case R.id.alarm_eatway:
                AlertDialog.Builder builder = new AlertDialog.Builder(AlarmActivity.this);
                builder.setTitle("请选择方式");
                final String[] ways = {"口服", "冲服"};
                builder.setSingleChoiceItems(ways, mychoic, new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        mychoic = which;
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        Message msg = new Message();
                        Bundle b = new Bundle();
                        b.putString("way", ways[mychoic]);
                        msg.setData(b);
                        handler.sendMessage(msg);
                    }
                });
                builder.show();
                break;

            case R.id.alarm_dose:
                if (canEdit){
                    Toast.makeText(AlarmActivity.this, etdose.getText().toString(), LENGTH_SHORT).show();
                }
                canEdit = !canEdit;
                etdose.setEnabled(canEdit);
                break;
        }
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        alarmTime.put("hourOfDay", hourOfDay);
        alarmTime.put("minute", minute);
    }
}

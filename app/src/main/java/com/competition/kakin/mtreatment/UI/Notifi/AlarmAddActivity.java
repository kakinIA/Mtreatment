package com.competition.kakin.mtreatment.UI.Notifi;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
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
 * Created by kakin on 2016/8/10.
 */
public class AlarmAddActivity extends Activity implements AppCompatCallback, View.OnClickListener, TimePicker.OnTimeChangedListener {

    private int mychoic = 0;
    private boolean canEdit = false;
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
        delegate.setContentView(R.layout.activity_alarm_add);
        initToolbar();
        initWidget();
        init_date();
        set_alarmProperty();
    }

    private void initToolbar(){
        toolbar = (Toolbar) findViewById(R.id.alarm_add_toolbar);
        delegate.setSupportActionBar(toolbar);
        toolbar.setTitle("添加提醒");
        toolbar.setTitleTextColor(getResources().getColor(R.color.text_white));
        delegate.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent upIntent = NavUtils.getParentActivityIntent(AlarmAddActivity.this);
                if (NavUtils.shouldUpRecreateTask(AlarmAddActivity.this,upIntent)){
                    TaskStackBuilder.create(AlarmAddActivity.this)
                            .addNextIntentWithParentStack(upIntent)
                            .startActivities();
                }else {
                    upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    NavUtils.navigateUpTo(AlarmAddActivity.this, upIntent);
                }
            }
        });
    }

    private void initWidget(){
        tvname = (TextView) findViewById(R.id.add_alarm_name);
        tptime = (TimePicker) findViewById(R.id.tp_add_alarm_time);
        tptime.setIs24HourView(true);
        tptime.setOnTimeChangedListener(this);
        tvyear = (TextView) findViewById(R.id.add_alarm_year);
        tvmonth = (TextView) findViewById(R.id.add_alarm_month);
        tvday = (TextView) findViewById(R.id.add_alarm_day);
        tvway = (TextView) findViewById(R.id.add_alarm_eatway_show);
        dateSet = (RelativeLayout) findViewById(R.id.add_alarm_date);
        dateSet.setOnClickListener(this);
        eatwaySet = (RelativeLayout) findViewById(R.id.add_alarm_eatway);
        eatwaySet.setOnClickListener(this);
        etdose = (EditText) findViewById(R.id.add_alarm_dose_show);
        etdose.setEnabled(canEdit);
        doseSet = (RelativeLayout) findViewById(R.id.add_alarm_dose);
        doseSet.setOnClickListener(this);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_alarm, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.complete :
                set_alarmProperty();
                Intent intent = new Intent();
                Bundle b = new Bundle();
                b.putParcelable("alarmProperty", alarmProperty);
                intent.putExtras(b);
                setResult(44, intent);
                finish();
//                send_alarmPropertyBroadcast();
                return true;
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
    private void init_date(){
        Calendar currentDate = Calendar.getInstance();
        tvyear.setText(currentDate.get(Calendar.YEAR) + "");
        tvmonth.setText(currentDate.get(Calendar.MONTH) + 1 + "");
        tvday.setText(currentDate.get(Calendar.DAY_OF_MONTH) + "");

    }

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

    private void send_alarmPropertyBroadcast(){
        set_alarmProperty();
        Intent intent = new Intent();
        intent.setAction("com.competition.kakin.mtreatment.broadcast.alarmpropertybroadcast");
        Bundle b = new Bundle();
        b.putParcelable("alarmProperty", alarmProperty);
        intent.putExtras(b);
        sendBroadcast(intent);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_alarm_date:
                new DatePickerDialog(AlarmAddActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        tvyear.setText(year + "");
                        tvmonth.setText(monthOfYear + 1 + "");
                        tvday.setText(dayOfMonth + "");
                    }
                }, Integer.parseInt(tvyear.getText().toString()), Integer.parseInt(tvmonth.getText().toString()) - 1, Integer.parseInt(tvday.getText().toString())).show();
                break;

            case R.id.add_alarm_eatway:
                AlertDialog.Builder builder = new AlertDialog.Builder(AlarmAddActivity.this);
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

            case R.id.add_alarm_dose:
                if (canEdit){
                    Toast.makeText(AlarmAddActivity.this, etdose.getText().toString(), LENGTH_SHORT).show();
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

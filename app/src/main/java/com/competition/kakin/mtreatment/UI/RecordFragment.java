package com.competition.kakin.mtreatment.UI;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.SmsMessage;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.competition.kakin.mtreatment.DbForMsg.DbRecordManager;
import com.competition.kakin.mtreatment.DbForMsg.Msg;
import com.competition.kakin.mtreatment.DbForMsg.Record;
import com.competition.kakin.mtreatment.R;
import com.competition.kakin.mtreatment.tool.CatchMsg;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by kakin on 2016/8/4.
 */
public class RecordFragment extends Fragment {
    private DbRecordManager dbRecordManager;
    private ListView listView;
    private SimpleCursorAdapter adapter;
    private RecordReceiver recordReceiver;
    private RecordReceiver2 recordReceiver2;
    private IntentFilter intentFilter;
    private IntentFilter intentFilter2;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dbRecordManager = new DbRecordManager(getContext());
        recordReceiver = new RecordReceiver();
        recordReceiver2 = new RecordReceiver2();
        intentFilter = new IntentFilter();
        intentFilter2 = new IntentFilter();
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        intentFilter2.addAction(".....");
        getActivity().registerReceiver(recordReceiver, intentFilter);
        getActivity().registerReceiver(recordReceiver2, intentFilter2);
//        initRecordDb();
        View view = inflater.inflate(R.layout.fragment_record, container, false);
        initView(view);
        return view;
    }

    private void initView(View view){
        listView = (ListView) view.findViewById(R.id.recordListView);
        Cursor c = dbRecordManager.queryTheCursor();
        adapter = new SimpleCursorAdapter(getContext(), R.layout.record_cell, c, new String[]{"ill", "year", "month", "day"},
                new int[]{R.id.record_ill, R.id.record_year, R.id.record_month, R.id.record_day});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Record record = dbRecordManager.getRecordById(adapter, position);
                new AlertDialog.Builder(getContext()).setMessage(record.getIllDetail()).show();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final int p = position;
                new AlertDialog.Builder(getContext()).setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbRecordManager.delete(adapter, position);
                        dbRecordManager.refreshListView(adapter);
                    }
                }).show();
                return true;
            }
        });
    }

    public class RecordReceiver2 extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String fullMsg = intent.getStringExtra("fullMsg");
            CatchMsg catchMsg = new CatchMsg(getContext());
            Record record = catchMsg.getRecord(fullMsg);
            dbRecordManager.add(record);
            dbRecordManager.refreshListView(adapter);
        }
    }
    public class RecordReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            SmsMessage msg = null;
            String address = "";
            String fullMsg = "";
            if (bundle != null) {
                Object[] smsobj = (Object[]) bundle.get("pdus");
                for (Object obj : smsobj) {
                    msg = SmsMessage.createFromPdu((byte[]) obj);
                    address = msg.getOriginatingAddress();
                    fullMsg = msg.getMessageBody();
                }
                CatchMsg catchMsg = new CatchMsg(getContext());
                Record record = catchMsg.getRecord(fullMsg);
                dbRecordManager.add(record);
                dbRecordManager.refreshListView(adapter);
            }
        }
    }
    private void initRecordDb(){
        List<Record> records = new ArrayList<>();
        String illDetail1 = "左侧肢体麻木，能行走但有点划圈，左臂能抬、左手有一定握力但不能作精细动作。";
        int year1 = 2014;
        int month1 = 4;
        int day1 = 16;
        Record record1 = new Record(illDetail1, String.valueOf(year1), String.valueOf(month1), String.valueOf(day1));
        records.add(record1);
        String illDetail2 = "普通头痛，发烧";
        int year2 = 2015;
        int month2 = 9;
        int day2 = 6;
        Record record2 = new Record(illDetail2, String.valueOf(year2), String.valueOf(month2), String.valueOf(day2));
        records.add(record2);
        String illDetail3 = "内分泌失调";
        int year3 = 2016;
        int month3 = 8;
        int day3 = 16;
        Record record3 = new Record(illDetail3, String.valueOf(year3), String.valueOf(month3), String.valueOf(day3));
        records.add(record3);
        dbRecordManager.add(records);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(recordReceiver);
        getActivity().unregisterReceiver(recordReceiver2);
        dbRecordManager.closeDb();
    }
}

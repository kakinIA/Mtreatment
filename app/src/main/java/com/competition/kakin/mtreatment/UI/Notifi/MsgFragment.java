package com.competition.kakin.mtreatment.UI.Notifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsMessage;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.competition.kakin.mtreatment.Adapter.MsgAdapter;
import com.competition.kakin.mtreatment.DbForMsg.DbMsgManager;
import com.competition.kakin.mtreatment.DbForMsg.Msg;
import com.competition.kakin.mtreatment.MsgContent;
import com.competition.kakin.mtreatment.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by kakin on 2016/8/9.
 */
public class MsgFragment extends Fragment {
    private DbMsgManager dbMsgManager;
    private ListView listView;
    private MsgAdapter msgAdapter;
    private SimpleCursorAdapter simpleCursorAdapter;
    private List<MsgContent> msgContents = new ArrayList<>();
    private MsgCatchReceiver msgCatchReceiver;
    private IntentFilter intentFilter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dbMsgManager = new DbMsgManager(getContext());
        msgCatchReceiver = new MsgCatchReceiver();
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        getActivity().registerReceiver(msgCatchReceiver, intentFilter);
//        initMsgDb();
        View view = inflater.inflate(R.layout.fragment_msg, container, false);
        initView(view);

        return view;
    }
    private void initView(View view){
        listView = (ListView) view.findViewById(R.id.msgListView);
        Cursor c = dbMsgManager.queryTheCursor();
        simpleCursorAdapter = new SimpleCursorAdapter(getContext(), R.layout.msg_cell, c, new String[]{"doctorName",
                "info", "month", "day"}, new int[]{R.id.msglist_doctorName, R.id.msglist_detail, R.id.msglist_month, R.id.msglist_day});
//        msgAdapter = new MsgAdapter(getContext(), R.layout.msg_cell, msgContents);
        listView.setAdapter(simpleCursorAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Msg msg = dbMsgManager.getMsgById(simpleCursorAdapter, position);
                new AlertDialog.Builder(getContext()).setMessage(msg.getInfo()).show();
            }
        });
        listView.setOnItemLongClickListener(listviewLongclickListener);
    }
    private AdapterView.OnItemLongClickListener listviewLongclickListener=new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

            new AlertDialog.Builder(getContext()).setPositiveButton("删除", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dbMsgManager.delete(simpleCursorAdapter, position);
                    dbMsgManager.refreshListView(simpleCursorAdapter);
                }
            }).show();
            return true;
        }
    };

    public class MsgCatchReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            SmsMessage msg = null;
            String address = "";
            String fullMsg = "";
            if (bundle != null){
                Object[] smsobj = (Object[]) bundle.get("pdus");
                for (Object obj : smsobj){
                    msg = SmsMessage.createFromPdu((byte[]) obj);
                    address = msg.getOriginatingAddress();
                    fullMsg = msg.getMessageBody();
                }
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(msg.getTimestampMillis());
                Msg newmsg = new Msg();
                if (address.equals("13211069112")){
                    newmsg.setDoctorName("王医生");
                } else if (address.equals("15626414578")) {
                    newmsg.setDoctorName("江医生");
                } else {
                    newmsg.setDoctorName(address);
                }
                newmsg.setInfo(fullMsg);
                newmsg.setMonth(calendar.get(Calendar.MONTH) + 1);
                newmsg.setDay(calendar.get(Calendar.DAY_OF_MONTH));
                dbMsgManager.add(newmsg);
                dbMsgManager.refreshListView(simpleCursorAdapter);
            }
        }
    }
    private void initMsgDb(){
        List<Msg> msgs = new ArrayList<>();
        String doctorName1 = "王医生";
        String detail1 = "咳嗽，喉咙发炎，伴随发热";
        int month1 = 8;
        int day1 = 23;
        Msg msg1 = new Msg(doctorName1, detail1, month1, day1);
        msgs.add(msg1);
        String doctorName2 = "江医生";
        String detail2 = "持续低烧不退，伴有发疹、剧烈头痛、关节痛、痉挛症状";
        int month2 = 9;
        int day2 = 2;
        Msg msg2 = new Msg(doctorName2, detail2, month2, day2);
        msgs.add(msg2);
        String doctorName3 = "林医生";
        String detail3 = "咽部压力升高，引起鼻出血、鼻骨受损等。细菌等可能由咽鼓管进入中耳鼓室，引起化脓性中耳炎、鼻窦炎等疾病";
        int month3 = 10;
        int day3 = 3;
        Msg msg3 = new Msg(doctorName3, detail3, month3, day3);
        msgs.add(msg3);
        dbMsgManager.add(msgs);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(msgCatchReceiver);
        dbMsgManager.closeDb();
    }
}

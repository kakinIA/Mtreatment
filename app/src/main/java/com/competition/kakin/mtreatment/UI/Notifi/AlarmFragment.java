package com.competition.kakin.mtreatment.UI.Notifi;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.competition.kakin.mtreatment.Adapter.MedAlarmAdapter;
import com.competition.kakin.mtreatment.AddAlarmProperty;
import com.competition.kakin.mtreatment.MedAlarmContent;
import com.competition.kakin.mtreatment.R;
import com.competition.kakin.mtreatment.broadcast.AlarmPropertyReceiver;
import com.competition.kakin.mtreatment.service.AlarmService2;
import com.competition.kakin.mtreatment.tool.AlarmInfo;
import com.competition.kakin.mtreatment.tool.CatchMsg;
import com.competition.kakin.mtreatment.tool.CustomMethod;
import com.competition.kakin.mtreatment.tool.SeriaLizableMapList;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kakin on 2016/8/9.
 */
public class AlarmFragment extends Fragment {
    public static final int UPDATA_TIME = 1;
    public static final int UPDATA_CONTENTS = 2;
    public static final int UPDATA_CONTENTDEL = 3;
    private boolean isBind = false;
    private ListView listView;
    private ArrayList<MedAlarmContent> medAlarmContents = new ArrayList<>();
    private MedAlarmAdapter medAlarmAdapter;
    private FloatingActionsMenu floatingActionsMenu;
    private FloatingActionButton fabAdd;
    private FloatingActionButton fabHist;
    
    private CustomMethod customMethod = new CustomMethod();

//    TestReceiver testReceiver;
    TestSmsReceiver testSmsReceiver;
    private IntentFilter receiverIntentFilter;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case UPDATA_TIME://更新时间到listview
//                    ArrayList<MedAlarmContent> medAlarmContentLista = msg.getData().getParcelableArrayList("upDataContents");
//                    medAlarmContents.clear();
//                    medAlarmContents.addAll(medAlarmContentLista);
                    medAlarmAdapter.notifyDataSetChanged();
//                    SeriaLizableMapList timeMapList = (SeriaLizableMapList) msg.getData().getSerializable("resTimes");
//                    List<Map<String, Integer>> resTimes = timeMapList.getMapList();
//                    for (int i = 0; i < resTimes.size(); i++){
//                        Map<String, Integer> resTime = resTimes.get(i);
//                        medAlarmContents.get(i).setResTime(resTime);
//                        medAlarmAdapter.notifyDataSetChanged();
//                    }

                    break;
                case UPDATA_CONTENTS://剩余时间为0时删除content而更新listview
//                    ArrayList<MedAlarmContent> medAlarmContentList = msg.getData().getParcelableArrayList("upDataContents");
                    medAlarmAdapter.notifyDataSetChanged();
                    break;
                case UPDATA_CONTENTDEL://主动删除content而更新listview
//                    ArrayList<MedAlarmContent> medAlarmContentLista = msg.getData().getParcelableArrayList("medAlarmContents");
//                    medAlarmContents.clear();
//                    medAlarmContents.addAll(medAlarmContentLista);
                    medAlarmAdapter.notifyDataSetChanged();
//                    medAlarmAdapter = new MedAlarmAdapter(getContext(), R.layout.medalarm_cell, medAlarmContentLista);
//                    listView.setAdapter(medAlarmAdapter);
//                    medAlarmAdapter.notifyDataSetChanged();
                default:
                    break;
            }


        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm, container, false);
        initView(view);

//        doRegisterReceiver();
        return view;
    }

    private void initView(View view){

        listView = (ListView) view.findViewById(R.id.medAlarmListView);
        medAlarmAdapter = new MedAlarmAdapter(getContext(), R.layout.medalarm_cell, medAlarmContents);
        listView.setAdapter(medAlarmAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AddAlarmProperty alarmProperty = new AlarmInfo(getContext()).getAlarm_Property().get(position);
                Intent i = new Intent(getContext(), AlarmActivity.class);
                Bundle b = new Bundle();
                b.putParcelable("AlarmProperty", alarmProperty);
                i.putExtras(b);
                i.putExtra("position", position);
                startActivityForResult(i, 4);
            }
        });
        floatingActionsMenu = (FloatingActionsMenu) view.findViewById(R.id.fabAlarm);
        fabAdd = (FloatingActionButton) view.findViewById(R.id.btn_add_alarm);
        fabHist = (FloatingActionButton) view.findViewById(R.id.btn_alarm_history);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//添加alarm
//                startActivity(new Intent(getContext(), AlarmAddActivity.class));
                startActivityForResult(new Intent(getContext(), AlarmAddActivity.class), 2);
            }
        });
        fabHist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                String fullMsg = "姓名：李四 " + "\n" +
                        "性别：男" + "\n" +
                        "病史：" + "\n" +
                        "1，咳嗽，喉咙发炎，伴随发热。2016年7月25日" + "\n" +
                        "过敏史：无" + "\n" +
                        "本次病情：喉咙痛，无痰。2016年8月16日" + "\n"  +
                        "药物：" + "\n" +
                        "咽炎片。服用方式：口服。服用量：1次1粒。 服用周期： 1天3次。";
                i.putExtra("fullMsg", fullMsg);
                i.setAction(".....");
                getActivity().sendBroadcast(i);
            }
        });
        initAlarmContents();//初始化content
        testSmsReceiver = new TestSmsReceiver();
        receiverIntentFilter = new IntentFilter();
        receiverIntentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        getActivity().registerReceiver(testSmsReceiver, receiverIntentFilter);
    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            isBind = true;
            AlarmService2.TimeBinder timeBinder = (AlarmService2.TimeBinder) service;
//            timeBinder.setEndTime(theEndTime);
            timeBinder.getService().setShowCallBack(new AlarmService2.ShowCallBack() {
                @Override
                public void UpdataContent(ArrayList<MedAlarmContent> medAlarmContentlist) {//每过1s回调resTime（剩余时间）一次
                    System.out.println("执行了onTimeShow");
                    Message msg = new Message();
                    msg.what = UPDATA_TIME;
                    medAlarmContents.clear();
                    medAlarmContents.addAll(medAlarmContentlist);
                    System.out.println("此时medAlarmContents的长度：" + medAlarmContents.size());
                    handler.sendMessage(msg);
                }

                @Override
                public void UpdataEndContent(final ArrayList<MedAlarmContent> medAlarmContentList, int p) {//每过1s回调一次更新content
                    System.out.println("执行了setEndContent");
                    final int position = p;
                    new Thread(){
                        @Override
                        public void run() {
                            AlarmInfo alarmInfo = new AlarmInfo(getContext());
                            ArrayList<MedAlarmContent> medAlarmContentlist = new ArrayList<MedAlarmContent>();
                            medAlarmContentlist.addAll(medAlarmContentList);
                            Intent i = new Intent(getContext(), AlarmService2.class);
                            //解出绑定和停止服务，再重新开过
                            getContext().unbindService(conn);
                            getContext().stopService(i);
                            try {
                                sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            ArrayList<AddAlarmProperty> addAlarmProperties = alarmInfo.getAlarm_Property();
                            if (addAlarmProperties.size() > 0){
                                addAlarmProperties.remove(position);
                                alarmInfo.setAlarm_Property(addAlarmProperties);
                            }
                            ArrayList<Boolean> setAlarms = alarmInfo.getIsSetAlarms();
                            setAlarms.remove(position);
                            alarmInfo.setIsSetAlarms(setAlarms);
                            medAlarmContentlist.remove(position);
                            Message msg = new Message();
                            msg.what = UPDATA_CONTENTS;
                            medAlarmContents.clear();
                            medAlarmContents.addAll(medAlarmContentlist);
                            handler.sendMessage(msg);
                            Bundle bundle = new Bundle();
                            bundle.putParcelableArrayList("medAlarmContents", (ArrayList<? extends Parcelable>) medAlarmContents);
                            i.putExtras(bundle);
                            getContext().startService(i);
                            getContext().bindService(i, conn, Context.BIND_AUTO_CREATE);
                        }
                    }.start();

                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    /***
     * 初始化listview的content
     */
    private void initAlarmContents(){
        AlarmInfo alarmInfo = new AlarmInfo(getContext());
        ArrayList<AddAlarmProperty> addAlarmProperties = alarmInfo.getAlarm_Property();
        if (addAlarmProperties != null){
            for (int i = 0; i < addAlarmProperties.size(); i++){
                Map<String, Integer> resTime = new HashMap<>();
                resTime = customMethod.getResTime(addAlarmProperties.get(i).getAlarmTime());
                MedAlarmContent medAlarmContent = new MedAlarmContent(addAlarmProperties.get(i).getName(),
                        addAlarmProperties.get(i).getAlarmTime(), addAlarmProperties.get(i).getDose(),
                        addAlarmProperties.get(i).getWay(), resTime);
                medAlarmContents.add(medAlarmContent);
            }
            System.out.println("service是否在运行："+ customMethod.isServiceWork(getContext(), "com.competition.kakin.mtreatment.service.AlarmService2")
                                + "\n" + "contents的大小是：" + medAlarmContents.size());
            System.out.println("contents的大小是：" + medAlarmContents.size());
            Log.d("contents的大小是：", medAlarmContents.size()+"");
            if (alarmInfo.getIsSetAlarms() != null) {
                if (customMethod.isServiceWork(getContext(), "com.competition.kakin.mtreatment.service.AlarmService2")) {
                    Intent i = new Intent(getContext(), AlarmService2.class);
//                    getActivity().unbindService(conn);
                    getActivity().stopService(i);
                    ArrayList<Boolean> setAlarms = alarmInfo.getIsSetAlarms();
                    for (int p = 0; p < setAlarms.size(); p++) {
                        Boolean setAlarm = setAlarms.get(p);
                        setAlarm = false;
                        setAlarms.set(p, setAlarm);
                    }
                    Bundle b = new Bundle();
                    b.putParcelableArrayList("medAlarmContents", medAlarmContents);
                    i.putExtras(b);
                    getActivity().startService(i);
                    getActivity().bindService(i, conn, Context.BIND_AUTO_CREATE);
                    for (int p = 0; p < setAlarms.size(); p++) {
                        Boolean setAlarm = setAlarms.get(p);
                        setAlarm = true;
                        setAlarms.set(p, setAlarm);
                    }
                } else {
                    if (medAlarmContents != null) {
                        Intent i = new Intent(getContext(), AlarmService2.class);
                        Bundle b = new Bundle();
                        b.putParcelableArrayList("medAlarmContents", medAlarmContents);
                        i.putExtras(b);
                        if (i != null) {
                            getActivity().startService(i);
                            getActivity().bindService(i, conn, Context.BIND_AUTO_CREATE);
                            isBind = true;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("The Activity", "execute onDestroy");
        System.out.println("isBind现在是:" + isBind);
        if (isBind){
            getActivity().unbindService(conn);
        }
        getActivity().unregisterReceiver(testSmsReceiver);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 2 :
                switch (resultCode){
                    case 44 :
                        dealNewAlarm(data);
                        break;
                    default :
                        break;
                }
                break;
            case 4 :
                switch (resultCode){
                    case 77 :
                        alterAlarm(data);
                        break;
                    case 88 :
                        delAlarm(data);
                    default:
                        break;
                }
                default:
                    break;
        }
    }

    /***
     * 添加新alarm回到该activity的处理
     * @param intent
     */
    public void dealNewAlarm(Intent intent){
        AddAlarmProperty alarmProperty = null;
        Bundle b = intent.getExtras();
        AddAlarmProperty theAddAlarmProperty = b.getParcelable("alarmProperty");
        alarmProperty = theAddAlarmProperty;
        if (alarmProperty != null && customMethod.getTimebetWeen(alarmProperty.getAlarmTime()) > 0) {

            AlarmInfo alarmInfo = new AlarmInfo(getContext());
            Boolean setAlarm = true;
            if (alarmInfo.getAlarm_Property() != null){
                ArrayList<AddAlarmProperty> addAlarmProperties = alarmInfo.getAlarm_Property();
                ArrayList<Boolean> setAlarms = alarmInfo.getIsSetAlarms();
                addAlarmProperties.add(theAddAlarmProperty);
                setAlarms.add(setAlarm);
                alarmInfo.setAlarm_Property(addAlarmProperties);
                alarmInfo.setIsSetAlarms(setAlarms);
                ArrayList<Boolean> setAlarmlist = alarmInfo.getIsSetAlarms();
                System.out.println("setLalrmList是空的吗？？" + setAlarmlist.size());
            }else {
                ArrayList<AddAlarmProperty> addAlarmProperties = new ArrayList<>();
                ArrayList<Boolean> setAlarms = new ArrayList<>();
                addAlarmProperties.add(theAddAlarmProperty);
                setAlarms.add(setAlarm);
                alarmInfo.setAlarm_Property(addAlarmProperties);
                alarmInfo.setIsSetAlarms(setAlarms);
                alarmInfo.setAlarm_Property(addAlarmProperties);
                ArrayList<Boolean> setAlarmlist = new AlarmInfo(getContext()).getIsSetAlarms();
                System.out.println("setLalrmList是空的吗？？" + setAlarmlist.size());
            }
            Map<String, Integer> resTime = new HashMap<>();
            resTime = customMethod.getResTime(alarmProperty.getAlarmTime());
            //将alarm属性转成content
            MedAlarmContent medAlarmContent = new MedAlarmContent(alarmProperty.getName(), alarmProperty.getAlarmTime(),
                    alarmProperty.getDose(), alarmProperty.getWay(), resTime);
            medAlarmContents.add(medAlarmContent);
            medAlarmAdapter.notifyDataSetChanged();
            System.out.println(alarmProperty.getName() + alarmProperty.getAlarmTime() +
                    alarmProperty.getDose() + alarmProperty.getWay());
            System.out.println("服务是否已经开启：" + new CustomMethod().isServiceWork(getContext(), "com.competition.kakin.mtreatment.service.AlarmService2"));
            if (customMethod.isServiceWork(getContext(), "com.competition.kakin.mtreatment.service.AlarmService2")) {
                Intent i = new Intent(getContext(), AlarmService2.class);
                //解出绑定和停止服务，再重新开过
                getContext().unbindService(conn);
                getContext().stopService(i);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("medAlarmContents", (ArrayList<? extends Parcelable>) medAlarmContents);
                i.putExtras(bundle);
                getContext().startService(i);
                getContext().bindService(i, conn, Context.BIND_AUTO_CREATE);
            }else {
                Intent i = new Intent(getContext(), AlarmService2.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("medAlarmContents", (ArrayList<? extends Parcelable>) medAlarmContents);
                i.putExtras(bundle);
                getContext().startService(i);
                getContext().bindService(i, conn, Context.BIND_AUTO_CREATE);
            }
        }
    }

    /***
     *修改alarm回到该activity时的处理
     * @param intent
     */
    private void alterAlarm(Intent intent){
        AddAlarmProperty alarmProperty = intent.getExtras().getParcelable("alarmProperty");
        int position = intent.getIntExtra("position", 0);
        if (alarmProperty != null && customMethod.getTimebetWeen(alarmProperty.getAlarmTime()) > 0){
            AlarmInfo alarmInfo = new AlarmInfo(getContext());
            ArrayList<AddAlarmProperty> addAlarmProperties = alarmInfo.getAlarm_Property();
            addAlarmProperties.set(position, alarmProperty);
            alarmInfo.setAlarm_Property(addAlarmProperties);
        }
        Map<String, Integer> resTime = new HashMap<>();
        resTime = customMethod.getResTime(alarmProperty.getAlarmTime());
        MedAlarmContent medAlarmContent = new MedAlarmContent(alarmProperty.getName(), alarmProperty.getAlarmTime(),
                alarmProperty.getDose(), alarmProperty.getWay(), resTime);
        medAlarmContents.set(position, medAlarmContent);
        medAlarmAdapter.notifyDataSetChanged();
        if (customMethod.isServiceWork(getContext(), "com.competition.kakin.mtreatment.service.AlarmService2")) {
            Intent i = new Intent(getContext(), AlarmService2.class);
            getContext().unbindService(conn);
            getContext().stopService(i);
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("medAlarmContents", (ArrayList<? extends Parcelable>) medAlarmContents);
            i.putExtras(bundle);
            getContext().startService(i);
            getContext().bindService(i, conn, Context.BIND_AUTO_CREATE);
        }else {
            Intent i = new Intent(getContext(), AlarmService2.class);
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("medAlarmContents", (ArrayList<? extends Parcelable>) medAlarmContents);
            i.putExtras(bundle);
            getContext().startService(i);
            getContext().bindService(i, conn, Context.BIND_AUTO_CREATE);
        }
    }

    /***
     * 删除alarm的处理
     * @param intent
     */
    private void delAlarm(Intent intent){
        final int position = intent.getIntExtra("position", 0);
        final AlarmInfo alarmInfo = new AlarmInfo(getContext());
        ArrayList<AddAlarmProperty> addAlarmProperties = alarmInfo.getAlarm_Property();
        ArrayList<Boolean> setAlarms = alarmInfo.getIsSetAlarms();
        if (customMethod.isServiceWork(getContext(), "com.competition.kakin.mtreatment.service.AlarmService2")) {
            new Thread(){
                @Override
                public void run() {
                    ArrayList<AddAlarmProperty> addAlarmProperties = alarmInfo.getAlarm_Property();
                    ArrayList<Boolean> setAlarms = alarmInfo.getIsSetAlarms();
                    Intent i = new Intent(getContext(), AlarmService2.class);
                    getContext().unbindService(conn);
                    getContext().stopService(i);
                    //我在刷新content时，用了不立刻在原来的线程上删除，而是重新又建立线程，当运行都一定的时候原来线程自己结束，
                    //所以不用延迟2s，但是在listview的adapter加载时可能会出错，报index溢出错误。是medAlarmAdapter.没能及时刷新的原因？？
//                    try {
//                        sleep(2000);//关了服务线程还没有立刻关所以会出错~最后的方法是判定线程确实关掉才重新开一个
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                    addAlarmProperties.remove(position);
                    setAlarms.remove(position);
                    alarmInfo.setIsSetAlarms(setAlarms);
                    alarmInfo.setAlarm_Property(addAlarmProperties);
//                    ArrayList<MedAlarmContent> medAlarmContentArrayList = medAlarmContents;
//                    medAlarmContentArrayList.remove(position);
                    medAlarmContents.remove(position);
                    Message msg = new Message();
                    msg.what = UPDATA_CONTENTDEL;
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("medAlarmContents", medAlarmContents);
//                    msg.setData(bundle);
                    handler.sendMessage(msg);
                    i.putExtras(bundle);
                    getContext().startService(i);
                    getContext().bindService(i, conn, Context.BIND_AUTO_CREATE);
                }
            }.start();
        }else {//没有这种情况，忽视..
            addAlarmProperties = alarmInfo.getAlarm_Property();
            setAlarms = alarmInfo.getIsSetAlarms();
            addAlarmProperties.remove(position);
            setAlarms.remove(position);
            medAlarmContents.remove(position);
            medAlarmAdapter.notifyDataSetChanged();
            alarmInfo.setIsSetAlarms(setAlarms);
            alarmInfo.setAlarm_Property(addAlarmProperties);
            if (medAlarmContents.size() > 0){
                Intent i = new Intent(getContext(), AlarmService2.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("medAlarmContents", (ArrayList<? extends Parcelable>) medAlarmContents);
                i.putExtras(bundle);
                getContext().startService(i);
                getContext().bindService(i, conn, Context.BIND_AUTO_CREATE);
            }
        }
    }
    public class TestSmsReceiver extends BroadcastReceiver {
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
                CatchMsg catchMsg = new CatchMsg(getContext());
                final AddAlarmProperty alarmProperty = catchMsg.getAlarm(fullMsg);
                new AlertDialog.Builder(getContext()).setTitle("检测到有医生提醒吃药").setMessage("是否添加吃药提醒？").setPositiveButton("添加",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                System.out.println("有反应吗？" + which);
                                dealNewAlarmFromMsg(alarmProperty);
                            }
                        }).show();
            }
        }
    }
    public class TestReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String theContent = intent.getStringExtra("testContent");
            int forwardMed = theContent.indexOf("用");
            int behindMed = theContent.indexOf("M");
            final String medName = theContent.substring(forwardMed + 1, behindMed);
            int startWay = theContent.indexOf("法") + 2;
            final String medWay = theContent.substring(startWay, startWay + 3);
            int startDose = theContent.indexOf("量") + 2;
            final String medDose = theContent.substring(startDose, startDose + 3);
            System.out.println(medName + "\n" + medWay + "\n" + medDose);
            Calendar currentDate = Calendar.getInstance();
            Map<String, Integer> alarmTime = new HashMap<String, Integer>();
            alarmTime.put("year", currentDate.get(Calendar.YEAR));
            alarmTime.put("monthOfYear", currentDate.get(Calendar.MONTH) + 1);
            alarmTime.put("dayOfMonth", currentDate.get(Calendar.DAY_OF_MONTH));
            alarmTime.put("hourOfDay", 19);
            alarmTime.put("minute", 30);
            final AddAlarmProperty alarmProperty = new AddAlarmProperty(medName, alarmTime, medWay, medDose);
            new AlertDialog.Builder(getContext()).setTitle("检测到有医生提醒吃药").setMessage("是否添加吃药提醒？").setPositiveButton("添加",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.out.println("有反应吗？" + which);
                            dealNewAlarmFromMsg(alarmProperty);
                        }
                    }).show();
        }
    }
    public void dealNewAlarmFromMsg(AddAlarmProperty alarmProperty){

        if (alarmProperty != null && customMethod.getTimebetWeen(alarmProperty.getAlarmTime()) > 0) {
            AlarmInfo alarmInfo = new AlarmInfo(getContext());
            Boolean setAlarm = true;
            if (alarmInfo.getAlarm_Property() != null){
                ArrayList<AddAlarmProperty> addAlarmProperties = alarmInfo.getAlarm_Property();
                ArrayList<Boolean> setAlarms = alarmInfo.getIsSetAlarms();
                addAlarmProperties.add(alarmProperty);
                setAlarms.add(setAlarm);
                alarmInfo.setAlarm_Property(addAlarmProperties);
                alarmInfo.setIsSetAlarms(setAlarms);
                ArrayList<Boolean> setAlarmlist = alarmInfo.getIsSetAlarms();
                System.out.println("setLalrmList是空的吗？？" + setAlarmlist.size());
            }else {
                ArrayList<AddAlarmProperty> addAlarmProperties = new ArrayList<>();
                ArrayList<Boolean> setAlarms = new ArrayList<>();
                addAlarmProperties.add(alarmProperty);
                setAlarms.add(setAlarm);
                alarmInfo.setAlarm_Property(addAlarmProperties);
                alarmInfo.setIsSetAlarms(setAlarms);
                alarmInfo.setAlarm_Property(addAlarmProperties);
                ArrayList<Boolean> setAlarmlist = new AlarmInfo(getContext()).getIsSetAlarms();
                System.out.println("setLalrmList是空的吗？？" + setAlarmlist.size());
            }
            Map<String, Integer> resTime = new HashMap<>();
            resTime = customMethod.getResTime(alarmProperty.getAlarmTime());
            //将alarm属性转成content
            MedAlarmContent medAlarmContent = new MedAlarmContent(alarmProperty.getName(), alarmProperty.getAlarmTime(),
                    alarmProperty.getDose(), alarmProperty.getWay(), resTime);
            medAlarmContents.add(medAlarmContent);
            medAlarmAdapter.notifyDataSetChanged();
            System.out.println(alarmProperty.getName() + alarmProperty.getAlarmTime() +
                    alarmProperty.getDose() + alarmProperty.getWay());
            System.out.println("服务是否已经开启：" + new CustomMethod().isServiceWork(getContext(), "com.competition.kakin.mtreatment.service.AlarmService2"));
            if (customMethod.isServiceWork(getContext(), "com.competition.kakin.mtreatment.service.AlarmService2")) {
                Intent i = new Intent(getContext(), AlarmService2.class);
                //解出绑定和停止服务，再重新开过
                getContext().unbindService(conn);
                getContext().stopService(i);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("medAlarmContents", (ArrayList<? extends Parcelable>) medAlarmContents);
                i.putExtras(bundle);
                getContext().startService(i);
                getContext().bindService(i, conn, Context.BIND_AUTO_CREATE);
            }else {
                Intent i = new Intent(getContext(), AlarmService2.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("medAlarmContents", (ArrayList<? extends Parcelable>) medAlarmContents);
                i.putExtras(bundle);
                getContext().startService(i);
                getContext().bindService(i, conn, Context.BIND_AUTO_CREATE);
            }
        }
    }
}

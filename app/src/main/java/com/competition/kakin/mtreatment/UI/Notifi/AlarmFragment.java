package com.competition.kakin.mtreatment.UI.Notifi;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.competition.kakin.mtreatment.tool.CustomMethod;
import com.competition.kakin.mtreatment.tool.SeriaLizableMapList;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;
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

    private boolean isRegister = false;
    private Map<String, Integer> theEndTime = new HashMap<>();
    private ListView listView;
    private ArrayList<MedAlarmContent> medAlarmContents = new ArrayList<>();
    private MedAlarmAdapter medAlarmAdapter;
    private FloatingActionsMenu floatingActionsMenu;
    private FloatingActionButton fabAdd;
    private FloatingActionButton fabHist;
    
    private CustomMethod customMethod = new CustomMethod();

    private AlarmPropertyReceiver alarmPropertyReceiver;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case UPDATA_TIME:
                    SeriaLizableMapList timeMapList = (SeriaLizableMapList) msg.getData().getSerializable("resTimes");
                    List<Map<String, Integer>> resTimes = timeMapList.getMapList();
                    for (int i = 0; i < resTimes.size(); i++){
                        Map<String, Integer> resTime = resTimes.get(i);
                        medAlarmContents.get(i).setResTime(resTime);
                        medAlarmAdapter.notifyDataSetChanged();
                    }
                    break;
                case UPDATA_CONTENTS:
                    ArrayList<MedAlarmContent> medAlarmContentList = msg.getData().getParcelableArrayList("upDataContents");
                    medAlarmContents = medAlarmContentList;
                    medAlarmAdapter = new MedAlarmAdapter(getContext(), R.layout.medalarm_cell, medAlarmContents);
                    listView.setAdapter(medAlarmAdapter);
                    medAlarmAdapter.notifyDataSetChanged();
                    break;
                case UPDATA_CONTENTDEL:
                    ArrayList<MedAlarmContent> medAlarmContentLista = msg.getData().getParcelableArrayList("medAlarmContents");
                    medAlarmAdapter = new MedAlarmAdapter(getContext(), R.layout.medalarm_cell, medAlarmContentLista);
                    listView.setAdapter(medAlarmAdapter);
                    medAlarmAdapter.notifyDataSetChanged();
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
            public void onClick(View v) {
//                startActivity(new Intent(getContext(), AlarmAddActivity.class));
                startActivityForResult(new Intent(getContext(), AlarmAddActivity.class), 2);
            }
        });
        fabHist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                isRegister = false;
//                getActivity().unbindService(conn);
//                isBind = false;
//                getActivity().stopService(new Intent(getContext(), AlarmService.class));
//                isServiceStart = false;
            }
        });
                theEndTime.put("hour", 17);
                theEndTime.put("minute", 35);
//                if(medAlarmContents != null){
//                    Intent i = new Intent(getContext(), AlarmService2.class);
//                    Bundle b = new Bundle();
//                    b.putParcelableArrayList("medAlarmContents", medAlarmContents);
//                    i.putExtras(b);
//                    if (!isServiceStart){
//                        if (i != null){
//                            getActivity().startService(i);
//                            isServiceStart = true;
//                            getActivity().bindService(i, conn, Context.BIND_AUTO_CREATE);
//                            isBind = true;
//                        }
//                    }
//                }
        initAlarmContents();
    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            isBind = true;
            AlarmService2.TimeBinder timeBinder = (AlarmService2.TimeBinder) service;
//            timeBinder.setEndTime(theEndTime);
            timeBinder.getService().setShowCallBack(new AlarmService2.ShowCallBack() {
                @Override
                public void onTimeShow(ArrayList<Map<String, Integer>> resTimes) {
                    Message msg = new Message();
                    msg.what = UPDATA_TIME;
                    Bundle b = new Bundle();
                    SeriaLizableMapList timeMapList = new SeriaLizableMapList();
                    timeMapList.setMapList(resTimes);
                    b.putSerializable("resTimes", timeMapList);
//                    SerializableMap timeMap = new SerializableMap();
//                    timeMap.setMap(resTime);
//                    b.putSerializable("TimeMap", timeMap);
                    msg.setData(b);
                    handler.sendMessage(msg);
                }

                @Override
                public void setEndContent(ArrayList<MedAlarmContent> medAlarmContentlist) {
                    System.out.println("执行了setEndContent");
                    Message msg = new Message();
                    msg.what = UPDATA_CONTENTS;
                    Bundle b =new Bundle();
                    b.putParcelableArrayList("upDataContents", medAlarmContentlist);
                    msg.setData(b);
                    handler.sendMessage(msg);

                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private void doRegisterReceiver(){
        AddAlarmProperty alarmProperty = null;
        alarmPropertyReceiver = new AlarmPropertyReceiver(getContext(), alarmProperty, medAlarmContents, medAlarmAdapter, listView, conn);
        IntentFilter filter = new IntentFilter("com.competition.kakin.mtreatment.broadcast.alarmpropertybroadcast");
        getActivity().registerReceiver(alarmPropertyReceiver, filter);

    }
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
//                Intent i = new Intent(getContext(), AlarmService2.class);
//                Bundle b = new Bundle();
//                b.putParcelableArrayList("medAlarmContents", medAlarmContents);
//                i.putExtras(b);
//                getActivity().startActivity(i);
//                getActivity().bindService(i, conn, Context.BIND_AUTO_CREATE);
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
//        send_alarmPropertyBroadcast();
//        Map<String, Integer> alarmTime = new HashMap<>();
//        alarmTime.put("minute", 34);
//        alarmTime.put("hourOfDay", 15);
//        alarmTime.put("dayOfMonth", 14);
//        alarmTime.put("monthOfYear", 8);
//        alarmTime.put("year", 2016);
//        Map<String, Integer> resTime = new HashMap<>();
//        resTime.put("dayOfMonth", 99);
//        resTime.put("hourOfDay", 19);
//        resTime.put("minute", 54);
//        MedAlarmContent medAlarmContent = new MedAlarmContent("可达宁", alarmTime, "2片", "口服", resTime);
//        medAlarmContents.add(medAlarmContent);
//
//        Map<String, Integer> alarmTime2 = new HashMap<>();
//        alarmTime2.put("minute", 35);
//        alarmTime2.put("hourOfDay", 15);
//        alarmTime2.put("dayOfMonth", 18);
//        alarmTime2.put("monthOfYear", 8);
//        alarmTime2.put("year", 2016);
//        Map<String, Integer> resTime2 = new HashMap<>();
//        resTime2.put("dayOfMonth", 99);
//        resTime2.put("hourOfDay", 19);
//        resTime2.put("minute", 52);
//        MedAlarmContent medAlarmContent2 = new MedAlarmContent("日益百服宁", alarmTime2, "1片", "口服", resTime2);
//        medAlarmContents.add(medAlarmContent2);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("The Activity", "execute onDestroy");
        System.out.println("isBind现在是:" + isBind);
        if (isBind){
            getActivity().unbindService(conn);
        }
//        if (isServiceStart){
//            getActivity().stopService(new Intent(getContext(), AlarmService2.class));
//        }
//        getActivity().unregisterReceiver(alarmPropertyReceiver);
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
                System.out.println("添加第二次之前" + setAlarms.size());
                addAlarmProperties.add(theAddAlarmProperty);
                setAlarms.add(setAlarm);
                alarmInfo.setAlarm_Property(addAlarmProperties);
                alarmInfo.setIsSetAlarms(setAlarms);
                ArrayList<Boolean> setAlarmlist = alarmInfo.getIsSetAlarms();
                System.out.println("setLalrmList是空的吗？？" + setAlarmlist.size());
            }else {
                ArrayList<AddAlarmProperty> addAlarmProperties = new ArrayList<>();
                ArrayList<Boolean> setAlarms = new ArrayList<>();
                System.out.println("添加第二次之前" + setAlarms.size());
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
            MedAlarmContent medAlarmContent = new MedAlarmContent(alarmProperty.getName(), alarmProperty.getAlarmTime(),
                    alarmProperty.getDose(), alarmProperty.getWay(), resTime);
            medAlarmContents.add(medAlarmContent);
            medAlarmAdapter.notifyDataSetChanged();
            System.out.println(alarmProperty.getName() + alarmProperty.getAlarmTime() +
                    alarmProperty.getDose() + alarmProperty.getWay());
            System.out.println("服务是否已经开启：" + new CustomMethod().isServiceWork(getContext(), "com.competition.kakin.mtreatment.service.AlarmService2"));
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
    }
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
                    try {
                        sleep(3000);//只能延迟3秒以上才不会出错啊，哎，关了服务线程还没有立刻关所以会出错~
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    addAlarmProperties.remove(position);
                    setAlarms.remove(position);
                    alarmInfo.setIsSetAlarms(setAlarms);
                    alarmInfo.setAlarm_Property(addAlarmProperties);
                    ArrayList<MedAlarmContent> medAlarmContentArrayList = medAlarmContents;
                    medAlarmContentArrayList.remove(position);
                    Message msg = new Message();
                    msg.what = UPDATA_CONTENTDEL;
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("medAlarmContents", medAlarmContentArrayList);
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                    i.putExtras(bundle);
                    getContext().startService(i);
                    getContext().bindService(i, conn, Context.BIND_AUTO_CREATE);
                }
            }.start();
        }else {
            addAlarmProperties = alarmInfo.getAlarm_Property();
            setAlarms = alarmInfo.getIsSetAlarms();
            addAlarmProperties.remove(position);
            setAlarms.remove(position);
            medAlarmContents.remove(position);
            medAlarmAdapter.notifyDataSetChanged();
            alarmInfo.setIsSetAlarms(setAlarms);
            alarmInfo.setAlarm_Property(addAlarmProperties);
            Intent i = new Intent(getContext(), AlarmService2.class);
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("medAlarmContents", (ArrayList<? extends Parcelable>) medAlarmContents);
            i.putExtras(bundle);
            getContext().startService(i);
            getContext().bindService(i, conn, Context.BIND_AUTO_CREATE);
        }
    }
}
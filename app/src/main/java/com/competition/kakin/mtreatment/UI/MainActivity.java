package com.competition.kakin.mtreatment.UI;

import android.content.res.Configuration;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.competition.kakin.mtreatment.R;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    CoordinatorLayout coordinatorLayout;
    NavigationView navigationView;
    Fragment fragment;
    RecordFragment recordFragment = new RecordFragment();
    DoctorsFragment doctorsFragment = new DoctorsFragment();
    PlanFragment planFragment = new PlanFragment();
    NotifiFragment notifiFragment = new NotifiFragment();
    SettingFragment settingFragment = new SettingFragment();

//    private IntentFilter receiverIntentFilter;
//    private MsgReceiver msgReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init_toolbar();
        init_page();
        init_instances();

//        receiverIntentFilter = new IntentFilter();
//        receiverIntentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
//        msgReceiver = new MsgReceiver();
//        registerReceiver(msgReceiver, receiverIntentFilter);
    }

//    public class MsgReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            Bundle bundle = intent.getExtras();
//            System.out.println("开始1");
////            SmsMessage msg = null;
////            if (bundle != null){
////                System.out.println("开始2");
////                Object[] smsobj = (Object[]) bundle.get("pdus");
////                for (Object obj : smsobj){
////                    msg = SmsMessage.createFromPdu((byte[]) obj);
////                    System.out.println("时间："+ msg.getTimestampMillis() + "\n" +
////                            "内容:" + msg.getMessageBody() + "\n" +
////                            "地址：" + msg.getDisplayOriginatingAddress());
////                }
////            }
//            Object[] pdus = (Object[]) bundle.get("pdus");
//            SmsMessage[] msgs = new SmsMessage[pdus.length];
//            for (int i = 0; i < msgs.length; i++ ){
//                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
//            }
//            String address = msgs[0].getOriginatingAddress();
//            String fullMsg = "";
//            for (SmsMessage msg : msgs){
//                fullMsg += msg.getMessageBody();
//            }
//            System.out.println("address:" + address + "\n" + "msg:" + fullMsg);
//        }
//    }
    private void init_toolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("健康档案");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void init_instances(){
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, toolbar, R.string.openDrawercontentDescRes, R.string.closeDrawercontentDescRes);
        actionBarDrawerToggle.syncState();
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
//        getSupportActionBar().setHomeButtonEnabled(true);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.rootLayout);
        navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void init_page(){
        fragment = new RecordFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.theContainer, fragment).commit();
        switchContent(fragment, recordFragment);
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.navRecord :
                toolbar.setTitle("健康档案");
                switchContent(fragment, recordFragment);
                System.out.println("健康档案");
                break;
            case R.id.navDoctors :
                toolbar.setTitle("我的医生");
                switchContent(fragment, doctorsFragment);
                System.out.println("我的医生");
                break;
            case R.id.navPlan :
                toolbar.setTitle("健康计划");
                switchContent(fragment, planFragment);
                break;
            case R.id.navNotifi :
                toolbar.setTitle("消息与提醒");
                switchContent(fragment, notifiFragment);
                break;
            case R.id.navSetting :
                toolbar.setTitle("设置");
                switchContent(fragment, settingFragment);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
//        super.onPostCreate(savedInstanceState, persistentState);
//        actionBarDrawerToggle.syncState();
//    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.about) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    private void switchContent(Fragment from ,Fragment to){

        if (fragment.getClass() != to.getClass()) {
            fragment = to;
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            if (!to.isAdded()) {
                ft.hide(from).add(R.id.theContainer, to).commit();
            }else {
                ft.hide(from).show(to).commit();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}

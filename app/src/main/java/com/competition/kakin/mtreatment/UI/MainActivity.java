package com.competition.kakin.mtreatment.UI;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.competition.kakin.mtreatment.UI.Notifi.AlarmAddActivity;


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


//    private MsgReceiver msgReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init_toolbar();
        init_page();
        init_instances();

    }


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

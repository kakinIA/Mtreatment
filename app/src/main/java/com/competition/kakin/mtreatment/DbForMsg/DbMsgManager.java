package com.competition.kakin.mtreatment.DbForMsg;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kakin on 2016/8/23.
 */
public class DbMsgManager {
    private DbHelper dbHelper;
    private SQLiteDatabase db;
    public DbMsgManager(Context context){
        dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
    }
    public void add(List<Msg> msgs){
        db.beginTransaction();
        try{
            for (Msg msg : msgs){
                db.execSQL("INSERT INTO msg VALUES(null, ?, ?, ?, ?)", new Object[]{msg.getDoctorName(), msg.getInfo(), msg.getMonth(), msg.getDay()});
            }
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
        }
    }
    public void add(Msg msg){
        ContentValues cv = new ContentValues();
        cv.put("doctorName", msg.getDoctorName());
        cv.put("info", msg.getInfo());
        cv.put("month", msg.getMonth());
        cv.put("day", msg.getDay());
        db.insert("msg", null, cv);
    }
    public void delete(SimpleCursorAdapter adapter, int position){
        Cursor c = adapter.getCursor();
        c.moveToPosition(position);
        int itemId = c.getInt(c.getColumnIndex("_id"));
        db.delete("msg", "_id = ?", new String[]{itemId + ""});
        c.close();
    }
    public Msg getMsgById (SimpleCursorAdapter adapter, int position){
        Msg msg = new Msg();
        Cursor c = adapter.getCursor();
        c.moveToPosition(position);
        msg.setDoctorName(c.getString(c.getColumnIndex("doctorName")));
        msg.setInfo(c.getString(c.getColumnIndex("info")));
        msg.setMonth(c.getInt(c.getColumnIndex("month")));
        msg.setDay(c.getInt(c.getColumnIndex("day")));
        return msg;
    }
    public List<Msg> query(){
        ArrayList<Msg> msgs = new ArrayList<>();
        Cursor c = queryTheCursor();
        while (c.moveToNext()){
            Msg msg = new Msg();
            msg.setDoctorName(c.getString(c.getColumnIndex("doctorName")));
            msg.setInfo(c.getString(c.getColumnIndex("info")));
            msg.setMonth(c.getInt(c.getColumnIndex("month")));
            msg.setDay(c.getInt(c.getColumnIndex("day")));
            msgs.add(msg);
        }
        c.close();
        return msgs;
    }
    public Cursor queryTheCursor() {
        Cursor c = db.rawQuery("SELECT * FROM msg", null);
        return c;
    }
    public void refreshListView(SimpleCursorAdapter adapter){
        Cursor c=db.query("msg",null,null,null,null,null,null);
        adapter.changeCursor(c);
    }
    public void closeDb(){
        db.close();
    }
}

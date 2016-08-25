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
public class DbRecordManager  {
    private DbHelper dbHelper;
    private SQLiteDatabase db;
    public DbRecordManager(Context context){
        dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
    }
    public void add(List<Record> records){
        db.beginTransaction();
        try{
            for (Record record : records){
                db.execSQL("INSERT INTO record VALUES(null, ?, ?, ?, ?)", new Object[]{record.getIllDetail(), record.getYear(), record.getMonth(), record.getDay()});
            }
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
        }
    }
    public void add(Record record){
        ContentValues cv = new ContentValues();
        cv.put("ill", record.getIllDetail());
        cv.put("year", record.getYear());
        cv.put("month", record.getMonth());
        cv.put("day", record.getDay());
        db.insert("record", null, cv);
    }
    public void delete(SimpleCursorAdapter adapter, int position){
        Cursor c= adapter.getCursor();
        c.moveToPosition(position);
        int itemId = c.getInt(c.getColumnIndex("_id"));
        db.delete("record", "_id = ?", new String[]{itemId + ""});
        c.close();
    }
    public List<Record> query(){
        ArrayList<Record> records = new ArrayList<>();
        Cursor c = queryTheCursor();
        while (c.moveToNext()){
            Record record = new Record();
            record.setIllDetail(c.getString(c.getColumnIndex("ill")));
            record.setYear(c.getString(c.getColumnIndex("year")));
            record.setMonth(c.getString(c.getColumnIndex("month")));
            record.setDay(c.getString(c.getColumnIndex("day")));
            records.add(record);
        }
        return records;
    }
    public Record getRecordById(SimpleCursorAdapter adapter, int position){
        Record record = new Record();
        Cursor c = adapter.getCursor();
        c.moveToPosition(position);
        record.setIllDetail(c.getString(c.getColumnIndex("ill")));
        record.setYear(c.getString(c.getColumnIndex("year")));
        record.setMonth(c.getString(c.getColumnIndex("month")));
        record.setDay(c.getString(c.getColumnIndex("day")));
        return record;
    }
    public Cursor queryTheCursor(){
        Cursor c = db.rawQuery("SELECT * FROM record", null);
        return c;
    }
    public void refreshListView(SimpleCursorAdapter adapter){
        Cursor c=db.query("record",null,null,null,null,null,null);
        adapter.changeCursor(c);
    }
    public void closeDb(){
        db.close();
    }
}

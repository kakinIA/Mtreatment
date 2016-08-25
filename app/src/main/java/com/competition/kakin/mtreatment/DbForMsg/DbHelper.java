package com.competition.kakin.mtreatment.DbForMsg;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kakin on 2016/8/23.
 */
public class DbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "msg.db";
    private static final int DATABASE_VERSION = 1;
    public DbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS msg(" +
                "_id INTEGER PRIMARY KEY  AUTOINCREMENT," +
                "doctorName VARCHAR," +
                "info TEXT," +
                "month INTEGER," +
                "day INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS record(" +
                "_id INTEGER PRIMARY KEY  AUTOINCREMENT," +
                "ill VARCHAR," +
                "year TEXT," +
                "month TEXT," +
                "day TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS msg");
        db.execSQL("DROP TABLE IF EXISTS record");
        onCreate(db);
    }
}

package com.example.pigfarm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DbOpenHelper {

    private static final String DATABASE_NAME = "InnerDatabase(SQLite).db";
    private static final int DATABASE_VERSION = 1;
    public static SQLiteDatabase mDB;
    private DatabaseHelper mDBHelper;
    private Context mCtx;

    private class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            db.execSQL(WorkDataBase.CreateWorkDB._CREATE0);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            db.execSQL("DROP TABLE IF EXISTS "+WorkDataBase.CreateWorkDB._TABLENAME0);
            onCreate(db);
        }
    }

    public DbOpenHelper(Context context){
        this.mCtx = context;
    }

    public DbOpenHelper open() throws SQLException {
        mDBHelper = new DatabaseHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
        mDB = mDBHelper.getWritableDatabase();
        return this;
    }

    public void create(){
        mDBHelper.onCreate(mDB);
    }
    public void update() { mDBHelper.onUpgrade(mDB, 1, 2);}

    public void close(){
        mDB.close();
    }
    public long insertColumn(String what_work, String how_work, String use_calorie, String work_month, String work_day){
        ContentValues values = new ContentValues();
        values.put(WorkDataBase.CreateWorkDB.what_work, what_work);
        values.put(WorkDataBase.CreateWorkDB.how_work, how_work);
        values.put(WorkDataBase.CreateWorkDB.use_calorie, use_calorie);
        values.put(WorkDataBase.CreateWorkDB.work_month, work_month);
        values.put(WorkDataBase.CreateWorkDB.work_day, work_day);
        return mDB.insert(WorkDataBase.CreateWorkDB._TABLENAME0, null, values);
    }
    public Cursor selectColumns(){
        return mDB.query(WorkDataBase.CreateWorkDB._TABLENAME0, null, null, null, null, null, null);

    }
    public void deleteAllColumns() {
        mDB.delete(WorkDataBase.CreateWorkDB._TABLENAME0, null, null);
    }
}
package com.example.pigfarm;


import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class NutritionDBOpenHelper {

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
            String sql = "create table Nutrition (food_name text, food_calorie text, months text, day text)";
            db.execSQL(sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            db.execSQL("DROP TABLE IF EXISTS "+NutritionDataBase.CreateNutritionDB._TABLENAME1);
            onCreate(db);
        }
    }

    public NutritionDBOpenHelper(Context context){
        this.mCtx = context;
    }

    public NutritionDBOpenHelper open() throws SQLException {
        mDBHelper = new DatabaseHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
        mDB = mDBHelper.getWritableDatabase();
        return this;
    }

    public void create(){
        mDBHelper.onCreate(mDB);
    }

    public void close(){
        mDB.close();
    }
    public long insertColumn(String food_name, String food_calorie, String work_month, String work_day){
        ContentValues values = new ContentValues();
        values.put("food_name", food_name);
        values.put("food_calorie", food_calorie);
        values.put("months", work_month);
        values.put("day", work_day);
        return mDB.insert("Nutrition", null, values);
    }
    public Cursor selectColumns(){
        return mDB.query("Nutrition", null, null, null, null, null, null);

    }
    public ArrayList<Item2Class> getAllItem() {
        ArrayList<Item2Class> ItemData = new ArrayList<Item2Class>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + "Nutrition";


        Cursor cursor = mDB.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst() && cursor.getCount() > 0) {
            do {
                Item2Class contact = new Item2Class(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
                ItemData.add(contact);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return ItemData; // return contact list
    }
}
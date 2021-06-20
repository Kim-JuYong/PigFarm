package com.example.pigfarm;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
            db.execSQL(NutritionDataBase.CreateNutritionDB._CREATE1);
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
        values.put(NutritionDataBase.CreateNutritionDB.food_name, food_name);
        values.put(NutritionDataBase.CreateNutritionDB.food_name, food_calorie);
        values.put(NutritionDataBase.CreateNutritionDB.months, work_month);
        values.put(NutritionDataBase.CreateNutritionDB.day, work_day);
        return mDB.insert(NutritionDataBase.CreateNutritionDB._TABLENAME1, null, values);
    }
    public Cursor selectColumns(){
        return mDB.query(NutritionDataBase.CreateNutritionDB._TABLENAME1, null, null, null, null, null, null);

    }
    public void deleteAllColumns() {
        mDB.delete(NutritionDataBase.CreateNutritionDB._TABLENAME1, null, null);
    }

}
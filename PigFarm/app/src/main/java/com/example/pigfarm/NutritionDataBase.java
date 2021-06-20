package com.example.pigfarm;

import android.provider.BaseColumns;

public final class NutritionDataBase {
    public static final class CreateNutritionDB implements BaseColumns {
        public static final String food_name = "food_name";
        public static final String food_calorie = "food_calorie";
        public static final String months = "work_month";
        public static final String day = "work_day";
        public static final String _TABLENAME1 = "food_table";
        public static final String _CREATE1 = "create table if not exists "+_TABLENAME1+"("
                +_ID+" integer primary key autoincrement, "
                +food_name+" text not null , "
                +months+"text not null , "
                +day+"text not null , "
                +food_calorie+" text not null );";
    }
}

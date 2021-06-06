package com.example.pigfarm;

import android.provider.BaseColumns;

public final class WorkDataBase {

    public static final class CreateWorkDB implements BaseColumns {
        public static final String what_work = "what_work";
        public static final String how_work = "how_work";
        public static final String use_calorie = "use_calorie";
        public static final String months = "work_month";
        public static final String day = "work_day";
        public static final String _TABLENAME0 = "work_table";
        public static final String _CREATE0 = "create table if not exists "+_TABLENAME0+"("
                +_ID+" integer primary key autoincrement, "
                +what_work+" text not null , "
                +how_work+" text not null , "
                +use_calorie+" integer not null);";
    }
}
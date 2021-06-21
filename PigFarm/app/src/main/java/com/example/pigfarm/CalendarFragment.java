package com.example.pigfarm;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CalendarFragment extends Fragment {
    RecyclerView recyclerView;
    CalendarView calendarView;
    TextView textView;
    Button delete_all_btn;
    ArrayList<ItemClass> list = new ArrayList<>();
    ArrayList<Item2Class> dbList = new ArrayList<>();
    CalendarRecyclerAdapter adapter;
    NutritionDBOpenHelper mDbOpenHelper = new NutritionDBOpenHelper(getContext());
    DbOpenHelper mDbOpenHelper2 = new DbOpenHelper(getContext());
    String getDay = "";
    String getMonth = "";
    private SQLiteDatabase db;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.calendar_fragment, container, false);
        textView = rootView.findViewById(R.id.total_calorie);
        RecyclerView recyclerView = rootView.findViewById(R.id.calendar_recycler_view);
        delete_all_btn = rootView.findViewById(R.id.delete_all_button);

        recyclerView.setHasFixedSize(true);

        calendarView = rootView.findViewById(R.id.calendar_view);
        adapter = new CalendarRecyclerAdapter(getActivity(), list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        dbList = mDbOpenHelper.getAllItem();
        adapter.notifyDataSetChanged();
        return rootView;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        delete_all_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFoodTableMonth();
                deleteWorkTableMonth();
                list.clear();
                textView.setText("0");
                adapter.notifyDataSetChanged();
            }

        });
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                list.clear();
                getDay = String.valueOf(dayOfMonth);
                getMonth = String.valueOf(month);

                if(dayOfMonth < 10){
                    getDay = "0" + String.valueOf(dayOfMonth);;
                }
                if(month < 10){
                    getMonth = "0" + String.valueOf(month + 1);
                }
                int s = dbList.size();
                System.out.println(s);
                for(int i = 0; i < s; i++) {
                    if (dbList.get(i).month.equals(getMonth) && dbList.get(i).day.equals(getDay)) {
                        ItemClass item = new ItemClass(dbList.get(i).title, dbList.get(i).how_many);
                        list.add(item);
                        System.out.println("dd");
                    }
                }
                double total = 0;
                for(int i = 0; i < list.size(); i++){
                    total += Double.parseDouble(list.get(i).how_many);
                }
                textView.setText(Double.toString(total));
                adapter.notifyDataSetChanged();
            }
        });

    }
    public void deleteWorkTableMonth() {
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
        Date date = new Date();
        String myPath = "/data/data/com.example.pigfarm/databases/" + "InnerDatabase(SQLite).db";
        SQLiteDatabase database = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        Cursor cursor = database.rawQuery("SELECT * FROM work_table", null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst() && cursor.getCount() > 0) {
            do {
                database.execSQL("DELETE FROM work_table WHERE work_month=" + monthFormat.format(date.getTime()));

            } while (cursor.moveToNext());
        }
        cursor.close();

    }
    public void deleteFoodTableMonth() {
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
        Date date = new Date();
        String myPath = "/data/data/com.example.pigfarm/databases/" + "InnerDatabase(SQLite).db";
        SQLiteDatabase database = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        Cursor cursor = database.rawQuery("SELECT * FROM Nutrition", null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst() && cursor.getCount() > 0) {
            do {
                database.execSQL("DELETE FROM Nutrition WHERE months=" + monthFormat.format(date.getTime()));

            } while (cursor.moveToNext());
        }
        cursor.close();

    }


}
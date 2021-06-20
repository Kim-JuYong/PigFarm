package com.example.pigfarm;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CalendarFragment extends Fragment {
    RecyclerView recyclerView;
    CalendarView calendarView;
    ArrayList<ItemClass> list = new ArrayList<>();
    ArrayList<Item2Class> dbList = new ArrayList<>();
    CalendarRecyclerAdapter adapter;
    NutritionDBOpenHelper mDbOpenHelper = new NutritionDBOpenHelper(getContext());
    String getDay = "";
    String getMonth = "";
    private SQLiteDatabase db;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.calendar_fragment, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.calendar_recycler_view);

        recyclerView.setHasFixedSize(true);

        calendarView = rootView.findViewById(R.id.calendar_view);
        adapter = new CalendarRecyclerAdapter(getActivity(), list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        dbList = mDbOpenHelper.getAllItem();
        return rootView;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

                adapter.notifyDataSetChanged();
            }
        });

    }

}
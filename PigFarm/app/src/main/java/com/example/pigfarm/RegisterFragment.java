package com.example.pigfarm;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class RegisterFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerView recyclerView2;
    Button today_delete;
    ArrayList<Item2Class> dbList = new ArrayList<>();
    ArrayList<ItemClass> dblist2 = new ArrayList<>();
    ArrayList<ItemClass> list = new ArrayList<>();
    ArrayList<ItemClass> list2 = new ArrayList<>();
    NutritionDBOpenHelper mDbOpenHelper = new NutritionDBOpenHelper(getContext());
    DbOpenHelper mDbOpenHelper2 = new DbOpenHelper(getContext());
    CalendarRecyclerAdapter adapter = new CalendarRecyclerAdapter(getActivity(), list);;
    CalendarRecyclerAdapter adapter2 = new CalendarRecyclerAdapter(getActivity(), list2);;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_fragment, container, false);
        recyclerView = v.findViewById(R.id.home_recycler);
        recyclerView2 = v.findViewById(R.id.home_work_recycler);
        today_delete = v.findViewById(R.id.today_delete);
        return v;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbList = mDbOpenHelper.getAllItem();
        dblist2 = this.getAllTable();

        adapter = new CalendarRecyclerAdapter(getActivity(), list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        adapter2 = new CalendarRecyclerAdapter(getActivity(), list2);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView2.setAdapter(adapter2);

        int s = dbList.size();
        list.clear();
        for(int i = 0; i < s; i++) {
            ItemClass item = new ItemClass(dbList.get(i).title, dbList.get(i).how_many);
            list.add(item);
        }
        adapter.notifyDataSetChanged();

        int s2 = dblist2.size();
        list2.clear();
        for(int i = 0; i < s2; i++) {
            ItemClass item = new ItemClass(dblist2.get(i).title, dblist2.get(i).how_many);
            list2.add(item);
        }
        adapter2.notifyDataSetChanged();
        double t1 = 0;
        for(int i = 0; i < list.size(); i++){
            t1 += Double.parseDouble(list.get(i).how_many);
        }
        double t2 = 0;
        for(int i = 0; i < list2.size(); i++){
            t2 += Double.parseDouble(list2.get(i).how_many);
        }

        TextView v1 = view.findViewById(R.id.total_cal);
        TextView v2 = view.findViewById(R.id.total_work);
        v1.setText(Double.toString(t1));
        v2.setText(Double.toString(t2));
        view.findViewById(R.id.meal_photo_register_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivityForResult(new Intent(getContext(), PhotoActivity.class), 0);
            }
        });
        view.findViewById(R.id.work_register_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WorkRegisterDialog dialog = new WorkRegisterDialog(getContext());
                dialog.callDialog();
            }
        });
        today_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFoodTableToday();
                deleteWorkTableToday();
                list.clear();
                list2.clear();
                adapter.notifyDataSetChanged();
                adapter2.notifyDataSetChanged();
            }
        });
    }
    private ArrayList<ItemClass> getAllTable() {
        ArrayList<ItemClass> ItemData = new ArrayList<ItemClass>();
        String myPath = "/data/data/com.example.pigfarm/databases/" + "InnerDatabase(SQLite).db";
        SQLiteDatabase database = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        Cursor cursor = database.rawQuery("SELECT * FROM work_table", null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst() && cursor.getCount() > 0) {
            do {
                ItemClass contact = new ItemClass(cursor.getString(1), cursor.getString(3));
                ItemData.add(contact);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return ItemData; // return contact list
        //database.execSQL("DELETE FROM work_table WHERE _id=" + 7);
    }
    public void deleteWorkTableToday() {
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
        Date date = new Date();
        String myPath = "/data/data/com.example.pigfarm/databases/" + "InnerDatabase(SQLite).db";
        SQLiteDatabase database = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        Cursor cursor = database.rawQuery("SELECT * FROM work_table", null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst() && cursor.getCount() > 0) {
            do {
                database.execSQL("DELETE FROM work_table WHERE months=" + monthFormat.format(date.getTime()) + " AND day=" + dayFormat.format(date.getTime()));

            } while (cursor.moveToNext());
        }
        cursor.close();

    }
    public void deleteFoodTableToday() {
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
        Date date = new Date();
        String myPath = "/data/data/com.example.pigfarm/databases/" + "InnerDatabase(SQLite).db";
        SQLiteDatabase database = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        Cursor cursor = database.rawQuery("SELECT * FROM Nutrition", null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst() && cursor.getCount() > 0) {
            do {
                database.execSQL("DELETE FROM Nutrition WHERE months=" + monthFormat.format(date.getTime()) + " AND day=" + dayFormat.format(date.getTime()));

            } while (cursor.moveToNext());
        }
        cursor.close();

    }


}

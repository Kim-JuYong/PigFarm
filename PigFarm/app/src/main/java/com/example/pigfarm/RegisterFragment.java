package com.example.pigfarm;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
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
    ArrayList<Item2Class> dbList = new ArrayList<>();
    ArrayList<ItemClass> list = new ArrayList<>();
    NutritionDBOpenHelper mDbOpenHelper = new NutritionDBOpenHelper(getContext());
    CalendarRecyclerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_fragment, container, false);
        recyclerView = v.findViewById(R.id.home_recycler);
        return v;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbList = mDbOpenHelper.getAllItem();
        adapter = new CalendarRecyclerAdapter(getActivity(), list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("MM");
        SimpleDateFormat simpleTime = new SimpleDateFormat("dd");
        String getDate = simpleDate.format(date);
        String getTime = simpleTime.format(date);
        if(Integer.parseInt(getDate) < 10){
            getDate = "0" + Integer.parseInt(getDate);;
        }
        if(Integer.parseInt(getTime) < 10){
            getTime = "0" + Integer.parseInt(getTime);
        }
        int s = dbList.size();
        System.out.println(s);
        list.clear();
        for(int i = 0; i < s; i++) {
            if (dbList.get(i).month.equals(getDate) && dbList.get(i).day.equals(getTime)) {
                ItemClass item = new ItemClass(dbList.get(i).title, dbList.get(i).how_many);
                list.add(item);
            }
        }
        adapter.notifyDataSetChanged();

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
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 0:
                System.out.println("fra");
        }
    }

            }
        });
    }



}

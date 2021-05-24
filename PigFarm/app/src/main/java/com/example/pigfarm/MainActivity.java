package com.example.pigfarm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    //ImageView imageView;

    private BottomNavigationView bottomNavigationView; // 바텀네비게이션 뷰
    private FragmentManager manager = getSupportFragmentManager();
    private FragmentTransaction transaction;

    private HomeFragment homeFragment = new HomeFragment();
    private AnalyzeFragment analyzeFragment = new AnalyzeFragment();
    private RecommendFragment recommendFragment = new RecommendFragment();
    private MypageFragment mypageFragment = new MypageFragment();
    private PhotoActivity photoActivity = new PhotoActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, homeFragment).commitAllowingStateLoss();
        //바텀 네비게이션뷰 안의 아이템들 설정
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = manager.beginTransaction();
                switch (item.getItemId()) {
                    case R.id.home: {
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, homeFragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.analyze: {
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, analyzeFragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.recommend: {
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, recommendFragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.mypage: {
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, mypageFragment).commitAllowingStateLoss();
                        break;
                    }
                }
                return true;
            }
        });
    }


}
package com.example.pigfarm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    //ImageView imageView;
    private BottomNavigationView bottomNavigationView; // 바텀네비게이션 뷰
    private FragmentManager manager = getSupportFragmentManager();
    private FragmentTransaction transaction;

    private HomeFragment homeFragment = new HomeFragment();
    private CalendarFragment analyzeFragment = new CalendarFragment();
    private RecommendFragment recommendFragment = new RecommendFragment();
    private MypageFragment mypageFragment = new MypageFragment();

    ImageView i;
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
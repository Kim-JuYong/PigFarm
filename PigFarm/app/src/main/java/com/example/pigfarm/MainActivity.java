package com.example.pigfarm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //ImageView imageView;
    private BottomNavigationView bottomNavigationView; // 바텀네비게이션 뷰
    private FragmentManager manager = getSupportFragmentManager();
    private FragmentTransaction transaction;
    private HomeFragment homeFragment = new HomeFragment();
    private CalendarFragment analyzeFragment = new CalendarFragment();
    private RecommendFragment recommendFragment = new RecommendFragment();
    //private MypageFragment mypageFragment = new MypageFragment();

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
                    /*case R.id.mypage: {
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, mypageFragment).commitAllowingStateLoss();
                        break;
                    }*/
                }
                return true;
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0:
                Dialog dialog = new Dialog(this);
                ArrayList<String> result = new ArrayList<String>();
                result = data.getStringArrayListExtra("result");
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.food_name_select_dialog_fragment);
                ViewGroup.LayoutParams params = dialog.getWindow().getAttributes();
                params.width = WindowManager.LayoutParams.MATCH_PARENT;
                dialog.getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
                dialog.show();
                for(int i = result.size(); i < 5; i++){
                    result.add("");
                }
                final RadioButton select_box1 = (RadioButton) dialog.findViewById(R.id.select_box1);
                final RadioButton select_box2 = (RadioButton) dialog.findViewById(R.id.select_box2);
                final RadioButton select_box3 = (RadioButton) dialog.findViewById(R.id.select_box3);
                final RadioButton select_box4 = (RadioButton) dialog.findViewById(R.id.select_box4);
                final RadioButton select_box5 = (RadioButton) dialog.findViewById(R.id.select_box5);
                select_box1.setText(result.get(0));
                select_box2.setText(result.get(1));
                select_box3.setText(result.get(2));
                select_box4.setText(result.get(3));
                select_box5.setText(result.get(4));
                final RadioGroup radioGroup = (RadioGroup)dialog.findViewById(R.id.select_group);
                final Button select_button = (Button) dialog.findViewById(R.id.select_register);
                final Button cancel_button = (Button) dialog.findViewById(R.id.select_cancel);
                select_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int id = radioGroup.getCheckedRadioButtonId();
                        //SQLite save code
                    }
                });
                /*cancel_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });*/
        }
    }

}
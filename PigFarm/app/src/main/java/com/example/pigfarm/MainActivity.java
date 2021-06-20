package com.example.pigfarm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    //ImageView imageView;
    private BottomNavigationView bottomNavigationView; // 바텀네비게이션 뷰
    private FragmentManager manager = getSupportFragmentManager();
    private FragmentTransaction transaction;
    private RegisterFragment homeFragment = new RegisterFragment();
    private CalendarFragment analyzeFragment = new CalendarFragment();
    private RecommendFragment recommendFragment = new RecommendFragment();
    private MypageFragment mypageFragment = new MypageFragment();
    NutritionDBOpenHelper mDbOpenHelper = new NutritionDBOpenHelper(this);
    String re_input = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mDbOpenHelper.open();

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, recommendFragment).commitAllowingStateLoss();
        //바텀 네비게이션뷰 안의 아이템들 설정
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                transaction = manager.beginTransaction();
                switch (item.getItemId()) {
                    case R.id.home: {
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, recommendFragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.analyze: {
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, analyzeFragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.recommend: {
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, mypageFragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.mypage: {
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, homeFragment).commitAllowingStateLoss();
                        break;
                    }
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
                File imageFile;
                HashMap<String, String> result;
                imageFile = (File)data.getExtras().get("result");
                SendImageGetCalorie getCal = new SendImageGetCalorie(imageFile);

                /*
                socket연결이 main Thread에서 불가능 해 thread를 만들어실행.
                그래서 칼로리 값을 받아올 때 까지 join 함수로 기다렸다가 계속 실행함
                받아온 칼로리값이 클래스 내부 변수에 저장되어 getter로 받아와야 함
                 */
                getCal.start();
                try {
                    getCal.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                result = getCal.getFoods();
                for (String s : result.keySet()) {
                    System.out.println("food:"+s);
                }

                Dialog dialog = new Dialog(this);
                Dialog re_dialog = new Dialog(this);
                Dialog final_dialog = new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.food_name_select_dialog_fragment);
                ViewGroup.LayoutParams params = dialog.getWindow().getAttributes();
                params.width = WindowManager.LayoutParams.MATCH_PARENT;
                dialog.getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
                dialog.show();

                RadioButton[] select_boxes = new RadioButton[5];
                select_boxes[0] = (RadioButton) dialog.findViewById(R.id.select_box1);
                select_boxes[1] = (RadioButton) dialog.findViewById(R.id.select_box2);
                select_boxes[2] = (RadioButton) dialog.findViewById(R.id.select_box3);
                select_boxes[3] = (RadioButton) dialog.findViewById(R.id.select_box4);
                select_boxes[4] = (RadioButton) dialog.findViewById(R.id.select_box5);

                int i=0;
                for (String s : result.keySet()) {
                    select_boxes[i].setText(s);
                    i++;
                }
                for (String s : result.keySet()) {
                    System.out.println("food:"+s);
                }
                final RadioGroup radioGroup = (RadioGroup)dialog.findViewById(R.id.select_group);
                final Button select_button = (Button) dialog.findViewById(R.id.select_register);
                final Button cancel_button = (Button) dialog.findViewById(R.id.select_cancel);
                select_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int id = radioGroup.getCheckedRadioButtonId();
                        RadioButton rb = (RadioButton)dialog.findViewById(id);
                        String getName = rb.getText().toString();
                        //SQLite save code
                        long now = System.currentTimeMillis();
                        Date date = new Date(now);
                        SimpleDateFormat simpleDate = new SimpleDateFormat("MM");
                        SimpleDateFormat simpleTime = new SimpleDateFormat("dd");
                        String getDate = simpleDate.format(date);
                        String getTime = simpleTime.format(date);
                        System.out.println(getDate);
                        System.out.println(getTime);
                        mDbOpenHelper.insertColumn(getName, result.get(getName), getDate, getTime);
                        dialog.cancel();
                    }
                });
                cancel_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                        re_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        re_dialog.setContentView(R.layout.retransmission_dialog_fragment);
                        ViewGroup.LayoutParams params = re_dialog.getWindow().getAttributes();
                        params.width = WindowManager.LayoutParams.MATCH_PARENT;
                        re_dialog.getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
                        re_dialog.show();
                        final Button re_select_button = (Button) re_dialog.findViewById(R.id.re_select_register);
                        final Button re_cancel_button = (Button) re_dialog.findViewById(R.id.re_select_cancel);
                        re_cancel_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                re_dialog.cancel();
                            }
                        });
                        

                        re_select_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                EditText editText = (EditText)re_dialog.findViewById(R.id.re_food_name);
                                String calorie;
                                re_input = editText.getText().toString();
                                SendStringGetCalorie getCalByStr = new SendStringGetCalorie(re_input);
                                getCalByStr.start();
                                try {
                                    getCalByStr.join();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                calorie = getCalByStr.getCalorie();
                                if(calorie.equals("0")){
                                    re_dialog.cancel();
                                    final_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    final_dialog.setContentView(R.layout.final_dialog_fragment);
                                    ViewGroup.LayoutParams params = final_dialog.getWindow().getAttributes();
                                    params.width = WindowManager.LayoutParams.MATCH_PARENT;
                                    final_dialog.getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
                                    final_dialog.show();
                                    Button final_select = (Button)final_dialog.findViewById(R.id.final_select_register);
                                    Button final_cancel = (Button)final_dialog.findViewById(R.id.final_select_cancel);
                                    final_select.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            EditText editText = (EditText)final_dialog.findViewById(R.id.final_food_name);
                                            EditText editText2 = (EditText)final_dialog.findViewById(R.id.final_food_calorie);
                                            String final_input_name;
                                            String final_input_calorie;
                                            final_input_name = editText.getText().toString();
                                            final_input_calorie = editText2.getText().toString();
                                            long now = System.currentTimeMillis();
                                            Date date = new Date(now);
                                            SimpleDateFormat simpleDate = new SimpleDateFormat("MM");
                                            SimpleDateFormat simpleTime = new SimpleDateFormat("dd");
                                            String getDate = simpleDate.format(date);
                                            String getTime = simpleTime.format(date);
                                            mDbOpenHelper.insertColumn(final_input_name, final_input_calorie, getDate, getTime);
                                            System.out.println(calorie);
                                            final_dialog.cancel();
                                        }
                                    });

                                }
                                else{
                                    long now = System.currentTimeMillis();
                                    Date date = new Date(now);
                                    SimpleDateFormat simpleDate = new SimpleDateFormat("MM");
                                    SimpleDateFormat simpleTime = new SimpleDateFormat("dd");
                                    String getDate = simpleDate.format(date);
                                    String getTime = simpleTime.format(date);
                                    mDbOpenHelper.insertColumn(re_input, calorie, getDate, getTime);
                                    System.out.println(calorie);
                                }
                            }
                        });

                    }
                });
        }
    }

}
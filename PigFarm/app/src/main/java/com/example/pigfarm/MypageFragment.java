package com.example.pigfarm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class MypageFragment extends Fragment {
    final private int tv_weekId[] = {R.id.tv_week0, R.id.tv_week1, R.id.tv_week2, R.id.tv_week3, R.id.tv_week4};
    final private int tv_inputId[] = {R.id.tv_input0, R.id.tv_input1, R.id.tv_input2, R.id.tv_input3, R.id.tv_input4};
    final private int tv_outputId[] = {R.id.tv_output0, R.id.tv_output1, R.id.tv_output2, R.id.tv_output3, R.id.tv_output4};
    final private int my_input[] = {R.id.et_week, R.id.et_input, R.id.et_output};
    TextView tv_weekArray[] = new TextView[5];
    TextView tv_inputArray[] = new TextView[5];
    TextView tv_outputArray[] = new TextView[5];
    EditText my_inputArray[] = new EditText[3];
    Button my_button;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.mypage_fragment, container, false);

        Init(v);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월");
        Calendar day = Calendar.getInstance();
        String time = simpleDateFormat.format(day.getTime());

        TextView titleView = (TextView)v.findViewById(R.id.tv_date);
        titleView.setText(time);

        for(int i=0; i<5; i++) {
            final int index; // 선언한 이유는 내부에서 tv_weekId 배열을 사용하기 위해 final로 선언 후 값을 저장하여 사
            index = i;
            String sunday = getDay(time, index, 0);
            String saturday = getDay(time, index, 1);
            tv_weekArray[index] = (TextView)v.findViewById(tv_weekId[i]);
            tv_weekArray[index].setText(sunday + " - " + saturday);
        }

        my_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String str_week = my_inputArray[0].getText().toString().trim();
                    String str_input = my_inputArray[1].getText().toString().trim();
                    String str_output = my_inputArray[2].getText().toString().trim();

                    int int_week = Integer.parseInt(str_week);
                    if (int_week < 1 || int_week > 5) {
                        Toast.makeText(getContext(), "1주차에서 5주차만 가능합니다.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        tv_inputArray[int_week-1].setText(str_input + " kcal");
                        tv_outputArray[int_week-1].setText(str_output + " kcal");
                    }
                }catch(NumberFormatException e){
                    Toast.makeText(getContext(), "숫자만 입력하세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }

    public static String getDay(String yearMonth, int w, int day) {
        SimpleDateFormat simpleWeekFormat = new SimpleDateFormat("MM월 dd일");
        Calendar week = Calendar.getInstance();
        int y = Integer.parseInt(yearMonth.substring(0,4));
        int m = Integer.parseInt(yearMonth.substring(6,8))-1;

        week.set(Calendar.YEAR, y);
        week.set(Calendar.MONTH, m);
        week.set(Calendar.WEEK_OF_MONTH, w+1);
        if (day == 0)
            week.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        else
            week.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        return simpleWeekFormat.format(week.getTime());
    }

    public void Init(View v) {
        for(int i=0; i<5; i++) {
            final int index;
            index = i;
            tv_inputArray[index] = (TextView)v.findViewById(tv_inputId[i]);
            tv_outputArray[index] = (TextView)v.findViewById(tv_outputId[i]);
        }
        for(int i=0; i<3; i++) {
            final int index;
            index = i;
            my_inputArray[index] = (EditText)v.findViewById(my_input[i]);
        }
        my_button = (Button)v.findViewById(R.id.myButton);
    }

}
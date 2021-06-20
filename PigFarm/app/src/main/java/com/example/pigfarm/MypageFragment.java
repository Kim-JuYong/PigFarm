package com.example.pigfarm;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

class Pair{
    private int preMonth;
    private int preDay;
    private int nowMonth;
    private int nowDay;
    public Pair(int preMonth, int preDay){
        this.preMonth = preMonth;
        this.preDay = preDay;
        this.nowMonth = 0;
        this.nowDay = 0;
    }
    public int getPreMonth() {
        return preMonth;
    }
    public int getPreDay() {
        return preDay;
    }
    public int getNowMonth() {
        return nowMonth;
    }
    public int getNowDay() {
        return nowDay;
    }
    public void setNowMonth(int nowMonth) {
        this.nowMonth = nowMonth;
    }
    public void setNowDay(int nowDay) {
        this.nowDay = nowDay;
    }
}

public class MypageFragment extends Fragment {
    final private int tv_weekId[] = {R.id.tv_week0, R.id.tv_week1, R.id.tv_week2, R.id.tv_week3, R.id.tv_week4};
    final private int tv_inputId[] = {R.id.tv_input0, R.id.tv_input1, R.id.tv_input2, R.id.tv_input3, R.id.tv_input4};
    final private int tv_outputId[] = {R.id.tv_output0, R.id.tv_output1, R.id.tv_output2, R.id.tv_output3, R.id.tv_output4};
    TextView tv_weekArray[] = new TextView[5];
    TextView tv_inputArray[] = new TextView[5];
    TextView tv_outputArray[] = new TextView[5];
    EditText my_inputArray[] = new EditText[3];
    private LineChart ct_input;
    private LineChart ct_output;
    List<Pair> pairList = new LinkedList<>();
    private int weekHealth[] = new int[5];
    private int weekEat[] = new int[5];

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
            weekHealth[index] = 0;
            weekEat[index] = 0;
        }

        drawGraph();

        return v;
    }

    private String getDay(String yearMonth, int w, int day) {
        SimpleDateFormat simpleWeekFormat = new SimpleDateFormat("MM월 dd일");
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
        Calendar week = Calendar.getInstance();
        int y = Integer.parseInt(yearMonth.substring(0,4));
        int m = Integer.parseInt(yearMonth.substring(6,8))-1;

        week.set(Calendar.YEAR, y);
        week.set(Calendar.MONTH, m);
        week.set(Calendar.WEEK_OF_MONTH, w+1);
        if (day == 0) {
            week.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            Pair pair = new Pair(Integer.parseInt(monthFormat.format(week.getTime())), Integer.parseInt(dayFormat.format(week.getTime())));
            pairList.add(pair);
        }
        else {
            week.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
            pairList.get(w).setNowMonth(Integer.parseInt(monthFormat.format(week.getTime())));
            pairList.get(w).setNowDay(Integer.parseInt(dayFormat.format(week.getTime())));
        }
        return simpleWeekFormat.format(week.getTime());
    }

    private void Init(View v) {
        ct_input = v.findViewById(R.id.chart_input);
        ct_output = v.findViewById(R.id.chart_output);
        for(int i=0; i<5; i++) {
            final int index;
            index = i;
            tv_inputArray[index] = (TextView)v.findViewById(tv_inputId[i]);
            tv_outputArray[index] = (TextView)v.findViewById(tv_outputId[i]);
        }
    }

    private void inputChartSetData() {
        XAxis xAxis = ct_input.getXAxis();
        YAxis yAxis = ct_input.getAxisLeft();

        ArrayList<Entry> values = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            String val_str[] = tv_inputArray[i].getText().toString().split(" ");
            int val = Integer.parseInt(val_str[0]);
            values.add(new Entry(val, i));
        }

        LineDataSet set;
        set = new LineDataSet(values, "주간 섭취 칼로리 / 단위 : kcal");

        ArrayList<String> labels = new ArrayList<String>();
        for (int i = 0; i < 5; i++) {
            String week_label = i+1 + "주차";
            labels.add(week_label);
        }

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set); // 데이터 셋을 추가

        LineData data = new LineData(labels, dataSets);

        set.setColor(Color.BLACK);
        set.setCircleColor(Color.BLACK);
        set.setDrawCubic(true); // 선 둥글게 만들기
        set.setDrawFilled(true); // 그래프 밑부분 색

        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        yAxis.setDrawGridLines(false);
        yAxis.setEnabled(false);

        MyMarkerView mv = new MyMarkerView(getContext(), R.layout.marker_view);
        ct_input.setMarkerView(mv);
        ct_input.setMaxVisibleValueCount(5); // 최대 보이는 그래프 개수를 5개로 정해주었다.
        ct_input.setData(data); // 그래프에 데이터를 설정
    }

    private void outputChartSetData() {
        XAxis xAxis = ct_output.getXAxis();
        YAxis yAxis = ct_output.getAxisLeft();

        ArrayList<Entry> values = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            String val_str[] = tv_outputArray[i].getText().toString().split(" ");
            int val = Integer.parseInt(val_str[0]);
            values.add(new Entry(val, i));
        }

        LineDataSet set;
        set = new LineDataSet(values, "주간 소비 칼로리 / 단위 : kcal");

        ArrayList<String> labels = new ArrayList<String>();
        for (int i = 0; i < 5; i++) {
            String week_label = i+1 + "주차";
            labels.add(week_label);
        }

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set); // add the data sets

        // create a data object with the data sets
        LineData data = new LineData(labels, dataSets);

        // black lines and points
        set.setColor(Color.BLACK);
        set.setCircleColor(Color.BLACK);
        set.setDrawCubic(true); // 선 둥글게 만들기
        set.setDrawFilled(true); // 그래프 밑부분 색

        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        yAxis.setDrawGridLines(false);
        yAxis.setEnabled(false);

        MyMarkerView mv = new MyMarkerView(getContext(), R.layout.marker_view);
        ct_output.setMarkerView(mv);
        ct_output.setMaxVisibleValueCount(5); // 최대 보이는 그래프 개수를 5개로 정해주었다.
        ct_output.setData(data);
    }


    private void getDataFromDatabase() {
        String myPath = "/data/data/com.example.pigfarm/databases/" + "InnerDatabase(SQLite).db";
        SQLiteDatabase database = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursorHealth = database.rawQuery("SELECT * FROM work_table", null);
        Cursor cursorEat = database.rawQuery("SELECT * FROM Nutrition", null);
        if (cursorHealth != null) {
            if (cursorHealth.moveToFirst()) {
                do {
                    int checkMonth = Integer.parseInt(cursorHealth.getString(4));
                    int checkDay = Integer.parseInt(cursorHealth.getString(5));
                    if (checkMonth == pairList.get(2).getNowMonth()) {
                        if (pairList.get(0).getNowDay() >= checkDay) {
                            weekHealth[0] += Integer.parseInt(cursorHealth.getString(3));
                        }
                        else if (pairList.get(1).getNowDay() >= checkDay) {
                            weekHealth[1] += Integer.parseInt(cursorHealth.getString(3));
                        }
                        else if (pairList.get(2).getNowDay() >= checkDay) {
                            weekHealth[2] += Integer.parseInt(cursorHealth.getString(3));
                        }
                        else if (pairList.get(3).getNowDay() >= checkDay) {
                            weekHealth[3] += Integer.parseInt(cursorHealth.getString(3));
                        }
                        else if (pairList.get(4).getPreDay() <= checkDay) {
                            if (pairList.get(4).getNowMonth() > checkMonth) {
                                weekHealth[4] += Integer.parseInt(cursorHealth.getString(3));
                            }
                            else {
                                if (pairList.get(4).getNowDay() >= checkDay) {
                                    weekHealth[4] += Integer.parseInt(cursorHealth.getString(3));
                                }
                            }
                        }
                    }
                    else if (checkMonth == pairList.get(2).getNowMonth()-1) {
                        if (pairList.get(0).getPreMonth() == checkMonth) {
                            if (pairList.get(0).getPreDay() <=  checkDay)
                                weekHealth[0] += Integer.parseInt(cursorHealth.getString(3));
                        }
                    }
                    else if (checkMonth == pairList.get(2).getNowMonth()+1) {
                        if (pairList.get(4).getNowMonth() == checkMonth) {
                            if (pairList.get(4).getNowDay() >= checkDay)
                                weekHealth[4] += Integer.parseInt(cursorHealth.getString(3));
                        }
                    }
                } while (cursorHealth.moveToNext());
            }
            cursorHealth.close();
        }
        if (cursorEat != null) {
            if (cursorEat.moveToFirst()) {
                do {
                    int checkMonth = Integer.parseInt(cursorEat.getString(2));
                    int checkDay = Integer.parseInt(cursorEat.getString(3));
                    if (checkMonth == pairList.get(2).getNowMonth()) {
                        if (pairList.get(0).getNowDay() >= checkDay) {
                            weekEat[0] += Integer.parseInt(cursorEat.getString(1));
                        }
                        else if (pairList.get(1).getNowDay() >= checkDay) {
                            weekEat[1] += Integer.parseInt(cursorEat.getString(1));
                        }
                        else if (pairList.get(2).getNowDay() >= checkDay) {
                            weekEat[2] += Integer.parseInt(cursorEat.getString(1));
                        }
                        else if (pairList.get(3).getNowDay() >= checkDay) {
                            weekEat[3] += Integer.parseInt(cursorEat.getString(1));
                        }
                        else if (pairList.get(4).getPreDay() <= checkDay) {
                            if (pairList.get(4).getNowMonth() > checkMonth) {
                                weekEat[4] += Integer.parseInt(cursorEat.getString(1));
                            }
                            else {
                                if (pairList.get(4).getNowDay() >= checkDay) {
                                    weekEat[4] += Integer.parseInt(cursorEat.getString(1));
                                }
                            }
                        }
                    }
                    else if (checkMonth == pairList.get(2).getNowMonth()-1) {
                        if (pairList.get(0).getPreMonth() == checkMonth) {
                            if (pairList.get(0).getPreDay() <=  checkDay)
                                weekEat[0] += Integer.parseInt(cursorEat.getString(1));
                        }
                    }
                    else if (checkMonth == pairList.get(2).getNowMonth()+1) {
                        if (pairList.get(4).getNowMonth() == checkMonth) {
                            if (pairList.get(4).getNowDay() >= checkDay)
                                weekEat[4] += Integer.parseInt(cursorEat.getString(1));
                        }
                    }
                } while (cursorEat.moveToNext());
            }
            cursorEat.close();
        }
    }

    private void tableSetData() {
        for(int i = 0; i < 5; i++) {
            tv_inputArray[i].setText(weekEat[i] + " kcal");
            tv_outputArray[i].setText(weekHealth[i] + " kcal");
        }
    }

    private void drawGraph() {
        getDataFromDatabase();
        tableSetData();
        inputChartSetData();
        outputChartSetData();
    }

}
package com.example.pigfarm;


import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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

import java.text.DecimalFormat;
import java.util.Calendar;

public class RecommendFragment extends Fragment {
    final private int recommend_tv[] = {R.id.tv_name, R.id.tv_height, R.id.tv_weight, R.id.tv_kcal, R.id.tv_standard_weight, R.id.tv_day, R.id.tv_exercise, R.id.tv_explanation};
    final private int recommend_et[] = {R.id.et_name, R.id.et_height, R.id.et_weight};
    final private int recommend_bt[] = {R.id.bt_edit, R.id.bt_search};
    TextView recommend_tvArray[] = new TextView[8];
    EditText recommend_etArray[] = new EditText[3];
    Button recommend_btArray[] = new Button[2];
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.recommend_fragment, container, false);

        Init(rootView);

        setDayRecommend();

        recommend_btArray[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String profile_name = recommend_etArray[0].getText().toString();
                try{
                    String str_height = recommend_etArray[1].getText().toString().trim();
                    String str_weight = recommend_etArray[2].getText().toString().trim();

                    int profile_height = Integer.parseInt(str_height);
                    int profile_weight = Integer.parseInt(str_weight);

                    double standard_weight = (profile_height - 100) * 0.9;
                    double today_calorie = standard_weight * 35;

                    DecimalFormat form = new DecimalFormat("#.###");

                    recommend_tvArray[0].setText("회원 이름 : " + profile_name);
                    recommend_tvArray[1].setText("회원 키 : " + profile_height);
                    recommend_tvArray[2].setText("회원 몸무게 : " + profile_weight);
                    recommend_tvArray[3].setText(" 하루 권장 칼로리 : " + form.format(today_calorie) + " kcal ");
                    recommend_tvArray[4].setText(" 표준 체중 : " + form.format(standard_weight) + " kg ");

                }catch(NumberFormatException e) {
                    Toast.makeText(getContext(), "숫자만 입력하세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        recommend_btArray[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://u-health.dobong.go.kr/hcal/activity.asp"));
                startActivity(intent);
            }
        });

        return rootView;
    }

    private void Init(View v) {
        for(int i = 0; i < 8; i++) {
            final int index;
            index = i;
            recommend_tvArray[index] = (TextView)v.findViewById(recommend_tv[i]);
        }
        for(int i = 0; i < 3; i++) {
            final int index;
            index = i;
            recommend_etArray[index] = (EditText)v.findViewById(recommend_et[i]);
        }
        for(int i = 0; i < 2; i++) {
            final int index;
            index = i;
            recommend_btArray[index] = (Button)v.findViewById(recommend_bt[i]);
        }
    }

    private void setDayRecommend() {
        Calendar cal = Calendar.getInstance();
        int dayNum = cal.get(Calendar.DAY_OF_WEEK);
        switch(dayNum) {
            case 1 :
                recommend_tvArray[5].setText("일");
                recommend_tvArray[6].setText("긴장해소 요가와 얼굴 자극");
                recommend_tvArray[7].setText("한 주를 마감하는 일요일에는 전신운동 효과를 볼 수 있는 릴렉스(긴장해소) 요가와 얼굴 근육을 자극시키는 피트니스를 한다. 일주일 동안 지친 심신을 안정시키고 혈액 순환을 도와준다. 쌓인 노폐물을 배출시키고 피로 해소에도 좋다.");
                break;
            case 2 :
                recommend_tvArray[5].setText("월");
                recommend_tvArray[6].setText("척추건강을 위한 골반운동");
                recommend_tvArray[7].setText("월요일 과정은 복근, 척추, 골반까지 기본 틀을 이루는 코어(중심)를 강화하고, 바로 세워 주는 동작으로 구성되어 있다. 잘못된 자세와 나쁜 습관, 운동 부족 등으로 척추 질환에 시달리는 현대인들에게 좋은 운동이다.");
                break;
            case 3 :
                recommend_tvArray[5].setText("화");
                recommend_tvArray[6].setText("팔, 다리 및 복부 운동");
                recommend_tvArray[7].setText("화요일에는 팔과 다리의 근력을 강화해 주는 프로그램 과정을 따라한다. 복부를 탄탄하게 만들고, 틀어진 골반의 위치를 교정하는데 도움을 주는 운동이다.");
                break;
            case 4 :
                recommend_tvArray[5].setText("수");
                recommend_tvArray[6].setText("허리근육 강화와 복부비만 해소");
                recommend_tvArray[7].setText("수요일에는 몸의 중심을 잡아주고 지탱하는 허리 근육을 강화하여 몸의 밸런스를 찾아주는 운동을 통해 균형잡힌 주중의 컨디션을 조절한다. 또 복근을 단련시켜 복부 비만을 해소하는 데에도 신경을 쓴다.");
                break;
            case 5 :
                recommend_tvArray[5].setText("목");
                recommend_tvArray[6].setText("옆구리와 복부 운동");
                recommend_tvArray[7].setText("목요일은 몸의 전반적인 근육을 모두 자극시키는 운동들을 시행해 자칫 늘어지기 쉬운 심신을 바로잡는다. 코어 근육(겉으로 드러나지 않은 중심 근육)의 강화와 함께, 옆구리와 복부를 단련하여 신체를 날씬하게 만드는데 노력한다.");
                break;
            case 6 :
                recommend_tvArray[5].setText("금");
                recommend_tvArray[6].setText("척추건강 및 성장기 운동");
                recommend_tvArray[7].setText("금요일은 척추를 바로 잡아주어 등과 허리의 불편함을 해소할 수 있는 운동이 제격이다. 기본 자세가 틀어져 있어 운동 효과를 보지 못하는 직장인이나 성장기 어린이들에게 좋은 프로그램이다.");
                break;
            case 7 :
                recommend_tvArray[5].setText("토");
                recommend_tvArray[6].setText("날씬한 하체라인 강화운동");
                recommend_tvArray[7].setText("토요일에는 하체를 이루는 전체 근육을 단련할 수 있는 운동을 해보자. 날씬하고 탄력있는 다리 라인을 만들어 주며, 하체 전체의 순환에 도움을 준다.");
                break;
        }
    }
}
package com.example.pigfarm;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;

public class WorkRegisterDialog {
    private Context context;

    public WorkRegisterDialog(Context context)
    {
        this.context = context;
    }

    public void callDialog()
    {
        final Dialog dialog = new Dialog(context);

        DbOpenHelper mDbOpenHelper = new DbOpenHelper(dialog.getContext());
        mDbOpenHelper.open();
        mDbOpenHelper.create();

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.work_register_dialog_fragment);
        ViewGroup.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        dialog.show();

        final Button work_register = (Button) dialog.findViewById(R.id.work_register);
        final Button work_cancel = (Button) dialog.findViewById(R.id.work_cancel);


        work_register.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view)
            {
                int months = LocalDate.now().getMonthValue();
                int day = LocalDate.now().getDayOfMonth();
                Log.v("m", String.valueOf(months));
                Log.v("d", String.valueOf(day));
                EditText what_work = (EditText)view.findViewById(R.id.what_work);
                EditText how_work = (EditText)view.findViewById(R.id.how_work);
                EditText use_calorie = (EditText)view.findViewById(R.id.use_calorie);


                /*mDbOpenHelper.insertColumn(what_work.getText().toString().trim(),
                        how_work.getText().toString().trim(),
                        use_calorie.getText().toString().trim(),
                        "month",
                        "day");*/
            }
        });

        work_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                dialog.dismiss();
                Toast.makeText(context, "종료를 취소했습니다", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
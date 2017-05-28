package com.example.katsutoshi.petsitter.activity;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.katsutoshi.petsitter.R;
import com.example.katsutoshi.petsitter.util.NotificationUtil;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {


    private static final String TAG = "petsitter";

    private EditText title, description, date, time;

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;

    private BroadcastReceiver customActionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "TAREFAS", Toast.LENGTH_SHORT).show();
            NotificationUtil.cancell(context, 3);
        }
    };
    @Override
    protected  void onResume()
    {
        super.onResume();
        registerReceiver(customActionReceiver, new IntentFilter(NotificationUtil.ACTION_VISUALIZAR));
    }
    @Override
    protected void onPause()
    {
        super.onPause();
        unregisterReceiver(customActionReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        title = (EditText) findViewById(R.id.etAlertName);
        description = (EditText) findViewById(R.id.etAlertDescription);
        date = (EditText) findViewById(R.id.etAlertDate);
        time = (EditText) findViewById(R.id.etAlertTime);

        date.setOnClickListener(new View.OnClickListener(){
            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        NotificationActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day
                );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        time.setOnClickListener(new View.OnClickListener(){
            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minutes = calendar.get(Calendar.MINUTE);

                TimePickerDialog dialog = new TimePickerDialog(
                        NotificationActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mTimeSetListener,hour,minutes,true
                );

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Log.d(TAG,"onDateSet: dd/mm/yyyy: " + dayOfMonth + "/" +month + "/" + year );


                String strDayOfMonth = (String.valueOf(dayOfMonth).length() != 2) ? "0" + String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth);
                String strMonth = (String.valueOf(month).length() != 2) ? "0" + String.valueOf(month) : String.valueOf(month);

                String strDate = strDayOfMonth + "/" +strMonth + "/" + year;

                date.setText(strDate);
            }
        };

        mTimeSetListener = new TimePickerDialog.OnTimeSetListener(){

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Log.d(TAG,"onTimeSet: hh:mm: " + hourOfDay + ":" +minute);

                String strHour = (String.valueOf(hourOfDay).length() != 2) ? "0" + String.valueOf(hourOfDay) : String.valueOf(hourOfDay);
                String strMinute = (String.valueOf(minute).length() != 2) ? "0" + String.valueOf(minute) : String.valueOf(minute);
                String strTime = strHour + ":" +strMinute;

                time.setText(strTime);
            }
        };
    }

    public void onClickNotificacaoSimples(View view) {
        int id = 1;
        String contentTitle = "Nova Tarefa";
        String contentText = "Você tem tarefas pendentes";
        Intent intent = new Intent(this, MessageActivity.class);
        intent.putExtra("msg", "Voce tem tarefas pendentes");
        NotificationUtil.create(this, intent, contentTitle, contentText,id);
    }

    public void onClickNotificacaoHeadsUp(View view) {
        int id = 1;
        String contentTitle = "Nova Tarefa";
        String contentText = "Você tem tarefas pendentes";
        Intent intent = new Intent(this, MessageActivity.class);
        intent.putExtra("msg", "Voce tem tarefas pendentes");
        NotificationUtil.create(this, intent, contentTitle, contentText,id);
    }

    public void onClickNotificacaoBig(View view) {
        int id = 2;
        String contentTitle = "Nova Tarefa";
        String contentText = "Você tem 3 tarefas pendentes";
        Intent intent = new Intent(this, MessageActivity.class);
        intent.putExtra("msg", "Você tem 3 tarefas pendentes");

        List<String> lines = new ArrayList<String>();
        lines.add("tarefa 1");
        lines.add("tarefa 2");
        lines.add("tarefa 3");
        NotificationUtil.createBig(this, intent, contentTitle, contentText,lines,id);
    }

    public void onClickNotificacaoComAcao(View view) {
        int id = 3;
        String contentTitle = "Nova Tarefa";
        String contentText = "Alimente seu animal";
        Intent intent = new Intent(this, MessageActivity.class);
        intent.putExtra("msg", "Tarefas");
        NotificationUtil.createWithAction(this, intent, contentTitle, contentText, id);
    }
}
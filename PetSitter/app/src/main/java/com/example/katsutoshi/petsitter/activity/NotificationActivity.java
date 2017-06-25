package com.example.katsutoshi.petsitter.activity;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.katsutoshi.petsitter.R;
import com.example.katsutoshi.petsitter.model.Agenda;
import com.example.katsutoshi.petsitter.model.Medication;
import com.example.katsutoshi.petsitter.util.AlarmUtil;
import com.example.katsutoshi.petsitter.util.NotificationPublisher;
import com.example.katsutoshi.petsitter.util.NotificationUtil;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.UUID.randomUUID;

public class NotificationActivity extends AppCompatActivity implements View.OnClickListener {


    private String child = "";
    private FirebaseDatabase mFirebaseDB;
    private DatabaseReference mDBReference;
    private String selectedPetName = "";
    private int ID = 0;
    private static final String TAG = "petsitter";
    private int day, month, year, hour, minutes;
    private EditText title, description, date, time;
    private Button btnSetAlarm;

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        Bundle bundle = getIntent().getExtras();
        child = bundle.getString("uid");

        initFirebase();

        title = (EditText) findViewById(R.id.etAlertName);
        description = (EditText) findViewById(R.id.etAlertDescription);
        date = (EditText) findViewById(R.id.etAlertDate);
        time = (EditText) findViewById(R.id.etAlertTime);
        btnSetAlarm = (Button) findViewById(R.id.btnAgendar);

        btnSetAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    long millis = getDate();
                    Calendar calendar = Calendar.getInstance();
                    Long currentTime = calendar.getTimeInMillis();
                    Long differenceTime = millis - currentTime;
                    //AlarmUtil.schedule(NotificationActivity.this, new Intent(NotificationActivity.this, MessageActivity.class), differenceTime);
                    //cal.setTimeInMillis(millis);
                    scheduleNotification(getNotification(title.getText().toString(),description.getText().toString()), differenceTime);
                    addNotification(title.getText(), description.getText(), millis,date.getText(), time.getText());
                    Toast.makeText(NotificationActivity.this,"Agendado com sucesso", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(NotificationActivity.this,"Erro", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
        date.setOnClickListener(new View.OnClickListener() {
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
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minutes = calendar.get(Calendar.MINUTE);

                TimePickerDialog dialog = new TimePickerDialog(
                        NotificationActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mTimeSetListener, hour, minutes, true
                );

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Log.d(TAG, "onDateSet: dd/mm/yyyy: " + dayOfMonth + "/" + month + "/" + year);


                String strDayOfMonth = (String.valueOf(dayOfMonth).length() != 2) ? "0" + String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth);
                String strMonth = (String.valueOf(month).length() != 2) ? "0" + String.valueOf(month) : String.valueOf(month);

                String strDate = strDayOfMonth + "/" + strMonth + "/" + year;

                date.setText(strDate);
            }
        };

        mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Log.d(TAG, "onTimeSet: hh:mm: " + hourOfDay + ":" + minute);

                String strHour = (String.valueOf(hourOfDay).length() != 2) ? "0" + String.valueOf(hourOfDay) : String.valueOf(hourOfDay);
                String strMinute = (String.valueOf(minute).length() != 2) ? "0" + String.valueOf(minute) : String.valueOf(minute);
                String strTime = strHour + ":" + strMinute;

                time.setText(strTime);
            }
        };
    }

    private long getDate() {
        long millis;
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        day = Integer.parseInt(date.getText().toString().substring(0, 2));
        cal.set(Calendar.DAY_OF_MONTH, day);
        month = Integer.parseInt(date.getText().toString().substring(3, 5));
        cal.set(Calendar.MONTH, month);
        year =  Integer.parseInt(date.getText().toString().substring(6, 10));
        cal.set(Calendar.YEAR, year);
        hour = Integer.parseInt(time.getText().toString().substring(0, 2));
        cal.set(Calendar.HOUR_OF_DAY, hour);
        minutes =  Integer.parseInt(time.getText().toString().substring(3, 5));
        //add 11 because of an error on minutes
        cal.set(Calendar.MINUTE, minutes);
        millis = cal.getTimeInMillis();
        return millis;
    }

    public void onClickNotificacaoSimples(View view) {
        int id = 1;
        String contentTitle = "Nova Tarefa";
        String contentText = "Você tem tarefas pendentes";
        Intent intent = new Intent(this, MessageActivity.class);
        intent.putExtra("msg", "Voce tem tarefas pendentes");
        NotificationUtil.create(this, intent, contentTitle, contentText, id);
    }

    public void onClickNotificacaoHeadsUp(View view) {
        int id = 1;
        String contentTitle = "Nova Tarefa";
        String contentText = "Você tem tarefas pendentes";
        Intent intent = new Intent(this, MessageActivity.class);
        intent.putExtra("msg", "Voce tem tarefas pendentes");
        NotificationUtil.create(this, intent, contentTitle, contentText, id);
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
        NotificationUtil.createBig(this, intent, contentTitle, contentText, lines, id);
    }

    public void onClickNotificacaoComAcao(View view) {
        int id = 3;
        String contentTitle = "Nova Tarefa";
        String contentText = "Alimente seu animal";
        Intent intent = new Intent(this, MessageActivity.class);
        intent.putExtra("msg", "Tarefas");
        NotificationUtil.createWithAction(this, intent, contentTitle, contentText, id);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnAgendar) {

            try {
//date.getText().toString() + " " + time.getText().toString());

                java.util.Calendar cal = null;
                cal.set(Integer.parseInt(date.getText().toString().substring(0, 2)),
                        Integer.parseInt(date.getText().toString().substring(3, 5)),
                        Integer.parseInt(date.getText().toString().substring(6, 8)),
                        Integer.parseInt(time.getText().toString().substring(0, 2)),
                        Integer.parseInt(time.getText().toString().substring(3, 5)));
                long millis = cal.getTimeInMillis();
                AlarmUtil.schedule(this, new Intent(this, MessageActivity.class), millis);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void scheduleNotification(Notification notification, long delay) {

        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, notificationID());
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, notificationID(), notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    private Notification getNotification(String title, String desc) {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setContentTitle(title);
        builder.setContentText(desc);
        builder.setSmallIcon(R.mipmap.ic_dog);
        return builder.build();
    }

    private void initFirebase() {
        FirebaseApp.initializeApp(this);
        mFirebaseDB = FirebaseDatabase.getInstance();
        mDBReference = mFirebaseDB.getReference();
        //sync the database
        mDBReference.keepSynced(true);
    }

    private void addNotification(Editable title, Editable desc, Long millis, Editable date, Editable time) {

            Agenda notific = new Agenda(randomUUID().toString(), title.toString(), desc.toString(), millis, date.toString(), time.toString());
            mDBReference.child(child + "/agenda").child(notific.getUid()).setValue(notific);
    }

    private int notificationID()
    {

        mDBReference.child(this.child + "/agenda").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    ID++;
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        return ID;
    }
}


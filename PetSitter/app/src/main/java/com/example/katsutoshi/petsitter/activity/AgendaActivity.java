package com.example.katsutoshi.petsitter.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.example.katsutoshi.petsitter.R;
import com.example.katsutoshi.petsitter.adapter.AgendaListViewAdapter;
import com.example.katsutoshi.petsitter.adapter.MedListViewAdapter;
import com.example.katsutoshi.petsitter.model.Agenda;
import com.example.katsutoshi.petsitter.model.Medication;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AgendaActivity extends AppCompatActivity {

    private ListView listAgenda;
    private List<Agenda> agenda = new ArrayList<Agenda>();
    String child;

    private FirebaseDatabase mFirebaseDB;
    private DatabaseReference mDBReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listAgenda = (ListView) findViewById(R.id.agendaList);

        Bundle bundle = getIntent().getExtras();
        child = bundle.getString("uid");

        initFirebase();

        if(bundle.getString("state").equals("agenda"))
        {
            listAgenda.setVisibility(View.INVISIBLE);

            mDBReference.child(this.child + "/agenda").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    agenda.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Agenda model = postSnapshot.getValue(Agenda.class);
                        if(compareDates(model.getDate(), model.getTime())) {
                            agenda.add(model);
                        }
                    }
                    AgendaListViewAdapter adapter = new AgendaListViewAdapter(AgendaActivity.this, agenda);
                    listAgenda.setAdapter(adapter);
                    listAgenda.setVisibility(View.VISIBLE);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        }
        else
        {
            listAgenda.setVisibility(View.INVISIBLE);

            mDBReference.child(this.child + "/agenda").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    agenda.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Agenda model = postSnapshot.getValue(Agenda.class);
                        if(compareDates(model.getDate(), model.getTime()) == false) {
                            agenda.add(model);
                        }
                    }
                    AgendaListViewAdapter adapter = new AgendaListViewAdapter(AgendaActivity.this, agenda);
                    listAgenda.setAdapter(adapter);
                    listAgenda.setVisibility(View.VISIBLE);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        }
        //agenda.add(new Agenda("uid","Veterinário","Higienização", "01/06/2017"));
        //agenda.add(new Agenda("uid","Limpeza de unhas","Limpar unhas e pelo", "30/05/2017"));

        AgendaListViewAdapter adapter = new AgendaListViewAdapter(AgendaActivity.this, agenda);
        listAgenda.setAdapter(adapter);
    }

    //returns true if the task stills on doing
    private boolean compareDates(String date, String time) {
        Calendar calendar = Calendar.getInstance();
        Long currentTime = calendar.getTimeInMillis();
        if(getDate(date, time) - currentTime >0)
        {
            return true;
        }
        return false;
    }

    private long getDate(String date, String time) {

        long millis;
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        int day = Integer.parseInt(date.substring(0, 2));
        cal.set(Calendar.DAY_OF_MONTH, day);
        int month = Integer.parseInt(date.substring(3, 5));
        cal.set(Calendar.MONTH, month);
        int year =  Integer.parseInt(date.substring(6, 10));
        cal.set(Calendar.YEAR, year);
        int hour = Integer.parseInt(time.substring(0, 2));
        cal.set(Calendar.HOUR_OF_DAY, hour);
        int minutes =  Integer.parseInt(time.substring(3, 5));
        //add 11 because of an error on minutes
        cal.set(Calendar.MINUTE, minutes);
        millis = cal.getTimeInMillis();
        return millis;
    }

    private void initFirebase() {
        FirebaseApp.initializeApp(this);
        mFirebaseDB = FirebaseDatabase.getInstance();
        mDBReference = mFirebaseDB.getReference();
        //sync the database
        mDBReference.keepSynced(true);
    }
}

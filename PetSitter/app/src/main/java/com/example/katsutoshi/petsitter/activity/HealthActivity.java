package com.example.katsutoshi.petsitter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.katsutoshi.petsitter.adapter.MedListViewAdapter;
import com.example.katsutoshi.petsitter.R;
import com.example.katsutoshi.petsitter.model.Medication;
import com.example.katsutoshi.petsitter.model.Pet;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.example.katsutoshi.petsitter.R;

import static java.util.UUID.*;

public class HealthActivity extends AppCompatActivity {

    private String child = "";
    private FirebaseDatabase mFirebaseDB;
    private DatabaseReference mDBReference;

    private ListView listVaccinations;
    private List<Medication> vaccinations = new ArrayList<Medication>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listVaccinations = (ListView) findViewById(R.id.healthList);

        Bundle bundle = getIntent().getExtras();
        child += bundle.getString("uid");

        initFirebase();
        addEventFirebaseListener();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              addVacAction();
            }
        });
    }

    private void addVacAction() {
        //btnAdd.setVisibility(View.INVISIBLE);
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(HealthActivity.this);
        View mView = LayoutInflater.from(HealthActivity.this).inflate(R.layout.dialog_new_vacination, null);
        final EditText mVac = (EditText) mView.findViewById(R.id.etVacine);
        final EditText mVacDesc = (EditText) mView.findViewById(R.id.etDescription);
        final EditText mVacDate = (EditText) mView.findViewById(R.id.etVacDate);
        final Button mbtnAdd = (Button) mView.findViewById(R.id.btnVacAdd);
        //View mView = getLayoutInflater().inflate(R.layout.dialog_new_pet, null);
        mBuilder.setView(mView);
        AlertDialog alert = mBuilder.create();
        alert.show();
        mbtnAdd.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           addVacine(mVac.getText(), mVacDesc.getText(), mVacDate.getText());
                                           //btnAdd.setVisibility(View.VISIBLE);
                                           mVac.setText("");
                                           mVacDesc.setText("");
                                           mVacDate.setText("");
                                       }
                                   }
        );

    }

    private void addVacine(Editable vac, Editable desc, Editable date) {

        if(validate(vac, desc, date)) {
            Medication vaccine = new Medication(randomUUID().toString(), vac.toString(), desc.toString(), date.toString());
            mDBReference.child(child + "/vaccines").child(vaccine.getUid()).setValue(vaccine);
            Toast.makeText(HealthActivity.this, "Vacina feita", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validate(Editable vac, Editable desc, Editable date) {
        //// TODO: 23/04/2017
        return true;
    }

    private void initFirebase() {
        FirebaseApp.initializeApp(this);
        mFirebaseDB = FirebaseDatabase.getInstance();
        mDBReference = mFirebaseDB.getReference();
        //sync the database
        mDBReference.keepSynced(true);
    }

    private void addEventFirebaseListener() {
        //circularProgressBar.setVisibility(View.VISIBLE);
        listVaccinations.setVisibility(View.INVISIBLE);

        mDBReference.child(child + "/vaccines").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                vaccinations.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Medication vac = postSnapshot.getValue(Medication.class);
                    vaccinations.add(vac);
                }
                MedListViewAdapter adapter = new MedListViewAdapter(HealthActivity.this, vaccinations);
                listVaccinations.setAdapter(adapter);
                listVaccinations.setVisibility(View.VISIBLE);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        listVaccinations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(HealthActivity.this, "click", Toast.LENGTH_SHORT).show();
            }
        });

        listVaccinations.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Medication lstVac = (Medication) parent.getItemAtPosition(position);
                Toast.makeText(HealthActivity.this, lstVac.getVetName() + " Long Click", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        //circularProgressBar.setVisibility(View.INVISIBLE);
    }

}

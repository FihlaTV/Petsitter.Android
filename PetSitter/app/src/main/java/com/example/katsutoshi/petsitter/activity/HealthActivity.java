package com.example.katsutoshi.petsitter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
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
    private String selectedPetName = "";
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
        child = bundle.getString("uid");
        selectedPetName = bundle.getString("petname");

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
        final AlertDialog alert = mBuilder.create();
        alert.show();
        mbtnAdd.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           if(addVacine(mVac.getText(), mVacDesc.getText(), mVacDate.getText()))
                                           {
                                               alert.dismiss();
                                           }
                                           //btnAdd.setVisibility(View.VISIBLE);
                                           else {
                                               mVac.setText("");
                                               mVacDesc.setText("");
                                               mVacDate.setText("");
                                           }
                                       }
                                   }
        );

    }

    private Boolean addVacine(Editable vac, Editable desc, Editable date) {

        if(validate(vac, desc, date)) {
            Medication vaccine = new Medication(randomUUID().toString(), vac.toString(), desc.toString(), date.toString());
            mDBReference.child(child + "/vaccines").child(vaccine.getUid()).setValue(vaccine);
            Toast.makeText(HealthActivity.this, "Medicado", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    private boolean validate(Editable vac, Editable desc, Editable date) {
        AlertDialog.Builder errorDialog = new AlertDialog.Builder(this);
        errorDialog.setTitle("Ops !");
        errorDialog.setCancelable(true);
        TextView msg = new TextView(this);

        try {
            //validate mandatory fields
            if (vac.toString().isEmpty()) {
                msg.setText("Medicamento é obrigatório.");
                errorDialog.setView(msg);
                AlertDialog alert = errorDialog.create();
                alert.show();
                return false;
            }
            return true;
        }
        catch (Exception ex)
        {
            msg.setText("Algo inesperado aconteceu. \nTente novamente.");
            errorDialog.setView(msg);
            AlertDialog alert = errorDialog.create();
            alert.show();
            return false;
        }
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

        mDBReference.child(this.child + "/vaccines").addValueEventListener(new ValueEventListener() {
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
                Medication selected = (Medication) parent.getItemAtPosition(position);
                Intent intent = new Intent(HealthActivity.this, CRUDHealthActivity.class);
                intent.putExtra("uid", (child + "/vaccines/" + selected.getUid()));
                intent.putExtra("name", (selected.getVetName()));
                intent.putExtra("desc", (selected.getDescription()));
                intent.putExtra("date", (selected.getConsultDate()));
                intent.putExtra("petname", selectedPetName);
                startActivity(intent);
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

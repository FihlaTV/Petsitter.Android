package com.example.katsutoshi.petsitter;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private ListView listPets;
    private ProgressBar circularProgressBar;

    private FirebaseDatabase mFirebaseDB;
    private DatabaseReference mDBReference;

    private Pet selectedPet;

    private List<Pet> pets = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //add Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.petToolbar);
        setSupportActionBar(toolbar);

        //Control
        final FloatingActionButton btnAdd = (FloatingActionButton) findViewById(R.id.btnAdd);
        circularProgressBar = (ProgressBar) findViewById(R.id.circularProgressBar);
        listPets = (ListView) findViewById(R.id.listPets);

        //Firebase
        initFirebase();
        addEventFirebaseListener();

        //New Pet Dialog
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //btnAdd.setVisibility(View.INVISIBLE);
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                View mView = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_new_pet, null);
                //View mView = getLayoutInflater().inflate(R.layout.dialog_new_pet, null);
                final EditText mPetName = (EditText) mView.findViewById(R.id.etPetName);
                final EditText mPetBirth = (EditText) mView.findViewById(R.id.etPetBirth);
                final EditText mPetWeight = (EditText) mView.findViewById(R.id.etPetWeight);
                Button mbtnAdd = (Button) mView.findViewById(R.id.btnPetAdd);
                mBuilder.setView(mView);
                AlertDialog alert = mBuilder.create();
                alert.show();
                mbtnAdd.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View view) {
                                                   createPet(mPetName.getText(), mPetBirth.getText(), mPetWeight.getText());
                                                   Toast.makeText(MainActivity.this, mPetName.getText() + " cadastrado", Toast.LENGTH_LONG).show();
                                                   //btnAdd.setVisibility(View.VISIBLE);
                                               }
                                           }
                );
            }
        });

    }

    private void addEventFirebaseListener() {
        circularProgressBar.setVisibility(View.VISIBLE);
        listPets.setVisibility(View.INVISIBLE);

        mDBReference.child("pets").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pets.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Pet pet = postSnapshot.getValue(Pet.class);
                    pets.add(pet);
                }
                ListViewAdapter adapter = new ListViewAdapter(MainActivity.this, pets);
                listPets.setAdapter(adapter);
                circularProgressBar.setVisibility(View.INVISIBLE);
                listPets.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void initFirebase() {
        FirebaseApp.initializeApp(this);
        mFirebaseDB = FirebaseDatabase.getInstance();
        mDBReference = mFirebaseDB.getReference();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    /*
     @Override
     public boolean onOptionsItemSelected(MenuItem item)
     {
        if(item.getItemId() == R.id.btnPetAdd)
        {
            //createPet();
        }
     }
     */

    private void createPet(Editable name, Editable birth, Editable weight) {

        Pet pet = new Pet(UUID.randomUUID().toString(), name.toString(), Double.parseDouble(weight.toString()), birth.toString());
        mDBReference.child("pets").child(pet.getUid()).setValue(pet);

    }
}

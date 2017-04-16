package com.example.katsutoshi.petsitter.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.katsutoshi.petsitter.adapter.ListViewAdapter;
import com.example.katsutoshi.petsitter.R;
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
                final EditText mPetName = (EditText) mView.findViewById(R.id.etPetName);
                final EditText mPetBirth = (EditText) mView.findViewById(R.id.etPetBirth);
                final EditText mPetWeight = (EditText) mView.findViewById(R.id.etPetWeight);
                final Button mbtnAdd = (Button) mView.findViewById(R.id.btnPetAdd);
                //View mView = getLayoutInflater().inflate(R.layout.dialog_new_pet, null);
                mBuilder.setView(mView);
                AlertDialog alert = mBuilder.create();
                alert.show();
                mbtnAdd.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View view) {
                                                   createPet(mPetName.getText(), mPetBirth.getText(), mPetWeight.getText());
                                                   //btnAdd.setVisibility(View.VISIBLE);
                                               }
                                           }
                );
                mPetBirth.setText("");
                mPetName.setText("");
                mPetWeight.setText("");
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
            public void onCancelled(DatabaseError databaseError) {}
        });

        listPets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Pet lstPet = (Pet)parent.getItemAtPosition(position);
                Toast.makeText(MainActivity.this, lstPet.getName() + "Click", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initFirebase() {
        FirebaseApp.initializeApp(this);
        mFirebaseDB = FirebaseDatabase.getInstance();
        mDBReference = mFirebaseDB.getReference();
        //sync the database
        mDBReference.keepSynced(true);
    }

    private android.widget.AdapterView.OnItemClickListener CallItemFunction()
    {
        return (new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listPets.getSelectedItem();
            }
        });
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

        if(validate(name, birth, weight)) {
            Pet pet = new Pet(UUID.randomUUID().toString(), name.toString(), Double.parseDouble(weight.toString()), birth.toString());
            mDBReference.child("pets").child(pet.getUid()).setValue(pet);
            Toast.makeText(MainActivity.this, name.toString() + " cadastrado", Toast.LENGTH_LONG).show();
        }
    }

    private boolean validate(Editable name, Editable birth, Editable weight)
    {
        AlertDialog.Builder errorDialog = new AlertDialog.Builder(this);
        errorDialog.setTitle("Ops !");
        errorDialog.setCancelable(true);
        TextView msg = new TextView(this);

        try {
            //validate mandatory fields
            if (name.toString().isEmpty()) {
                msg.setText("Nome é obrigatório.");
                errorDialog.setView(msg);
                AlertDialog alert = errorDialog.create();
                alert.show();
                return false;
            }
            if (weight.toString().isEmpty()) {
                msg.setText("Peso é obrigatório.");
                errorDialog.setView(msg);
                AlertDialog alert = errorDialog.create();
                alert.show();
                return false;
            }
            if (birth.toString().isEmpty()) {
                msg.setText("Data de nascimento é obrigatório.");
                errorDialog.setView(msg);
                AlertDialog alert = errorDialog.create();
                alert.show();
                return false;
            }
            //validate data types 
            //Verify max weight
            if (Integer.parseInt(weight.toString()) <= 0 || Integer.parseInt(weight.toString()) > 240) {
                msg.setText("Peso inválido.");
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
}

package com.example.katsutoshi.petsitter.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

import com.example.katsutoshi.petsitter.adapter.PetListViewAdapter;
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

    View mView = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_new_pet, null);
    final EditText mPetName = (EditText) mView.findViewById(R.id.etPetName);
    final EditText mPetBirth = (EditText) mView.findViewById(R.id.etPetBirth);
    final EditText mPetWeight = (EditText) mView.findViewById(R.id.etPetWeight);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //add Toolbar
        //Toolbar toolbar = (Toolbar) findViewById(R.id.petToolbar);
        //setSupportActionBar(toolbar);

        //Control
        final FloatingActionButton btnAdd = (FloatingActionButton) findViewById(R.id.fab);
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
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                final Button mbtnAdd = (Button) mView.findViewById(R.id.btnPetAdd);
                //View mView = getLayoutInflater().inflate(R.layout.dialog_new_pet, null);
                mBuilder.setView(mView);
                final AlertDialog alert = mBuilder.create();
                alert.show();
                mbtnAdd.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View view) {
                                                   btnAdd.setVisibility(View.VISIBLE);
                                                   if(createPet(mPetName.getText(), mPetBirth.getText(), mPetWeight.getText()))
                                                   {
                                                       alert.hide();
                                                   }
                                                   else {
                                                       mPetName.setText("");
                                                       mPetBirth.setText("");
                                                       mPetWeight.setText("");
                                                   }
                                               }
                                           }
                );
            }
        });
   }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    //add items in the list by retrieving from firebase database
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
                PetListViewAdapter adapter = new PetListViewAdapter(MainActivity.this, pets);
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

    private boolean createPet(Editable name, Editable birth, Editable weight) {

        if(validate(name, birth, weight)) {
            Pet pet = new Pet(UUID.randomUUID().toString(), name.toString(), Double.parseDouble(weight.toString()), birth.toString());
            mDBReference.child("pets").child(pet.getUid()).setValue(pet);
            Toast.makeText(MainActivity.this, name.toString() + " cadastrado", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
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

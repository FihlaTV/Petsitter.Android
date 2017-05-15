package com.example.katsutoshi.petsitter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.view.LayoutInflater;
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

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{

    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mToggle;

    private String child = "pets/dogs";
    private ListView listPets;
    private ProgressBar circularProgressBar;

    private FirebaseDatabase mFirebaseDB;
    private DatabaseReference mDBReference;
    private FloatingActionButton btnAdd;
    public static Pet selectedPet = new Pet();

    private List<Pet> pets = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        circularProgressBar = (ProgressBar) findViewById(R.id.circularProgressBar);
        listPets = (ListView) findViewById(R.id.listPets);

        //Firebase
        initFirebase();
        addEventFirebaseListener();

        btnAdd = (FloatingActionButton) findViewById(R.id.fab);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddPetAction();
            }
        });
        //btnAdd.setVisibility(View.INVISIBLE);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        mDrawer.addDrawerListener(mToggle);
        mToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    private void addEventFirebaseListener() {
        circularProgressBar.setVisibility(View.VISIBLE);
        listPets.setVisibility(View.INVISIBLE);

        mDBReference.child(this.child).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pets.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Pet pet = postSnapshot.getValue(Pet.class);
                    pets.add(pet);
                }
                PetListViewAdapter adapter = new PetListViewAdapter(HomeActivity.this, pets);
                listPets.setAdapter(adapter);
                listPets.setVisibility(View.VISIBLE);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        listPets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Pet lstPet = (Pet)parent.getItemAtPosition(position);
                Intent intent = new Intent(HomeActivity.this, PetMenuActivity.class);
                intent.putExtra("uid", (child + "/" + lstPet.getUid()));
                startActivity(intent);
            }
        });

        listPets.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Pet lstPet = (Pet)parent.getItemAtPosition(position);
                Toast.makeText(HomeActivity.this, lstPet.getName() + " Long Click", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        circularProgressBar.setVisibility(View.INVISIBLE);
    }

    private void initFirebase() {
        FirebaseApp.initializeApp(this);
        mFirebaseDB = FirebaseDatabase.getInstance();
        mDBReference = mFirebaseDB.getReference();
        //sync the database
        mDBReference.keepSynced(true);
    }

    private void AddPetAction() {
        //btnAdd.setVisibility(View.INVISIBLE);
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(HomeActivity.this);
        View mView = LayoutInflater.from(HomeActivity.this).inflate(R.layout.dialog_new_pet, null);
        final EditText mPetName = (EditText) mView.findViewById(R.id.etPetName);
        final EditText mPetBirth = (EditText) mView.findViewById(R.id.etPetBirth);
        final EditText mPetWeight = (EditText) mView.findViewById(R.id.etPetWeight);
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
                                               alert.dismiss();
                                           }
                                           else {
                                               mPetName.setText("");
                                               mPetBirth.setText("");
                                               mPetWeight.setText("");
                                           }
                                       }
                                   }
        );
        mPetBirth.setText("");
        mPetName.setText("");
        mPetWeight.setText("");
    }

    private boolean createPet(Editable name, Editable birth, Editable weight) {

        if(validate(name, birth, weight)) {
            Pet pet = new Pet(UUID.randomUUID().toString(), name.toString(), Double.parseDouble(weight.toString()), birth.toString());
            mDBReference.child(child).child(pet.getUid()).setValue(pet);
            Toast.makeText(HomeActivity.this, name.toString() + " cadastrado", Toast.LENGTH_LONG).show();
            return true;
        }
            return false;
    }

    private boolean validate(Editable name, Editable birth, Editable weight) {
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id)
        {
            case R.id.nav_dog:
                child = "pets/dogs";
                btnAdd.setVisibility(View.VISIBLE);
                addEventFirebaseListener();
                break;

            case R.id.nav_cat:
                child = "pets/cats";
                btnAdd.setVisibility(View.VISIBLE);
                addEventFirebaseListener();
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }
}

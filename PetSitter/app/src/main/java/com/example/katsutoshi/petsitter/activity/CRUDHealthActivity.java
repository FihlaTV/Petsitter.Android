package com.example.katsutoshi.petsitter.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.katsutoshi.petsitter.R;
import com.example.katsutoshi.petsitter.model.Medication;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static java.util.UUID.randomUUID;

public class CRUDHealthActivity extends AppCompatActivity {

    private TextView selectedPetName;
    private EditText name, desc, date;
    private String child;

    private FirebaseDatabase mFirebaseDB;
    private DatabaseReference mDBReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crudhealth);

        name = (EditText)findViewById(R.id.txtMedNameEdit);
        desc = (EditText)findViewById(R.id.txtMedDescEdit);
        date = (EditText)findViewById(R.id.txtMedDateEdit);
        selectedPetName = (TextView)findViewById(R.id.pageTitle);

        Bundle bundle = getIntent().getExtras();
        child = bundle.getString("uid");
        name.setText(bundle.getString("name"));
        desc.setText(bundle.getString("weight"));
        date.setText(bundle.getString("birth"));
        selectedPetName.setText(bundle.getString("petname"));

        Toast.makeText(this, bundle.getString("petname") , Toast.LENGTH_LONG).show();

        initFirebase();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_health, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_save) {
            editVacine(name.getText(), desc.getText(), date.getText());
            return true;
        }
        if (id == R.id.menu_delete)
        {
            AlertDialog al = new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Alerta")
                    .setMessage("Você tem certeza que quer deletar ?")
                    .setPositiveButton("Sim", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteVacine();
                        }

                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteVacine() {
        mDBReference.child(child).removeValue();
        finish();
    }

    private void editVacine(Editable vac, Editable desc, Editable date) {

        if(validate(vac, desc, date)) {
            Medication vaccine = new Medication(randomUUID().toString(), vac.toString(), desc.toString(), date.toString());
            mDBReference.child(child).child("vetName").setValue(vac.toString());
            mDBReference.child(child).child("description").setValue(desc.toString());
            mDBReference.child(child).child("consultDate").setValue(date.toString());
            Toast.makeText(CRUDHealthActivity.this, "Atualizado com sucesso", Toast.LENGTH_SHORT).show();
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
}

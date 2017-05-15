package com.example.katsutoshi.petsitter.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;

import com.example.katsutoshi.petsitter.R;
import com.example.katsutoshi.petsitter.adapter.PetMenuViewAdapter;

public class PetMenuActivity extends AppCompatActivity {

    private String child = "";
    private Intent intent;
    private ListView listOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_menu);

        Toolbar toolbar = (Toolbar) findViewById(R.id.menuToolbar);
        //setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        child += bundle.getString("uid");

        listOptions = (ListView) findViewById(R.id.listPetMenu);

        final PetMenuViewAdapter adapter = new PetMenuViewAdapter(PetMenuActivity.this);
        listOptions.setAdapter(adapter);

        listOptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position)
                {
                    case 0:
                        intent = new Intent(PetMenuActivity.this, HealthActivity.class);
                        intent.putExtra("uid", child);
                        startActivity(intent);
                        break;
                    case 1:

                        break;
                    case 2:
                        intent = new Intent(PetMenuActivity.this, NotificationActivity.class);
                        intent.putExtra("uid", child);
                        startActivity(intent);
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                }
            }
        });

    }
}

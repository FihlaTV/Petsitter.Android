package com.example.katsutoshi.petsitter.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.katsutoshi.petsitter.R;

public class MessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String msg = getIntent().getStringExtra("msg");
        TextView text = (TextView)findViewById(R.id.textMessage);
        text.setText(msg);
    }
}

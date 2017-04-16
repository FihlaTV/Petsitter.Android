package com.example.katsutoshi.petsitter;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Katsutoshi on 08/04/2017.
 */

public class PetSitter extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //enable persistence offline on FireBase
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}

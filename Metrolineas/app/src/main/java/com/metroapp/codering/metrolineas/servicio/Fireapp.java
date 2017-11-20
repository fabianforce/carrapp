package com.metroapp.codering.metrolineas.servicio;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by linuxforce on 10/8/17.
 */

public class Fireapp extends Application{


    @Override
    public void onCreate() {
        super.onCreate();



        if(!FirebaseApp.getApps(this).isEmpty()) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }

    }
}

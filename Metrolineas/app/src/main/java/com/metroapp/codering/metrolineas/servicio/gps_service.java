package com.metroapp.codering.metrolineas.servicio;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.audiofx.BassBoost;
import android.net.sip.SipAudioCall;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.metroapp.codering.metrolineas.Ma_mapa;
import com.metroapp.codering.metrolineas.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by linuxforce on 10/4/17.
 */

public class gps_service extends Service {


    private LocationListener listener; // ESTARA ESCUCHANDA LAS ACTUALIZACIONES DE POSICIONES LATITUD Y LONGITUD CADA VES QUE EL DISPOSIVVO ACTUALIZA SE ACTUALIZA EL locationlistener
    private LocationManager locationManager;
    FirebaseDatabase database;
    DatabaseReference myref_bd,bd_ref_user;
	SharedPreferences preferences;
	SharedPreferences preferences_pasadas;

	Map<String,Object> item = new HashMap<>();
	private FirebaseAuth auth_1;
	private FirebaseUser user_1;
	protected static final String TAG = ActualizandoPosiciones.class.getSimpleName();


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {



        return null;
    }

    @Override
    public void onCreate() {
		super.onCreate();

        database = FirebaseDatabase.getInstance();
        myref_bd = database.getReference();
		auth_1 = FirebaseAuth.getInstance();
		user_1 = auth_1.getCurrentUser();
        bd_ref_user = FirebaseDatabase.getInstance().getReference().child("usuarios").child(user_1.getUid());

	 preferences = getSharedPreferences("campos_texto", 0);
		preferences_pasadas = getSharedPreferences("miprefrenecia" , 0 );
		final String item1 = preferences_pasadas.getString("STEP1" ,"null");
		String item2 = preferences_pasadas.getString("STEP2" , "null");
		String item3 = preferences_pasadas.getString("STEP3" ,"null");
		String item4 = preferences_pasadas.getString("STEP4" , "null");



String rutas [] = { item1 , item2 , item3 , item4};
final List mis_rutas = new ArrayList<String>(Arrays.asList(rutas));


		bd_ref_user.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {




				item.put("usuario" , dataSnapshot.child("nombre").getValue());





			}

			@Override
			public void onCancelled(DatabaseError databaseError) {

				Log.e("HPTA" , databaseError.toString());

			}
		});




		listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {


				//String name = sharedPref.getString("fabito","");



                String salida = preferences.getString("txt_direccion","");
                String llegada = preferences.getString("txt_llegada" , "");
String hsalidaa = preferences.getString("txt_hora","");

                Intent i = new Intent("location_UPDATE");
                i.putExtra("longitud" , location.getLongitude());
                i.putExtra("latitud" , location.getLatitude());
                sendBroadcast(i);


				item.put("latitude", location.getLatitude());
				item.put("longitude", location.getLongitude());
				item.put("created", ServerValue.TIMESTAMP);
				item.put("salida" ,salida);
				item.put("llegada",llegada);
				item.put("hsalida" , hsalidaa );



				myref_bd.child("Conductores").child(user_1.getUid()).updateChildren(item);
                  myref_bd.child("Conductores").child(user_1.getUid()).child("rutas").setValue(mis_rutas);
/*
				if (rutas.equals("T3"))
				{




				}else if (rutas.equals("P8"))
				{

					myref_bd.child("transportes").child(rutas).child(user_1.getUid()).updateChildren(item);

					myref_bd.child("transportes").child("T3").child(user_1.getUid()).removeValue();


				}
				*/


            }

            @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);



            }
        };


        locationManager = (LocationManager)getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        //noinspection MissingPermission
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,3000,0 ,listener);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(listener);
    }
}

package com.metroapp.codering.metrolineas.servicio;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import com.google.android.gms.location.LocationListener;

import com.google.firebase.auth.FirebaseUser;
import com.metroapp.codering.metrolineas.R;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.metroapp.codering.metrolineas.modelo.Bus;

import java.util.Map;
import java.util.HashMap;

/**
 * Created by linuxforce on 9/22/17.
 */

public class ActualizandoPosiciones extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private NotificationManager mNM;
    /** todo lo que saba lo muestra en notificaiones cuando la APP esta en background  */
// Unique Identification Number for the Notification.
    // We use it on Notification start, and to cancel it.
    private int NOTIFICATION = 5606;
    private LocationManager locationManager;
    protected GoogleApiClient mGoogle_apicliente;
    protected static final String TAG = ActualizandoPosiciones.class.getSimpleName();
    FirebaseDatabase database;
    private FirebaseAuth auth_1;
    private FirebaseUser user_1;

    DatabaseReference myref_bd;
    private static Location ultima_localizacion;
Bus bs = new Bus();
    double fabian;

    protected LocationRequest mLocationRequest;
    public static final long UPDATE_INTERVAL_IN_MILISEGUNDOS = 10000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILISECONDS = UPDATE_INTERVAL_IN_MILISEGUNDOS / 2;
    FirebaseAuth.AuthStateListener mAtuhlistener;
    public static final float MAXIMA_DISTANCIS_EN_METROS = 30f;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCreate() {
        super.onCreate();
        String Select_route = PreferenceManager.getDefaultSharedPreferences(this).getString("SELECT_RUTAS", null);
        Map<String, Object> item2 = new HashMap<>();
        database = FirebaseDatabase.getInstance();
        myref_bd = database.getReference();
        auth_1 = FirebaseAuth.getInstance();
        user_1 = auth_1.getCurrentUser();
        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        item2.put("active", true);
       /* if (user_1 != null)
        {


        }*/
        myref_bd.child("transportes").child(Select_route).updateChildren(item2);
//showNotification();



            MostrarNotificaciones();


           BuildGoogleApiCliente();



        mAtuhlistener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user == null)
                {

                    ActualizandoPosiciones.this.stopLocationUpdates();
ActualizandoPosiciones.this.stopSelf();
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ActualizandoPosiciones.this);
                    sharedPreferences.edit().putBoolean("UPDATE_ENVIO_POSICION" , false);

                Log.i(TAG,"user logged in");
                }



            }
        };

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        Log.i("", "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }





    public void onConnected(@Nullable Bundle bundle) {
        Log.i("mensaje", "Connected to GoogleApiClient");
        //to start to make request
        //  Tag startLocationUpdates();
        starLocationUpdates();
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**callback that fires when the location change*/
    @Override
    public void onLocationChanged(Location location) {
// Location ultima_localizacion
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String selectRoute = sharedPref.getString("SELECT_RUTAS", null);

        if (ultima_localizacion != null) {

            //chekeaa si la posicion se cambio(posicion del bus) si no no hacer nada

            if (ultima_localizacion.distanceTo(location) < MAXIMA_DISTANCIS_EN_METROS)
                return;


        }


        Map<String, Object> item = new HashMap<>();
        item.put("latitude", location.getLatitude());
        item.put("longitude", location.getLongitude());
        item.put("created", ServerValue.TIMESTAMP); //ServerVlue clase de firebase

//actualizando la nueva localización en firebase

        //myref_bd.child("transportes").child(selectRoute).child(user_1.getUid()).updateChildren(item);
        myref_bd.child("transportes").child(selectRoute).updateChildren(item);
        ultima_localizacion = location;

        /*
               fabian =location.getLongitude();
                bs.setLatitud(location.getLatitude()); */

        Log.i(TAG, "onLocationChanged laitude" + location.getLatitude() + ",Longitude" + location.getLongitude());

    }

    @Override
    public void onConnectionSuspended(int i) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.


        Log.i(TAG, "Conexión Suspendida");
        mGoogle_apicliente.connect();

    }

    protected void starLocationUpdates() {

        FirebaseAuth.getInstance().addAuthStateListener(mAtuhlistener);

        //noinspection MissingPermission
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogle_apicliente, mLocationRequest, this);




    }


    protected void createdLocationRequest()
    {
mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.

mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILISEGUNDOS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }

    /**
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the
     * LocationServices API.
     */

    protected synchronized void BuildGoogleApiCliente()
    {

        Log.i(TAG,"Building GoogleApicliente");
        mGoogle_apicliente = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();

createdLocationRequest();
        mGoogle_apicliente.connect();

    }






    /**
     * Removes location updates from the FusedLocationApi.
     */
    protected void stopLocationUpdates() {
        if(mGoogle_apicliente != null)
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogle_apicliente,  this);



        }else
        {


        }



        //noinspection ResourceType

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(ActualizandoPosiciones.this);
  String select_ruta = sharedPref.getString("SELECT_RUTAS",null);
        Map<String,Object> item2 = new HashMap<>();
        item2.put("active",false);
        mNM.cancel(NOTIFICATION);

myref_bd.child("transportes").child(select_ruta).updateChildren(item2);

        FirebaseAuth.getInstance().removeAuthStateListener(mAtuhlistener);
stopLocationUpdates();
        sharedPref.edit().putBoolean("UPDATE_ENVIO_POSICION",false).apply();

        Log.i(TAG, "onDestroy");

    }


    /**
     * Show a notification while this service is running.
     */

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void MostrarNotificaciones()
    {

        /*
        CharSequence text = getText(R.string.not_summary_on_position_sender);

        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,new Intent(this , Ma_ajustes.class),0);

        Notification notificacion = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)  // the status icon
                .setTicker(text)  // the status text
                .setWhen(System.currentTimeMillis())  // the time stamp
                .setContentTitle("holam undo ")  // the label of the entry
                .setContentText(user_1.getEmail())  // the contents of the entry
                .setContentIntent(contentIntent)  // The intent to send when the entry is clicked
                .setOngoing(false)
                .build();


        notificacion.flags |= Notification.FLAG_NO_CLEAR;
// Send the notification.
        mNM.notify(NOTIFICATION,notificacion);

*/

    }

}

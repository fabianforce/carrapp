package com.metroapp.codering.metrolineas.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.firebase.auth.FirebaseAuth;
import com.metroapp.codering.metrolineas.servicio.ActualizandoPosiciones;

/**
 * Created by linuxforce on 10/12/17.
 */

public class LocationTrackerRestartBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		FirebaseAuth auth = FirebaseAuth.getInstance();
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

		if( auth.getCurrentUser() != null)
		{
			sharedPref.edit().putBoolean("UPDATE_ENVIO_POSICION",true).apply();
			context.startService(new Intent(context , ActualizandoPosiciones.class));


		}else
		{
			sharedPref.edit().putBoolean("UPDATE_ENVIO_POSICION",false).apply();


		}

	}
}

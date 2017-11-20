package com.metroapp.codering.metrolineas;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.metroapp.codering.metrolineas.servicio.gps_service;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnSuccessListener;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import android.support.v4.app.*;

public class Ma_mapa extends AppCompatActivity implements View.OnClickListener {


	private static final String TAG = Ma_mapa.class.getName();
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

	private FirebaseAuth auth_1;
	private FirebaseUser user_1;

	FirebaseDatabase database;
	DatabaseReference myref_bd,myref_array;
	private LocationRequest mloLocationRequest;
	private long UPDATE_INTERVAL = 10 * 1000; //10 secs
	private long FASTEST_INTERVAL = 2000; // 2 SECS
	private TextView txt_latiud;
	private TextView txt_longitud;

	private BroadcastReceiver broadcastReceiver;
	//private TextView visualizar;
	SharedPreferences preferences;

	ImageButton btn_horaa, btn_direcion;

	private int hora, minuto, segundo;
	private SwitchCompat sw_posiciones;
	String am_pm = "";

	// ** ***** **//

	// ** PREFERENCES **//
	private SharedPreferences mPreferences;
	private SharedPreferences.Editor editor;


	// ** ***** **//

	//**FLOATING BUTTON **//
	FloatingActionMenu btn_float_menu;
	FloatingActionButton floatingActionButton1, floatingActionButton2;
	// ** ***** **//

	private TextView ver_hora;
	private EditText txt_dir,txt_llego;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ma_mapa);

		auth_1 = FirebaseAuth.getInstance();
		user_1 = auth_1.getCurrentUser();
		isntanciando();


		preferences = getSharedPreferences("miprefrenecia", 0);
		String item1 = preferences.getString("STEP1", "null");
		String item2 = preferences.getString("STEP2", "null");
		String item3 = preferences.getString("STEP3", "null");
		String item4 = preferences.getString("STEP4", "null");


		String [] rutas = {item1 , item2 , item3, item4};



		//	visualizar.setText(item1 + item2 + item3 + item4);
		Context cont = this.getApplicationContext();
		SharedPreferences mPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
		editor = mPreferences.edit(); //deberia estar despues de los commit ¿?

		if (!runtime_permissions()) {
			enablebuttons();

		}
/*
        myref_bd.child("Conductores").addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {

				for (DataSnapshot snapshot : dataSnapshot.getChildren())
				{



						visualizar.setText(snapshot.child("latitude").getValue().toString());





				}

			}

			@Override
			public void onCancelled(DatabaseError databaseError) {

			}
		});
		*/

/*

        rgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
	     @Override
	      public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {

		int checkedindex = radioGroup.indexOfChild(rgroup);

			 SharedPreferences smShared = getSharedPreferences("metrolienas" , MODE_PRIVATE);
SharedPreferences.Editor edit = smShared.edit();
			 edit.putInt("Check" , checkedindex);

			 edit.commit();



	}
});
*/

	}

	private void isntanciando() {

		txt_dir = (EditText)findViewById(R.id.Txt_salida);
		txt_llego = (EditText)findViewById(R.id.txt_llegada);



		ver_hora = (TextView) findViewById(R.id.mostar_hora);
		btn_horaa = (ImageButton) findViewById(R.id.btn_hora);
		btn_direcion = (ImageButton) findViewById(R.id.btn_posicion);
		btn_horaa.setOnClickListener(this);
		database = FirebaseDatabase.getInstance();
		myref_bd  = FirebaseDatabase.getInstance().getReference().child("Conductores");

		myref_array = FirebaseDatabase.getInstance().getReference().child("conductores").child(user_1.getUid()).child("rutas");


		//	visualizar = (TextView)  findViewById(R.id.lab_mostrar);
		txt_longitud = (TextView) findViewById(R.id.longitude);
		txt_latiud = (TextView) findViewById(R.id.latitude);



		//GEOCODER


//mydireccion= geocoder.getFromLocation(1.0,1,1);
//mydireccion.get(0).getAdminArea();
/*


		myref_bd.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				//GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {};
				GenericTypeIndicator<Map<Integer, String>> t = new GenericTypeIndicator<Map<Integer, String>>() {};

Map<Integer , String> map  =dataSnapshot.getValue(t);

	txt_dir.setText(map.get(1));





			}

			@Override
			public void onCancelled(DatabaseError databaseError) {

			}
		});


*/



		btn_direcion.setOnClickListener(new View.OnClickListener() {

			@Override


			public void onClick(View view) {
				FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(Ma_mapa.this);
				if (ActivityCompat.checkSelfPermission(Ma_mapa.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Ma_mapa.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
					// TODO: Consider calling
					//    ActivityCompat#requestPermissions
					// here to request the missing permissions, and then overriding
					//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
					//                                          int[] grantResults)
					// to handle the case where the user grants the permission. See the documentation
					// for ActivityCompat#requestPermissions for more details.
					return;
				}
				client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
					@Override
					public void onSuccess(Location location) {


					getAddress(location.getLatitude() , location.getLongitude());

					}
				});

			}
		});

		sw_posiciones = (SwitchCompat)findViewById(R.id.my_switch1);
		SharedPreferences sharedPrefs = getSharedPreferences("estado_sw", 0);
		boolean sl = sharedPrefs.getBoolean("sw_estadoAC" ,false);
		sw_posiciones.setChecked(sl); //sirve pero siempre q inicio la actividd esta el sw on

		btn_float_menu = (FloatingActionMenu)findViewById(R.id.btn_floating_action_menu);
		floatingActionButton1 = (FloatingActionButton) findViewById(R.id.floating_action_menu_item1);
		floatingActionButton1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				actividad_pasadas();
			}
		});





		}


		public void getsincornizacion(DatabaseReference ref)
		{

		}

		public void verificar_todo_completo(String salida , String llegada ,String hora)
		{


			if (!salida.equals("") && !llegada.equals(""))
			{


				SharedPreferences st1 = getSharedPreferences("campos_texto" , 0);
				SharedPreferences.Editor editor11 = st1.edit();
				editor11.putString("txt_direccion" , salida);
				editor11.putString("txt_llegada" , llegada);
				editor11.putString("txt_hora" , hora );

				editor11.commit();



			}




		}




		public Address getAddress(double latitud , double longitud)
		{
			Geocoder geocoder;
			List<Address> mydireccion;
			geocoder = new Geocoder(this , Locale.getDefault());

			try {
				mydireccion= geocoder.getFromLocation(latitud, longitud ,1 );
				txt_dir.setText(mydireccion.get(0).getAddressLine(0));
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;

		}

	@Override
	public void onClick(View view) {

		final Calendar c= Calendar.getInstance();
		hora=c.get(Calendar.HOUR_OF_DAY);
		minuto=c.get(Calendar.MINUTE);

		//segundo = c.get(Calendar.HOUR_OF_DAY);

		if (c.get(Calendar.AM_PM) == Calendar.AM)

			am_pm = "AM";


		else if (c.get(Calendar.AM_PM) == Calendar.PM){

			am_pm = "PM";


		}





		TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

			@Override

			public void onTimeSet(TimePicker view, int hourOfDay, int minute ) {

String formato12 = "";

				if(hourOfDay > 12) {
					hourOfDay -= 12;
					am_pm = "pm";
				} else if (hourOfDay == 0) {

					hourOfDay +=12;
					am_pm = "am";
				}else if (hourOfDay == 12)
				{
					am_pm="pm";


				}else
				{
					am_pm = "am";


				}

				ver_hora.setText(hourOfDay+":"+minute + am_pm);

			}
		},hora,minuto,false); //estaba en false


		timePickerDialog.show();

	}




	private void actividad_pasadas()
	{
		startActivity(new Intent(this, Ma_mispasadas.class));

	}


    private void enablebuttons() {

/*
        btn_1.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),gps_service.class);
                startService(i);
            }
        });
        btn_2.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(),gps_service.class);
                stopService(i);

            }
        });
        */


        sw_posiciones.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// do something, the isChecked will be
				// true if the switch is in the On position

				if(isChecked)
				{

					Intent i = new Intent(getApplicationContext(),gps_service.class);
					startService(i);
					verificar_todo_completo(txt_dir.getText().toString().trim() , txt_llego.getText().toString().trim() ,  ver_hora.getText().toString());



				} else
				{


					Intent i = new Intent(getApplicationContext(),gps_service.class);
					stopService(i);
					myref_bd.child(user_1.getUid()).removeValue();




				}
				SharedPreferences st = getSharedPreferences("estado_sw" , 0);
				SharedPreferences.Editor editor1 = st.edit();
				editor1.putBoolean("sw_estadoAC" , isChecked);
				editor1.commit();

			}
		});




    }

    private boolean runtime_permissions() {

        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},100);

            return true;
        }
        return false;



    }




    //PARA BROADCASTRECEIVER
    @Override
    protected void onResume() {
        super.onResume();
        if(broadcastReceiver == null)
        {

broadcastReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {


    txt_longitud.setText(intent.getExtras().get("longitud").toString());
    txt_latiud.setText(intent.getExtras().get("latitud").toString());
    }
};
 }
 registerReceiver(broadcastReceiver , new IntentFilter("location_UPDATE"));
 }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null)
        {
            unregisterReceiver(broadcastReceiver);



        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    if (requestCode == 100)
    {
        if( grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){


            enablebuttons();


        }else {
            runtime_permissions();
        }


    }

    }


    /**   GoogleApiClient.ConnectionCallbacks ,GoogleApiClient.OnConnectionFailedListener , LocationListener
    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    /**********/
}

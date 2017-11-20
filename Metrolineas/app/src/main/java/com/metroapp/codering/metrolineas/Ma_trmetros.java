package com.metroapp.codering.metrolineas;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.internal.zzp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.metroapp.codering.metrolineas.modelo.Conductores;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ma_trmetros extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener , GoogleMap.OnMarkerClickListener{
	SupportMapFragment mapFragment;
	private GoogleMap maps;
	private boolean ismapready;
	MarkerOptions mCurrLocationMarker;
	DatabaseReference myRef;
	private ListView milista;
	ArrayAdapter<String> myadaptador,customAdapter;
	private ArrayList<String> datos = new ArrayList<>();

	private HashMap<Marker, Conductores> mMarkersHashMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ma_trmetros);
		mMarkersHashMap = new HashMap<Marker, Conductores>();
		mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_treal);
		mapFragment.getMapAsync(this);
		myRef = FirebaseDatabase.getInstance().getReference().child("Conductores");


		milista = (ListView)findViewById(R.id.my_lista);
		//myadaptador = new ArrayAdapter<String>(this , android.R.layout.simple_list_item_1 , datos);




	 customAdapter = new myadaptador1(this, datos);

	//ArrayList<String> datos2 = new ArrayList<>();
      //  myadaptador = new myadaptador1(this, datos2);
		milista.setAdapter(customAdapter);





		 //myadaptador1 adap = new myadaptador1(this,android.R.layout.simple_list_item_1 , datos);
	//	ListAdapter customadpa = new ListAdapter(this,R.layout.row_info_coduc,datos);


	}





	/**METODOS  PARA UTILIZAR LOS OnMapReadyCallback , ConnectionCallback y onConnectionFailedListener **/


	@Override
	public void onConnected(@Nullable Bundle bundle) {


	}

	@Override
	public void onConnectionSuspended(int i) {

	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

	}

	@Override
	public void onMapReady(GoogleMap googleMap) {

		maps = googleMap;
		ismapready  = true;
		if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}
		maps.setMyLocationEnabled(true);
		maps.getUiSettings().setCompassEnabled(true);
		maps.getUiSettings().setMyLocationButtonEnabled(true);
		maps.getUiSettings().setZoomControlsEnabled(true);



		ValueEventListener mPOstlistener = new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {


				maps.clear(); //Cleaning the last markers
				HashMap<String, Conductores> busHashMap = (HashMap<String, Conductores>) dataSnapshot.getValue();
				for (Map.Entry<String , Conductores> entry : busHashMap.entrySet())
				{
					final Conductores conduc = Conductores.coductores_al_mapa((Map) entry.getValue()); //agrega lso valores


					Marker newmarker = maps.addMarker(new MarkerOptions().position(new LatLng(conduc.getLatitude(),conduc.getLongitude())).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_car2))
					.title(conduc.getHsalida()  + conduc.getSalida() + conduc.getLlegada() + conduc.getUsuario())
							.snippet("soy el mejor"));



					mMarkersHashMap.put(newmarker , conduc);

					maps.setInfoWindowAdapter(new MarkerInfoWindowAdapter());


					maps.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
						@Override
						public void onInfoWindowClick(Marker marker) {




								Log.i("VAMOS JAJAJA" , marker.getTitle());




						}
					});

				}
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {

			}
		};






		myRef.addValueEventListener(mPOstlistener);



		//mCurrLocationMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.person));




	}
	//**************************************//




	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public boolean onMarkerClick(Marker marker) {


		Log.i("MENSAJE AGREST" , marker.getTitle());

		return false;
	}


	public class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter
	{

		public  MarkerInfoWindowAdapter()
		{


		}

		@Override
		public View getInfoWindow(Marker marker) {
			return null;
		}

		@Override
		public View getInfoContents(Marker marker) {

			View v = getLayoutInflater().inflate(R.layout.estilo_marker, null);
			Conductores mymarker = mMarkersHashMap.get(marker);
			TextView markerLabel =  v.findViewById(R.id.marker_label);
			TextView markerlabel2 = (TextView) v.findViewById(R.id.marker_labelllega);
			TextView markerlabel3 = (TextView) v.findViewById(R.id.marker_labelhora);
			TextView markerlabel4 = (TextView) v.findViewById(R.id.marker_user);
			markerLabel.setText(mymarker.getSalida());
			markerlabel2.setText(mymarker.getLlegada());
			markerlabel3.setText(mymarker.getHsalida());
			markerlabel4.setText(mymarker.getUsuario());


			datos.add("Conductor: "+ mymarker.getUsuario());
			datos.add("Hora Salida: "+ mymarker.getHsalida());
			datos.add("Punto partida: "+ mymarker.getSalida());
			datos.add("Punto llegada: "+ mymarker.getLlegada());



			customAdapter.notifyDataSetChanged();

			if (datos.size() > 4)
			{
				customAdapter.clear();
				datos.add("Conductor: "+ mymarker.getUsuario());
				datos.add("Hora Salida: "+ mymarker.getHsalida());
				datos.add("Punto partida: "+ mymarker.getSalida());
				datos.add("Punto llegada: "+ mymarker.getLlegada());



			}

							return v;




		}




	}



	public  class myadaptador1 extends ArrayAdapter<String>
	{


		ArrayList<String> mydata;

		public  myadaptador1(Context context , ArrayList<String> conduct)
		{
			super(context , 0 , conduct);
			this.mydata = conduct;

		}

		@NonNull
		@Override
		public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {



			convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_info_coduc , parent , false);



			TextView txt = (TextView)findViewById(R.id.lab_lsalida);
TextView txt1 = (TextView)findViewById(R.id.label1);

            // txt.setText("");
//             txt1.setText("");


			mydata.get(3);

			return convertView;

		}
	}




}

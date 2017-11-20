package com.metroapp.codering.metrolineas;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.metroapp.codering.metrolineas.modelo.Conductores;
import com.metroapp.codering.metrolineas.modelo.Informar;
import com.squareup.picasso.Picasso;

public class Ma_conductors extends AppCompatActivity {
	private DatabaseReference mDatabase;
	private DatabaseReference bd_usuario;
	private FirebaseAuth auth;



	private RecyclerView  list_conductores_dispo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ma_conductors);

		//RelativeLayout mealLayout = (RelativeLayout) findViewById(R.id.cartas);

		//mealLayout.setBackgroundColor(Color.GREEN);
		mDatabase = FirebaseDatabase.getInstance().getReference().child("Conductores");

		//RecyclerView.LayoutManager mlayout = new GridLayoutManager(this , 2);

		list_conductores_dispo = (RecyclerView)findViewById(R.id.lis_conductores);
		list_conductores_dispo.setHasFixedSize(true);

		list_conductores_dispo.setLayoutManager(new LinearLayoutManager(this));


	}

	@Override
	protected void onStart() {
		super.onStart();



		FirebaseRecyclerAdapter<Conductores,ConductoresViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Conductores,ConductoresViewHolder>(
				Conductores.class,
				R.layout.la_conductores,
				ConductoresViewHolder.class,
				mDatabase) {

			@Override
			protected void populateViewHolder(ConductoresViewHolder viewHolder, Conductores model, int position) {

//populateviewholder rellena el Reciclerview viewHolder -> instancia de la clase PostViewHolder que hereda de RecyclerView.ViewHolder
				viewHolder.guardar_salida(model.getSalida());  //le pasa el titulo de las quejas al texfield del linearlayout en el blog_menu.XML
				viewHolder.guardar_llegada(model.getLlegada());
				viewHolder.guardar_usuario(model.getUsuario());
				viewHolder.guardar_hora(model.getHsalida());

				//viewHolder.guardar_img(getApplicationContext(),model.getImage());


			}
		};

		//** termina FirebaseRecyclerAdapter

		list_conductores_dispo.setAdapter(firebaseRecyclerAdapter);
















		/*
		FirebaseRecyclerAdapter<Conductores,ConductoresViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Conductores, ConductoresViewHolder>(Conductores.class,
				R.layout.la_conductores,
				ConductoresViewHolder.class,
				mDatabase){


			@Override
			protected void populateViewHolder(ConductoresViewHolder viewHolder, Conductores model, int position) {


				viewHolder.guardar_latitud(model.getLatitud());
				viewHolder.guardar_longitud(model.getLongitud());

			}
		};

		list_conductores_dispo.setAdapter(firebaseRecyclerAdapter);

	*/
	}

	/*

	public static class ConductoresViewHolder extends RecyclerView.ViewHolder
	{
		View mview;



		public ConductoresViewHolder(View itemView) {
			super(itemView);
			mview = itemView;


		}

		public void guardar_longitud(double longitud)
		{
			TextView txt_titulo = (TextView) mview.findViewById(R.id.con_latidud);
			txt_titulo.setText(String.valueOf(longitud));

		}


		public void guardar_latitud(double latitud)
		{

			TextView txt_des = (TextView)mview.findViewById(R.id.con_longitud);
			txt_des.setText(String.valueOf(latitud));


		}





	}
	*/

	public static class ConductoresViewHolder extends RecyclerView.ViewHolder
	{
		View mview;



		public ConductoresViewHolder(View itemView) {
			super(itemView);
			mview = itemView;


		}

		public void guardar_salida(String salida)
		{
			TextView txt_titulo = (TextView) mview.findViewById(R.id.con_latidud);
			txt_titulo.setText(salida);

		}


		public void guardar_llegada(String llegada)
		{

			TextView txt_des = (TextView)mview.findViewById(R.id.con_longitud);
			txt_des.setText(llegada);


		}


		public void guardar_usuario(String usuario)
		{

			TextView text_user  = (TextView)mview.findViewById(R.id.con_usuario);
			text_user.setText(usuario);

		}

		public  void guardar_hora(String hora)
		{

			TextView txt_hora = (TextView)mview.findViewById(R.id.con_hora);
			txt_hora.setText(hora);


		}



	}



}

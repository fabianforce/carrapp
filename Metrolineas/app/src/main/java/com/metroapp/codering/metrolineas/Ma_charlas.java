package com.metroapp.codering.metrolineas;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.metroapp.codering.metrolineas.modelo.Informar;
import com.squareup.picasso.Picasso;

public class Ma_charlas extends AppCompatActivity {

private RecyclerView mi_blog_lista;
	private DatabaseReference mDatabase;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ma_charlas);
		mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog");
mDatabase.keepSynced(true);
		mi_blog_lista = (RecyclerView) findViewById(R.id.blog_list);
		mi_blog_lista.setHasFixedSize(true);
		mi_blog_lista.setLayoutManager(new LinearLayoutManager(this));



	}

	/*****/
	@Override
	protected void onStart() {
		super.onStart();
		FirebaseRecyclerAdapter<Informar,PostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Informar, PostViewHolder>(
				Informar.class,
				R.layout.blog_menu,
				PostViewHolder.class,
				mDatabase) {

			@Override
			protected void populateViewHolder(PostViewHolder viewHolder, Informar model, int position) {

//populateviewholder rellena el Reciclerview viewHolder -> instancia de la clase PostViewHolder que hereda de RecyclerView.ViewHolder
				viewHolder.guardar_titulo(model.getTitulo());  //le pasa el titulo de las quejas al texfield del linearlayout en el blog_menu.XML
				viewHolder.guardar_desc(model.getDesc());
				viewHolder.guardar_img(getApplicationContext(),model.getImage());


			}
		  };

		  //** termina FirebaseRecyclerAdapter

		  mi_blog_lista.setAdapter(firebaseRecyclerAdapter);

	}

	public static class PostViewHolder extends RecyclerView.ViewHolder
	{
           View mview;



		public PostViewHolder(View itemView) {
			super(itemView);
			mview = itemView;


		}

		public void guardar_titulo(String titutlo)
		{
			TextView txt_titulo = (TextView) mview.findViewById(R.id.post_titulo);
txt_titulo.setText(titutlo);

		}


		public void guardar_desc(String descripcion)
		{

			TextView txt_des = (TextView)mview.findViewById(R.id.post_descrip);
			txt_des.setText(descripcion);


		}

		public void guardar_img(Context contx , String img)
		{

			ImageView img_inf = (ImageView)mview.findViewById(R.id.img_cap_inf);
			Picasso.with(contx).load(img).into(img_inf);


		}



	}

	/*creamos el menu creado en res/menu/main_menu.xml*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu , menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {


		if (item.getItemId() == R.id.action_add){

			startActivity(new Intent(Ma_charlas.this,Ma_postear.class));
		}
		return super.onOptionsItemSelected(item);



	}
}

package com.metroapp.codering.metrolineas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

public class Ma_nmenu extends AppCompatActivity {


	CardView car1,car2,car3,car4;
	TextView lab_user;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ma_nmenu);
		car1 = (CardView)findViewById(R.id.car_autos_online);
		car2 = (CardView) findViewById(R.id.car_soyconductor);
		car3 = (CardView)findViewById(R.id.car_postear_problemas);
		car4 = (CardView)findViewById(R.id.car_pruebas);
lab_user = (TextView)findViewById(R.id.lab_acuser);

		car1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				startActivity(new Intent(Ma_nmenu.this, Ma_trmetros.class));





			}
		});



		opbciones_menu();

	}



	public void opbciones_menu()
	{

		car2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				startActivity(new Intent(Ma_nmenu.this , Ma_mapa.class));


			}
		});

		car3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				startActivity(new Intent(Ma_nmenu.this , Ma_charlas.class));

			}
		});


		car4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				startActivity(new Intent(Ma_nmenu.this , Ma_conductors.class));

			}
		});

		lab_user.setText("Usuario:"+ getIntent().getExtras().getString("Email"));


	}
}

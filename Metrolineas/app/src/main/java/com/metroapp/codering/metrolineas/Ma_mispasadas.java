package com.metroapp.codering.metrolineas;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ernestoyaquello.com.verticalstepperform.VerticalStepperFormLayout;
import ernestoyaquello.com.verticalstepperform.interfaces.VerticalStepperForm;

public class Ma_mispasadas extends AppCompatActivity implements VerticalStepperForm {
	private EditText txt_para1;
	private EditText txt_parada2;
	private EditText txt_parada3;
	private EditText txt_parada4;
	private ProgressDialog progressDialog;
	private VerticalStepperFormLayout verticalStepperFormLayout;

	private SharedPreferences mPreferences;
	private  SharedPreferences.Editor editor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ma_mispasadas);
		initializeActivity();

	}


	private void initializeActivity()
	{


		SharedPreferences mPreferences = getSharedPreferences("miprefrenecia" , Context.MODE_PRIVATE);
		editor = mPreferences.edit();




//Stepper ----------------------------------------------------------------
	/*
			int tamano_array = 5;
		//String [] mysteps  = new String[5];

		List<String> mylist = new ArrayList<String>();
		mylist.add("");
		String  []  myarray= new String[tamano_array];


		for (int cont = 0 ; cont < tamano_array ; cont++)
		{
 			myarray[cont] = "jeje";

			//mysteps[cont] = "jiji";

		}

*/



	String [] mysteps = { "" , "" , "" , "" };
		String [] subtitulos = { "sub_1" , "sub_2" , "sub3" ,"otra"};
		int colorprimary = ContextCompat.getColor(getApplicationContext() , R.color.colorPrimaryDark);
		int colorprimarydark = ContextCompat.getColor(getApplicationContext() , R.color.colorAccent);
		verticalStepperFormLayout = (VerticalStepperFormLayout) findViewById(R.id.vertical_stepper_form);

		VerticalStepperFormLayout.Builder.newInstance(verticalStepperFormLayout, mysteps, this, this)
				.primaryColor(colorprimary)
				.primaryDarkColor(colorprimarydark)
				.materialDesignInDisabledSteps(true)
				.displayBottomNavigation(false) // It is true by default, so in this case this line is not necessary
				.stepsSubtitles(subtitulos)
				.init();


/// ----------------------------------------------------------------------

	}

	@Override
	public View createStepContentView(int stepNumber) {

		View view = null;
		switch (stepNumber)
		{
			case 0:
				view = parada1();


				break;
			case 1:

				view = parada2();
				break;
			case  2:

				view = parada3();

				break;
			case 3:

				view = parada4();

				break;

		}

		return view;
	}



	@Override
	public void onStepOpening(int stepNumber) {
		switch (stepNumber)
		{
			case 0:
				//checkTitleStep(titleEditText.getText().toString());


				break;
			case 1:


				break;
			case 2:

				verticalStepperFormLayout.setStepAsCompleted(stepNumber);
				break;

			case 3:
				checkTitleStep(txt_parada4.getText().toString());
				break;




		}
	}

	@Override
	public void sendData() {


		progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(true);
		progressDialog.show();
		verticalStepperFormLayout.setStepSubtitle(3,txt_parada4.getText().toString());
		progressDialog.setMessage("buenas");
		executeDataSending();
	}

	private void executeDataSending() {
		// TODO Use here the data of the form as you wish

		// Fake data sending effect
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					/*
					Intent intent = getIntent();
					setResult(RESULT_OK, intent);
					intent.putExtra("", true);
					intent.putExtra(STATE_TITLE, titleEditText.getText().toString());
					intent.putExtra(STATE_DESCRIPTION, descriptionEditText.getText().toString());
					intent.putExtra(STATE_TIME_HOUR, time.first);
					intent.putExtra(STATE_TIME_MINUTES, time.second);
					intent.putExtra(STATE_WEEK_DAYS, weekDays);
					// You must set confirmBack to false before calling finish() to avoid the confirmation dialog
					*/
					editor.putString("STEP1" ,  txt_para1.getText().toString());
					editor.putString("STEP2" , txt_parada2.getText().toString());
					editor.putString("STEP3" , txt_parada3.getText().toString());
					editor.putString("STEP4" , txt_parada4.getText().toString());
					editor.commit();
					//confirmBack = false;
					progressDialog.dismiss();
					//finish();
					volver_atras();


				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start(); // You should delete this code and add yours

	}
	public void volver_atras()
	{

		startActivity(new Intent(this, Ma_mapa.class));

	}


	private View parada1() {
// This step view is generated programmatically
		txt_para1 = new EditText(this);
		txt_para1.setHint("fefe");
		txt_para1.setSingleLine(true);
		txt_para1.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				checkTitleStep(s.toString());

			}

			@Override
			public void afterTextChanged(Editable s) {



			}
		});
		txt_para1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(checkTitleStep(v.getText().toString())) {



					verticalStepperFormLayout.goToNextStep();
					verticalStepperFormLayout.setStepSubtitle(0,txt_para1.getText().toString());


				}


				return false;
			}
		});

		return txt_para1;

	}


	private View parada2() {
		txt_parada2 = new EditText(this);
		txt_parada2.setHint("cdede");
		txt_parada2.setSingleLine(true);
		txt_parada2.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

				checkTitleStep(charSequence.toString());


			}

			@Override
			public void afterTextChanged(Editable editable) {

			}
		});
		txt_parada2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

				if(checkTitleStep(v.getText().toString())) {



					verticalStepperFormLayout.goToNextStep();
					verticalStepperFormLayout.setStepSubtitle(1,txt_parada2.getText().toString());

				}


				return false;
			}

		});
		return txt_parada2;
	}



	private View parada3() {
		txt_parada3 = new EditText(this);
		txt_parada3.setHint("cdede");
		txt_parada3.setSingleLine(true);



		txt_parada3.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				verticalStepperFormLayout.goToNextStep();
				verticalStepperFormLayout.setStepSubtitle(2,txt_parada3.getText().toString());
				return false;
			}

		});
		return txt_parada3;
	}

	private View parada4(){

		txt_parada4 = new EditText(this);
		txt_parada4.setHint("Pegelo");
		txt_parada4.setSingleLine(true);

		txt_parada4.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

				checkTitleStep(charSequence.toString());
			}

			@Override
			public void afterTextChanged(Editable editable) {

			}
		});
		txt_parada4.setOnEditorActionListener(new TextView.OnEditorActionListener(){

			@Override
			public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {


				if(checkTitleStep(textView.getText().toString())) {


//agregar el if para que continue

					verticalStepperFormLayout.goToNextStep();
					//	verticalStepperFormLayout.setStepSubtitle(3,"MIERDA HP");
					verticalStepperFormLayout.setStepSubtitle(3,txt_parada4.getText().toString());


				}

				return false;
			}
		});

		return  txt_parada4;

	}


	private boolean checkTitleStep(String title) {
		boolean titleIsCorrect = false;

		if(title.length() >= 3) { //minimico character en el textview 3
			titleIsCorrect = true;

			verticalStepperFormLayout.setActiveStepAsCompleted();
			// Equivalent to: verticalStepperForm.setStepAsCompleted(TITLE_STEP_NUM);

		} else {
			String titleErrorString = " > 3";
			String titleError = String.format(titleErrorString, 3);

			verticalStepperFormLayout.setActiveStepAsUncompleted(titleError);
			// Equivalent to: verticalStepperForm.setStepAsUncompleted(TITLE_STEP_NUM, titleError);

		}

		return titleIsCorrect;
	}




}

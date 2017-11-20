package com.metroapp.codering.metrolineas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View.OnClickListener;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Ma_registro extends AppCompatActivity {


    private EditText txt_ruser,txt_remail,txt_rcontra;
    private Button btn_rregis;
    private FirebaseAuth mAuth;
    private DatabaseReference bd_ref,bd_ref1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ma_registro);



        mAuth = FirebaseAuth.getInstance();
        bd_ref = FirebaseDatabase.getInstance().getReference().child("usuarios");
        bd_ref1 = FirebaseDatabase.getInstance().getReference().child("Conductores");
        txt_ruser = (EditText) findViewById(R.id.r_user);
        txt_remail = (EditText) findViewById(R.id.r_email);
        txt_rcontra = (EditText) findViewById(R.id.r_contra);
        btn_rregis = (Button) findViewById(R.id.b_registro);





    }

    public void btn_registrar_usuer(View v)
    {
registar_usuario(txt_remail.getText().toString().trim() , txt_rcontra.getText().toString().trim());

    }

    public void registar_usuario(String email , String contra)
    {


        final ProgressDialog progressDialog = ProgressDialog.show(Ma_registro.this , "hola" , "espere un poquito");

        (mAuth.createUserWithEmailAndPassword(email , contra)).addOnCompleteListener(Ma_registro.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


                if(task.isSuccessful())
                {

                  //  Toast.makeText(Ma_registro.this ,"Registro exitoso", Toast.LENGTH_LONG).show();

					progressDialog.dismiss();

					String usuario_id = mAuth.getCurrentUser().getUid();
					DatabaseReference user_actual_id =  bd_ref.child(usuario_id);
					//DatabaseReference user_actual  = bd_ref1.child(usuario_id); //gurdarndo nombre de usuario en la bd real time
					user_actual_id.child("nombre").setValue(txt_ruser.getText().toString().trim());
					//user_actual.child("usuario").setValue(txt_ruser.getText().toString().trim());


                    Intent i = new Intent(Ma_registro.this , MainActivity.class);
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);

                }else
                {
                    progressDialog.dismiss();
                    Log.e("ERROR" , task.getException().toString());
                    Toast.makeText(Ma_registro.this ,task.getException().getMessage(), Toast.LENGTH_LONG).show();


                }
            }
        });




    }


/*

    public  void registarse(String email , String contr , FirebaseAuth fir)
    {

        fir.createUserWithEmailAndPassword(email, contr)
                .addOnCompleteListener(Ma_registro.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });




*/



    }







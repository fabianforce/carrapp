package com.metroapp.codering.metrolineas;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText email,contrasena;
    private Button btn_entrar,btn_regis;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = (EditText) findViewById(R.id.txt_user);
        contrasena = (EditText) findViewById(R.id.txt_contra);
        btn_entrar = (Button) findViewById(R.id.btn_log);
        btn_regis = (Button) findViewById(R.id.btn_regi);
        auth = FirebaseAuth.getInstance();


        btn_regis.setOnClickListener(new View.OnClickListener()
                                     {
                                         @Override
                                         public void onClick(View v) {

                                             startActivity(new Intent(MainActivity.this , Ma_registro.class));

                                         }}) ;


    }


    public void btn_ingresar(View v)
    {
btn_ingresar(email.getText().toString().trim() , contrasena.getText().toString().trim());




    }


    public void btn_ingresar(String correo , String contra){


(auth.signInWithEmailAndPassword(correo , contra)).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {

if (task.isSuccessful())
{


//    Toast.makeText(MainActivity.this, "Login Exitoso", Toast.LENGTH_LONG).show();

    Intent i = new Intent(MainActivity.this , Ma_nmenu.class);
    i.putExtra("Email", auth.getCurrentUser().getEmail()); //envia el email(usuario) para que la otra vsita pueda verlo
    startActivity(i);


}else
{
    Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();


}


    }
});





    }

}

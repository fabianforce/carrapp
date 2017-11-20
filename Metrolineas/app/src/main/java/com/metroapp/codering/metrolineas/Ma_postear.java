package com.metroapp.codering.metrolineas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

public class Ma_postear extends AppCompatActivity {
	private ImageButton btn_select_image;
	private TextView txt_Titulo,txt_Publi;
	private Button btn_postear;
	private Uri miImagen = null;
	private StorageReference msStorageReference;
	private DatabaseReference mDatabase;
	private DatabaseReference bd_usuario;
	private FirebaseAuth auth;
	private FirebaseUser user_actual;
	private static final int gallery_request = 1; //codigo de solicitud
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ma_postear);
auth = FirebaseAuth.getInstance();
         msStorageReference = FirebaseStorage.getInstance().getReference(); //REFERENCIA FIREBASE_STORE
		mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog");
		user_actual = auth.getCurrentUser();
		bd_usuario = FirebaseDatabase.getInstance().getReference().child("usuarios").child(user_actual.getUid()); // nombre de usuario en la tabal usuarios

		btn_postear = (Button)findViewById(R.id.btn_publicar);
		btn_select_image  = (ImageButton)findViewById(R.id.btn_seleccionar_imagen);
		txt_Titulo = (TextView)findViewById(R.id.txt_titulo);
		txt_Publi = (TextView)findViewById(R.id.txt_publicacion);

btn_select_image.setOnClickListener(new View.OnClickListener(){

	@Override
	public void onClick(View view) {

		Intent galeria = new Intent(Intent.ACTION_GET_CONTENT);
		galeria.setType("image/*");
		startActivityForResult(galeria , gallery_request);//2parametro: int -> requestcode

	}
});

		btn_postear.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view) {

				publicar();

			}
		});






	}

	private void publicar() {

		final String titulo_post = txt_Titulo.getText().toString().trim();
		final String descripcion = txt_Publi.getText().toString().trim();

		//si todo el mensaje ya esta listo foto / titulo 7/ descripcion
		if(!TextUtils.isEmpty(titulo_post) && !TextUtils.isEmpty(descripcion) && miImagen != null)
		{
			//si nada esta vacio
			final ProgressDialog progressDialog = ProgressDialog.show(Ma_postear.this , "Aviso" , "Espera un poquito");
StorageReference filepath = msStorageReference.child("Blog_Images").child(miImagen.getLastPathSegment()); //miImagen Uri de la iagen tomada de la ruta imgae/* del celular
filepath.putFile(miImagen).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
	@Override
	public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

		final Uri dowload_image = taskSnapshot.getDownloadUrl(); // descargamosla imagen la obtenemos

	    final DatabaseReference newPost = mDatabase.push();

//bd_usuario -> usuarios/uid/
		bd_usuario.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {


				newPost.child("titulo").setValue(titulo_post);
				newPost.child("desc").setValue(descripcion);
				newPost.child("Image").setValue(dowload_image.toString());
				newPost.child("uid").setValue(user_actual.getUid());
				newPost.child("usuario").setValue(dataSnapshot.child("nombre").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
					@Override
					public void onComplete(@NonNull Task<Void> task) {

if (task.isSuccessful())
{

startActivity(new Intent(Ma_postear.this,Ma_charlas.class));

}}});//addOnCompleteListener

			}

			@Override
			public void onCancelled(DatabaseError databaseError) {

			}
		});

		progressDialog.dismiss();
	}
});

		}


	}

	/**toma el resultado que mandamos en el startActivityForResult para obtener la uri**/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if(requestCode == gallery_request && resultCode == RESULT_OK) //resultcode= -1 == RESULT_OK = -1
		{
//si todo esta correctamente
			miImagen = data.getData();
			btn_select_image.setImageURI(miImagen);



		}


	}
}

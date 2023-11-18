package com.example.lab6;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.lab6.databinding.ActivityMainBinding;
import com.example.lab6.dtos.Usuario;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db;
    ActivityMainBinding binding;
    ListenerRegistration snapshotListener;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        binding.button.setOnClickListener(view -> {
            String email = binding.textFieldNombre.getEditText().getText().toString();
            String pass = binding.textFieldApellido.getEditText().getText().toString();

            Usuario usuario = new Usuario();

            Log.d("msg-test", "se recibio los parametros de sesion");
            Log.d("msg-test", email+" "+pass);

            mAuth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("msg-test", "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                                String email = user.getEmail();
                                Log.d("msg-test", "El correo es: "+email);

                                db.collection("usuarios")
                                        .get()
                                        .addOnCompleteListener(task2 -> {
                                            if (task2.isSuccessful()) {
                                                QuerySnapshot usuariosCollection = task2.getResult();
                                                Log.d("msg-test", "task2 ha sido valido");
                                                for (QueryDocumentSnapshot document : usuariosCollection) {
                                                    String codigo = document.getId();
                                                    String pass = (String) document.get("contrasenha");
                                                    String correo = (String) document.get("correo");
                                                    String rol = (String) document.get("rol");

                                                    if(correo.equals(email)){
                                                        usuario.setContrasenha(pass);
                                                        usuario.setCorreo(correo);
                                                        usuario.setId(codigo);
                                                        usuario.setRol(rol);
                                                        Log.d("msg-test", "usuario correo: "+usuario.getCorreo()+" usuario rol: "+usuario.getRol());
                                                        break;
                                                    }
                                                }

                                                if(usuario.getRol().equals("gestor")){
                                                    Log.d("msg-test", "Entra rol gestor");
                                                    Intent intent = new Intent(MainActivity.this, gestorMain.class);
                                                    startActivity(intent);
                                                }else if(usuario.getRol().equals("cliente")){
                                                    Log.d("msg-test", "Entra rol cliente");
                                                    Intent intent = new Intent(MainActivity.this, clienteMain.class);
                                                    startActivity(intent);
                                                } else{
                                                    Toast.makeText(MainActivity.this, "El usuario no ha sido validado.",
                                                            Toast.LENGTH_SHORT).show();
                                                }



                                            }
                                        })
                                        .addOnFailureListener(e -> {
                                            // Maneja la excepción que ocurra al intentar obtener los documentos
                                            Log.e("msg-test", "Excepción al obtener documentos de la colección usuarios: ", e);
                                            Toast.makeText(MainActivity.this, "Error al obtener datos del usuario.", Toast.LENGTH_SHORT).show();
                                        });
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("msg-test", "signInWithEmail:failure", task.getException());
                                Toast.makeText(MainActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                        }
                    })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Manejar la excepción
                            Log.e("msg-test", "Exception: " + e.getMessage());
                        }
                    });


        });





        binding.registerBtn.setOnClickListener(view -> {
            AuthUI.getInstance().signOut(MainActivity.this)
                    .addOnCompleteListener(task -> {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    });
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (snapshotListener != null)
            snapshotListener.remove();
    }

    private void updateUI(FirebaseUser user) {}


}
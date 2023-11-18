package com.example.lab6;

import static androidx.core.content.ContentProviderCompat.requireContext;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.lab6.adapter.ActividadAdapter;
import com.example.lab6.databinding.ActivityMainBinding;
import com.example.lab6.dtos.citasDia;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class gestorMain extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseAuth mAuth;
    Button buttonDatosSalon;

    private List<citasDia> citasDia = new ArrayList<>();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestor_main);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.RecyclerView);

        db.collection("citasDia")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot eventosCollection = task.getResult();
                        if(citasDia.isEmpty()){
                            for (QueryDocumentSnapshot document : eventosCollection) {
                                String id = document.getId();
                                String correo = (String) document.get("correo");
                                String servicio = (String) document.get("servicio");
                                String hora = (String) document.get("hora");
                                citasDia citasDia1 = new citasDia();
                                citasDia1.setCorreo(correo);
                                citasDia1.setId(id);
                                citasDia1.setServicio(servicio);
                                citasDia1.setHora(hora);
                                citasDia.add(citasDia1);
                                Log.d("msg-test", " | correo: " + correo + " | hora: " + hora + " | servicio: " + servicio);
                            }
                        }

                        ActividadAdapter actividadAdapter = new ActividadAdapter();
                        actividadAdapter.setActividadList(citasDia);
                        actividadAdapter.setContext(gestorMain.this);

                        // Inicializa el RecyclerView y el adaptador
                        recyclerView.setAdapter(actividadAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(gestorMain.this));

                    }
                });

        buttonDatosSalon = findViewById(R.id.button3);

        buttonDatosSalon.setOnClickListener(view -> {
            Intent intent = new Intent(gestorMain.this, DatosSalon.class);
            startActivity(intent);
        });



    }
}
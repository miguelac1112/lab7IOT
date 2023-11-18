package com.example.lab6;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.lab6.adapter.ActividadAdapter;
import com.example.lab6.dtos.citasDia;
import com.example.lab6.dtos.salones;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class clienteMain extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseAuth mAuth;

    private List<salones> salones = new ArrayList<>();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_main);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.RecyclerView2);

        db.collection("salones")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot eventosCollection = task.getResult();
                        if(salones.isEmpty()){
                            for (QueryDocumentSnapshot document : eventosCollection) {
                                String id = document.getId();
                                String nombre = (String) document.get("nombre");
                                String servicio = (String) document.get("servicio");
                                String hora = (String) document.get("hora");
                                salones salones2 = new salones();
                                salones2.setId(id);
                                salones2.setNombre(nombre);
                                salones.add(salones2);
                                //Log.d("msg-test", " | correo: " + correo + " | hora: " + hora + " | servicio: " + servicio);
                            }
                        }

                        /*ActividadAdapter actividadAdapter = new ActividadAdapter();
                        actividadAdapter.setActividadList(citasDia);
                        actividadAdapter.setContext(gestorMain.this);

                        // Inicializa el RecyclerView y el adaptador
                        recyclerView.setAdapter(actividadAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(gestorMain.this));*/

                    }
                });




    }
}
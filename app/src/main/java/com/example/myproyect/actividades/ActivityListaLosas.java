package com.example.myproyect.actividades;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Button;
import com.example.myproyect.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ActivityListaLosas extends AppCompatActivity {

    private RecyclerView rvLosas;
    private Button btnIrAgregarLosa, btnVolver;
    private FirebaseDatabase database;
    private DatabaseReference dbRef;
    private LosaAdapter losaAdapter;
    private List<Losa> losaList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_losas);

        // Inicializar Firebase
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("losas");

        rvLosas = findViewById(R.id.rv_losas);
        btnIrAgregarLosa = findViewById(R.id.btn_ir_agregar_losa);
        btnVolver = findViewById(R.id.btn_volver);

        // Configurar RecyclerView
        rvLosas.setLayoutManager(new LinearLayoutManager(this));
        losaList = new ArrayList<>();
        losaAdapter = new LosaAdapter(this, losaList);
        rvLosas.setAdapter(losaAdapter);

        // Cargar losas desde Realtime Database
        cargarLosas();

        // Acción del botón para ir a la actividad de agregar una losa
        btnIrAgregarLosa.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityListaLosas.this, ActivityCrudLosas.class);
            startActivity(intent);
        });

        // Acción del botón de volver
        btnVolver.setOnClickListener(v -> finish());
    }

    private void cargarLosas() {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                losaList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Losa losa = snapshot.getValue(Losa.class);
                    losa.setKey(snapshot.getKey()); // Establecer la clave aquí
                    losaList.add(losa);
                }
                losaAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar el error
            }
        });
    }
}

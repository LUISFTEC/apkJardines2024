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
    private Button btnIrAgregarLosa;
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

        // Configurar RecyclerView
        rvLosas.setLayoutManager(new LinearLayoutManager(this));
        losaList = new ArrayList<>();
        losaAdapter = new LosaAdapter(losaList);
        rvLosas.setAdapter(losaAdapter);

        // Cargar losas desde Realtime Database
        cargarLosas();

        // Acción del botón para ir a la actividad de agregar una losa
        btnIrAgregarLosa.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityListaLosas.this, ActivityCrudLosas.class);
            startActivity(intent);
        });
    }

    private void cargarLosas() {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                losaList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Losa losa = snapshot.getValue(Losa.class);
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

    // Clase Losa
    public static class Losa {
        public String nombre;
        public String descripcion;
        public String imageUrl;

        public Losa() {
            // Constructor vacío necesario para Firebase
        }

        public Losa(String nombre, String descripcion, String imageUrl) {
            this.nombre = nombre;
            this.descripcion = descripcion;
            this.imageUrl = imageUrl;
        }
    }
}

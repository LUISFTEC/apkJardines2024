package com.example.myproyect.actividades;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Button;
import com.example.myproyect.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

public class ActivityListaLosas extends AppCompatActivity {

    private RecyclerView rvLosas;
    private Button btnIrAgregarLosa;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_losas);

        // Inicializar Firebase
        FirebaseApp.initializeApp(this);

        // Obtener una instancia de Firestore
        db = FirebaseFirestore.getInstance();

        rvLosas = findViewById(R.id.rv_losas);
        btnIrAgregarLosa = findViewById(R.id.btn_ir_agregar_losa);

        // Configurar RecyclerView (esto es solo un ejemplo, necesitarás un adaptador)
        rvLosas.setLayoutManager(new LinearLayoutManager(this));

        // Acción del botón para ir a la actividad de agregar una losa
        btnIrAgregarLosa.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityListaLosas.this, ActivityCrudLosas.class);
            startActivity(intent);
        });
    }
}

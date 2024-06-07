package com.example.myproyect.actividades.actividades.usuario;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import com.example.myproyect.R;
import com.example.myproyect.actividades.LosaAdapterCliente; // Cambiar el import para usar el nuevo adaptador
import com.example.myproyect.actividades.Losa;
import com.example.myproyect.actividades.actividades.Login_Activity;
import com.example.myproyect.actividades.clases.InterfaceMenu;
import com.example.myproyect.actividades.entidades.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BienvenidoActivity extends AppCompatActivity implements InterfaceMenu {

    TextView lblSaludo;
    Button btnSalida, btnReservar, btnActualizarDatos, btnConsultar;
    RecyclerView rvLosas;
    LosaAdapterCliente losaAdapter; // Usar el nuevo adaptador
    List<Losa> losaList;
    DatabaseReference databaseReference;
    Usuario usuario = Login_Activity.getUsuario();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenido);

        referencias();

        lblSaludo = findViewById(R.id.bieLblSaludo);
        String nomUsuario = usuario.getNombre();
        lblSaludo.setText("Bienvenido " + nomUsuario);

        rvLosas = findViewById(R.id.rv_losas_dinamicas);
        rvLosas.setLayoutManager(new LinearLayoutManager(this));
        losaList = new ArrayList<>();
        losaAdapter = new LosaAdapterCliente(this, losaList); // Usar el nuevo adaptador
        rvLosas.setAdapter(losaAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("losas");
        cargarLosas();
    }

    private void cargarLosas() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                losaList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Losa losa = snapshot.getValue(Losa.class);
                    losaList.add(losa);
                }
                losaAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Manejar el error
            }
        });
    }

    private void referencias() {
        btnConsultar = findViewById(R.id.btnReservasRealizadas_User);
        btnConsultar.setOnClickListener(view -> {
            Intent intent = new Intent(this, ConsultarReservaUser_Activity.class);
            startActivity(intent);
            finish();
        });
        btnReservar = findViewById(R.id.btnRealizarRsv_BienvenidoActy);
        btnReservar.setOnClickListener(view -> {
            Intent intent = new Intent(this, TablaReservaUser_Activity.class);
            startActivity(intent);
            finish();
        });
        btnSalida = findViewById(R.id.actvbtnCerrar);
        btnSalida.setOnClickListener(view -> {
            Intent intent = new Intent(this, Login_Activity.class);
            startActivity(intent);
            this.finish();
        });
        btnActualizarDatos = findViewById(R.id.btnActualizarDatos_BienvenidoActv);
        btnActualizarDatos.setOnClickListener(view -> {
            finish();
            Intent intent = new Intent(this, ActualizarDatosUSER_Activity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onClickMenu(int idBoton) {
        Intent iMenu = new Intent(this, Menu_Losas_Activity.class);
        iMenu.putExtra("idBoton", idBoton);
        startActivity(iMenu);
    }

    private void cerrarSesion() {
        //borrar sesion de la base de datos interna
        //Sesion sesion = new Sesion(this);
        //sesion.eliminarUsuario(1);
        //destruir historial
        this.finish();
        //mandar al login
        Intent iLogin = new Intent(this, Login_Activity.class);
        startActivity(iLogin);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

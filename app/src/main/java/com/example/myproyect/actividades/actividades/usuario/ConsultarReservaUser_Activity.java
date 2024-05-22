package com.example.myproyect.actividades.actividades.usuario;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myproyect.R;
import com.example.myproyect.actividades.actividades.Login_Activity;
import com.example.myproyect.actividades.entidades.CanchaDeportiva;
import com.example.myproyect.actividades.entidades.Reserva;
import com.example.myproyect.actividades.modelos.DAO_Losa;
import com.example.myproyect.actividades.modelos.DAO_Reserva;

import java.util.ArrayList;
import java.util.List;

public class ConsultarReservaUser_Activity extends AppCompatActivity {
    Button btnVolver;
    TextView txtvListado;
    Spinner spnLosas;
    String nombre_tabla;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_reserva_user);

        asignarReferencias();


    }
    private void funSpinner(){
        List<CanchaDeportiva> lista = new ArrayList<>();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        lista = DAO_Losa.listarNombres();

        List<String> opciones = new ArrayList<>();
        int i=1;
        for(CanchaDeportiva canchaDeportiva : lista){
            opciones.add(i+". "+canchaDeportiva.getNombre());
            i++;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opciones);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnLosas.setAdapter(adapter);

        List<CanchaDeportiva> finalLista = lista;
        spnLosas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //String opcion = (String) adapterView.getItemAtPosition(i);
                nombre_tabla = finalLista.get(i).getNombre_tabla();
                mostrarLista();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    private void mostrarLista(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        List<Reserva> listaRsv = new ArrayList<>();
        listaRsv = DAO_Reserva.ConsultarRsv(nombre_tabla);

        int contador=0; //contador reservas
        if(listaRsv.size() == 0 ){
            txtvListado.setText("NO HAY RESERVAS EN ESTA LOSA");
            Toast.makeText(this, "NO HAY RESERVAS EN ESTA LOSA", Toast.LENGTH_SHORT).show();


        }else {
            txtvListado.setText("");
            StringBuilder sb = new StringBuilder();

            for (Reserva reserva : listaRsv) {
                for (int j = 0; j < 3; j++) {
                    String dni = reserva.getArrayDni()[j];
                    String dni_cli = Login_Activity.getUsuario().getDNI();
                    if (dni != null && dni.equals(dni_cli)) {
                        sb.append("----------------------------------").append("\n");
                        sb.append("|      RESERVA #").append(contador+1).append("      |").append("\n");
                        sb.append("----------------------------------").append("\n");
                        sb.append(" FECHA: ").append(reserva.getDia()).append("\n");
                        int hora = 3 + (2 * j);
                        sb.append(" HORA: ").append(hora).append("pm").append("\n");
                        sb.append("----------------------------------").append("\n\n");
                        contador++;
                    }
                }
            }
            txtvListado.setText(sb.toString());
            Toast.makeText(this, "Hay "+listaRsv.size()+" fechas con reservas actualmente en esta losa", Toast.LENGTH_LONG).show();
        }
    }
    private void asignarReferencias(){
        spnLosas = findViewById(R.id.spnConsultarRsv_CLI);
        funSpinner();
        btnVolver = findViewById(R.id.btnVolver_ConsultRsvUser);
        btnVolver.setOnClickListener(view -> {
            startActivity(new Intent(this, BienvenidoActivity.class));
            finish();
        });
        txtvListado = findViewById(R.id.txtv_consultasRsv_user);
        txtvListado.setLines(10);
        txtvListado.setEllipsize(TextUtils.TruncateAt.END);
        txtvListado.setMovementMethod(new ScrollingMovementMethod());

    }


}
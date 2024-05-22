package com.example.myproyect.actividades.actividades.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myproyect.R;
import com.example.myproyect.actividades.actividades.admin.MenuAdmin_Activity;
import com.example.myproyect.actividades.entidades.Usuario;
import com.example.myproyect.actividades.modelos.DAO_Cliente;

import java.util.List;

public class ListarUsers_Admin_Activity extends AppCompatActivity {
    Button btnRegresar;
    TextView txtListar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_users_admin);

        asignarReferencias();
        listar();
    }
    private void asignarReferencias(){
        btnRegresar = findViewById(R.id.btnRegresar_ListarUsersAdmn);
        btnRegresar.setOnClickListener(view -> {
            Intent intent = new Intent(this, MenuAdmin_Activity.class);
            startActivity(intent);
            finish();

        });
        txtListar = findViewById(R.id.txtvListarUsers_Admin);
    }
    private void listar(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        List<Usuario> listaUsers = DAO_Cliente.listarClientes();

        txtListar.setLines(50);
        txtListar.setEllipsize(TextUtils.TruncateAt.END);
        txtListar.setMovementMethod(new ScrollingMovementMethod());
        if(listaUsers.size()!=0){
            int contador=0;
            for(Usuario user : listaUsers){
                String fechaR = user.getFecha_registro().substring(0,10);
                int num = contador+1;
                txtListar.append(
                        " | CLIENTE       #"+num+ "    | \n"+
                        "--------------------------------"+"\n"+
                        "FECHA REGISTRO: "+fechaR+"\n"+
                        "DNI: "+user.getDNI()+"\n"+
                        "NOMBRES: "+user.getNombre()+"\n"+
                        "APELLIDOS: "+user.getApellido()+"\n"+
                        "CORREO: "+user.getCorreo()+"\n"+
                        "CELULAR: "+user.getCelular()+"\n"+
                        "---------------------------------"+"\n");
                contador++;
            }
            Toast.makeText(this, "Hay "+contador+" usuarios registrados", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "No hay usuarios registrados", Toast.LENGTH_SHORT).show();
        }

    }
}
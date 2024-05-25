package com.example.myproyect.actividades.actividades.usuario;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.myproyect.R;
import com.example.myproyect.actividades.actividades.Login_Activity;
import com.example.myproyect.actividades.actividades.usuario.TerminosActivity;
import com.example.myproyect.actividades.clases.MostrarMensaje;
import com.example.myproyect.actividades.entidades.Usuario;
import com.example.myproyect.actividades.modelos.DAO_Cliente;
import com.example.myproyect.actividades.actividades.filtros.NoLeadingSpaceFilter;

import java.util.regex.Pattern;

public class RegistroActivity extends AppCompatActivity implements View.OnClickListener {
    EditText txtNombre, txtApellido, txtCorreo, txtClave, txtFechaNac, txtDni, txtCel;
    Button btnContinuar, btnRegresar;
    CheckBox chkTerminos;
    TextView lblIniciar, lblTerminos;

    Usuario user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        //asociacion de la parte
        //logica        con la        grafica
        txtDni = findViewById(R.id.regTxtDni);
        txtNombre = findViewById(R.id.regTxtNombre);
        txtApellido = findViewById(R.id.regTxtApellido);
        txtCorreo = findViewById(R.id.regTxtCorreo);
        txtClave = findViewById(R.id.regTxtClave);
        txtCel = findViewById(R.id.regTxtCel);

        // Aplicar el filtro a los campos de texto
        txtNombre.setFilters(new InputFilter[]{ new NoLeadingSpaceFilter() });
        txtApellido.setFilters(new InputFilter[]{ new NoLeadingSpaceFilter() });
        txtCorreo.setFilters(new InputFilter[]{ new NoLeadingSpaceFilter() });

        //link
        lblIniciar = findViewById(R.id.regLblIniciar);
        lblTerminos = findViewById(R.id.regLblTerminos);
        //chek
        chkTerminos = findViewById(R.id.regChkTerminos);
        //button
        btnRegresar = findViewById(R.id.regBtnRegresar);
        btnContinuar = findViewById(R.id.regBtnContinuar);
        //asociar el evento on click a los controles
        chkTerminos.setOnClickListener(this);
        btnContinuar.setOnClickListener(this);
        btnRegresar.setOnClickListener(this);
        lblIniciar.setOnClickListener(this);
        lblTerminos.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.regChkTerminos:
                validarTerminos();
                break;
            case R.id.regLblTerminos:
                cargarTerminos();
                break;
            case R.id.regBtnContinuar:
                registrar();
                break;
            case R.id.regBtnRegresar:
                regresar();
                break;
            case R.id.regLblIniciar:
                cargarActividadIniciar();
                break;
        }
    }

    private void cargarTerminos() {
        Intent iTerminos = new Intent(this, TerminosActivity.class);
        startActivity(iTerminos);
    }

    private void validarTerminos() {
        boolean activo = chkTerminos.isChecked() ? true : false;
        btnContinuar.setEnabled(activo);
    }

    private void capturarDatos(){
        //guardar datos ingresados por el usuario
        String dni, correo, clave, nombre, apellido,   celular;
        dni = txtDni.getText().toString();
        nombre = txtNombre.getText().toString();
        apellido = txtApellido.getText().toString();
        correo = txtCorreo.getText().toString();
        clave =  txtClave.getText().toString();
        celular = txtCel.getText().toString();

        user = new Usuario(dni, nombre, apellido, correo, clave, celular);
    }

    // Expresión regular más estricta para correos electrónicos
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", Pattern.CASE_INSENSITIVE);


    //CONTINUAR
    private void registrar() {
        capturarDatos();
        boolean isValid = true;  // Variable para controlar la validación

        // Validación del DNI
        String dni = user.getDNI().trim();  // Eliminar espacios en blanco al inicio y al final
        if (dni.isEmpty()) {
            txtDni.setError("El DNI no puede estar vacío");
            if (isValid) {
                txtDni.requestFocus();  // Enfocar en el campo DNI si es el primer error encontrado
            }
            isValid = false;
        } else if (dni.length() != 8) {
            txtDni.setError("El DNI debe tener exactamente 8 números");
            if (isValid) {
                txtDni.requestFocus();  // Enfocar en el campo DNI si es el primer error encontrado
            }
            isValid = false;
        } else if (!dni.matches("\\d+")) {
            txtDni.setError("El DNI solo puede contener números");
            if (isValid) {
                txtDni.requestFocus();  // Enfocar en el campo DNI si es el primer error encontrado
            }
            isValid = false;
        }

        // Validación del nombre
        String nombre = user.getNombre().trim();  // Eliminar espacios en blanco al inicio y al final

        if (nombre.isEmpty()) {
            txtNombre.setError("El nombre no puede estar vacío");
            if (isValid) {
                txtNombre.requestFocus();  // Enfocar en el campo nombre si es el primer error encontrado
            }
            isValid = false;
        } else if (nombre.contains(" ")) {  // Verificar que no haya espacios en el nombre
            txtNombre.setError("El nombre no puede contener espacios");
            if (isValid) {
                txtNombre.requestFocus();  // Enfocar en el campo nombre si es el primer error encontrado
            }
            isValid = false;
        } else if (nombre.length() < 3) {
            txtNombre.setError("El nombre debe tener al menos 3 caracteres");
            if (isValid) {
                txtNombre.requestFocus();  // Enfocar en el campo nombre si es el primer error encontrado
            }
            isValid = false;
        } else if (nombre.length() > 20) {
            txtNombre.setError("El nombre no puede tener más de 20 caracteres");
            if (isValid) {
                txtNombre.requestFocus();  // Enfocar en el campo nombre si es el primer error encontrado
            }
            isValid = false;
        } else if (!nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ]+$")) {  // Verificar que el nombre solo contiene letras y no tiene espacios
            txtNombre.setError("El nombre solo puede contener letras");
            if (isValid) {
                txtNombre.requestFocus();  // Enfocar en el campo nombre si es el primer error encontrado
            }
            isValid = false;
        }

        // Validación del apellido
        String apellido = user.getApellido().trim();  // Eliminar espacios en blanco al inicio y al final

        if (apellido.isEmpty()) {
            txtApellido.setError("El apellido no puede estar vacío");
            if (isValid) {
                txtApellido.requestFocus();  // Enfocar en el campo apellido si es el primer error encontrado
            }
            isValid = false;
        } else if (apellido.contains(" ")) {  // Verificar que no haya espacios en el apellido
            txtApellido.setError("El apellido no puede contener espacios");
            if (isValid) {
                txtApellido.requestFocus();  // Enfocar en el campo apellido si es el primer error encontrado
            }
            isValid = false;
        } else if (apellido.length() < 3) {
            txtApellido.setError("El apellido debe tener al menos 3 caracteres");
            if (isValid) {
                txtApellido.requestFocus();  // Enfocar en el campo apellido si es el primer error encontrado
            }
            isValid = false;
        } else if (apellido.length() > 20) {
            txtApellido.setError("El apellido no puede tener más de 20 caracteres");
            if (isValid) {
                txtApellido.requestFocus();  // Enfocar en el campo apellido si es el primer error encontrado
            }
            isValid = false;
        } else if (!apellido.matches("^[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ]+$")) {  // Verificar que el apellido solo contiene letras
            txtApellido.setError("El apellido solo puede contener letras");
            if (isValid) {
                txtApellido.requestFocus();  // Enfocar en el campo apellido si es el primer error encontrado
            }
            isValid = false;
        }


        // Validación del celular
        String celular = user.getCelular().trim();  // Eliminar espacios en blanco al inicio y al final
        if (celular.isEmpty()) {
            txtCel.setError("El celular no puede estar vacío");
            if (isValid) {
                txtCel.requestFocus();  // Enfocar en el campo celular si es el primer error encontrado
            }
            isValid = false;
        } else if (celular.length() != 9) {
            txtCel.setError("El celular debe tener exactamente 9 dígitos");
            if (isValid) {
                txtCel.requestFocus();  // Enfocar en el campo celular si es el primer error encontrado
            }
            isValid = false;
        } else if (!celular.matches("\\d+")) {
            txtCel.setError("El celular solo puede contener números");
            if (isValid) {
                txtCel.requestFocus();  // Enfocar en el campo celular si es el primer error encontrado
            }
            isValid = false;
        }

        // Validación del correo
        String correo = user.getCorreo().trim();  // Eliminar espacios en blanco al inicio y al final
        if (correo.isEmpty()) {
            txtCorreo.setError("El correo no puede estar vacío");
            if (isValid) {
                txtCorreo.requestFocus();  // Enfocar en el campo correo si es el primer error encontrado
            }
            isValid = false;
        } else if (!correo.contains("@")) {
            txtCorreo.setError("El correo debe contener '@'");
            if (isValid) {
                txtCorreo.requestFocus();  // Enfocar en el campo correo si es el primer error encontrado
            }
            isValid = false;
        } else {
            String[] parts = correo.split("@");
            if (parts.length != 2 || parts[0].isEmpty()) {
                txtCorreo.setError("El correo debe tener un nombre de usuario válido antes de '@'");
                if (isValid) {
                    txtCorreo.requestFocus();  // Enfocar en el campo correo si es el primer error encontrado
                }
                isValid = false;
            } else if (!parts[1].contains(".") || parts[1].lastIndexOf(".") == parts[1].length() - 1 || parts[1].split("\\.").length < 2) {
                txtCorreo.setError("El correo debe tener un dominio válido");
                if (isValid) {
                    txtCorreo.requestFocus();  // Enfocar en el campo correo si es el primer error encontrado
                }
                isValid = false;
            } else if (!parts[0].matches("^[a-zA-Z0-9._%+-]+$")) {
                txtCorreo.setError("El nombre de usuario contiene caracteres no permitidos");
                if (isValid) {
                    txtCorreo.requestFocus();  // Enfocar en el campo correo si es el primer error encontrado
                }
                isValid = false;
            } else if (correo.contains(" ")) {
                txtCorreo.setError("El correo no puede contener espacios");
                if (isValid) {
                    txtCorreo.requestFocus();  // Enfocar en el campo correo si es el primer error encontrado
                }
                isValid = false;
            } else if (correo.length() > 30) {
                txtCorreo.setError("El correo no puede tener más de 30 caracteres");
                if (isValid) {
                    txtCorreo.requestFocus();  // Enfocar en el campo correo si es el primer error encontrado
                }
                isValid = false;
            } else if (!EMAIL_PATTERN.matcher(correo).matches()) {
                txtCorreo.setError("Ingrese un correo válido");
                if (isValid) {
                    txtCorreo.requestFocus();  // Enfocar en el campo correo si es el primer error encontrado
                }
                isValid = false;
            }
        }

        // Validación de la clave
        String clave = user.getClave().trim();  // Eliminar espacios en blanco al inicio y al final
        if (clave.isEmpty()) {
            txtClave.setError("La clave no puede estar vacía");
            if (isValid) {
                txtClave.requestFocus();  // Enfocar en el campo clave si es el primer error encontrado
            }
            isValid = false;
        } else if (clave.length() < 8) {
            txtClave.setError("La clave debe tener al menos 8 caracteres");
            if (isValid) {
                txtClave.requestFocus();  // Enfocar en el campo clave si es el primer error encontrado
            }
            isValid = false;
        } else if (clave.length() > 20) {
            txtClave.setError("La clave no puede tener más de 20 caracteres");
            if (isValid) {
                txtClave.requestFocus();  // Enfocar en el campo clave si es el primer error encontrado
            }
            isValid = false;
        }


        if (!isValid) {
            return;  // Salir si hay errores
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        if (DAO_Cliente.ConsultarDni(user.getDNI())) {
            // DNI INGRESADO YA SE ENCUENTRA REGISTRADO EN LA BD
            Toast.makeText(this, "DNI ya registrado", Toast.LENGTH_SHORT).show();
        } else {
            // DNI INGRESADO NO ESTÁ REGISTRADO
            if (DAO_Cliente.ConsultarCorreo(user.getCorreo())) {
                Toast.makeText(this, "CORREO ya registrado", Toast.LENGTH_SHORT).show();
            } else {
                // USUARIO REGISTRADO CORRECTAMENTE
                String msg = DAO_Cliente.insertarCLI(user);
                MostrarMensaje.mensaje(msg, this, Login_Activity.class);
            }
        }
    }

    private void regresar() {
        Intent iIniciarSesion = new Intent(this, Login_Activity.class);
        startActivity(iIniciarSesion);
        finish();
    }
    private void cargarActividadIniciar() {
        Intent iIniciarSesion = new Intent(this, Login_Activity.class);
        startActivity(iIniciarSesion);
        finish();
    }

}
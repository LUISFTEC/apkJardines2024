package com.example.myproyect.actividades;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.myproyect.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

public class ActivityCrudLosas extends AppCompatActivity {

    private EditText etLosaNombre, etLosaDescripcion;
    private ImageView ivLosaVistaPrevia;
    private Button btnSubirFoto, btnAñadirLosa, btnRegresar;

    private Uri imageUri;
    private FirebaseDatabase database;
    private FirebaseStorage storage;
    private DatabaseReference dbRef;

    private String losaKey;

    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null && result.getData().getData() != null) {
                    imageUri = result.getData().getData();
                    ivLosaVistaPrevia.setImageURI(imageUri);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_losas);

        // Inicializar Firebase
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        dbRef = database.getReference("losas");

        etLosaNombre = findViewById(R.id.et_losa_nombre);
        etLosaDescripcion = findViewById(R.id.et_losa_descripcion);
        ivLosaVistaPrevia = findViewById(R.id.iv_losa_vista_previa);
        btnSubirFoto = findViewById(R.id.btn_subir_foto);
        btnAñadirLosa = findViewById(R.id.btn_añadir_losa);
        btnRegresar = findViewById(R.id.btn_regresar);

        btnRegresar.setOnClickListener(v -> finish());

        btnSubirFoto.setOnClickListener(v -> openFileChooser());

        // Verificar si es modo edición
        if (getIntent().hasExtra("key")) {
            losaKey = getIntent().getStringExtra("key");
            Log.d("ActivityCrudLosas", "Received key: " + losaKey);
            etLosaNombre.setText(getIntent().getStringExtra("nombre"));
            etLosaDescripcion.setText(getIntent().getStringExtra("descripcion"));
            Glide.with(this).load(getIntent().getStringExtra("imageUrl")).into(ivLosaVistaPrevia);

            btnAñadirLosa.setText("Actualizar Losa");
            btnAñadirLosa.setOnClickListener(v -> {
                String nombre = etLosaNombre.getText().toString().trim();
                String descripcion = etLosaDescripcion.getText().toString().trim();

                if (nombre.isEmpty()) {
                    etLosaNombre.setError("El nombre es obligatorio");
                    return;
                }

                if (descripcion.isEmpty()) {
                    etLosaDescripcion.setError("La descripción es obligatoria");
                    return;
                }

                if (imageUri != null) {
                    btnAñadirLosa.setEnabled(false); // Deshabilitar el botón para evitar múltiples clics
                    uploadImage(nombre, descripcion, true);
                } else {
                    updateLosaInDatabase(nombre, descripcion, getIntent().getStringExtra("imageUrl"));
                }
            });
        } else {
            btnAñadirLosa.setOnClickListener(v -> {
                String nombre = etLosaNombre.getText().toString().trim();
                String descripcion = etLosaDescripcion.getText().toString().trim();

                if (nombre.isEmpty()) {
                    etLosaNombre.setError("El nombre es obligatorio");
                    return;
                }

                if (descripcion.isEmpty()) {
                    etLosaDescripcion.setError("La descripción es obligatoria");
                    return;
                }

                if (imageUri != null) {
                    btnAñadirLosa.setEnabled(false); // Deshabilitar el botón para evitar múltiples clics
                    uploadImage(nombre, descripcion, false);
                } else {
                    Toast.makeText(ActivityCrudLosas.this, "Por favor, seleccione una imagen", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activityResultLauncher.launch(intent);
    }

    private void uploadImage(String nombre, String descripcion, boolean isEdit) {
        if (imageUri != null) {
            StorageReference storageRef = storage.getReference();
            StorageReference fileReference = storageRef.child("images/" + UUID.randomUUID().toString());

            fileReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        if (isEdit) {
                            Log.d("ActivityCrudLosas", "Updating losa with new image URL: " + imageUrl);
                            updateLosaInDatabase(nombre, descripcion, imageUrl);
                        } else {
                            addLosaToDatabase(nombre, descripcion, imageUrl);
                        }
                    }))
                    .addOnFailureListener(e -> {
                        Toast.makeText(ActivityCrudLosas.this, "Fallo al subir la imagen: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        btnAñadirLosa.setEnabled(true); // Rehabilitar el botón si hay un error
                    });
        }
    }

    private void addLosaToDatabase(String nombre, String descripcion, String imageUrl) {
        String losaId = dbRef.push().getKey();

        Losa losa = new Losa(nombre, descripcion, imageUrl);
        dbRef.child(losaId).setValue(losa)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(ActivityCrudLosas.this, "Losa añadida con éxito", Toast.LENGTH_SHORT).show();
                    clearFields(); // Limpiar los campos después de añadir una losa
                    btnAñadirLosa.setEnabled(true); // Rehabilitar el botón después del éxito
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ActivityCrudLosas.this, "Error al añadir la losa", Toast.LENGTH_SHORT).show();
                    btnAñadirLosa.setEnabled(true); // Rehabilitar el botón si hay un error
                });
    }

    private void updateLosaInDatabase(String nombre, String descripcion, String imageUrl) {
        Log.d("ActivityCrudLosas", "Updating losa with key: " + losaKey);
        Losa losa = new Losa(nombre, descripcion, imageUrl);
        dbRef.child(losaKey).setValue(losa)
                .addOnSuccessListener(aVoid -> {
                    Log.d("ActivityCrudLosas", "Losa updated successfully");
                    Toast.makeText(ActivityCrudLosas.this, "Losa actualizada con éxito", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.e("ActivityCrudLosas", "Error al actualizar la losa", e);
                    Toast.makeText(ActivityCrudLosas.this, "Error al actualizar la losa", Toast.LENGTH_SHORT).show();
                    btnAñadirLosa.setEnabled(true); // Rehabilitar el botón si hay un error
                });
    }

    private void clearFields() {
        etLosaNombre.setText("");
        etLosaDescripcion.setText("");
        ivLosaVistaPrevia.setImageURI(null);
        imageUri = null;
    }
}

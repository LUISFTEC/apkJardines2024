package com.example.myproyect.actividades;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myproyect.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ActivityCrudLosas extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText etLosaNombre, etLosaDescripcion;
    private ImageView ivLosaVistaPrevia;
    private Button btnSubirFoto, btnAñadirLosa, btnRegresar;

    private Uri imageUri;
    private FirebaseDatabase database;
    private FirebaseStorage storage;

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

        etLosaNombre = findViewById(R.id.et_losa_nombre);
        etLosaDescripcion = findViewById(R.id.et_losa_descripcion);
        ivLosaVistaPrevia = findViewById(R.id.iv_losa_vista_previa);
        btnSubirFoto = findViewById(R.id.btn_subir_foto);
        btnAñadirLosa = findViewById(R.id.btn_añadir_losa);
        btnRegresar = findViewById(R.id.btn_regresar);

        btnRegresar.setOnClickListener(v -> finish());

        btnSubirFoto.setOnClickListener(v -> openFileChooser());

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
                uploadImage(nombre, descripcion);
            } else {
                Toast.makeText(ActivityCrudLosas.this, "Por favor, seleccione una imagen", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activityResultLauncher.launch(intent);
    }

    private void uploadImage(String nombre, String descripcion) {
        if (imageUri != null) {
            StorageReference storageRef = storage.getReference();
            StorageReference fileReference = storageRef.child("images/" + UUID.randomUUID().toString());

            fileReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl = uri.toString();
                                    addLosaToDatabase(nombre, descripcion, imageUrl);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ActivityCrudLosas.this, "Fallo al subir la imagen: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            btnAñadirLosa.setEnabled(true); // Rehabilitar el botón si hay un error
                        }
                    });
        }
    }

    private void addLosaToDatabase(String nombre, String descripcion, String imageUrl) {
        DatabaseReference dbRef = database.getReference("losas");
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

    private void clearFields() {
        etLosaNombre.setText("");
        etLosaDescripcion.setText("");
        ivLosaVistaPrevia.setImageURI(null);
        imageUri = null;
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

package com.example.myproyect.actividades;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myproyect.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ActivityCrudLosas extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText etLosaNombre, etLosaDescripcion;
    private ImageView ivLosaVistaPrevia;
    private Button btnSubirFoto, btnAñadirLosa, btnRegresar;

    private Uri imageUri;
    private FirebaseFirestore db;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_losas);

        // Inicializar Firebase
        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();
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
            String nombre = etLosaNombre.getText().toString();
            String descripcion = etLosaDescripcion.getText().toString();
            if (imageUri != null) {
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
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            ivLosaVistaPrevia.setImageURI(imageUri);
        }
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
                                    addLosaToFirestore(nombre, descripcion, imageUrl);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@androidx.annotation.NonNull Exception e) {
                            Toast.makeText(ActivityCrudLosas.this, "Fallo al subir la imagen: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void addLosaToFirestore(String nombre, String descripcion, String imageUrl) {
        Map<String, Object> losa = new HashMap<>();
        losa.put("nombre", nombre);
        losa.put("descripcion", descripcion);
        losa.put("imageUrl", imageUrl);

        db.collection("losas")
                .add(losa)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(ActivityCrudLosas.this, "Losa añadida con éxito", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ActivityCrudLosas.this, "Error al añadir la losa", Toast.LENGTH_SHORT).show();
                });
    }
}

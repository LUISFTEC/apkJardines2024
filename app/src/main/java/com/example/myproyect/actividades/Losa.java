package com.example.myproyect.actividades;

public class Losa {
    private String key;
    private String nombre;
    private String descripcion;
    private String imageUrl;

    // Constructor vacío necesario para Firebase
    public Losa() {
    }

    public Losa(String nombre, String descripcion, String imageUrl) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imageUrl = imageUrl;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

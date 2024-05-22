package com.example.myproyect.actividades.entidades;

public class CanchaDeportiva {
    private int id;
    private String nombre;
    private String descripcion;
    private String horario ;
    private String direccion;
    private Boolean mantenimiento;
    private double precio;

    private  String nombre_tabla;

    public CanchaDeportiva(String nombre, String descripcion, String horario, String direccion, Boolean mantenimiento, double precio) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.horario = horario;
        this.direccion = direccion;
        this.mantenimiento = mantenimiento;
        this.precio = precio;
    }
    public CanchaDeportiva(String nombre, int id, String nombre_tabla){
        this.nombre = nombre;
        this.id = id;
        this.nombre_tabla = nombre_tabla;
    }

    public CanchaDeportiva(){

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Boolean getMantenimiento() {
        return mantenimiento;
    }

    public void setMantenimiento(Boolean mantenimiento) {
        this.mantenimiento = mantenimiento;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getNombre_tabla() {
        return nombre_tabla;
    }

    public void setNombre_tabla(String nombre_tabla) {
        this.nombre_tabla = nombre_tabla;
    }
}

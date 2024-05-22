package com.example.myproyect.actividades.entidades;
//HORARIO1: 3PM - 4PM
//HORARIO2: 5PM - 6PM
//HORARIO3: 7PM - 8PM
public class Reserva {

    private String dia; //fecha = '2023-12-31'
    //private boolean[] arrayB = new boolean[3]; // true= ocupado - false = libre;
    private String[] arrayDni = new String[3]; // null = libre ; !null = ocupado
    private int id_losa;


    public Reserva(String dia, String[] arrayDni, int id_losa) {
        this.dia = dia;
        this.arrayDni = arrayDni;
        this.id_losa = id_losa;
    }
    public Reserva(String dia, String[] arrayDni) {
        this.dia = dia;
        this.arrayDni = arrayDni;
        this.id_losa = id_losa;
    }
    public Reserva() {

    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String[] getArrayDni() {
        return arrayDni;
    }

    public void setArrayDni(String[] arrayDni) {
        this.arrayDni = arrayDni;
    }

    public int getId_losa() {
        return id_losa;
    }

    public void setId_losa(int id_losa) {
        this.id_losa = id_losa;
    }
}

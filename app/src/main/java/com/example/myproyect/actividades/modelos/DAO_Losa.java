package com.example.myproyect.actividades.modelos;

import android.util.Log;

import com.example.myproyect.actividades.actividades.Login_Activity;
import com.example.myproyect.actividades.conexion.ConexionMySQL;
import com.example.myproyect.actividades.entidades.CanchaDeportiva;
import com.example.myproyect.actividades.entidades.Reserva;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DAO_Losa {
    public static String consultarNombre(int id){
        String nom= "";
        try{
            Connection cnx= ConexionMySQL.getConexion();
            Statement statement = cnx.createStatement();
            String sql="SELECT nombre_losa FROM tb_losa where id= "+id;
            ResultSet rs= statement.executeQuery(sql);
            if(rs.next()) nom = rs.getString(1);
            ConexionMySQL.cerrarConexion(cnx);
        }catch(Exception e){System.out.println("Error consultarNombre(): "+e);}
        return nom;
    }
    public static  List<CanchaDeportiva> listarNombres(){
        List<CanchaDeportiva> lista = new ArrayList<>();
        try {
            Connection cnx= ConexionMySQL.getConexion();
            Statement statement = cnx.createStatement();
            String sql="SELECT id,nombre_losa,nombre_tabla FROM tb_losa order by id asc";
            ResultSet rs= statement.executeQuery(sql);
            while(rs.next()){
                int id = rs.getInt(1);
                String nom = rs.getString(2);
                String nom_tb = rs.getString(3);

                lista.add(new CanchaDeportiva(nom,id, nom_tb) );
            }

            ConexionMySQL.cerrarConexion(cnx);
        }catch (Exception e){
            Log.d("DAO", "ERROR DAO[LOSA] listarNombres"+e);
        }
        return  lista;
    }

}

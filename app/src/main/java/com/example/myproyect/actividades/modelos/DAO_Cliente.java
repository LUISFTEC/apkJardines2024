package com.example.myproyect.actividades.modelos;

import android.os.StrictMode;

import com.example.myproyect.actividades.actividades.Login_Activity;
import com.example.myproyect.actividades.conexion.ConexionMySQL;
import com.example.myproyect.actividades.entidades.Usuario;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class DAO_Cliente {
    public static final String tabla = "CLIENTE";


    public static ArrayList<Usuario> listarClientes() {//PARA V. MANTENIMIENTO DE EMPLEADO
        ArrayList<Usuario> lista = new ArrayList<>();
        Connection cnx = null;
        try {
            cnx = ConexionMySQL.getConexion();
            Statement statement = cnx.createStatement();
            String consulta = "SELECT * FROM "+tabla;
            ResultSet rs = statement.executeQuery(consulta);
            Usuario user;
            while (rs.next()) {
                String DNI = rs.getString(1);
                String nom = rs.getString(2);
                String ape = rs.getString(3);
                String correo = rs.getString(4);
                String clave = rs.getString(5);
                String cel = rs.getString(6);
                String fecha = rs.getString(7);
                user = new Usuario(DNI, nom, ape, correo, clave, cel, fecha);
                lista.add(user);
            }
        } catch (Exception e) {
            System.out.println("ERROR[DAO_CLI] listarClientes(): " + e);
        }
        ConexionMySQL.cerrarConexion(cnx);
        return lista;
    }

    public static Usuario ObtenerCLI(String correo, String pass){
        //PARA V. LOGIN
        Usuario user = null;
        boolean b= false;
        try{
            Connection cnx=ConexionMySQL.getConexion();
            Statement statement = cnx.createStatement();
            String consulta = "SELECT * FROM "+tabla+" WHERE CORREO_CLI= '"+correo+"' AND CONTRA_CLI = '"+pass+"' ";


            ResultSet rs= statement.executeQuery(consulta);
            if(rs.next()) {
                String dni = rs.getString(1);
                String nom = rs.getString(2);
                String ape = rs.getString(3);
                String email = rs.getString(4);
                String clave = rs.getString(5);
                String cel = rs.getString(6);
                String fecha = rs.getString(7);
                user = new Usuario(dni,nom,ape,email,clave,cel,fecha);
            }

            ConexionMySQL.cerrarConexion(cnx);
        }catch(Exception e){System.out.println("ERROR[DAO_CLI] ObtenerCLI(): "+e);}

        return user;
    }

    public static String insertarCLI(Usuario user){
        String msg=null;
        try{
            Connection cnx=ConexionMySQL.getConexion();
            CallableStatement csta=	cnx.prepareCall("{call sp_InsertarCLI(?,?,?,?,?,?)}");
            csta.setString(1, user.getDNI());
            csta.setString(2, user.getNombre());
            csta.setString(3, user.getApellido());
            csta.setString(4, user.getCorreo());
            csta.setString(5, user.getClave());
            csta.setString(6, user.getCelular());
            csta.executeUpdate();
            msg="Usuario registrado correctamente";
            ConexionMySQL.cerrarConexion(cnx);
        }catch(Exception e){
            System.out.println("ERROR[DAO_CLI] insertarCLI(): " +e);
            msg= "Error al registrar!";
        }

        return msg;
    }

    public static boolean ConsultarCorreo(String correo){
        //PARA V. REGISTRARSE & RESET_PASS
        boolean b = false;
        try{
            Connection cnx=ConexionMySQL.getConexion();
            CallableStatement csta=cnx.prepareCall("{call sp_consultarCorreoCLI(?)}");
            csta.setString(1, correo);
            ResultSet rs= csta.executeQuery();
            if(rs.next()) b= true;
            ConexionMySQL.cerrarConexion(cnx);
        }catch(Exception e){System.out.println("Error[DAO_CLI] ConsultarCorreo(): "+e);}
        return b;
    }
    public static boolean ConsultarCelular(String celular){

        boolean b = false;
        try{
            Connection cnx=ConexionMySQL.getConexion();
            CallableStatement csta=cnx.prepareCall("{call sp_ConsultarCelularCLI(?)}");
            csta.setString(1, celular);
            ResultSet rs= csta.executeQuery();
            if(rs.next()) b= true;
            ConexionMySQL.cerrarConexion(cnx);
        }catch(Exception e){System.out.println("ERROR[DAO_CLI] ConsultarCelular(): "+e);}
        return b;
    }


    public static boolean ConsultarDni(String dni){//PARA CONFIRMAR EL RESET PASS
        //PARA V. RESET PASS
        boolean b= false;
        try{
            Connection cnx=ConexionMySQL.getConexion();
            CallableStatement csta = cnx.prepareCall("{call sp_consultarDniCLI(?)}");
            csta.setString(1, dni);
            ResultSet rs= csta.executeQuery();
            if(rs.next()) b = true;
            ConexionMySQL.cerrarConexion(cnx);
        }catch(Exception e){System.out.println("ERROR[DAO_CLI] ConsultarDni(): "+e);}
        return b;
    }

    public static String editarPass(String dni, String pass){
        String msg=null;
        try {
            Connection cnx = ConexionMySQL.getConexion();
            CallableStatement psta = cnx.prepareCall("{call sp_editarPassCLI(?,?)}");
            psta.setString(1, dni);
            psta.setString(2, pass);
            psta.executeQuery();
            ConexionMySQL.cerrarConexion(cnx);
            msg="Se actualizó su contraseña";
        } catch (Exception e) {
            System.out.println("ERROR[DAO_CLI] editarPass: "+e);
            msg="Error al actualizar la contraseña";
        }
        return msg;
    }

    public static  String updateDatos(String correo, String celular){
        String msg= "Error: ";
        String correo_login = Login_Activity.getUsuario().getCorreo();
        String cel_login = Login_Activity.getUsuario().getCelular();
        String dni = Login_Activity.getUsuario().getDNI();
        boolean retorno = false;

        if(!correo_login.equals(correo) ){
            if(ConsultarCorreo(correo)) {
                msg = msg+ "Correo ya existe ";
                retorno = true;
            }
        }
        if(!cel_login.equals(celular)){
            if(ConsultarCelular(celular)){
                msg = msg+" Celular ya existe ";
                retorno = true;
            }

        }
        if(retorno) return msg;
        else{
            try {
                Connection cnx = ConexionMySQL.getConexion();
                CallableStatement psta = cnx.prepareCall("{call sp_EditarDatosCLI(?,?,?)}");
                psta.setString(1, dni);
                psta.setString(2, correo);
                psta.setString(3, celular);
                psta.executeQuery();
                ConexionMySQL.cerrarConexion(cnx);
                msg = "Datos actualizados correctamente";
                Login_Activity.usuario.setCorreo(correo);
                Login_Activity.usuario.setCelular(celular);
            } catch (Exception e) {
                System.out.println("ERROR[DAO_CLI] updateDatos(): "+e);
                msg = "Error al actualizar";
            }

            return msg;
        }
    }

    public static String deleteCLI(){
        String msg="";
        String dni = Login_Activity.getUsuario().getDNI();
        try {
            Connection cnx = ConexionMySQL.getConexion();
            CallableStatement psta = cnx.prepareCall("{call sp_EliminarCLI(?)}");
            psta.setString(1, dni);
            psta.executeQuery();
            ConexionMySQL.cerrarConexion(cnx);
            msg = "Usuario eliminado correctamente";

        } catch (Exception e) {
            System.out.println("ERROR[DAO_CLI] deleteCLI(): "+e);
            msg = "Error al eliminar"+e;
        }
        return msg;
    }

}

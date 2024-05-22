package com.example.myproyect.actividades.clases;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class Fecha {
    public static  String dia1,dia6;
    public static String lblTablaReserva;
    private static String zonaHoraria = "America/Lima";

    public static  List<String> obtenerDiasSemanaProximos() {
        List<String> listaDiasSemana = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone(zonaHoraria));

        calendar.add(Calendar.DAY_OF_YEAR, 1); // Avanzar al día de mañana
        int anioActual = calendar.get(Calendar.YEAR);
        int mesActual = calendar.get(Calendar.MONTH);

        DateFormatSymbols dfs = new DateFormatSymbols(new Locale("es", "ES")); //cambiar a español

        String[] nombresMesesAbreviados = dfs.getShortMonths();
        String nombreMesAbreviado = nombresMesesAbreviados[mesActual];
        String[] nombresDiasSemana = dfs.getShortWeekdays();

        for (int i = 0; i < 6; i++) {

            int numeroDiaSemana = calendar.get(Calendar.DAY_OF_WEEK);
            String diaSemana = nombresDiasSemana[numeroDiaSemana];
            String numeroDia = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));

            String cadenaDiaSemana = diaSemana + " " + numeroDia;
            listaDiasSemana.add(cadenaDiaSemana);
            if(i==0){
                dia1 = numeroDia;
            }else if(i==5){
                dia6= numeroDia;
                lblTablaReserva = "SEMANA "+dia1+" - "+dia6+" -> "+nombreMesAbreviado.toUpperCase()+" "+String.valueOf(anioActual);
            }
            calendar.add(Calendar.DAY_OF_YEAR, 1); // Avanzar al siguiente día
        }

        return listaDiasSemana;
    }

    public static List<String>  getFechas(){
        //retornar las fechas de los proximos días
        List<String> fechas = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone(zonaHoraria));
        calendar.add(Calendar.DAY_OF_YEAR, 1); // Saltar al día de mañana

        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        formato.setTimeZone(TimeZone.getTimeZone(zonaHoraria));

        for (int i = 0; i < 6; i++) {
            fechas.add(formato.format(calendar.getTime()));
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        return fechas;
    }

    public static int obtenerNumeroDiaActual() {
        Calendar calendar = Calendar.getInstance();
        int numeroDiaActual = calendar.get(Calendar.DAY_OF_YEAR);
        return numeroDiaActual;
    }



}

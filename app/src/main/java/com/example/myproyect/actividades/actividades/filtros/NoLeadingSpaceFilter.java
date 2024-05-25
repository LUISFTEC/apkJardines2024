package com.example.myproyect.actividades.actividades.filtros;

import android.text.InputFilter;
import android.text.Spanned;

public class NoLeadingSpaceFilter implements InputFilter {
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        // Evitar espacios al inicio
        if (source.length() > 0 && Character.isWhitespace(source.charAt(0)) && dstart == 0) {
            return "";
        }

        // Evitar espacios al final
        if (source.length() > 0 && Character.isWhitespace(source.charAt(source.length() - 1)) && dstart + source.length() == dest.length() + source.length()) {
            return "";
        }

        return null; // Permite que el texto se inserte normalmente
    }
}

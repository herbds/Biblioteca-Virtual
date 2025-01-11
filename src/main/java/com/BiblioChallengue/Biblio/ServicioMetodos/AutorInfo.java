package com.BiblioChallengue.Biblio.ServicioMetodos;
import com.BiblioChallengue.Biblio.Procesamiento.ExtraccionLibro;

import java.util.List;

public class AutorInfo {
    private String fechaNacimiento;
    private String fechaMuerte;
    private List<ExtraccionLibro> libros;

    public AutorInfo(String fechaNacimiento, String fechaMuerte, List<ExtraccionLibro> libros) {
        this.fechaNacimiento = fechaNacimiento;
        this.fechaMuerte = fechaMuerte;
        this.libros = libros;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public String getFechaMuerte() {
        return fechaMuerte;
    }

    public List<ExtraccionLibro> getLibros() {
        return libros;
    }
}

package com.BiblioChallengue.Biblio.ServicioMetodos;
import com.BiblioChallengue.Biblio.Procesamiento.ExtraccionAutores;

import java.util.List;

public class LibroInfo {
    private int descargas;
    private List<String> idiomas;
    private ExtraccionAutores autor;

    public LibroInfo(int descargas, List<String> idiomas, ExtraccionAutores autor) {
        this.descargas = descargas;
        this.idiomas = idiomas;
        this.autor = autor;
    }

    public int getDescargas() {
        return descargas;
    }

    public List<String> getIdiomas() {
        return idiomas;
    }

    public ExtraccionAutores getAutor() {
        return autor;
    }
}
package com.BiblioChallengue.Biblio.ServicioMetodos;

import java.util.List;

public class LibroDescargadoInfo {

    private String titulo;
    private String autor;
    private int descargas;
    private List<String> idiomas;

    public LibroDescargadoInfo(String titulo, String autor, int descargas, List<String> idiomas) {
        this.titulo = titulo;
        this.autor = autor;
        this.descargas = descargas;
        this.idiomas = idiomas;
    }

    // Getters y Setters
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getDescargas() {
        return descargas;
    }

    public void setDescargas(int descargas) {
        this.descargas = descargas;
    }

    public List<String> getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(List<String> idiomas) {
        this.idiomas = idiomas;
    }

    @Override
    public String toString() {
        return "Titulo: " + titulo + ", Autor: " + autor + ", Descargas: " + descargas + ", Idiomas: " + idiomas;
    }
}

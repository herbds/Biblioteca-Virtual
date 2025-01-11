package com.BiblioChallengue.Biblio.Almacenamiento;
import com.BiblioChallengue.Biblio.Procesamiento.Idioma;

import java.util.List;

public class Almacenamiento {

    private String id;
    private String titulo;
    private String autor;
    private List<String> idiomas;
    private String descargas;
    private String fechaNacimiento;
    private String fechaMuerte;

    public Almacenamiento(String id, String titulo, String autor, List<String> idiomas, String descargas, String fechaNacimiento, String fechaMuerte) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.idiomas = idiomas;
        this.descargas = descargas;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaMuerte = fechaMuerte;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public List<String> getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(List<String> idiomas) {
        this.idiomas = idiomas;
    }

    public String getDescargas() {
        return descargas;
    }

    public void setDescargas(String descargas) {
        this.descargas = descargas;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getFechaMuerte() {
        return fechaMuerte;
    }

    public void setFechaMuerte(String fechaMuerte) {
        this.fechaMuerte = fechaMuerte;
    }

    public void limpiar() {
        this.id = "";
        this.titulo = "";
        this.autor = "";
        this.idiomas.clear();
        this.descargas = "";
        this.fechaNacimiento = "";
        this.fechaMuerte = "";
    }

    public void imprimirInfo() {
        System.out.println();
        System.out.println("--------------------------");
        System.out.println("Información del libro:");
        System.out.println();
        System.out.println("Título: " + titulo);
        System.out.println("Autor: " + autor);
        System.out.println("Descargas: " + descargas);
        System.out.println("Idiomas versiones traducidas:");
        for (String idioma : idiomas) {
            String idiomaCompleto = Idioma.obtenerNombrePorCodigo(idioma);
            System.out.println("- " + idiomaCompleto);
        }
        System.out.println();
        System.out.println("Guardando información en base de datos");
        System.out.println("--------------------------");
    }
}

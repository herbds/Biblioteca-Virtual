package com.BiblioChallengue.Biblio.Procesamiento;

import com.BiblioChallengue.Biblio.Almacenamiento.Almacenamiento;
import jakarta.persistence.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name="Libros")
public class ExtraccionLibro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String apiConsulta;
    private String titulo;
    private String descargas;
    private String idiomas;

    // Relación ManyToOne: Cada libro tiene un solo autor
    @ManyToOne(cascade = CascadeType.REFRESH , fetch = FetchType.EAGER)
    @JoinColumn(name = "autor_id")  // Esta es la columna de la relación
    private ExtraccionAutores autor;

    public ExtraccionLibro() {
    }

    public ExtraccionLibro(Almacenamiento libro, ExtraccionAutores autor) {
        this.apiConsulta = libro.getId();
        this.titulo = libro.getTitulo();

        this.idiomas = libro.getIdiomas().stream()
                .map(Idioma::obtenerNombrePorCodigo)
                .collect(Collectors.joining(", "));

        this.descargas = libro.getDescargas();
        this.autor = autor;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApiConsulta() {
        return apiConsulta;
    }

    public void setApiConsulta(String apiConsulta) {
        this.apiConsulta = apiConsulta;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(String idiomas) {
        this.idiomas = idiomas;
    }

    public String getDescargas() {
        return descargas;
    }

    public void setDescargas(String descargas) {
        this.descargas = descargas;
    }

    public ExtraccionAutores getAutor() {
        return autor;
    }

    public void setAutor(ExtraccionAutores autor) {
        this.autor = autor;
    }
}

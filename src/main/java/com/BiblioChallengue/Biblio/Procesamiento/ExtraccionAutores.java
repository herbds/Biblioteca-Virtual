package com.BiblioChallengue.Biblio.Procesamiento;

import com.BiblioChallengue.Biblio.Almacenamiento.Almacenamiento;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name="Autores")
public class ExtraccionAutores {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String autor;
    private String fechasNacimiento;
    private String fechasMuerte;

    @OneToMany(mappedBy = "autor", fetch = FetchType.EAGER , cascade = CascadeType.PERSIST)
    private List<ExtraccionLibro> libros;

    public ExtraccionAutores() {
    }

    public ExtraccionAutores(Almacenamiento libro) {
        this.autor = libro.getAutor();
        this.fechasNacimiento = libro.getFechaNacimiento();
        this.fechasMuerte = libro.getFechaMuerte();
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAutores() {
        return autor;
    }

    public void setAutores(String autor) {
        this.autor = autor;
    }

    public String getFechasNacimiento() {
        return fechasNacimiento;
    }

    public void setFechasNacimiento(String fechasNacimiento) {
        this.fechasNacimiento = fechasNacimiento;
    }

    public String getFechasMuerte() {
        return fechasMuerte;
    }

    public void setFechasMuerte(String fechasMuerte) {
        this.fechasMuerte = fechasMuerte;
    }

    public List<ExtraccionLibro> getLibros() {
        return libros;
    }

    public void setLibros(List<ExtraccionLibro> libros) {
        this.libros = libros;
    }
}

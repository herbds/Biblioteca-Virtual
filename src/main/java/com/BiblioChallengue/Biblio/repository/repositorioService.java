package com.BiblioChallengue.Biblio.repository;

import com.BiblioChallengue.Biblio.Procesamiento.ExtraccionAutores;
import com.BiblioChallengue.Biblio.Procesamiento.ExtraccionLibro;
import com.BiblioChallengue.Biblio.ServicioMetodos.AutorInfo;
import com.BiblioChallengue.Biblio.ServicioMetodos.LibroDescargadoInfo;
import com.BiblioChallengue.Biblio.ServicioMetodos.LibroInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;
import java.util.stream.Collectors;

public interface repositorioService extends JpaRepository<ExtraccionLibro, Long> {

    Optional<ExtraccionLibro> findByTituloIgnoreCase(String titulo);


    default Optional<ExtraccionAutores> findAutorIgnoreCase(String autor) {
        return findAll().stream()
                .filter(libro -> libro.getAutor().getAutores().equalsIgnoreCase(autor))
                .map(ExtraccionLibro::getAutor)
                .findFirst();
    }

    default Map<String, AutorInfo> findAllAutoresConInfo() {
        Map<String, AutorInfo> autoresConInfo = new HashMap<>();

        findAll().forEach(libro -> {
            ExtraccionAutores autor = libro.getAutor();
            String nombreAutor = autor.getAutores();

            if (!autoresConInfo.containsKey(nombreAutor)) {
                List<ExtraccionLibro> librosDelAutor = autor.getLibros();
                AutorInfo infoAutor = new AutorInfo(autor.getFechasNacimiento(), autor.getFechasMuerte(), librosDelAutor);
                autoresConInfo.put(nombreAutor, infoAutor);
            }
        });

        return autoresConInfo;
    }

    default Map<String, LibroInfo> findAllTitulosConInfo() {
        Map<String, LibroInfo> titulosConInfo = new HashMap<>();

        findAll().forEach(libro -> {
            String titulo = libro.getTitulo();
            int descargas = Integer.parseInt(libro.getDescargas());

            // Convertir la cadena de idiomas a una lista
            List<String> idiomas = libro.getIdiomas() != null ?
                    Arrays.asList(libro.getIdiomas().split(", ")) : new ArrayList<>(); // Manejo de null

            ExtraccionAutores autor = libro.getAutor();
            titulosConInfo.put(titulo, new LibroInfo(descargas, idiomas, autor));
        });

        return titulosConInfo;
    }

    default List<ExtraccionLibro> findLibrosByIdioma(String idiomaSeleccionado) {
        List<ExtraccionLibro> librosFiltrados = new ArrayList<>();

        for (ExtraccionLibro libro : findAll()) {
            // Si el libro tiene el idioma seleccionado
            if (libro.getIdiomas() != null && libro.getIdiomas().contains(idiomaSeleccionado)) {
                librosFiltrados.add(libro);
            }
        }

        return librosFiltrados;
    }

    default List<LibroDescargadoInfo> findTopLibrosPorDescargas(int maxResultados) {

        int limit = Math.min(maxResultados, 10);
        List<ExtraccionLibro> libros = findAll();

        List<LibroDescargadoInfo> librosDescargadosInfo = libros.stream()
                .map(libro -> {
                    int descargas = Integer.parseInt(libro.getDescargas());
                    String titulo = libro.getTitulo();
                    String autor = libro.getAutor().getAutores();
                    List<String> idiomas = libro.getIdiomas() != null ?
                            Arrays.asList(libro.getIdiomas().split(", ")) : new ArrayList<>();
                    return new LibroDescargadoInfo(titulo, autor, descargas, idiomas);
                })
                .sorted(Comparator.comparingInt(LibroDescargadoInfo::getDescargas).reversed())
                .collect(Collectors.toList());

        if (librosDescargadosInfo.size() < maxResultados) {
            System.out.println();
            System.out.println("Cantidad de libros registrados insuficientes, libros encontrados: " + librosDescargadosInfo.size());
        }

        return librosDescargadosInfo.stream()
                .limit(limit)
                .collect(Collectors.toList());
    }
}

package com.BiblioChallengue.Biblio.repository;

import com.BiblioChallengue.Biblio.Procesamiento.ExtraccionAutores;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.stream.Collectors;

public interface DuplicadoService extends JpaRepository<ExtraccionAutores, Long> {


    default List<ExtraccionAutores> findAutoresByFechaNacimientoAfter(String nacimiento) {

        int fechaNacimiento = Integer.parseInt(nacimiento);

        return findAll().stream()
                .filter(autor -> {

                    String fechaNacimientoAutor = autor.getFechasNacimiento();

                    if (fechaNacimientoAutor != null && !fechaNacimientoAutor.isEmpty()) {
                        int nacimientoAutor = Integer.parseInt(fechaNacimientoAutor);
                        return nacimientoAutor > fechaNacimiento;
                    }
                    return false;
                })
                .collect(Collectors.toList()); // Recolectar los autores en una lista
    }
    default List<ExtraccionAutores> findAutoresByFechaMuerteBefore(String muerte) {

        int fechaMuerte = Integer.parseInt(muerte);

        return findAll().stream()
                .filter(autor -> {

                    String fechaMuerteAutor = autor.getFechasMuerte();

                    if (fechaMuerteAutor != null && !fechaMuerteAutor.isEmpty()) {
                        int muerteAutor = Integer.parseInt(fechaMuerteAutor);
                        return muerteAutor < fechaMuerte;
                    }
                    return false;
                })
                .collect(Collectors.toList()); // Recolectar los autores en una lista
    }

    default List<ExtraccionAutores> findAutoresByEntreFechas(String fechaInicio, String fechaFin) {

        int fechaInicioInt = Integer.parseInt(fechaInicio);
        int fechaFinInt = Integer.parseInt(fechaFin);

        return findAll().stream()
                .filter(autor -> {
                    String fechaMuerteAutor = autor.getFechasMuerte();
                    String fechaNacimientoAutor = autor.getFechasNacimiento();

                    if (fechaMuerteAutor != null && !fechaMuerteAutor.isEmpty() && fechaNacimientoAutor != null && !fechaNacimientoAutor.isEmpty()) {
                        int nacimientoAutor = Integer.parseInt(fechaNacimientoAutor);
                        int muerteAutor = Integer.parseInt(fechaMuerteAutor);

                        return (nacimientoAutor <= fechaFinInt && muerteAutor >= fechaInicioInt && muerteAutor <= fechaFinInt);
                    }

                    return false;
                })
                .collect(Collectors.toList()); // Recolectamos los autores que cumplen con la condición
    }

}

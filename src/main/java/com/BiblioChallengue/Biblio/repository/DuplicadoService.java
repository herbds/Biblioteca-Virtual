package com.BiblioChallengue.Biblio.repository;

import com.BiblioChallengue.Biblio.Procesamiento.ExtraccionAutores;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.stream.Collectors;

public interface DuplicadoService extends JpaRepository<ExtraccionAutores, Long> {

    // Método para obtener autores nacidos después de una fecha
    default List<ExtraccionAutores> findAutoresByFechaNacimientoAfter(String nacimiento) {
        int fechaNacimiento = Integer.parseInt(nacimiento);

        return findAll().stream()
                .filter(autor -> {
                    String fechaNacimientoAutor = autor.getFechasNacimiento();
                    String fechaMuerteAutor = autor.getFechasMuerte(); // Para no considerar autores muertos antes de la fecha

                    if (fechaNacimientoAutor != null && !fechaNacimientoAutor.isEmpty()) {
                        int nacimientoAutor = Integer.parseInt(fechaNacimientoAutor);
                        // El autor debe estar vivo o haber nacido después de la fecha indicada
                        if (fechaMuerteAutor == null || fechaMuerteAutor.isEmpty()) {
                            return nacimientoAutor > fechaNacimiento; // Autores vivos también se consideran
                        } else {
                            int muerteAutor = Integer.parseInt(fechaMuerteAutor);
                            return nacimientoAutor > fechaNacimiento && muerteAutor >= fechaNacimiento; // Autores muertos después de la fecha también se consideran
                        }
                    }
                    return false;
                })
                .collect(Collectors.toList());
    }

    // Método para obtener autores muertos antes de una fecha
    default List<ExtraccionAutores> findAutoresByFechaMuerteBefore(String muerte) {
        int fechaMuerte = Integer.parseInt(muerte);

        return findAll().stream()
                .filter(autor -> {
                    String fechaMuerteAutor = autor.getFechasMuerte();
                    String fechaNacimientoAutor = autor.getFechasNacimiento(); // Verificamos que haya nacido antes

                    if (fechaMuerteAutor != null && !fechaMuerteAutor.isEmpty()) {
                        int muerteAutor = Integer.parseInt(fechaMuerteAutor);
                        if (fechaNacimientoAutor != null && !fechaNacimientoAutor.isEmpty()) {
                            int nacimientoAutor = Integer.parseInt(fechaNacimientoAutor);
                            return muerteAutor < fechaMuerte && nacimientoAutor <= fechaMuerte;
                        }
                        return muerteAutor < fechaMuerte; // Si no tiene fecha de nacimiento registrada
                    }
                    return false;
                })
                .collect(Collectors.toList());
    }

    // Método para obtener autores vivos entre dos fechas
    default List<ExtraccionAutores> findAutoresByEntreFechas(String fechaInicio, String fechaFin) {
        int fechaInicioInt = Integer.parseInt(fechaInicio);
        int fechaFinInt = Integer.parseInt(fechaFin);

        return findAll().stream()
                .filter(autor -> {
                    String fechaMuerteAutor = autor.getFechasMuerte();
                    String fechaNacimientoAutor = autor.getFechasNacimiento();

                    if (fechaNacimientoAutor != null && !fechaNacimientoAutor.isEmpty()) {
                        int nacimientoAutor = Integer.parseInt(fechaNacimientoAutor);
                        int muerteAutor = (fechaMuerteAutor != null && !fechaMuerteAutor.isEmpty()) ?
                                Integer.parseInt(fechaMuerteAutor) : Integer.MAX_VALUE; // Si no tiene fecha de muerte, consideramos que está vivo

                        // Autores nacidos antes de la fechaFin y muertos después de la fechaInicio o vivos durante ese periodo
                        return (nacimientoAutor <= fechaFinInt && muerteAutor >= fechaInicioInt);
                    }
                    return false;
                })
                .collect(Collectors.toList());
    }
}

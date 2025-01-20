package com.BiblioChallengue.Biblio.Principal;

import com.BiblioChallengue.Biblio.Metodos.VersionesSimilares;
import com.BiblioChallengue.Biblio.Procesamiento.ExtraccionAutores;
import com.BiblioChallengue.Biblio.ServicioMetodos.AutorInfo;
import com.BiblioChallengue.Biblio.ServicioMetodos.LibroDescargadoInfo;
import com.BiblioChallengue.Biblio.ServicioMetodos.LibroInfo;
import com.BiblioChallengue.Biblio.repository.DuplicadoService;
import com.BiblioChallengue.Biblio.repository.repositorioService;
import com.BiblioChallengue.Biblio.Procesamiento.ExtraccionLibro;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.HashSet;

public class Principal {
    @Autowired
    private repositorioService Repositorio;
    @Autowired
    private DuplicadoService RepoForAutores;

    public Principal(repositorioService repositorio, DuplicadoService repoForAutores) {
        this.Repositorio = repositorio;
        this.RepoForAutores = repoForAutores;
    }

    public void MenuSelecciones() {
        System.out.println();
        System.out.println("Menu:");
        System.out.println("1. Buscar libros por título");
        System.out.println("2. Mostrar información de libros registrados");
        System.out.println("3. Mostrar información de autores registrados");
        System.out.println("4. Buscar autores por filtros de fechas");
        System.out.println("5. Buscar libros por idioma");
        System.out.println("6. Mostrar top libros más descargados");
        System.out.println("7. Salir");
        System.out.println();
        System.out.print("Introduce una opción: ");
    }

    public void CasoBusquedayAlmacenamiento() {
        Scanner tecladoMenu = new Scanner(System.in);
        System.out.print("Introduce el título del libro que deseas buscar: ");
        String tituloBuscado = tecladoMenu.nextLine().toLowerCase();

        Optional<ExtraccionLibro> libroBuscado = Repositorio.findByTituloIgnoreCase(tituloBuscado);

        if (libroBuscado.isPresent()) {
            ExtraccionLibro libro = libroBuscado.get();
            ArrayList<String> idiomasList = new ArrayList<>(List.of(libro.getIdiomas().split(", ")));

            System.out.println();
            System.out.println("--------------------------");
            System.out.println("Libro encontrado:");
            System.out.println();
            System.out.println("Título: " + libro.getTitulo());
            System.out.println("Autores: " + libro.getAutor().getAutores());
            System.out.println("Descargas: " + libro.getDescargas());
            System.out.println("Idiomas versiones traducidas:");
            for (String idioma : idiomasList) {
                System.out.println("- " + idioma);
            }
            System.out.println("--------------------------");

        } else {
            VersionesSimilares.verificacionesEnTitulos(tituloBuscado, Repositorio, RepoForAutores);
        }
    }

    public void CasoLibrosRegistrados() {
        Map<String, LibroInfo> librosConInfo = Repositorio.findAllTitulosConInfo();

        if (librosConInfo.isEmpty()) {
            System.out.println("No hay libros registrados.");
        } else {
            for (Map.Entry<String, LibroInfo> entry : librosConInfo.entrySet()) {
                String tituloLibro = entry.getKey();
                LibroInfo infoLibro = entry.getValue();

                System.out.println();
                System.out.println("--------------------------");
                System.out.println("Título: " + tituloLibro);
                System.out.println();
                System.out.println("Descargas: " + infoLibro.getDescargas());
                System.out.println("Autor: " + infoLibro.getAutor().getAutores());
                List<String> idiomas = infoLibro.getIdiomas();
                if (idiomas != null && !idiomas.isEmpty()) {
                    System.out.println("Idiomas: " + String.join(", ", idiomas));
                } else {
                    System.out.println("Idiomas: No disponibles");
                }
                System.out.println("--------------------------");
            }
        }
    }

    public void CasoAutoresRegistrados() {
        Map<String, AutorInfo> autoresConInfo = Repositorio.findAllAutoresConInfo();

        if (autoresConInfo.isEmpty()) {
            System.out.println("No hay autores registrados.");
        } else {
            for (Map.Entry<String, AutorInfo> entry : autoresConInfo.entrySet()) {
                String nombreAutor = entry.getKey();
                AutorInfo infoAutor = entry.getValue();

                System.out.println();
                System.out.println("--------------------------");
                System.out.println();
                System.out.println("Autor: " + nombreAutor);
                System.out.println("Fecha de Nacimiento: " + infoAutor.getFechaNacimiento());
                System.out.println("Fecha de Muerte: " + infoAutor.getFechaMuerte());
                System.out.println();
                System.out.println("Libros: ");
                for (ExtraccionLibro libro : infoAutor.getLibros()) {
                    System.out.println(" - " + libro.getTitulo());
                }
                System.out.println("--------------------------");
            }
        }
    }

    public void CasoFechasAutores() {
        Scanner tecladoMenu = new Scanner(System.in);

        System.out.println("Selecciona el filtro:");
        System.out.println("1. Buscar autores nacidos después de una fecha");
        System.out.println("2. Buscar autores muertos antes de una fecha");
        System.out.println("3. Buscar autores vivos entre dos fechas");

        System.out.print("Introduce una opción: ");
        int opcion = 0;
        String input = tecladoMenu.nextLine();
        try {
            opcion = Integer.parseInt(input);
        } catch (Exception e) {
            System.out.println("Por favor, introduce un número válido.");
            return; // Volver a pedir la opción
        }

        List<ExtraccionAutores> autores = new ArrayList<>();
        switch (opcion) {
            case 1:
                System.out.print("Introduce el año de nacimiento: ");
                String Nacimiento = tecladoMenu.nextLine();
                autores = RepoForAutores.findAutoresByFechaNacimientoAfter(Nacimiento);

                if (autores.isEmpty()) {
                    System.out.println("No hay autores nacidos después de " + Nacimiento + ".");
                } else {
                    System.out.println("Autores nacidos después de " + Nacimiento + ":");
                    mostrarAutoresYLibros(autores);
                }
                break;

            case 2:
                System.out.print("Introduce el año de muerte: ");
                String Muerte = tecladoMenu.nextLine();
                autores = RepoForAutores.findAutoresByFechaMuerteBefore(Muerte);

                if (autores.isEmpty()) {
                    System.out.println("No hay autores muertos antes de " + Muerte + ".");
                } else {
                    System.out.println("Autores muertos antes de " + Muerte + ":");
                    mostrarAutoresYLibros(autores);
                }
                break;

            case 3:
                System.out.print("Introduce el año de inicio: ");
                String Inicio = tecladoMenu.nextLine();
                System.out.print("Introduce el año de fin: ");
                String Fin = tecladoMenu.nextLine();
                autores = RepoForAutores.findAutoresByEntreFechas(Inicio, Fin);

                if (autores.isEmpty()) {
                    System.out.println("No hay autores nacidos entre " + Inicio + " y " + Fin + ".");
                } else {
                    System.out.println("Autores nacidos entre " + Inicio + " y " + Fin + ":");
                    mostrarAutoresYLibros(autores);
                }
                break;

            default:
                System.out.println("Opción no válida.");
        }
    }

    private void mostrarAutoresYLibros(List<ExtraccionAutores> autores) {
        for (ExtraccionAutores autor : autores) {
            System.out.println();
            System.out.println("--------------------------");
            System.out.println();
            System.out.println("Autor: " + autor.getAutores());
            System.out.println("Fecha de Nacimiento: " + autor.getFechasNacimiento());
            System.out.println("Fecha de Muerte: " + autor.getFechasMuerte());

            List<ExtraccionLibro> librosDelAutor = autor.getLibros();
            if (librosDelAutor != null && !librosDelAutor.isEmpty()) {
                System.out.println("Libros del autor:");
                for (ExtraccionLibro libro : librosDelAutor) {
                    System.out.println(" - " + libro.getTitulo());
                }
            } else {
                System.out.println("No hay libros asociados a este autor.");
            }
            System.out.println();
            System.out.println("--------------------------");
        }
    }

    public void CasoBuscarLibrosPorIdioma() {
        Scanner tecladoMenu = new Scanner(System.in);

        List<ExtraccionLibro> libros = Repositorio.findAll();
        Set<String> idiomasDisponibles = new HashSet<>();
        for (ExtraccionLibro libro : libros) {
            if (libro.getIdiomas() != null) {
                idiomasDisponibles.addAll(List.of(libro.getIdiomas().split(", ")));
            }
        }

        System.out.println("Idiomas disponibles:");
        for (String idioma : idiomasDisponibles) {
            System.out.println("- " + idioma);
        }

        System.out.print("Introduce el idioma que deseas seleccionar: ");
        String idiomaSeleccionado = tecladoMenu.nextLine();

        if (!idiomasDisponibles.contains(idiomaSeleccionado)) {
            System.out.println("Idioma no disponible.");
            return;
        }

        List<ExtraccionLibro> librosPorIdioma = Repositorio.findLibrosByIdioma(idiomaSeleccionado);

        if (librosPorIdioma.isEmpty()) {
            System.out.println("No se encontraron libros en el idioma seleccionado.");
        } else {
            System.out.println();
            System.out.println("--------------------------");
            System.out.println("Libros en el idioma '" + idiomaSeleccionado + "':");
            System.out.println();
            for (ExtraccionLibro libro : librosPorIdioma) {
                System.out.println("Título: " + libro.getTitulo());
                System.out.println("Descargas: " + libro.getDescargas());
                System.out.println("--------------------------");
            }
        }
    }

    public void CasoTopLibrosMasDescargados() {
        Scanner tecladoMenu = new Scanner(System.in);

        System.out.print("Introduce el número de libros a mostrar (máximo 10): ");
        int maxResultados = 0;
        String input = tecladoMenu.nextLine();
        try {
            maxResultados = Integer.parseInt(input);
        } catch (Exception e) {
            System.out.println("Por favor, introduce un número válido.");
            return;
        }

        List<LibroDescargadoInfo> topLibros = Repositorio.findTopLibrosPorDescargas(maxResultados);

        if (topLibros.isEmpty()) {
            System.out.println("No se encontraron libros en la base de datos.");
        } else {
            System.out.println();
            System.out.println("--------------------------");
            System.out.println("Top de libros más descargados:");
            System.out.println("--------------------------");
            System.out.println();
            System.out.println();
            for (LibroDescargadoInfo libro : topLibros) {
                System.out.println(libro);
                System.out.println("--------------------------");
            }
        }
    }

    public void ejecutarMenu() {
        Scanner tecladoMenu = new Scanner(System.in);

        while (true) {
            MenuSelecciones();

            int opcion = 0;
            System.out.print("Introduce una opción: ");
            String input = tecladoMenu.nextLine();

            try {
                opcion = Integer.parseInt(input);  // Intentamos convertir la entrada a un número
            } catch (Exception exception) {
                System.out.println("Por favor, introduce números, no caracteres.");
                continue;  // Si hay un error, simplemente continuamos solicitando una opción válida
            }

            switch (opcion) {
                case 1:
                    CasoBusquedayAlmacenamiento();
                    break;
                case 2:
                    CasoLibrosRegistrados();
                    break;
                case 3:
                    CasoAutoresRegistrados();
                    break;
                case 4:
                    CasoFechasAutores();
                    break;
                case 5:
                    CasoBuscarLibrosPorIdioma();
                    break;
                case 6:
                    CasoTopLibrosMasDescargados();
                    break;
                case 7:
                    System.out.println("Saliendo...");
                    return;
                default:
                    System.out.println("Opción no válida. Por favor, intenta de nuevo.");
            }
        }
    }
}

package com.BiblioChallengue.Biblio.Metodos;

import com.BiblioChallengue.Biblio.Almacenamiento.Almacenamiento;
import com.BiblioChallengue.Biblio.Procesamiento.ExtraccionAutores;
import com.BiblioChallengue.Biblio.Procesamiento.ExtraccionLibro;
import com.BiblioChallengue.Biblio.repository.DuplicadoService;
import com.BiblioChallengue.Biblio.repository.repositorioService;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.BiblioChallengue.Biblio.Metodos.ObtencionUrl.EjecutarBusqueda;

public class VersionesSimilares {
    private static class LibrosUrl {
        private final String urlBase = "https://gutendex.com/books/?search=";

        public String librosUrlAPI(String texto) {
            String textoBusqueda = texto.toLowerCase().replace(" ", "%20");
            return urlBase + textoBusqueda;
        }
    }

    private static class ProtocoloBusqueda {

        public String reconocimientoLibrosDetalles(String urlFinalLibros) {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlFinalLibros))
                    .build();

            try {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    String body = response.body();
                    JSONObject jsonResponse = new JSONObject(body);
                    JSONArray results = jsonResponse.getJSONArray("results");

                    if (results.length() > 0) {
                        JSONObject libro = results.getJSONObject(0);
                        return libro.getString("title").toLowerCase();
                    }else{
                        System.out.println("No se encontró el titulo");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static void verificacionesEnTitulos(String texto, repositorioService Repositorio, DuplicadoService RepAutores) {
        LibrosUrl librosUrl = new LibrosUrl();
        String urlFinal = librosUrl.librosUrlAPI(texto);

        ProtocoloBusqueda protocoloBusqueda = new ProtocoloBusqueda();
        String titulo = protocoloBusqueda.reconocimientoLibrosDetalles(urlFinal);

        if (titulo != null) {
            Optional<ExtraccionLibro> libroBuscado = Repositorio.findByTituloIgnoreCase(titulo);

            if (libroBuscado.isPresent()) {
                ExtraccionLibro libro = libroBuscado.get();

                ArrayList<String> idiomasList = new ArrayList<>(List.of(libro.getIdiomas().split(", ")));

                System.out.println();
                System.out.println("--------------------------");
                System.out.println("Versión similar encontrada: ");
                System.out.println();
                System.out.println("Título: " + libro.getTitulo());
                System.out.println("Autor: " + libro.getAutor().getAutores());
                System.out.println("Descargas: " + Integer.parseInt(libro.getDescargas()));
                System.out.println("Idiomas versiones traducidas:");
                for (String idioma : idiomasList) {
                    System.out.println("- " + idioma);
                }
                System.out.println("--------------------------");

            } else {
                Almacenamiento libroAlmacenado = EjecutarBusqueda(texto);
                if (libroAlmacenado != null) {
                    libroAlmacenado.imprimirInfo();

                    Optional<ExtraccionAutores> autorEncontrado = Repositorio.findAutorIgnoreCase(libroAlmacenado.getAutor());

                    ExtraccionAutores autor;
                    if (autorEncontrado.isPresent()) {
                        autor = autorEncontrado.get();
                    } else {
                        autor = new ExtraccionAutores(libroAlmacenado);
                        RepAutores.save(autor); // Guardar el nuevo autor en la base de datos
                    }

                    ExtraccionLibro libro = new ExtraccionLibro(libroAlmacenado, autor);
                    Repositorio.save(libro);
                } else {
                    System.out.println("Intente con el nombre del libro en su idioma original.");
                }
            }
        }
    }}
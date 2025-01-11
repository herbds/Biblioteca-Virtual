package com.BiblioChallengue.Biblio.Metodos;

import com.BiblioChallengue.Biblio.Almacenamiento.Almacenamiento;
import com.BiblioChallengue.Biblio.Traduccion.Traduccion;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ObtencionUrl {

    private static List<Almacenamiento> librosDetalles = new ArrayList<>();

    private static class LibrosUrl {
        private final String urlBase = "https://gutendex.com/books/?search=";

        public String librosUrlAPI(String texto) {
            return urlBase + texto;
        }
    }

    private static class ProtocoloBusqueda {

        public List<Almacenamiento> obtenerLibrosDetalles(String urlFinalLibros) {
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

                        String autor = "Varios";
                        String fechaNacimiento = "0000";
                        String fechaMuerte = "0000";
                        List<String> idiomas = new ArrayList<>();
                        String id = "";
                        String titulo = "";
                        String descargas = "";

                        for (int idx = 0; idx < results.length(); idx++) {
                            JSONObject libro = results.getJSONObject(idx);

                            if (idx == 0) {
                                id = String.valueOf(libro.getInt("id"));
                                titulo = libro.getString("title");
                                descargas = libro.optString("download_count");

                                JSONArray autores = libro.getJSONArray("authors");
                                if (autores.length() > 0) {
                                    JSONObject primerAutor = autores.getJSONObject(0);
                                    autor = primerAutor.getString("name");

                                    fechaNacimiento = primerAutor.optString("birth_year", "No hay registro");
                                    fechaMuerte = primerAutor.optString("death_year", "No hay registro");
                                }
                            }

                            JSONArray idiomasArray = libro.getJSONArray("languages");
                            for (int i = 0; i < idiomasArray.length(); i++) {
                                String idioma = idiomasArray.getString(i);
                                if (!idiomas.contains(idioma)) {
                                    idiomas.add(idioma);
                                }
                            }
                        }

                        Almacenamiento libroAlmacenado = new Almacenamiento(
                                id,
                                titulo,
                                autor,
                                idiomas,
                                descargas,
                                fechaNacimiento,
                                fechaMuerte
                        );

                        librosDetalles.add(libroAlmacenado);
                    }

                } else {
                    System.out.println("Error en la respuesta: " + response.statusCode());
                }

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error en la conexión o en la solicitud");
            }

            return librosDetalles;
        }
    }

    private static class ProtocoloTratamientoTraducciones {

        public static String obtenerUrlValida(Map<String, String> traducciones) {
            LibrosUrl libInfoBusqueda = new LibrosUrl();
            List<String> idiomas = List.of("original", "ingles", "frances", "italiano", "portugues");
            String libInfo = null;
            boolean foundValidUrl = false;

            for (String idioma : idiomas) {
                String traduccion = traducciones.get(idioma);
                if (traduccion != null && !traduccion.isEmpty()) {
                    String url = libInfoBusqueda.librosUrlAPI(traduccion);
                    if (esUrlValida(url)) {
                        libInfo = url;
                        foundValidUrl = true;
                        break;
                    }
                }
            }

            if (!foundValidUrl) {
                System.out.println("No se encontró el libro en las traducciones disponibles.");
                System.out.println("Por favor, ingrese el título en su idioma original.");
            }

            return libInfo;
        }

        private static boolean esUrlValida(String url) {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            try {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                JSONObject jsonResponse = new JSONObject(response.body());
                int count = jsonResponse.optInt("count", 0);
                return count > 0;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    private static class BusquedaAPI {
        public static List<Almacenamiento> ejecutarBusqueda(String libInfo) {
            ProtocoloBusqueda busquedaTitulo = new ProtocoloBusqueda();

            return busquedaTitulo.obtenerLibrosDetalles(libInfo);
        }
    }

    public static void limpiarAlmacenamiento() {
        if (!librosDetalles.isEmpty()) {
            librosDetalles.clear();
        }
    }

    public static String obtenerLibroBusqueda(String libros) {
        return libros;
    }

    public static Almacenamiento EjecutarBusqueda(String libros) {
        limpiarAlmacenamiento();

        String libroBusqueda = obtenerLibroBusqueda(libros);
        Map<String, String> traducciones = Traduccion.traducir(libroBusqueda);

        String libInfo = ProtocoloTratamientoTraducciones.obtenerUrlValida(traducciones);

        if (libInfo != null) {
            List<Almacenamiento> librosDetalles = BusquedaAPI.ejecutarBusqueda(libInfo);
            if (!librosDetalles.isEmpty()) {
                return librosDetalles.get(0);
            } else {
                System.out.println("No se encontraron resultados para la búsqueda.");
            }
        } else {
            System.out.println("No se encontró el libro en las traducciones disponibles.");
        }
        return null;
    }
}

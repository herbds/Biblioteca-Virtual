package com.BiblioChallengue.Biblio.Traduccion;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class Traduccion {

    private static class UrlAPI {
        private final String UrlBase = "https://apertium.org/apy/translate?langpair=";

        public String obtenerUrl(String IdiomaBase, String IdiomaDestino, String Busqueda) {
            String texto = Busqueda.toLowerCase().replace(" ", "%20").replace("*", "");
            return UrlBase + IdiomaBase + "%7C" + IdiomaDestino + "&q=" + texto;
        }
    }

    private static class ProtocoloTraduccion {
        public String obtenerTraduccion(String UrlTraduccion) {

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(UrlTraduccion))
                    .build();

            try {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {

                    String body = response.body();
                    JSONObject jsonResponse = new JSONObject(body);
                    JSONObject dataTraducida = jsonResponse.getJSONObject("responseData");

                    return dataTraducida.getString("translatedText");
                } else {
                    return "Error en la traducción: " + response.statusCode();
                }

            } catch (Exception e) {
                e.printStackTrace();
                return "Error en la conexión o en la solicitud";
            }
        }
    }

    public static Map<String, String> traducir(String texto) {
        UrlAPI api = new UrlAPI();
        String urlEng = api.obtenerUrl("spa", "eng", texto);
        String urlFra = api.obtenerUrl("spa", "fra", texto);
        String urlIta = api.obtenerUrl("spa", "ita", texto);
        String urlPor = api.obtenerUrl("spa", "por_BR", texto);

        ProtocoloTraduccion protocolo = new ProtocoloTraduccion();
        String traduccionEng = protocolo.obtenerTraduccion(urlEng).toLowerCase().replace("*", "").replace(" ", "%20");
        String traduccionFra = protocolo.obtenerTraduccion(urlFra).toLowerCase().replace("*", "").replace(" ", "%20");
        String traduccionIta = protocolo.obtenerTraduccion(urlIta).toLowerCase().replace("*", "").replace(" ", "%20");
        String traduccionPor = protocolo.obtenerTraduccion(urlPor).toLowerCase().replace("*", "").replace(" ", "%20");
        String traduccionNone = texto.toLowerCase().replace(" ", "%20");

        Map<String, String> traducciones = new HashMap<>();
        traducciones.put("original", traduccionNone);
        traducciones.put("ingles", traduccionEng);
        traducciones.put("frances", traduccionFra);
        traducciones.put("italiano", traduccionIta);
        traducciones.put("portugues", traduccionPor);

        return traducciones;
    }
}

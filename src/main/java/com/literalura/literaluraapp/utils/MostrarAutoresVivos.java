package com.literalura.literaluraapp.utils;

import com.literalura.literaluraapp.service.ConsumoAPI;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class MostrarAutoresVivos {
    private final ConsumoAPI consumoAPI;
    private final String URL_BASE = "https://gutendex.com/books/";

    public MostrarAutoresVivos(ConsumoAPI consumoAPI) {
        this.consumoAPI = consumoAPI;
    }

    public void mostrarAutoresVivosEnUnDeterminadoAnio() {
        Scanner teclado = new Scanner(System.in);
        System.out.println("Ingrese un año: ");
        int anio = teclado.nextInt();


        int startYear = anio - 1;
        int endYear = anio + 1;


        String url = URL_BASE + "?author_year_start=" + startYear + "&author_year_end=" + endYear;

        String jsonResponse = consumoAPI.obtenerDatos(url);

        // Procesar la respuesta JSON para obtener los autores
        List<String> autoresNombre = procesarRespuestaJson(jsonResponse);

        System.out.println();
        System.out.println("\nLos autores vivos en el año " + anio + " son:");
        System.out.println();

        autoresNombre.forEach(System.out::println);
    }

    private List<String> procesarRespuestaJson(String jsonResponse) {
        List<String> autoresNombre = new ArrayList<>();

        try {
            // Convertir la respuesta JSON a un objeto JSONObject
            JSONObject jsonObject = new JSONObject(jsonResponse);

            // Obtener el array de resultados de autores
            JSONArray results = jsonObject.getJSONArray("results");

            // Iterar sobre cada objeto de autor en los resultados
            for (int i = 0; i < results.length(); i++) {
                JSONObject autorObj = results.getJSONObject(i);

                // Obtener el nombre del autor y agregarlo a la lista de nombres
                JSONArray autoresArray = autorObj.getJSONArray("authors");
                for (int j = 0; j < autoresArray.length(); j++) {
                    JSONObject autor = autoresArray.getJSONObject(j);
                    String nombre = autor.getString("name");
                    autoresNombre.add(nombre);
                }
            }
        } catch (JSONException e) {
            System.out.println("Error al procesar la respuesta JSON: " + e.getMessage());
        }

        return autoresNombre;
    }

}

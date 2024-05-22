package com.literalura.literaluraapp.utils;

import com.literalura.literaluraapp.service.ConsumoAPI;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Scanner;

public class BuscarCopyright {
    private final ConsumoAPI consumoAPI;
    private final String URL_BASE = "https://gutendex.com/books/";
    private Scanner teclado = new Scanner(System.in);

    public BuscarCopyright(ConsumoAPI consumoAPI) {
        this.consumoAPI = consumoAPI;
    }

    public void buscarCopyrightDeLibros() {
        System.out.println("Ingrese el título del libro del cual desea buscar el copyright:");
        String nombreLibro = teclado.nextLine();

        var json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + nombreLibro.replace(" ", "+"));



        // Parseamos el JSON
        JSONObject jsonObject = new JSONObject(json);
        JSONArray results = jsonObject.getJSONArray("results");

        boolean encontrado = false;
        for (int i = 0; i < results.length(); i++) {
            JSONObject libro = results.getJSONObject(i);
            String titulo = libro.getString("title");
            boolean tieneCopyright = libro.getBoolean("copyright");

            // Verificamos si el título coincide
            if (titulo.equalsIgnoreCase(nombreLibro)) {
                encontrado = true;
                if (tieneCopyright) {
                    System.out.println("El libro '" + nombreLibro + "' tiene copyright.");
                } else {
                    System.out.println("El libro '" + nombreLibro + "' no tiene copyright.");
                }
                break;
            }
        }

        if (!encontrado) {
            System.out.println("No se encontró un libro con el título '" + nombreLibro + "'.");
            System.out.println("Recuerde que debe ingresar el nombre exacto del libro, para ello puede utilizar nuestra opcion 1.");
        }
    }
}


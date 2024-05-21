package com.literalura.literaluraapp.utils;

import com.literalura.literaluraapp.model.Autor;
import com.literalura.literaluraapp.model.DatosAutor;
import com.literalura.literaluraapp.model.Libro;
import com.literalura.literaluraapp.service.ConsumoAPI;
import com.literalura.literaluraapp.service.ConvierteDatosAutor;

import java.util.*;
import java.util.stream.Collectors;

public class AutoresEnDerechoPublico {
    private final ConsumoAPI consumoAPI;
    private final ConvierteDatosAutor conversorAutor;
    private final String URL_BASE = "https://gutendex.com/books/";

    public AutoresEnDerechoPublico(ConsumoAPI consumoAPI, ConvierteDatosAutor conversorAutor) {
        this.consumoAPI = consumoAPI;
        this.conversorAutor = conversorAutor;
    }

    public void listarAutoresEnDerechoPublico() {
        try {
            String json = consumoAPI.obtenerDatos(URL_BASE + "?sort");
            List<DatosAutor> datosAutor = conversorAutor.obtenerDatosArray(json, DatosAutor.class);

            Map<String, Autor> autoresMap = new HashMap<>();

            for (DatosAutor datoAutor : datosAutor) {
                String nombre = datoAutor.nombre();
                Autor autor = autoresMap.get(nombre);

                if (autor == null) {
                    autor = new Autor(nombre, datoAutor.fechaDeNacimiento(), datoAutor.fechaDeFallecimiento());
                    autoresMap.put(nombre, autor);
                }

                List<Libro> librosArray = new ArrayList<>();
                autor.setLibros(librosArray);
            }

            List<Autor> autoresOrdenados = autoresMap.values().stream()
                    .filter(a -> a.getFechaDeFallecimiento() < 1954)
                    .collect(Collectors.toList());

            List<Autor> diezAutores = autoresOrdenados.subList(0, Math.min(10, autoresOrdenados.size()));

            for (int i = 0; i < diezAutores.size(); i++) {
                System.out.println((i + 1) + ". " + diezAutores.get(i).getNombre() + "/n" +
                        ", año de fallecimiento: " + diezAutores.get(i).getFechaDeFallecimiento());
            }

        } catch (NullPointerException e) {
            System.out.println("Error ocurrido: " + e.getMessage());
        }
    }
}


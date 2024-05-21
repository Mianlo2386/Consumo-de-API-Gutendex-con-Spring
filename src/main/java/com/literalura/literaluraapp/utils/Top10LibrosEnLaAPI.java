package com.literalura.literaluraapp.utils;

import com.literalura.literaluraapp.model.Autor;
import com.literalura.literaluraapp.model.DatosAutor;
import com.literalura.literaluraapp.model.DatosLibro;
import com.literalura.literaluraapp.model.Libro;
import com.literalura.literaluraapp.service.ConsumoAPI;
import com.literalura.literaluraapp.service.ConvierteDatos;
import com.literalura.literaluraapp.service.ConvierteDatosAutor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Top10LibrosEnLaAPI {
    private final ConsumoAPI consumoAPI;
    private final ConvierteDatos conversor;
    private final ConvierteDatosAutor conversorAutor;
    private final String URL_BASE = "https://gutendex.com/books/";

    public Top10LibrosEnLaAPI(ConsumoAPI consumoAPI, ConvierteDatos conversor, ConvierteDatosAutor conversorAutor) {
        this.consumoAPI = consumoAPI;
        this.conversor = conversor;
        this.conversorAutor = conversorAutor;
    }

    public void top10LibrosEnLaAPI() {
        try {
            String json = consumoAPI.obtenerDatos(URL_BASE + "?sort");

            List<DatosLibro> datosLibros = conversor.obtenerDatosArray(json, DatosLibro.class);
            List<DatosAutor> datosAutor = conversorAutor.obtenerDatosArray(json, DatosAutor.class);

            List<Libro> libros = new ArrayList<>();
            for (int i = 0; i < datosLibros.size(); i++) {
                Autor autor = new Autor(
                        datosAutor.get(i).nombre(),
                        datosAutor.get(i).fechaDeNacimiento(),
                        datosAutor.get(i).fechaDeFallecimiento());

                Libro libro = new Libro(
                        datosLibros.get(i).titulo(),
                        autor,
                        datosLibros.get(i).idioma(),
                        datosLibros.get(i).numeroDeDescargas());
                libros.add(libro);
            }

            libros.sort(Comparator.comparingDouble(Libro::getNumeroDeDescargas).reversed());

            List<Libro> top10Libros = libros.subList(0, Math.min(10, libros.size()));

            for (int i = 0; i < top10Libros.size(); i++) {
                System.out.println((i + 1) + ". " + top10Libros.get(i));
            }

        } catch (NullPointerException e) {
            System.out.println("Error ocurrido: " + e.getMessage());
        }
    }
}

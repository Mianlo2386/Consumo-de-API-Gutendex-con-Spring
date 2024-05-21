package com.literalura.literaluraapp.utils;

import com.literalura.literaluraapp.model.Autor;
import com.literalura.literaluraapp.model.DatosAutor;
import com.literalura.literaluraapp.model.DatosLibro;
import com.literalura.literaluraapp.model.Libro;
import com.literalura.literaluraapp.repository.AutorRepository;
import com.literalura.literaluraapp.repository.LibroRepository;
import com.literalura.literaluraapp.service.ConsumoAPI;
import com.literalura.literaluraapp.service.ConvierteDatos;
import com.literalura.literaluraapp.service.ConvierteDatosAutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class BuscarLibro {
    private List<Libro> libros;
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private ConvierteDatosAutor conversorAutor = new ConvierteDatosAutor();
    private final String URL_BASE = "https://gutendex.com/books/";

    public BuscarLibro(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void buscarLibroPorTitulo() {
        String libroBuscado = IngresoOpcion.ingresoDeOpcion();

        libros = libros != null ? libros : new ArrayList<>();

        Optional<Libro> book = libros.stream()
                .filter(l -> l.getTitulo().toLowerCase()
                        .contains(libroBuscado.toLowerCase()))
                .findFirst();

        if (book.isPresent()) {
            var libroEncontrado = book.get();
            System.out.println(libroEncontrado);
            System.out.println("El libro ya fue cargado anteriormente.");
        } else {
            try {
                DatosLibro datosLibro = getDatosLibro(libroBuscado);

                if (datosLibro != null) {
                    DatosAutor datosAutor = getDatosAutor(libroBuscado);
                    if (datosAutor != null) {
                        List<Autor> autores = autorRepository.findAll();
                        autores = autores != null ? autores : new ArrayList<>();

                        Optional<Autor> writer = autores.stream()
                                .filter(a -> datosAutor.nombre() != null &&
                                        a.getNombre().toLowerCase().contains(datosAutor.nombre().toLowerCase()))
                                .findFirst();

                        Autor autor;
                        if (writer.isPresent()) {
                            autor = writer.get();
                        } else {
                            autor = new Autor(
                                    datosAutor.nombre(),
                                    datosAutor.fechaDeNacimiento(),
                                    datosAutor.fechaDeFallecimiento()
                            );
                            autorRepository.save(autor);
                        }

                        Libro libro = new Libro(
                                datosLibro.titulo(),
                                autor,
                                datosLibro.idioma() != null ? datosLibro.idioma() : Collections.emptyList(),
                                datosLibro.numeroDeDescargas()
                        );

                        libros.add(libro);
                        autor.setLibros(libros);
                        libroRepository.save(libro);

                        System.out.println(libro);
                        System.out.println("Libro guardado exitosamente!");
                    } else {
                        System.out.println("No se encontró el autor para el libro.");
                    }

                } else {
                    System.out.println("No se encontró el libro lamentablemente.");
                }
            } catch (Exception e) {
                System.out.println("Se produjo la siguiente excepción: " + e.getMessage());
            }
        }
    }

    private DatosLibro getDatosLibro(String nombreLibro) {
        var json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + nombreLibro.replace(" ", "+"));
        DatosLibro datos = conversor.obtenerDatos(json, DatosLibro.class);
        return datos;
    }

    private DatosAutor getDatosAutor(String nombreLibro) {
        var json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + nombreLibro.replace(" ", "+"));
        DatosAutor datos = conversorAutor.obtenerDatos(json, DatosAutor.class);
        return datos;
    }
}

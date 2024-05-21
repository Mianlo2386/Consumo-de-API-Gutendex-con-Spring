package com.literalura.literaluraapp.utils;

import com.literalura.literaluraapp.model.Libro;
import com.literalura.literaluraapp.repository.LibroRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ListarLibros {
    private final LibroRepository libroRepository;

    public ListarLibros(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    public void mostrarLibrosRegistrados() {
        List<Libro> libros = new ArrayList<>();
        try {
            libros = libroRepository.findAll();
            libros.stream()
                    .sorted(Comparator.comparing(Libro::getNumeroDeDescargas).reversed())
                    .forEach(System.out::println);
        } catch (NullPointerException e) {
            System.out.println("No se encontraron libros registrados: " + e.getMessage());
        }
    }
}

package com.literalura.literaluraapp.utils;

import com.literalura.literaluraapp.model.Libro;
import com.literalura.literaluraapp.repository.LibroRepository;

import java.util.Comparator;
import java.util.List;

public class MostrarUltimoLibroIngresado {
    private final LibroRepository libroRepository;

    public MostrarUltimoLibroIngresado(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    public void mostrarUltimoLibroIngresado() {
        try {
            List<Libro> libros = libroRepository.findAll();
            if (!libros.isEmpty()) {
                // Ordenamos los libros por ID
                libros.sort(Comparator.comparing(Libro::getId));
                // Obtenemos el último libro ingresado
                Libro ultimoLibro = libros.get(libros.size() - 1);
                System.out.println(ultimoLibro);
            } else {
                System.out.println("No hay libros en el repositorio.");
            }
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
    }
}


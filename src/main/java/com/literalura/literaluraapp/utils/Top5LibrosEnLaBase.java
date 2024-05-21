package com.literalura.literaluraapp.utils;

import com.literalura.literaluraapp.model.Libro;
import com.literalura.literaluraapp.repository.LibroRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Top5LibrosEnLaBase {
    private final LibroRepository libroRepository;

    public Top5LibrosEnLaBase(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    public void top5LibrosEnLaBase() {
        try {
            List<Libro> libros = libroRepository.findAll();
            List<Libro> librosOrdenados = libros.stream()
                    .sorted(Comparator.comparingDouble(Libro::getNumeroDeDescargas).reversed())
                    .collect(Collectors.toList());
            List<Libro> top5Libros = librosOrdenados.subList(0, Math.min(5, librosOrdenados.size()));
            for (int i = 0; i < top5Libros.size(); i++) {
                System.out.println((i + 1) + ". " + top5Libros.get(i));
            }
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
    }
}


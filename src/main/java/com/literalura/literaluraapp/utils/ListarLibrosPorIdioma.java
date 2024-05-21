package com.literalura.literaluraapp.utils;

import com.literalura.literaluraapp.model.Libro;
import com.literalura.literaluraapp.repository.LibroRepository;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ListarLibrosPorIdioma {
    private LibroRepository libroRepository;
    private Scanner teclado;

    public ListarLibrosPorIdioma(LibroRepository libroRepository, Scanner teclado) {
        this.libroRepository = libroRepository;
        this.teclado = teclado;
    }

    public void listarLibrosPorIdioma() {
        List<Libro> libros = libroRepository.findAll();
        List<String> idiomasUnicos = libros.stream()
                .map(Libro::getIdioma)
                .distinct()
                .collect(Collectors.toList());

        System.out.println(" ");
        System.out.println("Ingrese las siglas del idioma que desea buscar: ");
        idiomasUnicos.forEach(idioma -> {
            switch (idioma) {
                case "en":
                    System.out.println("en -> para idioma inglés.");
                    break;
                case "es":
                    System.out.println("es -> para idioma español.");
                    break;
                case "pt":
                    System.out.println("pt -> para idioma portugués.");
                    break;
                default:
                    System.out.println(idioma + " -> idioma desconocido.");
            }
        });

        String idiomaBuscado = teclado.nextLine();
        List<Libro> librosBuscados = libros.stream()
                .filter(l -> l.getIdioma().contains(idiomaBuscado))
                .collect(Collectors.toList());
        librosBuscados.forEach(System.out::println);
    }
}

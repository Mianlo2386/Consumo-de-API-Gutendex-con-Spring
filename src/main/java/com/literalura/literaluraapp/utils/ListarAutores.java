package com.literalura.literaluraapp.utils;

import com.literalura.literaluraapp.model.Autor;
import com.literalura.literaluraapp.model.Libro;
import com.literalura.literaluraapp.repository.AutorRepository;

import java.util.List;

public class ListarAutores {
    private AutorRepository autorRepository;

    public ListarAutores(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    public void mostrarAutoresRegistrados() {
        List<Autor> autores = autorRepository.findAll();
        autores.forEach(autor -> {
            System.out.println("Autor: " + autor.getNombre());
            System.out.println("Fecha de nacimiento: " + autor.getFechaDeNacimiento());
            System.out.println("Fecha de fallecimiento: " + autor.getFechaDeFallecimiento());
            System.out.print("Libros: ");

            if (!autor.getLibros().isEmpty()) {
                boolean first = true;
                for (Libro libro : autor.getLibros()) {
                    if (!first) {
                        System.out.print(", ");
                    }
                    System.out.print(libro.getTitulo());
                    first = false;
                }
            } else {
                System.out.println("Ninguno");
            }
            System.out.println();
            System.out.println("***********************************************************************");
            System.out.println();
        });
    }

}


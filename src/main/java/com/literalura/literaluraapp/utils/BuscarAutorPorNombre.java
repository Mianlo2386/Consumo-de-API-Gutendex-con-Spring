package com.literalura.literaluraapp.utils;

import com.literalura.literaluraapp.model.Autor;
import com.literalura.literaluraapp.repository.AutorRepository;

import java.util.Optional;
import java.util.Scanner;

public class BuscarAutorPorNombre {
    private AutorRepository autorRepository;
    private Scanner teclado;

    public BuscarAutorPorNombre(AutorRepository autorRepository, Scanner teclado) {
        this.autorRepository = autorRepository;
        this.teclado = teclado;
    }

    public void buscarAutorPorNombre() {
        System.out.println("Ingrese el nombre del autor que desea buscar:");
        var nombreAutor = teclado.nextLine();
        Optional<Autor> autorBuscado = autorRepository.findByNombreContainingIgnoreCase(nombreAutor);
        if (autorBuscado.isPresent()) {
            System.out.println(autorBuscado.get());
        } else {
            System.out.println("Autor no encontrado");
        }
    }
}

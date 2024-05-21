package com.literalura.literaluraapp.utils;

import com.literalura.literaluraapp.model.Autor;
import com.literalura.literaluraapp.repository.AutorRepository;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class MostrarAutoresVivos {
    private AutorRepository autorRepository;

    public MostrarAutoresVivos(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    public void mostrarAutoresVivosEnUnDeterminadoAnio() {
        Scanner teclado = new Scanner(System.in);
        System.out.println("Ingrese un año: ");
        int anio = teclado.nextInt();
        List<Autor> autores = autorRepository.findAll();
        List<String> autoresNombre = autores.stream()
                .filter(a -> (a.getFechaDeFallecimiento() >= anio) && (a.getFechaDeNacimiento() <= anio))
                .map(Autor::getNombre)
                .collect(Collectors.toList());
        autoresNombre.forEach(System.out::println);
    }
}

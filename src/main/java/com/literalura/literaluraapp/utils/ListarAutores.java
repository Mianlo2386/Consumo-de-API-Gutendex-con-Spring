package com.literalura.literaluraapp.utils;

import com.literalura.literaluraapp.model.Autor;
import com.literalura.literaluraapp.repository.AutorRepository;

import java.util.List;

public class ListarAutores {
    private AutorRepository autorRepository;

    public ListarAutores(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    public void mostrarAutoresRegistrados() {
        List<Autor> autores = autorRepository.findAll();
        autores.forEach(System.out::println);
    }
}


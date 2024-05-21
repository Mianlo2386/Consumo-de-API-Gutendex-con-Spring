package com.literalura.literaluraapp.service;

import com.literalura.literaluraapp.dto.AutorDTO;
import com.literalura.literaluraapp.model.Autor;
import com.literalura.literaluraapp.repository.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AutorServicio {
    @Autowired
    private AutorRepository repository;

    public List<AutorDTO> obtenerTodosLosAutores() {
        return convierteDatos(repository.findAll());
    }

    public List<AutorDTO> convierteDatos(List<Autor> autor) {
        return autor.stream()
                .map(a -> new AutorDTO(
                        a.getId(),
                        a.getNombre(),
                        a.getFechaDeNacimiento(),
                        a.getFechaDeFallecimiento()
                ))
                .collect(Collectors.toList());
    }
}

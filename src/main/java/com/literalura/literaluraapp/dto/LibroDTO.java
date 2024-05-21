package com.literalura.literaluraapp.dto;

import com.literalura.literaluraapp.model.Autor;

public record LibroDTO(
        Long Id,
        String titulo,
        Autor autor,
        String idioma,
        Double numeroDeDescargas

) {
}

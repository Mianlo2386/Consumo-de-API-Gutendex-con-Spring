package com.literalura.literaluraapp.dto;

public record AutorDTO(
        Long Id,
        String nombre,
        int fechaDeNacimiento,
        int fechaDeFallecimiento
) {
}

package com.literalura.literaluraapp.utils;

import java.util.Scanner;

public class IngresoOpcion {
    private static Scanner teclado = new Scanner(System.in);

    public static String ingresoDeOpcion() {
        System.out.println("Escribe el nombre del libro que deseas buscar: ");
        var nombreLibro = teclado.nextLine();
        return nombreLibro;
    }
}

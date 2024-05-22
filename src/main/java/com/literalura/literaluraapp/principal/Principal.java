package com.literalura.literaluraapp.principal;

import com.literalura.literaluraapp.model.Autor;

import com.literalura.literaluraapp.model.Libro;
import com.literalura.literaluraapp.repository.AutorRepository;
import com.literalura.literaluraapp.repository.LibroRepository;
import com.literalura.literaluraapp.service.ConsumoAPI;
import com.literalura.literaluraapp.service.ConvierteDatos;
import com.literalura.literaluraapp.service.ConvierteDatosAutor;
import com.literalura.literaluraapp.utils.*;

import java.util.*;


public class Principal {
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private ConvierteDatosAutor conversorAutor = new ConvierteDatosAutor();
    private Scanner teclado = new Scanner(System.in);
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;
    private BuscarLibro buscadorLibro;
    private ListarLibros listarLibros;
    private ListarAutores listarAutores;
    private MostrarAutoresVivos mostrarAutoresVivos;
    private ListarLibrosPorIdioma listarLibrosPorIdioma;
    private BuscarAutorPorNombre buscarAutorPorNombre;
    private Top10LibrosEnLaAPI top10LibrosEnLaAPI;
    private Top5LibrosEnLaBase top5LibrosEnLaBase;
    private AutoresEnDerechoPublico autoresEnDerechoPublico;
    private MostrarUltimoLibroIngresado mostrarUltimoLibroIngresado;

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
        this.buscadorLibro = new BuscarLibro(libroRepository, autorRepository);
        this.listarLibros = new ListarLibros(libroRepository);
        this.listarAutores = new ListarAutores(autorRepository);
        this.mostrarAutoresVivos = new MostrarAutoresVivos(autorRepository);
        this.listarLibrosPorIdioma = new ListarLibrosPorIdioma(libroRepository, teclado);
        this.buscarAutorPorNombre = new BuscarAutorPorNombre(autorRepository, teclado);
        this.top10LibrosEnLaAPI = new Top10LibrosEnLaAPI(consumoAPI, conversor, conversorAutor);
        this.top5LibrosEnLaBase = new Top5LibrosEnLaBase(libroRepository);
        this.autoresEnDerechoPublico = new AutoresEnDerechoPublico(consumoAPI, conversorAutor);
        this.mostrarUltimoLibroIngresado = new MostrarUltimoLibroIngresado(libroRepository);
    }

    public void mostrarMenu() {
        BuscarLibro buscadorLibro = new BuscarLibro(libroRepository, autorRepository);

        System.out.println("\n");
        System.out.println("**************************************");
        System.out.println("*      Bienvenido al LiterAlura      *");
        System.out.println("**************************************");

        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                                        
                    Menú:
                                        
                    1 - Buscar libro por título.
                    2 - Listar libros registrados.
                    3 - Listar autores registrados.
                    4 - Listar autores vivos en un determinado año.
                    5 - Listar libros por idioma.
                    6 - Buscar autores por nombre.
                    7 - Top 10 libros en la API.
                    8 - Top 5 libros en la base de datos.
                    9 - Autores en derecho público.
                                                   
                    0 - Salir
                    *****************************************************
                    Elija una opción:
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();
            switch (opcion) {
                case 1:
                    buscadorLibro.buscarLibroPorTitulo();
                    break;
                case 2:
                    listarLibros.mostrarLibrosRegistrados();
                    break;
                case 3:
                    listarAutores.mostrarAutoresRegistrados();
                    break;
                case 4:
                    mostrarAutoresVivos.mostrarAutoresVivosEnUnDeterminadoAnio();
                    break;
                case 5:
                    listarLibrosPorIdioma.listarLibrosPorIdioma();
                    break;
                case 6:
                    buscarAutorPorNombre.buscarAutorPorNombre();
                    break;
                case 7:
                    top10LibrosEnLaAPI.top10LibrosEnLaAPI();
                    break;
                case 8:
                    top5LibrosEnLaBase.top5LibrosEnLaBase();
                    break;
                case 9:
                    autoresEnDerechoPublico.listarAutoresEnDerechoPublico();
                    break;
                case 10:
                    mostrarUltimoLibroIngresado.mostrarUltimoLibroIngresado();
                    break;
                case 0:
                    System.out.println("Gracias por usar LiterAlura. Hasta luego!\n");
                    break;
                default:
                    System.out.println("Por favor ingrese una opción válida.");
            }
        }
        System.exit(0);
    }
}


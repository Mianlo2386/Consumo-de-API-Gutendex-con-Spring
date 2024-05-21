package com.literalura.literaluraapp.principal;



import com.literalura.literaluraapp.model.Autor;
import com.literalura.literaluraapp.model.DatosAutor;
import com.literalura.literaluraapp.model.DatosLibro;
import com.literalura.literaluraapp.model.Libro;
import com.literalura.literaluraapp.repository.AutorRepository;
import com.literalura.literaluraapp.repository.LibroRepository;
import com.literalura.literaluraapp.service.ConsumoAPI;
import com.literalura.literaluraapp.service.ConvierteDatos;
import com.literalura.literaluraapp.service.ConvierteDatosAutor;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private ConvierteDatosAutor conversorAutor = new ConvierteDatosAutor();
    private final String URL_BASE = "https://gutendex.com/books/";
    private Scanner teclado = new Scanner(System.in);
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;
    private List<Libro> libros;
    private List<Autor> autores;
    private Optional<Autor> autorBuscado;

    public void mostrarMenu() {
        System.out.println("************************************");
        System.out.println("*      Bienvenido al LiterAlura    *");
        System.out.println("************************************");

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
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    mostrarLibrosRegistrados();
                    break;
                case 3:
                    mostrarAutoresRegistrados();
                    break;
                case 4:
                    mostrarAutoresVivosEnUnDeterminadoAnio();
                    break;
                case 5:
                    listarLibrosPorIdioma();
                    break;
                case 6:
                    buscarAutorPorNombre();
                    break;
                case 7:
                    top10LibrosEnLaAPI();
                    break;
                case 8:
                    top5LibrosEnLaBase();
                    break;
                case 9:
                    autoresEnDerechoPublico();
                    break;
                case 10:
                    mostrarUltimoLibroIngresado();
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

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    private DatosLibro getDatosLibro(String nombreLibro) {
        var json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + nombreLibro.replace(" ", "+"));
        DatosLibro datos = conversor.obtenerDatos(json, DatosLibro.class);
        return datos;
    }

    private DatosAutor getDatosAutor(String nombreLibro) {
        var json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + nombreLibro.replace(" ", "+"));
        DatosAutor datos = conversorAutor.obtenerDatos(json, DatosAutor.class);
        return datos;
    }

    private String ingresoDeOpcion() {
        System.out.println("Escribe el nombre del libro que deseas buscar: ");
        var nombreLibro = teclado.nextLine();
        return nombreLibro;
    }

    private void mostrarLibrosRegistrados() {
        try {
            List<Libro> libros = libroRepository.findAll();
            libros.stream()
                    .sorted(Comparator.comparing(Libro::getNumeroDeDescargas))
                    .forEach(System.out::println);
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
            libros = new ArrayList<>();
        }
    }

    public void buscarLibroPorTitulo() {

        String libroBuscado = ingresoDeOpcion();

        libros = libros != null ? libros : new ArrayList<>();

        Optional<Libro> book = libros.stream()
                .filter(l -> l.getTitulo().toLowerCase()
                        .contains(libroBuscado.toLowerCase()))
                .findFirst();

        if (book.isPresent()) {
            var libroEncontrado = book.get();
            System.out.println(libroEncontrado);
            System.out.println("El libro ya fue cargado anteriormente.");
        } else {
            try {
                DatosLibro datosLibro = getDatosLibro(libroBuscado);
                //System.out.println(datosLibro);

                if (datosLibro != null) {
                    DatosAutor datosAutor = getDatosAutor(libroBuscado);
                    if (datosAutor != null) {
                        List<Autor> autores = autorRepository.findAll();
                        autores = autores != null ? autores : new ArrayList<>();

                        Optional<Autor> writer = autores.stream()
                                .filter(a -> datosAutor.nombre() != null &&
                                        a.getNombre().toLowerCase().contains(datosAutor.nombre().toLowerCase()))
                                .findFirst();

                        Autor autor;
                        if (writer.isPresent()) {
                            autor = writer.get();
                        } else {
                            autor = new Autor(
                                    datosAutor.nombre(),
                                    datosAutor.fechaDeNacimiento(),
                                    datosAutor.fechaDeFallecimiento()
                            );
                            autorRepository.save(autor);
                        }

                        Libro libro = new Libro(
                                datosLibro.titulo(),
                                autor,
                                datosLibro.idioma() != null ? datosLibro.idioma() : Collections.emptyList(),
                                datosLibro.numeroDeDescargas()
                        );

                        libros.add(libro);
                        autor.setLibros(libros);


                        libroRepository.save(libro);

                        System.out.println(libro);
                        System.out.println("Libro guardado exitosamente!");
                        //mostrarUltimoLibroIngresado();
                    } else {
                        System.out.println("No se encontró el autor para el libro.");
                    }

                } else {
                    System.out.println("No se encontró el libro lamentablemente.");
                }
            } catch (Exception e) {
                System.out.println("Se produjo la siguiente excepción: " + e.getMessage());
            }
        }
    }
    private void mostrarUltimoLibroIngresado() {
        try {
            List<Libro> libros = libroRepository.findAll();
            if (!libros.isEmpty()) {
                // Ordenamos los libros por ID (suponiendo que el ID es autoincremental y refleja el orden de ingreso)
                libros.sort(Comparator.comparing(Libro::getId));
                // Obtenemos el último libro ingresado
                Libro ultimoLibro = libros.get(libros.size() - 1);
                System.out.println(ultimoLibro);
            } else {
                System.out.println("No hay libros en el repositorio.");
            }
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
    }

    public void mostrarAutoresRegistrados() {
        autores = autorRepository.findAll();
        autores.stream()
                .forEach(System.out::println);
    }

    public void mostrarAutoresVivosEnUnDeterminadoAnio() {
        System.out.println("Ingrese un año: ");
        int anio = teclado.nextInt();
        autores = autorRepository.findAll();
        List<String> autoresNombre = autores.stream()
                .filter(a -> (a.getFechaDeFallecimiento() >= anio) && (a.getFechaDeNacimiento() <= anio))
                .map(a -> a.getNombre())
                .collect(Collectors.toList());
        autoresNombre.forEach(System.out::println);
    }

    public void listarLibrosPorIdioma() {
        libros = libroRepository.findAll();
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
            }
        });

        String idiomaBuscado = teclado.nextLine();
        List<Libro> librosBuscados = libros.stream()
                .filter(l -> l.getIdioma().contains(idiomaBuscado))
                .collect(Collectors.toList());
        librosBuscados.forEach(System.out::println);

    }


    public void buscarAutorPorNombre() {
        System.out.println("Ingrese el nombre del autor que desea buscar");
        var nombreAutor = teclado.nextLine();
        autorBuscado = autorRepository.findByNombreContainingIgnoreCase(nombreAutor);
        if (autorBuscado.isPresent()) {
            System.out.println(autorBuscado.get());
        } else {
            System.out.println("Autor no encontrado");
        }
    }

    public void top10LibrosEnLaAPI() {
        try {
            String json = consumoAPI.obtenerDatos(URL_BASE + "?sort");

            List<DatosLibro> datosLibros = conversor.obtenerDatosArray(json, DatosLibro.class);
            List<DatosAutor> datosAutor = conversorAutor.obtenerDatosArray(json, DatosAutor.class);

            List<Libro> libros = new ArrayList<>();
            for (int i = 0; i < datosLibros.size(); i++) {
                Autor autor = new Autor(
                        datosAutor.get(i).nombre(),
                        datosAutor.get(i).fechaDeNacimiento(),
                        datosAutor.get(i).fechaDeFallecimiento());

                Libro libro = new Libro(
                        datosLibros.get(i).titulo(),
                        autor,
                        datosLibros.get(i).idioma(),
                        datosLibros.get(i).numeroDeDescargas());
                libros.add(libro);
            }

            libros.sort(Comparator.comparingDouble(Libro::getNumeroDeDescargas).reversed());

            List<Libro> top10Libros = libros.subList(0, Math.min(10, libros.size()));

            for (int i = 0; i < top10Libros.size(); i++) {
                System.out.println((i + 1) + ". " + top10Libros.get(i));
            }

        } catch (NullPointerException e) {
            System.out.println("Error occurrido: " + e.getMessage());
        }
    }

    public void top5LibrosEnLaBase() {
        try {
            List<Libro> libros = libroRepository.findAll();
            List<Libro> librosOrdenados = libros.stream()
                    .sorted(Comparator.comparingDouble(Libro::getNumeroDeDescargas).reversed())
                    .collect(Collectors.toList());
            List<Libro> top5Libros = librosOrdenados.subList(0, Math.min(5, librosOrdenados.size()));
            for (int i = 0; i < top5Libros.size(); i++) {
                System.out.println((i + 1) + ". " + top5Libros.get(i));
            }
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
            libros = new ArrayList<>();
        }
    }

    public void autoresEnDerechoPublico() {
        try {
            String json = consumoAPI.obtenerDatos(URL_BASE + "?sort");

            List<DatosAutor> datosAutor = conversorAutor.obtenerDatosArray(json, DatosAutor.class);

            Map<String, Autor> autoresMap = new HashMap<>();

            for (DatosAutor datoAutor : datosAutor) {
                String nombre = datoAutor.nombre();
                Autor autor = autoresMap.get(nombre);

                if (autor == null) {
                    autor = new Autor(nombre, datoAutor.fechaDeNacimiento(), datoAutor.fechaDeFallecimiento());
                    autoresMap.put(nombre, autor);
                }

                List<Libro> librosArray = new ArrayList<>();
                autor.setLibros(librosArray);
            }

            List<Autor> autoresOrdenados = autoresMap.values().stream()
                    .filter(a -> a.getFechaDeFallecimiento() < 1954)
                    .collect(Collectors.toList());

            List<Autor> diezAutores = autoresOrdenados.subList(0, Math.min(10, autoresOrdenados.size()));

            for (int i = 0; i < diezAutores.size(); i++) {
                System.out.println((i + 1) + ". " + diezAutores.get(i).getNombre() + "/n" +
                        ", año de fallecimiento: " + diezAutores.get(i).getFechaDeFallecimiento());
            }

        } catch (NullPointerException e) {
            System.out.println("Error occurrido: " + e.getMessage());
        }
    }

}


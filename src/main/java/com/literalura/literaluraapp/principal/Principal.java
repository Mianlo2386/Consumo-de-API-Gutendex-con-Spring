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
import com.literalura.literaluraapp.utils.BuscarLibro;
import com.literalura.literaluraapp.utils.ListarLibros;


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
    private BuscarLibro buscadorLibro;
    private ListarLibros listarLibros;
    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
        this.buscadorLibro = new BuscarLibro(libroRepository, autorRepository);
        this.listarLibros = new ListarLibros(libroRepository);
    }

    public void mostrarMenu() {
        BuscarLibro buscadorLibro = new BuscarLibro(libroRepository, autorRepository);


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
                    buscadorLibro.buscarLibroPorTitulo();
                    break;

                case 2:
                    listarLibros.mostrarLibrosRegistrados();
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

    private void mostrarUltimoLibroIngresado() {
        try {
            List<Libro> libros = libroRepository.findAll();
            if (!libros.isEmpty()) {
                // Ordenamos los libros por ID
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


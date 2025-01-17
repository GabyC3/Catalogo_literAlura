package com.GC.Challenge_literalura.Principal;

import com.GC.Challenge_literalura.Model.*;
import com.GC.Challenge_literalura.Repository.IAutoresRepository;
import com.GC.Challenge_literalura.Repository.ILibrosRepository;
import com.GC.Challenge_literalura.Service.ConsumoApi;
import com.GC.Challenge_literalura.Service.ConvierteDatos;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConvierteDatos conversor = new ConvierteDatos();
    private final static String URL_BASE = "https://gutendex.com/books/?search=";

    public void Menu  () {
        var opcion = -1;
        System.out.println("\nElija la opción a través de su número: ");
        while (opcion != 0) {
            var Opciones = """
                    1 - | Buscar libro por título
                    2 - | Listar libros registrados
                    3 - | Listar autores registrados
                    4 - | Listar autores vivos en un determinado año
                    5 - | Listar libros por idioma
                    6 - | Top 10 libros más descargados
                    7 - | Obtener estadísiticas
                    0 - | Salir
                    """;

            System.out.println(Opciones);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibro();
                    break;
                case 2:
                    librosRegistrados();
                    break;
                case 3:
                    autoresRegistrados();
                    break;
                case 4:
                    autoresPorAño();
                    break;
                case 5:
                    listarPorIdioma();
                    break;
                case 6:
                    topDiezLibros();
                    break;
                case 7:
                    Estaditicas();
                    break;
                case 0:
                    System.out.println("Finalizando aplicación...");
                    break;

                default:
                    System.out.println("Opción no válida, intenta de nuevo\n");
            }

        }
    }

    private Datos getDatosLibros() {
        var nombreLibro = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + nombreLibro.replace(" ", "+"));
        Datos datosLibros = conversor.obtenerDatos(json, Datos.class);
        return datosLibros;
    }


    private IAutoresRepository autoresRepository;
    private ILibrosRepository librosRepository;

    public Principal(IAutoresRepository autoresRepository, ILibrosRepository librosRepository) {
        this.autoresRepository = autoresRepository;
        this.librosRepository = librosRepository;
    }


    private  void buscarLibro() {
        System.out.println("\nIngrese el nombre del libro que desea buscar: ");
        Datos datos = getDatosLibros();

        if (!datos.resultados().isEmpty()) {
            DatosLibros datosLibro = datos.resultados().get(0);
            DatosAutores datosAutores = datosLibro.autor().get(0);
            Libros libro = null;
            Libros libroRepositorio = librosRepository.findByTitulo(datosLibro.titulo());

            if (libroRepositorio != null) {
                System.out.println("Este libro se encuentra en la base de datos");
                System.out.println(libroRepositorio.toString());
            } else {
                Autores autorRepositorio = autoresRepository.findByNameIgnoreCase(datosLibro.autor().get(0).nombreAutor());
                if (autorRepositorio != null) {
                    libro = agregarLibro(datosLibro, autorRepositorio);
                    librosRepository.save(libro);
                    System.out.println("----- EL LIBRO HA SIDO AGREGADO -----\n");
                    System.out.println(libro);
                } else {
                    Autores autor = new Autores(datosAutores);
                    autor = autoresRepository.save(autor);
                    libro = agregarLibro(datosLibro, autor);
                    librosRepository.save(libro);
                    System.out.println("----- EL LIBRO Y AUTOR HAN SIDO AGREGADOS -----\n");
                    System.out.println(libro);
                }
            }
        } else {
            System.out.println("El libro no existe en la lista\n");
        }
    }

    private Libros agregarLibro(DatosLibros datosLibros, Autores autor) {
        if (autor != null) {
            return new Libros(datosLibros, autor);
        } else {
            System.out.println("El autor es null, no se puede crear el libro");
            return null;
        }
    }

    private void librosRegistrados() {
        List<Libros> libros = librosRepository.findAll();
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados");
            return;
        }
        System.out.println("----- LOS LIBROS REGISTRADOS SON: -----\n");
        libros.stream()
                .sorted(Comparator.comparing(Libros::getTitulo))
                .forEach(System.out::println);
    }

    private void autoresRegistrados() {
        List<Autores> autores = autoresRepository.findAll();
        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados\n");
            return;
        }
        System.out.println("----- LOS AUTORES REGISTRADOS SON: -----\n");
        autores.stream()
                .sorted(Comparator.comparing(Autores::getName))
                .forEach(System.out::println);
    }

    private void autoresPorAño() {
        System.out.println("Ingrese el año en el que desea buscar: ");
        var año = teclado.nextInt();
        teclado.nextLine();

        if(año < 0) {
            System.out.println("El año no puede ser negativo, intente de nuevo");
            return;
        }
        List<Autores> autoresPorAño = autoresRepository.findByAñoNacimientoLessThanEqualAndAñoMuerteGreaterThanEqual(año, año);

        if (autoresPorAño.isEmpty()) {
            System.out.println("No hay autores registrados en ese año\n4");
            return;
        }
        System.out.println("----- LOS AUTORES VIVOS REGISTRADOS EN EL AÑO " + año + " SON: -----\n");
        autoresPorAño.stream()
                .sorted(Comparator.comparing(Autores::getName))
                .forEach(System.out::println);
    }

    private void listarPorIdioma() {
        System.out.println("Ingrese el idioma para buscar los libros: ");
        String menu = """
                es - Español
                en - Inglés
                fr - Francés
                pt - Portugués
                """;
        System.out.println(menu);
        var idioma = teclado.nextLine();

        if (!idioma.equals("es") && !idioma.equals("en") && !idioma.equals("fr") && !idioma.equals("pt")) {
            System.out.println("Idioma no válido, intenta de nuevo\n");
            return;
        }

        List<Libros> librosPorIdioma = librosRepository.findByLenguajesContaining(idioma);
        if (librosPorIdioma.isEmpty()) {
            System.out.println("No hay libros registrados en ese idioma\n");
            return;
        }
        System.out.println("----- LOS LIBROS REGISTRADOS EN EL IDIOMA SELECCIONADO SON: -----\n");
        librosPorIdioma.stream()
                .sorted(Comparator.comparing(Libros::getTitulo))
                .forEach(System.out::println);
    }

    private void topDiezLibros() {
        System.out.println("----- LOS 10 LIBROS MÁS DESCARGADOS SON: -----\n");
            var json = consumoApi.obtenerDatos(URL_BASE);
            Datos datos = conversor.obtenerDatos(json, Datos.class);
            List<Libros> libros = new ArrayList<>();

            for (DatosLibros datosLibros : datos.resultados()) {
                Autores autor = new Autores(datosLibros.autor().get(0));
                Libros libro = new Libros(datosLibros, autor);
                libros.add(libro);
            }
            libros.stream()
                    .sorted(Comparator.comparing(Libros::getNumeroDescargas).reversed())
                    .limit(10)
                    .forEach(System.out::println);
    }

    private void Estaditicas() {
        System.out.println("----- ESTADÍSTICAS DE DESCARGAS EN GUTENDEX -----\n");
        var json = consumoApi.obtenerDatos(URL_BASE);
        Datos datos = conversor.obtenerDatos(json, Datos.class);
        DoubleSummaryStatistics estadisticas = datos.resultados().stream()
                    .collect(Collectors.summarizingDouble(DatosLibros::numeroDescargas));
        System.out.println(" Libro con más descargas: " + estadisticas.getMax());
        System.out.println(" Libro con menos descargas: " + estadisticas.getMin());
        System.out.println(" Promedio de descargas: " + estadisticas.getAverage());
        System.out.println("\n");
    }
}
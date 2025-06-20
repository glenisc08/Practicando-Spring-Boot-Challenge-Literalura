package com.aluracursos.literalura.principal;

import com.aluracursos.literalura.dto.Datos;
import com.aluracursos.literalura.dto.DatosAutor;
import com.aluracursos.literalura.dto.DatosLibro;
import com.aluracursos.literalura.modelos.Autor;
import com.aluracursos.literalura.modelos.Idioma;
import com.aluracursos.literalura.modelos.Libro;
import com.aluracursos.literalura.repository.AutorRepository;
import com.aluracursos.literalura.repository.LibroRepository;
import com.aluracursos.literalura.servicio.ConsumoAPI;
import com.aluracursos.literalura.servicio.ConvierteDatos;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private final String URL_BASE = "https://gutendex.com/books/";

    private LibroRepository libroRepository;
    private AutorRepository autorRepository;

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void mostrarMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                -------------------------------------
                Elige una opción:
                1 - Buscar libro por título
                2 - Listar libros registrados
                3 - Listar autores registrados
                4 - Listar autores vivos en un determinado año
                5 - Listar libros por idioma
                6 - Top 10 libros más descargados
                7 - Generar estadísticas de libros
                0 - Salir
                -------------------------------------
                """;
            System.out.println(menu);
            try {
                opcion = Integer.valueOf(teclado.nextLine());

                switch (opcion) {
                    case 1:
                        buscarLibroPorTitulo();
                        break;
                    case 2:
                        listarLibrosRegistrados();
                        break;
                    case 3:
                        listarAutoresRegistrados();
                        break;
                    case 4:
                        listarAutoresVivosPorAno();
                        break;
                    case 5:
                        listarLibrosPorIdioma();
                        break;
                    case 6:
                        mostrarTop10Libros();
                        break;
                    case 7:
                        generarEstadisticasLibros();
                        break;
                    case 0:
                        System.out.println("Cerrando la aplicación LiterAlura. ¡Hasta pronto!");
                        break;
                    default:
                        System.out.println("Opción inválida. Por favor, elige una opción del 0 al 7.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida: Por favor, ingrese un número.");
            }
        }
    }

    private void buscarLibroPorTitulo() {
        System.out.println("Escribe el título del libro que deseas buscar:");
        var tituloLibro = teclado.nextLine();
        var url = URL_BASE + "?search=" + tituloLibro.replace(" ", "+");
        var json = consumoAPI.obtenerDatos(url);

        Datos datos = conversor.obtenerDatos(json, Datos.class);

        if (datos != null && !datos.resultados().isEmpty()) {
            DatosLibro datosLibro = datos.resultados().get(0);

            Libro libroExistente = libroRepository.findByTituloContainsIgnoreCase(datosLibro.titulo());
            if (libroExistente != null) {
                System.out.println("\n--- ¡ATENCIÓN! ---");
                System.out.println("Este libro ya está registrado en la base de datos.");
                System.out.println("Información del libro ya registrado:");
                System.out.println(libroExistente);
                return;
            }

            Autor autor = null;
            if (datosLibro.autores() != null && !datosLibro.autores().isEmpty()) {
                DatosAutor datosAutor = datosLibro.autores().get(0);
                Optional<Autor> autorExistente = autorRepository.findByNombreContainsIgnoreCase(datosAutor.nombre());

                if (autorExistente.isPresent()) {
                    autor = autorExistente.get();
                    System.out.println("\nAutor ya existente en la base de datos: " + autor.getNombre());
                } else {
                    autor = new Autor(datosAutor.nombre(), datosAutor.anoNacimiento(), datosAutor.anoFallecimiento());
                    autorRepository.save(autor);
                    System.out.println("\nNuevo autor guardado en la base de datos: " + autor.getNombre());
                }
            } else {
                System.out.println("\nNo se encontró información del autor para este libro.");
                return;
            }

            Idioma idioma = null;
            if (datosLibro.idiomas() != null && !datosLibro.idiomas().isEmpty()) {
                try {
                    idioma = Idioma.fromString(datosLibro.idiomas().get(0));
                } catch (IllegalArgumentException e) {
                    System.out.println("Idioma desconocido encontrado para el libro: " + datosLibro.idiomas().get(0));
                }
            } else {
                System.out.println("No se encontró información de idioma para este libro.");
            }

            Libro libro = new Libro(datosLibro.titulo(), idioma, datosLibro.numeroDescargas());
            libro.setAutor(autor);

            libroRepository.save(libro);
            System.out.println("\n--- LIBRO ENCONTRADO Y GUARDADO ---");
            System.out.println(libro);
        } else {
            System.out.println("\nLibro no encontrado. Intenta con otro título.");
        }
    }

    private void listarLibrosRegistrados() {
        List<Libro> libros = libroRepository.findAll();
        if (libros.isEmpty()) {
            System.out.println("\nNo hay libros registrados en la base de datos.");
        } else {
            System.out.println("\n--- LIBROS REGISTRADOS ---");
            libros.forEach(System.out::println);
            System.out.println("--------------------------");
        }
    }

    private void listarAutoresRegistrados() {
        List<Autor> autores = autorRepository.findAll();
        if (autores.isEmpty()) {
            System.out.println("\nNo hay autores registrados en la base de datos.");
        } else {
            System.out.println("\n--- AUTORES REGISTRADOS ---");
            autores.forEach(a -> System.out.println(
                    "Nombre: " + a.getNombre() +
                            ", Año de Nacimiento: " + a.getAnoNacimiento() +
                            ", Año de Fallecimiento: " + (a.getAnoFallecimiento() != null ? a.getAnoFallecimiento() : "N/A")
            ));
            System.out.println("---------------------------");
        }
    }

    private void listarAutoresVivosPorAno() {
        System.out.println("Escribe el año en el que deseas buscar autores vivos:");
        try {
            var ano = Integer.valueOf(teclado.nextLine());
            List<Autor> autoresVivos = autorRepository.findAutoresVivosEnAno(ano);

            if (autoresVivos.isEmpty()) {
                System.out.println("\nNo se encontraron autores vivos en el año " + ano + " registrados en la base de datos.");
            } else {
                System.out.println("\n--- AUTORES VIVOS EN EL AÑO " + ano + " ---");
                autoresVivos.forEach(a -> System.out.println(
                        "Nombre: " + a.getNombre() +
                                ", Año de Nacimiento: " + a.getAnoNacimiento() +
                                ", Año de Fallecimiento: " + (a.getAnoFallecimiento() != null ? a.getAnoFallecimiento() : "N/A")
                ));
                System.out.println("----------------------------------");
            }
        } catch (NumberFormatException e) {
            System.out.println("Año inválido: Por favor, ingrese un número entero para el año.");
        }
    }

    private void listarLibrosPorIdioma() {
        System.out.println("""
            Escribe el idioma para buscar los libros:
            es - Español
            en - Inglés
            fr - Francés
            pt - Portugués
            """);
        var idiomaSeleccionado = teclado.nextLine().trim().toLowerCase();

        try {
            Idioma idioma = Idioma.fromString(idiomaSeleccionado);
            List<Libro> librosPorIdioma = libroRepository.findByIdioma(idioma);

            if (librosPorIdioma.isEmpty()) {
                System.out.println("\nNo se encontraron libros en " + idioma.name() + " registrados en la base de datos.");
            } else {
                System.out.println("\n--- LIBROS EN IDIOMA " + idioma.name() + " ---");
                librosPorIdioma.forEach(System.out::println);
                System.out.println("----------------------------------");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Idioma inválido: Por favor, selecciona uno de la lista (es, en, fr, pt).");
        }
    }

    private void mostrarTop10Libros() {
        List<Libro> top10Libros = libroRepository.findTop10ByNumeroDescargas();

        if (top10Libros.isEmpty()) {
            System.out.println("\nNo hay libros registrados para mostrar el Top 10.");
        } else {
            System.out.println("\n--- TOP 10 LIBROS MÁS DESCARGADOS ---");
            top10Libros.forEach(libro ->
                    System.out.println(
                            "Título: " + libro.getTitulo() +
                                    " | Autor: " + (libro.getAutor() != null ? libro.getAutor().getNombre() : "Desconocido") +
                                    " | Descargas: " + libro.getNumeroDescargas()
                    )
            );
            System.out.println("--------------------------------------");
        }
    }

    private void generarEstadisticasLibros() {
        List<Libro> libros = libroRepository.findAll();

        if (libros.isEmpty()) {
            System.out.println("\nNo hay libros registrados para generar estadísticas.");
            return;
        }

        DoubleSummaryStatistics stats = libros.stream()
                .mapToDouble(Libro::getNumeroDescargas)
                .summaryStatistics();

        System.out.println("\n--- ESTADÍSTICAS DE DESCARGAS DE LIBROS ---");
        System.out.println("Total de libros: " + stats.getCount());
        System.out.println("Número de descargas Mínimo: " + stats.getMin());
        System.out.println("Número de descargas Máximo: " + stats.getMax());
        System.out.println("Promedio de descargas: " + String.format("%.2f", stats.getAverage()));
        System.out.println("Suma total de descargas: " + stats.getSum());
        System.out.println("-------------------------------------------");
    }
}
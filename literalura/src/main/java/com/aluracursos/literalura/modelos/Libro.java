package com.aluracursos.literalura.modelos;

import jakarta.persistence.*;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String titulo;

    @ManyToOne
    @JoinColumn(name = "autor_id")
    private Autor autor;

    @Enumerated(EnumType.STRING)
    private Idioma idioma;

    private Long numeroDescargas;

    public Libro() {}

    public Libro(String titulo, Idioma idioma, Long numeroDescargas) {
        this.titulo = titulo;
        this.idioma = idioma;
        this.numeroDescargas = numeroDescargas;
    }

    // --- Getters y Setters ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public Idioma getIdioma() {
        return idioma;
    }

    public void setIdioma(Idioma idioma) {
        this.idioma = idioma;
    }

    public Long getNumeroDescargas() {
        return numeroDescargas;
    }

    public void setNumeroDescargas(Long numeroDescargas) {
        this.numeroDescargas = numeroDescargas;
    }

    // --- toString() para fácil depuración ---
    @Override
    public String toString() {
        return String.format("----- LIBRO -----%n" +
                        "Título: %s%n" +
                        "Autor: %s%n" +
                        "Idioma: %s%n" +
                        "Número de Descargas: %d%n" +
                        "-----------------%n",
                titulo,
                (autor != null ? autor.getNombre() : "Desconocido"),
                idioma,
                numeroDescargas);
    }
}
package com.aluracursos.literalura.modelos;

public enum Idioma {
    ES("es"), // Español
    EN("en"), // Inglés
    FR("fr"), // Francés
    PT("pt"); // Portugués

    private String idiomaGutendex;

    Idioma(String idiomaGutendex) {
        this.idiomaGutendex = idiomaGutendex;
    }

    public String getIdiomaGutendex() {
        return idiomaGutendex;
    }

    // Método estático para obtener un Idioma a partir de su código Gutendex
    public static Idioma fromString(String text) {
        for (Idioma idioma : Idioma.values()) {
            if (idioma.idiomaGutendex.equalsIgnoreCase(text)) {
                return idioma;
            }
        }
        throw new IllegalArgumentException("Ningún idioma encontrado para el texto: " + text);
    }
}
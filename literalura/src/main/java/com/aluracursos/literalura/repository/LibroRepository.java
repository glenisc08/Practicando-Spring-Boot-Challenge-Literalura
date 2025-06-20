package com.aluracursos.literalura.repository;

import com.aluracursos.literalura.modelos.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    Libro findByTituloContainsIgnoreCase(String titulo);

    List<Libro> findByIdioma(com.aluracursos.literalura.modelos.Idioma idioma);

    // Nuevo método para el Top 10
    @Query("SELECT l FROM Libro l ORDER BY l.numeroDescargas DESC LIMIT 10")
    List<Libro> findTop10ByNumeroDescargas();

    // O, si prefieres usar el método de convencion de Spring Data JPA
    // List<Libro> findTop10ByOrderByNumeroDescargasDesc();
}
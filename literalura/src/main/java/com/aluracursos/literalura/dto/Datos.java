package com.aluracursos.literalura.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true) // Ignora propiedades JSON que no estén definidas aquí
public record Datos(
        @JsonAlias("results") List<DatosLibro> resultados
) {
}

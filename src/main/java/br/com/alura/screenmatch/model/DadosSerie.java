package br.com.alura.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @param titulo
 * @param totalTemporadas
 * @param avaliacao
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosSerie(

        @JsonAlias("Title") String titulo,

        @JsonAlias("Year") String year,

        @JsonAlias("Rated") String rated,

        @JsonAlias("TV-PG") String tvPG,

        @JsonAlias("totalSeasons") Integer totalTemporadas,

        @JsonAlias("imdbRating") String avaliacao
    ) {
}

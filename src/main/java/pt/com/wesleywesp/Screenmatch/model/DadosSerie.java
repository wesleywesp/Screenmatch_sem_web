package pt.com.wesleywesp.Screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosSerie(@JsonAlias("Title") String titulo,
                         @JsonAlias("totalSeasons") Integer totalSeasons,
                         @JsonAlias("Plot") String sinopse,
                         @JsonAlias("imdbRating") String avaliacao,
                         @JsonAlias("Actors")String atores,
                         @JsonAlias("Poster")String poster,
                         @JsonAlias("Genre")String genero,
                         @JsonAlias("Awards")String premios,
                         @JsonAlias("Released")String anoDeLancamento){
}

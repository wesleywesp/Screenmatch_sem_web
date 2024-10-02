package pt.com.wesleywesp.Screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosSerie(@JsonAlias("Title") String titulo,
                         @JsonAlias("totalSeasons") Integer totalSeasons,
                         @JsonAlias("Plot") String plot,
                         @JsonAlias("imdbRating") String avaliacao){
}

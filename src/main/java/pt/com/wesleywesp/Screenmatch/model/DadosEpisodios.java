package pt.com.wesleywesp.Screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosEpisodios(@JsonAlias("Title")String titulo,
                             @JsonAlias("Episode") Integer episodioNumero,
                             @JsonAlias("Released")String dataDeLancamento,
                             @JsonAlias("imdbRating")String avaliacao) {
}

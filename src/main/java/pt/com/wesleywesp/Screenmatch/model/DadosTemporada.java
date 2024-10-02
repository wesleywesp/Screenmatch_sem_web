package pt.com.wesleywesp.Screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosTemporada(@JsonAlias("Title")String title,
                             @JsonAlias("Season") Integer NumeroDaTemporada,
                            @JsonAlias("Episodes") List<DadosEpisodios> episodios ) {
}

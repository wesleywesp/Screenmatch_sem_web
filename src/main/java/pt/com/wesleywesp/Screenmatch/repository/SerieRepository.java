package pt.com.wesleywesp.Screenmatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pt.com.wesleywesp.Screenmatch.model.Categoria;
import pt.com.wesleywesp.Screenmatch.model.Episodios;
import pt.com.wesleywesp.Screenmatch.model.Serie;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {

    Optional<Serie> findByTituloContainingIgnoreCase(String nomeSerie);

    List<Serie> findByatoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(String nomeAtor, Double Avaliacao);

    List<Serie> findTop5ByOrderByAvaliacaoDesc();

    List<Serie>findByGenero(Categoria categoria);





   // List<Serie>findBytotalSeasonsLessThanEqualAndAvaliacaoGreaterThanEqual(int temporadasQuantidade, double avaliacao);

    @Query("select s from Serie s where s.totalSeasons <= :temporadasQuantidade AND s.avaliacao >= :avaliacao")
    List<Serie>SeriePortemporadaEAvaliacao(int temporadasQuantidade, double avaliacao);


    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE e.titulo ILIKE %:trecho%")
    List<Episodios> episodioPorTechoDeTitulo(String trecho);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie ORDER BY e.avaliacao DESC LIMIT 5")
    List<Episodios> topEpisodiosPorSerie(Serie serie);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie AND YEAR(e.dataDeLancamento) >= :anoLancamento")
    List<Episodios> EpisodiosPorSerieEAno(Serie serie, int anoLancamento);
}

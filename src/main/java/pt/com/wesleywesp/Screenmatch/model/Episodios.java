package pt.com.wesleywesp.Screenmatch.model;


import jakarta.persistence.*;

import java.time.DateTimeException;
import java.time.LocalDate;
@Entity
@Table(name ="episodios")
public class Episodios {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer temporada;
    private String titulo;
    private Integer episodioNumero;
    private LocalDate dataDeLancamento;
    private Double avaliacao;

    @ManyToOne
    private Serie serie;

    public Episodios(Integer numeroTemporada, DadosEpisodios dadosEpisodio) {
        this.temporada = numeroTemporada;
        this.titulo = dadosEpisodio.titulo();
        this.episodioNumero = dadosEpisodio.episodioNumero();

        try{
            this.avaliacao = Double.valueOf(dadosEpisodio.avaliacao());

        }catch (NumberFormatException ex){
            this.avaliacao = 0.0;

        }

        try {
            this.dataDeLancamento =LocalDate.parse(dadosEpisodio.dataDeLancamento());

        }catch (DateTimeException ex) {
            this.dataDeLancamento = null;
        }
    }
    public Episodios(){}

    public Serie getSerie() {
        return serie;
    }

    public void setSerie(Serie serie) {
        this.serie = serie;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDataDeLancamento(LocalDate dataDeLancamento) {
        this.dataDeLancamento = dataDeLancamento;
    }

    public Integer getTemporada() {
        return temporada;
    }

    public void setTemporada(Integer temporada) {
        this.temporada = temporada;
    }

    public Integer getEpisodioNumero() {
        return episodioNumero;
    }

    public void setEpisodioNumero(Integer episodioNumero) {
        this.episodioNumero = episodioNumero;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public LocalDate getDataDeLancamento() {
        return dataDeLancamento;
    }

    public void setdataDeLancamento(LocalDate dataDeLancamento) {
        this.dataDeLancamento = dataDeLancamento;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }

    @Override
    public String toString() {
        return
                "temporada=" + temporada +
                ", titulo='" + titulo + '\'' +
                ", episodioNumero=" + episodioNumero +
                ", dataDeLancamento=" + dataDeLancamento +
                ", avaliacao=" + avaliacao;
    }
}

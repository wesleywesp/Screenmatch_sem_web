package pt.com.wesleywesp.Screenmatch.principal;

import pt.com.wesleywesp.Screenmatch.model.*;
import pt.com.wesleywesp.Screenmatch.repository.SerieRepository;
import pt.com.wesleywesp.Screenmatch.service.ConsumirApi;
import pt.com.wesleywesp.Screenmatch.service.ConverteDados;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumirApi consumo = new ConsumirApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = System.getenv("OMDb_API_KEY");

    private List<DadosSerie> dadosSeries = new ArrayList<>();

    private SerieRepository repository;

    private List<Serie> serie = new ArrayList<>();

    private Optional<Serie> SerieBusca;

    public Principal(SerieRepository serieRepository) {
        this.repository = serieRepository;
    }

    public void exibeMenu() {
        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                    1 - Buscar séries
                    2 - Buscar episódios
                    3-  Listar Series buscadas
                    4-  Buscar series por titulo
                    5-  Buscar series por ator
                    6-  Top 5 series
                    7-  Buscar Series por categoria
                    8-  Series com numeros de temporadas especificas
                    9-  Buscar Episodio por trecho de titulo
                    10- Top 5 episodios por série
                    11 - Buscar episodios a partir de uma data
                    0 - Sair                                 
                    """;

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    ListarSeriesBuscadas();
                    break;
                case 4:
                    BuscarSeriePorTitulo();
                    break;
                case 5:
                    BuscarSeriePorAtor();
                case 6:
                    BuscarTop5Series();
                    break;
                case 7:
                    BuscarSeriePorCategoria();
                    break;
                case 8:
                    BuscarNumeroDeTemporadaEAvaliacao();
                    break;
                case 9:
                    BuscarEpisodioPorTrechoDeTitulo();
                    break;
                case 10:
                    TopEpisodiosPorSerie();
                    break;
                case 11:
                    BuscarEpisodioAPartirDeData();

                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }




    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
        //dadosSeries.add(dados);
        repository.save(serie);
        System.out.println(dados);
    }

    private DadosSerie getDadosSerie() {

        System.out.println("Digite o nome da série para busca");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        return dados;
    }

    private void buscarEpisodioPorSerie() {
        ListarSeriesBuscadas();
        //DadosSerie dadosSerie = getDadosSerie();
        System.out.println("Escolha uma serie pelo nome");

        var nomeSerie = leitura.nextLine();

        Optional<Serie> first = serie.stream().filter(n -> n.getTitulo().toLowerCase().contains(nomeSerie.toLowerCase()))
                .findFirst();

        if (first.isPresent()) {

            var serieEncontrada = first.get();

            List<DadosTemporada> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalSeasons(); i++) {
                var json = consumo.obterDados(ENDERECO + serieEncontrada
                        .getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }
            temporadas.forEach(System.out::println);

            List<Episodios> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream().map(e -> new Episodios(d.NumeroDaTemporada(), e)))
                    .collect(Collectors.toList());

            serieEncontrada.setEpisodios(episodios);
            repository.save(serieEncontrada);

        } else {
            System.out.println("serie não encontrada");
        }
    }

    private void ListarSeriesBuscadas() {
        serie = new ArrayList<>();
        serie = repository.findAll();
//        serie = dadosSeries.stream().map(d-> new Serie(d))
//                        .collect(Collectors.toList());
        serie.stream().sorted(Comparator.comparing((Serie::getGenero)))
                .forEach(System.out::println);

    }

    private void BuscarSeriePorTitulo() {
        System.out.println("Escolha uma serie pelo nome");

        var nomeSerie = leitura.nextLine();
        SerieBusca = repository.findByTituloContainingIgnoreCase(nomeSerie);
        if (SerieBusca.isPresent()) {
            System.out.println("Dados da serie: " + SerieBusca.get());
        } else {
            System.out.println("serie não encontrada");
        }
    }

    private void BuscarSeriePorAtor() {

        System.out.println("Digite o nome do ator");

        var nomeAtor = leitura.nextLine();
        System.out.println("avaliação a partir de que valor");
        var avaliacao = leitura.nextDouble();
        List<Serie> SerieEncontradas = repository
                .findByatoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor, avaliacao);

        if (!SerieEncontradas.isEmpty()) {
            System.out.println("Série em que " + nomeAtor + " trabalhou:");
            SerieEncontradas.forEach(s ->
                    System.out.println(s.getTitulo() + " avaliação: " + s.getAvaliacao()));
        } else {
            System.out.println("serie não encontrada");
        }
    }

    private void BuscarTop5Series() {

        System.out.println("Tops5 series");
        List<Serie> serieTop = repository
                .findTop5ByOrderByAvaliacaoDesc();
        serieTop.forEach(s ->
                System.out.println(s.getTitulo() + " avaliação: " + s.getAvaliacao()));
    }

    private void BuscarSeriePorCategoria() {
        System.out.println("Deseja buscar Series de qual categoria");
        var nomeGenero = leitura.nextLine();
        Categoria categoria = Categoria.fromPortugues(nomeGenero);
        List<Serie> seriesPorCategoria = repository.findByGenero(categoria);
        if (!seriesPorCategoria.isEmpty()) {
            System.out.println("Series da categoria " + nomeGenero);
            seriesPorCategoria.forEach(System.out::println);
        } else {
            System.out.println("não tens nenhuma serie nesta categoria");
        }
    }

    private void BuscarNumeroDeTemporadaEAvaliacao() {
        System.out.println("Quantidades de Tesmporadas");
        var temporadasQuantidade = leitura.nextInt();
        leitura.nextLine();
        System.out.println("avaliação a partir de que valor");
        var avaliacao = leitura.nextDouble();
        List<Serie> seriePorTemporadaEAvaliacao = repository
                .SeriePortemporadaEAvaliacao(temporadasQuantidade, avaliacao);
        if (!seriePorTemporadaEAvaliacao.isEmpty()) {
            System.out.println("Série com o total de " + temporadasQuantidade + " e com a avaliação maior ou igual a:");
            seriePorTemporadaEAvaliacao.forEach(s ->
                    System.out.println(s.getTitulo() + " total de temporadas : " + s.getTotalSeasons()
                            + " avaliação: " + s.getAvaliacao()));
        } else {
            System.out.println("serie não encontrada");
        }
    }

    private void BuscarEpisodioPorTrechoDeTitulo() {

        System.out.println("Digite o nome do trecho do titulo");

        var trecho = leitura.nextLine();
        List<Episodios> episodiosEncontrados = repository.episodioPorTechoDeTitulo(trecho);

        if (episodiosEncontrados != null && !episodiosEncontrados.isEmpty()) {
            episodiosEncontrados.forEach(e ->
                    System.out.printf("Série: %s Temporada %s - Episódio %s - %s\n",
                            e.getSerie().getTitulo(), e.getTemporada(),
                            e.getEpisodioNumero(), e.getTitulo()));
        } else {
            System.out.println("Nenhum episódio encontrado com o trecho fornecido.");
        }
    }

    private void TopEpisodiosPorSerie() {
        BuscarSeriePorTitulo();
        if (SerieBusca.isPresent()) {
            Serie serie = SerieBusca.get();
            List<Episodios> topEpisodios = repository.topEpisodiosPorSerie(serie);
            topEpisodios.forEach(e ->
                    System.out.printf("Série: %s Temporada %s - Episódio %s - %s - Avaliação %s\n",
                            e.getSerie().getTitulo(), e.getTemporada(),
                            e.getEpisodioNumero(), e.getTitulo(), e.getAvaliacao()));
        }
    }
    private void BuscarEpisodioAPartirDeData() {
        BuscarSeriePorTitulo();
        if(SerieBusca.isPresent()){
            Serie serie = SerieBusca.get();
            System.out.println("Digite o ano limite de lançamento");
            var anoLancamento = leitura.nextInt();
            leitura.nextLine();

            List<Episodios> episodiosAno = repository.EpisodiosPorSerieEAno(serie , anoLancamento);
            episodiosAno.forEach(e ->
                    System.out.printf("Série: %s Temporada %s - Episódio %s - %s - Avaliação %s " +
                                    "- Data de Lançaamento %S\n",
                            e.getSerie().getTitulo(), e.getTemporada(),
                            e.getEpisodioNumero(), e.getTitulo(), e.getAvaliacao(), e.getDataDeLancamento()));
        }
    }
}



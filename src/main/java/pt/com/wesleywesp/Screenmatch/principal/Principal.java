package pt.com.wesleywesp.Screenmatch.principal;

import org.springframework.beans.factory.annotation.Autowired;
import pt.com.wesleywesp.Screenmatch.model.*;
import pt.com.wesleywesp.Screenmatch.repository.SerieRepository;
import pt.com.wesleywesp.Screenmatch.service.ConsumirApi;
import pt.com.wesleywesp.Screenmatch.service.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumirApi consumo = new ConsumirApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = System.getenv("OMDb_API_KEY");

    private List<DadosSerie>dadosSeries = new ArrayList<>();

    private SerieRepository repository;

    private List<Serie> serie = new ArrayList<>();

public Principal(SerieRepository serieRepository){
    this.repository=serieRepository;
}

    public void exibeMenu() {
        var opcao = -1;
        while (opcao != 0){
            var menu = """
                    1 - Buscar séries
                    2 - Buscar episódios
                    3-  Listar Series buscadas
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

    private void buscarEpisodioPorSerie(){
        ListarSeriesBuscadas();
        //DadosSerie dadosSerie = getDadosSerie();
        System.out.println("Escolha uma serie pelo nome");

        var nomeSerie =leitura.nextLine();

        Optional<Serie> first = serie.stream().filter(n -> n.getTitulo().toLowerCase().contains(nomeSerie.toLowerCase()))
                .findFirst();

        if(first.isPresent()){

            var serieEncontrada = first.get();

        List<DadosTemporada> temporadas = new ArrayList<>();

        for (int i = 1; i <= serieEncontrada.getTotalSeasons(); i++) {
            var json = consumo.obterDados(ENDERECO + serieEncontrada
                    .getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }
        temporadas.forEach(System.out::println);

        List<Episodios>episodios = temporadas.stream()
                .flatMap(d->d.episodios().stream().map(e->new Episodios(d.NumeroDaTemporada(), e)))
                .collect(Collectors.toList());

        serieEncontrada.setEpisodios(episodios);
        repository.save(serieEncontrada);

        }else {
            System.out.println("serie não encontrada");
        }
    }
    private void ListarSeriesBuscadas(){
        serie = new ArrayList<>();
        serie =repository.findAll();
//        serie = dadosSeries.stream().map(d-> new Serie(d))
//                        .collect(Collectors.toList());
        serie.stream().sorted(Comparator.comparing((Serie::getGenero)))
                        .forEach(System.out::println);

    }
}

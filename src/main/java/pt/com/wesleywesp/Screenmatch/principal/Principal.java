package pt.com.wesleywesp.Screenmatch.principal;

import pt.com.wesleywesp.Screenmatch.model.DadosEpisodios;
import pt.com.wesleywesp.Screenmatch.model.DadosSerie;
import pt.com.wesleywesp.Screenmatch.model.DadosTemporada;
import pt.com.wesleywesp.Screenmatch.model.Episodios;
import pt.com.wesleywesp.Screenmatch.service.ConsumirApi;
import pt.com.wesleywesp.Screenmatch.service.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner leitura = new Scanner(System.in);

    private ConverteDados converter = new ConverteDados();

    private ConsumirApi consumirApi = new ConsumirApi();

    private final String API_KEY="&apikey=5fdeae5e&t";

    private final String ENDERECO = "http://www.omdbapi.com/?t=";

    public void exibirMenu()
    {

        System.out.println("Digite uma serie para pesquisa");
        var nomeSerie = leitura.nextLine();
        //apanhar a serie

        var json= consumirApi.obterDados(ENDERECO+nomeSerie.replace(" ","+")+API_KEY);

        DadosSerie dadosSerie =converter.obterDados(json,DadosSerie.class);
        System.out.println(dadosSerie);
        //------------------------------------------------------------
        //apanhar as temporadas/episodios

        List<DadosTemporada> temporada = new ArrayList<>();

        for (int i = 1; i <= dadosSerie.totalSeasons() ; i++) {
            json= consumirApi.obterDados(ENDERECO+nomeSerie.replace(" ","+")+
                    "&season="+ i + API_KEY);
            DadosTemporada dadosTemporada = converter.obterDados(json, DadosTemporada.class);

            temporada.add(dadosTemporada);
        }
        temporada.forEach(t->t.episodios().forEach(e->System.out.println(e.titulo())));

        List<DadosEpisodios> dadosEpisodios = temporada.stream()
                .flatMap(t->t.episodios().stream()).collect(Collectors.toList());


        List<Episodios> episodios = temporada.stream()
                                             .flatMap(t->t.episodios().stream()
                                            .map(d-> new Episodios(t.NumeroDaTemporada(), d)))
                                             .collect(Collectors.toList());

  episodios.forEach(System.out::println);
  System.out.println("digite o nome do episodio!");

  var trechoTitulo = leitura.nextLine();

        Optional<Episodios> epsodioBuscado = episodios.stream()
                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
                .findFirst();

        if(epsodioBuscado.isPresent()){
            System.out.println("episodio encontrado");
            System.out.println("Temporada: "+ epsodioBuscado.get().getTemporada());
        }else {
            System.out.println("episodio não encontrado");
        }


//        System.out.println("\nTOP 5 Episodios");
//        episodios.stream().sorted(Comparator.comparing(Episodios::getAvaliacao)
//                        .reversed())
//                .limit(5)
//                .forEach(System.out::println);
//
//        System.out.println("apartir de qual ano vc deseja ver os epsodios?");
//        var ano = leitura.nextInt();
//        leitura.nextLine();
//
//        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//
//        LocalDate dateBusca = LocalDate.of(ano,1,1);
//        episodios.stream().filter(e-> !(e.getDataDeLancamento() == null && e.getDataDeLancamento().isAfter(dateBusca)))
//                .forEach(e -> System.out.println(
//                        "Temporada: " + e.getTemporada() +
//                                "Episodio: " + e.getTitulo() +
//                                "Data LanÇamaneto: " + e.getDataDeLancamento().format(formatador)
 //               ));

        Map<Integer, Double> avaliacaoPorTemporada = episodios.stream()
                .filter(e-> e.getAvaliacao() > 0.0)
                .collect(Collectors.groupingBy(Episodios::getTemporada,
                        Collectors.averagingDouble(Episodios::getAvaliacao)));
        System.out.println(avaliacaoPorTemporada);

        DoubleSummaryStatistics est = episodios.stream().filter(e-> e.getAvaliacao() > 0.0)
                .collect(Collectors.summarizingDouble(Episodios::getAvaliacao));

        System.out.println("media "+ est.getAverage());
        System.out.println("melhor episodio "+ est.getMax());
        System.out.println("pior episodio "+ est.getMin());
        System.out.println("quantidade " + est.getCount());
    }
}

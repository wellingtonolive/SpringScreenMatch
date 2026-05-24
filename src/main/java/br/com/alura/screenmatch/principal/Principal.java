package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.DadosEpisodio;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();

    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String APIKEY = "&apikey=94399dfc";



    public void exibirMenu(){
        System.out.println("##### SCREEN MATCH ##### ");
        System.out.println("######################## MENU ########################");
        System.out.println("Digite o nome da série para buscar");

        var nomeSerie = leitura.nextLine();
        var urlFindSerie = ENDERECO.concat(nomeSerie.replace(" ", "+")).concat(APIKEY);
        var json = consumoApi.obterDados(urlFindSerie);

        DadosSerie dadosSerie = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dadosSerie.toString());

        List<DadosTemporada> temporadas = new ArrayList<>();

		for(int i =1; i <=dadosSerie.totalTemporadas(); i++){
            var urlFindSerieWithSeason = urlFindSerie.concat("&season=").concat(String.valueOf(i));
			json = consumoApi.obterDados(urlFindSerieWithSeason);
			DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
			temporadas.add(dadosTemporada);
		}

		temporadas.forEach(System.out::println);

        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

        List<DadosEpisodio>  dadosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());

        System.out.println("Top 5 Episodios");
        dadosEpisodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                .limit(5)
                .forEach(System.out::println);

        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(), d))
                ).collect(Collectors.toList());

        episodios.forEach(System.out::println);

        System.out.println("Qual nome do titulo você deseja filtrar ??");
        var trechoTitulo = leitura.nextLine();

        Optional<Episodio> episodioEncontrado = episodios.stream().
                filter(e -> e.getTitulo().toLowerCase().contains(trechoTitulo.toLowerCase()))
                .findFirst();

        if(episodioEncontrado.isPresent()){
            System.out.println("Episodio encontrado: " + episodioEncontrado.get().getTitulo());
        }else{
            System.out.println("Nenhum episodio encontrado");
        }

        System.out.println("A partir de que ano você deseja ver os episódios ??");
        var ano = leitura.nextInt();

        LocalDate dataBusca = LocalDate.of(ano, 1, 1);

        DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        episodios.stream()
                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
                .forEach(e -> System.out.println(
                        "Temporada: " + e.getDataLancamento() +
                                " Episódio: " + e.getTitulo() +
                                " Data Lançamento: " + e.getDataLancamento().format(formatterDate)
                ));


        Map<Integer, Double> avaliacoesPorTemporadas = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio :: getAvaliacao)));

        System.out.println("AVALIAÇÕES POR TEMPORADA: " + avaliacoesPorTemporadas);

        DoubleSummaryStatistics est = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio :: getAvaliacao));

        System.out.println("MÉDIA: " + est.getAverage());
        System.out.println("MELHOR EPISÓDIO: " + est.getMax());
        System.out.println("PIOR EPISÓDIO: " + est.getMin());
        System.out.println("QUANTIDADE DE EPISÓDIOS: " + est.getCount());


    }
}

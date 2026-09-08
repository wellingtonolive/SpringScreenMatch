package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.enums.Categoria;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.model.Serie;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6585022c";
    private List<DadosSerie> dadosSeries = new ArrayList<>();
    private SerieRepository serieRepository;
    private Optional<Serie> serieBusca;

    private List<Serie> series = new ArrayList<>();

    public Principal(SerieRepository serieRepository) {
        this.serieRepository = serieRepository;
    }

    public void exibeMenu() {

        var opcao = -1;
        while (opcao != 0) {

            var menu = """
                    1 - Buscar séries
                    2 - Buscar episódios
                    3 - Listar séries buscadas
                    4 - Buscar serie por título
                    5 - Buscar sério por ator
                    6 - Buscar Top Séris
                    7 - Buscar séries por categoria
                    8 - Buscar séries por avaliação e temporadas
                    9 - Buscar Episodios por Trecho
                    10 - Top Episodios por Serie
                    11 - Busca Episodio por Data
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
                    listarSerieBuscadas();
                    break;
                case 4:
                    buscaSeriePorTitulo();
                    break;
                case 5:
                    buscarSeriesPorAtor();
                    break;
                case 6:
                    buscarTop5Series();
                    break;
                case 7:
                    buscarSeriesPorCategoria();
                    break;
                case 8:
                    filtrarSeriesPorTemporadaEAvaliacao();
                    break;
                case 9:
                    buscarEpisodioPorTrecho();
                    break;
                case 10:
                    topEpisodiosPorSerie();
                    break;
                case 11:
                    buscaEpisodioPorData();
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
        serieRepository.save(serie);
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

        listarSerieBuscadas();
        System.out.println("Escolha uma serie pelo nome: ");
        var nomeSerie = leitura.nextLine();

        Optional<Serie> serie = serieRepository.findByTituloContainingIgnoreCase(nomeSerie);

        if (serie.isPresent()){
            var dadosSerie = serie.get();
            List<DadosTemporada> temporadas = new ArrayList<>();

            for (int i = 1; i <= dadosSerie.getTotalTemporadas(); i++) {
                var json = consumo.obterDados(ENDERECO + dadosSerie.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }
            temporadas.forEach(System.out::println);

            List<Episodio> episodios = temporadas.stream().flatMap(
                    d -> d.episodios().stream().map(
                            e -> new Episodio(d.numero(), e))
                ).collect(Collectors.toList()
            );
            dadosSerie.setEpisodios(episodios);
            serieRepository.save(dadosSerie);

        }else{
            System.out.println("Série não encontrada!");
        }
    }

    private void listarSerieBuscadas() {
        series = serieRepository.findAll();;
        series.stream().
                sorted(Comparator.comparing(Serie:: getGenero))
                .forEach(System.out::println);
    }

    private void buscaSeriePorTitulo() {
        System.out.println("Escolha uma serie pelo nome: ");
        var nomeSerie = leitura.nextLine();

        serieBusca = serieRepository.findByTituloContainingIgnoreCase(nomeSerie);

        if (serieBusca.isPresent()){
            System.out.println("Dados da Serie: " + serieBusca.get());
        }
        else{
            System.out.println("Série não encontrada!");
        }
    }

    private void buscarSeriesPorAtor() {
        System.out.println("Qual nome do ator ???");
        var nomeAtor = leitura.nextLine();
        System.out.println("Avaliações a partir de qual nota ??");
        var avaliacao = leitura.nextDouble();
        List<Serie> seriesEncontradas = serieRepository.findByActoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor, avaliacao);
        System.out.println("Série em que: " + nomeAtor + " trabalhou");
       seriesEncontradas.forEach(s -> System.out.println(s.getTitulo() + " Avaliação " + s.getAvaliacao()));
    }

    private void buscarTop5Series() {

        List<Serie> topSeries = serieRepository.findTop5ByOrderByAvaliacaoDesc();
        topSeries.forEach(s -> System.out.println(s.getTitulo() + " Avaliação " + s.getAvaliacao()));

    }

    private void buscarSeriesPorCategoria(){
        System.out.println("Deseja buscar séris de qual categoria ???");
        var nomeGenero = leitura.nextLine();
        Categoria categoria = Categoria.fromStringEmPortugues(nomeGenero);
        List<Serie> seriesPorCategoria = serieRepository.findByGenero(categoria);
        System.out.println("Serie por categoria");
        seriesPorCategoria.forEach(System.out::println);
    }

    private void filtrarSeriesPorTemporadaEAvaliacao(){
        System.out.println("Filtrar séries até quantas temporadas ??");
        var totalTemporadas = leitura.nextInt();
        System.out.println("Com avaliacao a partir de qual valor???");
        var avaliacao = leitura.nextDouble();
        List<Serie> filtrosSerie = serieRepository.seriesPorTemporadaEAvaliacao(totalTemporadas, avaliacao);
        System.out.println("***Séris Filtradas***");
        filtrosSerie.forEach(s -> System.out.println(s.getTitulo() + " Avaliacao " + s.getAvaliacao()));
    }

    private void buscarEpisodioPorTrecho(){
        System.out.println("Qual nome do episodio para busca ??");
        var trechoEpisodio = leitura.nextLine();
        List<Episodio> episodios = serieRepository.episodioPorTrecho(trechoEpisodio);
        System.out.println("***Episodios Filtradas***");
        episodios.forEach(e->
                System.out.printf("Série: %s Temporada %s - Episódio %s - %s\n",
                        e.getSerie().getTitulo(), e.getTemporada(),e.getNumeroEpisodio(),e.getTitulo()));
    }

    private void topEpisodiosPorSerie(){
        buscaSeriePorTitulo();
        if (serieBusca.isPresent()){
            Serie serie = serieBusca.get();
            List<Episodio> topEpisodios = serieRepository.topEpisodiosPorSerie(serie);
            topEpisodios.forEach(e->
                    System.out.printf("Série: %s Temporada %s - Episódio %s - %s\n",
                            e.getSerie().getTitulo(), e.getTemporada(),e.getNumeroEpisodio(),e.getTitulo()));

        }
    }

    private void buscaEpisodioPorData(){
        buscaSeriePorTitulo();
        if (serieBusca.isPresent()){
            System.out.println("Digite o ano limite de lançamento::");
            var anoLancamento = leitura.nextInt();
            leitura.nextLine();
            List<Episodio> episodiosByAno = serieRepository.episodioPorSerieEAno(serieBusca.get(),anoLancamento);
            episodiosByAno.forEach(e->
                    System.out.printf("Série: %s Temporada %s - Episódio %s - %s\n",
                            e.getSerie().getTitulo(), e.getTemporada(),e.getNumeroEpisodio(),e.getTitulo()));
        }
    }

}
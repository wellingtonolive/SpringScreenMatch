package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.DadosEpisodio;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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


    }
}

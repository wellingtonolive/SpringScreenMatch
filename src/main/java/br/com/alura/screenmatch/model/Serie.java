package br.com.alura.screenmatch.model;

import br.com.alura.screenmatch.enums.Categoria;

public class Serie {

    private String titulo;

    private Categoria genero;

    private String actores;

    private String sinopse;

    private String year;

    private String rated;

    private String tvPG;

    private Integer totalTemporadas;

    private Double avaliacao;

    private String poster;

    public Serie(DadosSerie dadosSerie){
        this.titulo = dadosSerie.titulo();
        this.actores = dadosSerie.actores();
        this.sinopse = dadosSerie.sinopse();
        this.year = dadosSerie.year();
        this.rated = dadosSerie.rated();
        this.tvPG = dadosSerie.tvPG();
        this.totalTemporadas = dadosSerie.totalTemporadas();
        this.poster = dadosSerie.poster();
        this.genero = Categoria.fromString(dadosSerie.genero().split(",")[0].trim());

        try{
            this.avaliacao = Double.valueOf(dadosSerie.avaliacao());
        }catch (NumberFormatException | NullPointerException e){
            this.avaliacao = 0.0;
        }

    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Categoria getGenero() {
        return genero;
    }

    public void setGenero(Categoria genero) {
        this.genero = genero;
    }

    public String getActores() {
        return actores;
    }

    public void setActores(String actores) {
        this.actores = actores;
    }

    public String getSinopse() {
        return sinopse;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getRated() {
        return rated;
    }

    public void setRated(String rated) {
        this.rated = rated;
    }

    public String getTvPG() {
        return tvPG;
    }

    public void setTvPG(String tvPG) {
        this.tvPG = tvPG;
    }

    public Integer getTotalTemporadas() {
        return totalTemporadas;
    }

    public void setTotalTemporadas(Integer totalTemporadas) {
        this.totalTemporadas = totalTemporadas;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    @Override
    public String toString() {
        return  "Título : '" + titulo + '\'' +
                ", Gênero: " + genero +
                ", Atores: '" + actores + '\'' +
                ", Sinopse: '" + sinopse + '\'' +
                ", Ano: '" + year + '\'' +
                ", Rated: '" + rated + '\'' +
                ", TV Paga: '" + tvPG + '\'' +
                ", Total Temporadas: " + totalTemporadas +
                ", Avaliação: " + avaliacao +
                ", Poster: '" + poster + '\'';
    }
}

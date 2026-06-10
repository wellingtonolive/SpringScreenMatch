package br.com.alura.screenmatch.enums;

public enum Categoria {
    ACAO("Action"),
    COMEDIA("Comedy"),
    ROMANCE("Romance"),
    DRAMA("Drama"),
    CRIME("Crime"),
    TERROR("Terror"),
    REALITY_TV("Reality-TV");


    private String categoriaOMDB;

    Categoria(String categoriaOMDB) {
        this.categoriaOMDB = categoriaOMDB;
    }

    public static Categoria fromString(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaOMDB.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
    }

}

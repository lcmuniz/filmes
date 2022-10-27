package com.acme.filmes;

import lombok.Data;

/*
 * A classe Filme representa um filme.
 * A anota��o @Data � usada para criar automaticamente os m�todos get e set
 * para cara atributo da classe.
 */
@Data
public class Filme {
	private Integer id;  // chave do filme
	private String titulo;
	private String tituloOriginal;
	private String direcao;
	private String roteiro;
	private String elenco;
	private String sinopse;
	private String posterUrl;
	private Integer estrelas;
	private Integer ano;
}

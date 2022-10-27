package com.acme.filmes;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

/*
 * Classe respons�vel por responder as requisi��es do usu�rio no navegador
 * A anota��o @Controller � usada para definir que esta
 * classe retorna p�ginas HTML.
 * A anota��o @RequestMapping � usada para definir que esta classe
 * atende a todas as requisi��es que comecem com /filmes
 */
@Controller
@RequestMapping("/filmes")
public class FilmeController {


	private String urlApi = "https://filmes-api-123.herokuapp.com";
	// private String urlApi = "http://localhost:8080";

	// a vari�vel restTemplate � usada para realizar
	// requisi��es para a API de filmes
	private RestTemplate restTemplate = new RestTemplate();

	// m�todo que atende as requi��es GET em http://localhost:8080/filmes
	// retorna uma p�gina HTML com uma lista de filmes do banco de dados.
	@GetMapping
	@SuppressWarnings("unchecked")
	public ModelAndView index(String filtro) {
		if (filtro == null) filtro = "";

		String url = "";
		if (filtro.equals("")) {
			// se n�o foi passado um filtro
			// usa o endpoint abaixo
			url = urlApi + "/filmes";
		}
		else {
			// se foi passado um filtro
			// usa o endpoint abaixo
			url = urlApi + "/filmes/filtrar?texto=" + filtro;
		}

		// chama a api com a url definida acima
		// a api retorna uma lista de filmes
		List<Filme> filmes = restTemplate.getForObject(url, List.class);

		ModelAndView mv = new ModelAndView("index");
		mv.addObject("filmes", filmes); // coloca os filmes no model para serem mostrados na p�gina HTML
		mv.addObject("filtro", filtro); // coloca o filtro no model para ser mostrado na p�gina HTML
		return mv;
	}

	// m�todo que atende as requi��es GET em http://localhost:8080/filmes/{id}
	// exemplo: http://localhost:8080/filmes/1
	// retorna uma p�gina HTML com o filme de id = {id} do banco de dados.
	@GetMapping("{id}")
	public ModelAndView get(@PathVariable Integer id) {

		// chama a api para buscar o filme com id = {id}
		Filme filme = restTemplate.getForObject(urlApi + "/filmes/" + id, Filme.class);

		if (filme == null) {
			// se filme n�o existir, mostra mensagem de erro
			throw new RuntimeException("Filme com ID " + id + " n�o encontrado");
		}
		ModelAndView mv = new ModelAndView("form");
		mv.addObject("filme", filme);  // coloca o filme no model para ser usado no HTML
		return mv;
	}

	// m�todo que atende as requi��es POST em http://localhost:8080/filmes/
	// inclui um novo filme no banco.
	// os dados do filme a ser inclu�do s�o informados no formul�rio HTML e
	// enviados para a API no corpo (body) da requisi��o.
	// Ao final, redireciona para o endpoint http://localhost:8080/filmes
	@PostMapping
	public String salvar(Filme filme) {
		if (filme.getId() == null) {
			// chama a api passando o filme a ser inclu�do
			restTemplate.postForEntity(urlApi + "/filmes", filme, Filme.class);
		}
		return "redirect:/filmes";
	}

	// m�todo que atende as requi��es POST em http://localhost:8080/filmes/{id}
	// exemplo: http://localhost:8080/filmes/1
	// altera o filme de id = {id} no banco.
	// os dados do filme a ser alterado s�o informados no formul�rio HTML e
	// enviados para a API no corpo (body) da requisi��o.
	// Ao final, redireciona para o endpoint http://localhost:8080/filmes
	@PostMapping("{id}")
	public String atualizar(@PathVariable Integer id, Filme filme) {
		filme.setId(null); // a api n�o aceita o campo id no corpo da requisi��o

		// chama a api passando o filme a ser alterado
		restTemplate.put(urlApi + "/filmes/" + id, filme, Filme.class);
		return "redirect:/filmes";  // redireciona para /filmes
	}

}

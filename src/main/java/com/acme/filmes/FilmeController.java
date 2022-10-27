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
 * Classe responsável por responder as requisições do usuário no navegador
 * A anotação @Controller é usada para definir que esta
 * classe retorna páginas HTML.
 * A anotação @RequestMapping é usada para definir que esta classe
 * atende a todas as requisições que comecem com /filmes
 */
@Controller
@RequestMapping("/filmes")
public class FilmeController {


	private String urlApi = "https://filmes-api-123.herokuapp.com";
	// private String urlApi = "http://localhost:8080";

	// a variável restTemplate é usada para realizar
	// requisições para a API de filmes
	private RestTemplate restTemplate = new RestTemplate();

	// método que atende as requições GET em http://localhost:8080/filmes
	// retorna uma página HTML com uma lista de filmes do banco de dados.
	@GetMapping
	@SuppressWarnings("unchecked")
	public ModelAndView index(String filtro) {
		if (filtro == null) filtro = "";

		String url = "";
		if (filtro.equals("")) {
			// se não foi passado um filtro
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
		mv.addObject("filmes", filmes); // coloca os filmes no model para serem mostrados na página HTML
		mv.addObject("filtro", filtro); // coloca o filtro no model para ser mostrado na página HTML
		return mv;
	}

	// método que atende as requições GET em http://localhost:8080/filmes/{id}
	// exemplo: http://localhost:8080/filmes/1
	// retorna uma página HTML com o filme de id = {id} do banco de dados.
	@GetMapping("{id}")
	public ModelAndView get(@PathVariable Integer id) {

		// chama a api para buscar o filme com id = {id}
		Filme filme = restTemplate.getForObject(urlApi + "/filmes/" + id, Filme.class);

		if (filme == null) {
			// se filme não existir, mostra mensagem de erro
			throw new RuntimeException("Filme com ID " + id + " não encontrado");
		}
		ModelAndView mv = new ModelAndView("form");
		mv.addObject("filme", filme);  // coloca o filme no model para ser usado no HTML
		return mv;
	}

	// método que atende as requições POST em http://localhost:8080/filmes/
	// inclui um novo filme no banco.
	// os dados do filme a ser incluído são informados no formulário HTML e
	// enviados para a API no corpo (body) da requisição.
	// Ao final, redireciona para o endpoint http://localhost:8080/filmes
	@PostMapping
	public String salvar(Filme filme) {
		if (filme.getId() == null) {
			// chama a api passando o filme a ser incluído
			restTemplate.postForEntity(urlApi + "/filmes", filme, Filme.class);
		}
		return "redirect:/filmes";
	}

	// método que atende as requições POST em http://localhost:8080/filmes/{id}
	// exemplo: http://localhost:8080/filmes/1
	// altera o filme de id = {id} no banco.
	// os dados do filme a ser alterado são informados no formulário HTML e
	// enviados para a API no corpo (body) da requisição.
	// Ao final, redireciona para o endpoint http://localhost:8080/filmes
	@PostMapping("{id}")
	public String atualizar(@PathVariable Integer id, Filme filme) {
		filme.setId(null); // a api não aceita o campo id no corpo da requisição

		// chama a api passando o filme a ser alterado
		restTemplate.put(urlApi + "/filmes/" + id, filme, Filme.class);
		return "redirect:/filmes";  // redireciona para /filmes
	}

}

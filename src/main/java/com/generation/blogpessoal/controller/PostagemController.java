package com.generation.blogpessoal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.generation.blogpessoal.model.Postagem;
import com.generation.blogpessoal.repository.PostagemRepository;

@RestController //notação que diz ao spring q essa é uma controladora de rotas e acesso aos metodos
@RequestMapping ("/postagens") //rota p chegar a essa classe pelo insomnia
@CrossOrigin (origins = "*", allowedHeaders = "*") //liberar o acesso a outras máquinas / qual a origem ("*") qe vai acessar. Nessa caso o asterisco indica TODAS as origens
public class PostagemController {
	
	@Autowired //notação p injeção de dependencias = instanciar a classe PostagemRepository, em vez de instanciar a classe tda vez q for usar um objeto da JPA
	private PostagemRepository postagemRepository;
	
	@GetMapping //define o verbo http que atende o metodo abaixo dessa notação
	public ResponseEntity<List<Postagem>> getAll () { // o list define que vao ser varias postagens Postagem é o nome do metodo. Pode ser oq vc quiser
		// o responseentity é uma classe que permite q o método retorne em formato de requisição http
		return ResponseEntity.ok (postagemRepository.findAll());
		// findall = select*from tb_postagens (q esta relacionada c a postagemrepository
	}
}

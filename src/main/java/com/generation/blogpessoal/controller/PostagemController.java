package com.generation.blogpessoal.controller;

import java.util.List;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.blogpessoal.model.Postagem;
import com.generation.blogpessoal.repository.PostagemRepository;
import com.generation.blogpessoal.repository.TemaRepository;
import jakarta.validation.Valid;

@RestController //notação que diz ao spring q essa é uma controladora de rotas e acesso aos metodos
@RequestMapping ("/postagens") //rota p chegar a essa classe pelo insomnia
@CrossOrigin (origins = "*", allowedHeaders = "*") //liberar o acesso a outras máquinas / qual a origem ("*") qe vai acessar. Nessa caso o asterisco indica TODAS as origens
public class PostagemController {
	
	@Autowired //notação p injeção de dependencias = instanciar a classe PostagemRepository, em vez de instanciar a classe tda vez q for usar um objeto da JPA
	private PostagemRepository postagemRepository;
	
	@Autowired 
	private TemaRepository temaRepository;
	
	@GetMapping //define o verbo http que atende o metodo abaixo dessa notação
	public ResponseEntity<List<Postagem>> getAll () { // o list define que vao ser varias postagens Postagem é o nome do metodo. Pode ser oq vc quiser
		// o responseentity é uma classe que permite q o método retorne em formato de requisição http
		return ResponseEntity.ok (postagemRepository.findAll());
		// findall = select*from tb_postagens (q esta relacionada c a postagemrepository
	}
	// V localhost:8080/postagens/1
	@GetMapping ("/{id}") //precisa ser diferente do outro metodo tb de get map, c uma rota que sera atendida/endereço complementar. As chaves {} especificam q é um id especifico, pode ser o id 1, 2, 3... as infos desse id especifico vai ser devolvido na app
	public ResponseEntity <Postagem> getById(@PathVariable Long id) {//diferente do outro metodo esse n tem lista, pois não sabemos quantos elementos 
	
		return postagemRepository.findById(id) //a postagemrepository tem os metodos do db. deppis do ponto escreve o metodo. o METODOS FINDBYID É O MESMO QUE SELECT FROM TBPOSTAGENS WHERE ID=1;
				.map(resposta -> ResponseEntity.ok (resposta)) //o map rastreia dentro dos () contem uma expressão lambda
				.orElse (ResponseEntity.status(HttpStatus.NOT_FOUND).build()); // aqui define q a saida vai ser um status, sem um corpo. o build nesse cod é como um break, avisando q nd mais será feito
	}
	// NO WORKBENCH o metodo abaixo é o mesmo que SELECT *FROM tb_postagens WHERE TITULO = "TITULO";
	@GetMapping ("/titulo/{titulo}") //localhost:8080/postagens/titulo/Postagem 02 . a primeira palavra /titulo funciona cmo palavra fixa p diferenciar do metodo de cima de id e não gerar ambiguidade
	public ResponseEntity <List<Postagem>> getByTitulo(@PathVariable String titulo) {
		return ResponseEntity.ok(postagemRepository.findAllByTituloContainingIgnoreCase(titulo));
	}
	//INSERT INTO tb_postagens (titulo, texto,data) VALUES ("Titulo", "texto","2024-12-31 14:05:01);
	@PostMapping //atende ao verbo post
	public ResponseEntity<Postagem> post(@Valid @RequestBody Postagem postagem){ /* Definição do metodo post.O "post" é o nome do método. Pode ser qlqr um. 
		As notações trazem ações prontas sem necessidade de escrita maior de cod. 
		A notação valid valida a qnt min e max de caracteres indicada anteriormente. e resuqestbody indica q será solicitado e exibido um corpo*/
		if (temaRepository.existsById(postagem.getTema().getId()))
		
			return ResponseEntity.status(HttpStatus.CREATED) //retorna em formato ResponseEntity IGUAL ao metodo
				.body(postagemRepository.save(postagem)); //definição doq será visualizado no corpo. SAVE = INSERTINTO. Metodo da repository  salva o post e mostra
		
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O tema não existe", null);
	}
	@PutMapping
	public ResponseEntity <Postagem> put(@Valid @RequestBody Postagem postagem){ //cuidado c espaço depois de put/nome do metodo
		if (postagemRepository.existsById(postagem.getId())) {
			if(temaRepository.existsById(postagem.getTema().getId())) {
		
			return ResponseEntity.status(HttpStatus.OK) 
				.body(postagemRepository.save(postagem));
			}
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O tema não existe", null);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	//DELETE FROM tb_postagens WHERE id =id;
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) { //opath variable garante que somente algo especifico vai ser deletado. Não odb todo
		Optional<Postagem> postagem = postagemRepository.findById(id);
		
		if (postagem.isEmpty()) //se o objeto postagem estiver vazio v
			throw new ResponseStatusException(HttpStatus.NOT_FOUND); // responde c uma saida notfound
		
		postagemRepository.deleteById(id); // se não será executado o metodo deletebyid para deletar
	} 
	/* Sobre os verbos:
	 * Verbo get recupera infos do db
	 * Verbo post faz inserts no db. Guarda infos em alguma tabela do db
	 * Verbo push faz o update no db
	 * delete deleta infos/registros no db
	 * Como boa prática, uma APIREST deve ter um verbo CORRESPONDENTE/CORRETO a ação q será feita
	 */
	
	
	
}

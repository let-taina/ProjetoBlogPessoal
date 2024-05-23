package com.generation.blogpessoal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.generation.blogpessoal.model.Tema;
import com.generation.blogpessoal.repository.TemaRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping ("/temas") // Cria o endereço. Oq está dentro dos parenteses forma a URL/endereço p acesso pelo insomnia
@CrossOrigin (origins = "*", allowedHeaders = "*") 

public class TemaController {
	@Autowired //funciona como injeção de dependencia de tudo q está na interface tema repository. cria acesso aos métodos dentro da repository
    private TemaRepository temaRepository; //TemaRepository indica de qual interface vai trazer. o segundo temarepository é a nomeação dessa injeção de dependencia 
    
    @GetMapping //busca por tema
    public ResponseEntity<List<Tema>> getAll(){ //response entity é uma resposta em http. É em lista pq é mais de um tema, o tipo de dado é tema(tdo q está na model tema), getall é o nome da fnção
        return ResponseEntity.ok(temaRepository.findAll()); //a resposta em http vai ser "ok", trazendo tds os dados encontrados na tabela (findall)/(body/corpo)
    }
    @PostMapping
    public ResponseEntity<Tema> post(@Valid @RequestBody Tema tema){
        return ResponseEntity.status(HttpStatus.CREATED) // httpstatus site dos doguineos/catineos. created = 201
                .body(temaRepository.save(tema));
    }
    @GetMapping ("/{id}") //busca o tema pelo id. As chaves indicam q é um Id especifico
    public ResponseEntity <Tema> getById(@PathVariable Long id){
    	return temaRepository.findById(id) //é o mesmo q selectfrom where id=x. Traz de acordo com o criterio
    			.map(resposta -> ResponseEntity.ok(resposta)) //retorno se o id for encontrado
    			.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build()); //retorno se o id n for encontrado
    }   
    
    @GetMapping ("/descricao/{descricao}") //busca o tema pela descrição /palavra fixa p diferenciação do outro get na url/descrição especifica
    public ResponseEntity <List<Tema>> getByDescricao(@PathVariable String descricao) {
    	return ResponseEntity.ok(temaRepository.findAllByDescricaoContainingIgnoreCase(descricao));
    }
    
    @PostMapping //cadastra um novo tema
    public ResponseEntity<Tema> cadtema (@Valid @RequestBody Tema tema){ //o nome do metodo é cadtema mas pode ser qlqr um. ira VALIDAR a qnt de caracteres e requisitará um body p exibição
    	return ResponseEntity.status(HttpStatus.CREATED) //retornará o status de q foi criado =201
    			.body(temaRepository.save(tema)); //
    }
}

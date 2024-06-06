package com.generation.blogpessoal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.repository.UsuarioRepository;
import com.generation.blogpessoal.service.UsuarioService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT) //determina que vai rodar em qualquer porta que não estiver sendo utilizada
@TestInstance(TestInstance.Lifecycle.PER_CLASS) //o teste vai acontecer testando a classe toda
public class UsuarioControllerTest {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private TestRestTemplate testRestTemplate;
	
	@BeforeAll
	void start() {
		usuarioRepository.deleteAll(); //= drop table no sql 
		
		usuarioService.cadastrarUsuario(new Usuario(0L, "root", "root@root.com", "rootroot", "")); //0L somente para não ficar em branco o campo do ID, L de long
	}
	//1° teste
	@Test //indica que é um teste
	@DisplayName("Deve cadastrar um novo usuario")
	public void DeveCriarUmNovoUsuario() { //especificar o quanto possível o nome do método
		HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>( 
				new Usuario(0L, "Leticia", "leticia@gmail.com", "123456789", "")
				); //todo esse método representa o body do jason do método
		
		// a response entity traz uma resposta no formato http
		ResponseEntity<Usuario> corpoResposta = testRestTemplate.exchange(
				"/usuarios/cadastrar", HttpMethod.POST, corpoRequisicao, Usuario.class); //usando a classe de usuario e o metodo post o corpo vai ser mandado p a URL "/usuarios/cadastrar". Desobriga a criação do método no Insomnia
	
		assertEquals(HttpStatus.CREATED, corpoResposta.getStatusCode()); // o esperado é que a resposta seja igual a CREATED inserido no corpo da resposta
	}
	
	@Test
	@DisplayName("Não deve permitir duplicação do usuário")
	public void naoDeveDuplicarUsuario() {
		
		//Testando se vai deixar cadastar usuario duplicado:
		usuarioService.cadastrarUsuario(new Usuario(0L,"Maria da Silva","maria_silva@email.com","-","12345678"));
		
		HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(new Usuario(0L,"Maria da Silva","maria_silva@email.com","-","12345678"));
		
		ResponseEntity<Usuario> corpoResposta = testRestTemplate
				.exchange("/usuarios/cadastrar",
				HttpMethod.POST, corpoRequisicao, Usuario.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, corpoResposta.getStatusCode());
		
	}
	@Test
	@DisplayName("Atualizar um usuário")
	public void deveAtualizarUmUsuario() {
		
		Optional<Usuario> usuarioCadastrado = usuarioService.cadastrarUsuario(
				new Usuario(0L,"Juliana Andrews","juliana_and@email.com","juliana123","-"));
		
		Usuario usuarioUpdate = new Usuario(usuarioCadastrado.get().getId(),
				"Juliana Andrews Ramos", "juliana_ram@email.com", "jualiana123","-");
		
		HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(usuarioUpdate);
		
		ResponseEntity<Usuario> corpoResposta = testRestTemplate
				.withBasicAuth("root@root.com","rootroot")
				.exchange("/usuarios/atualizar",HttpMethod.PUT, corpoRequisicao, Usuario.class);
		
		assertEquals(HttpStatus.OK, corpoResposta.getStatusCode());
	}
	@Test
	@DisplayName("Listar todos os usuários")
	public void deveMostrarTodosUsuarios() {
		
		usuarioService.cadastrarUsuario(new Usuario(0L, "Sabrina Sanches", "sabrina_s@email.com", "-", "sabrina123"));
		
		usuarioService.cadastrarUsuario(new Usuario(0L,"Ricardo Marques", "ricardo-m@email.com", "-","ricardo123"));
		
		ResponseEntity<String> resposta = testRestTemplate
				.withBasicAuth("root@root.com","rootroot")
				.exchange("/usuarios/all", HttpMethod.GET, null, String.class);
		
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
	
	}
}


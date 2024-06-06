package com.generation.blogpessoal.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.model.UsuarioLogin;
import com.generation.blogpessoal.repository.UsuarioRepository;
import com.generation.blogpessoal.service.UsuarioService;

import jakarta.validation.Valid;

@RestController //define a classe coo controladora
@RequestMapping("/usuarios") //
@CrossOrigin(origins="*", allowedHeaders = "*") //aceita acesso das requisições por outros servidores. * = qlqr origin
public class UsuarioController {

	//injeção de dependencias
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@PostMapping("/cadastrar")
	public ResponseEntity<Usuario> postUsuario(@RequestBody @Valid Usuario usuario){
		return usuarioService.cadastrarUsuario(usuario)
				.map(resposta-> ResponseEntity.status(HttpStatus.CREATED).body(resposta))
				.orElse( ResponseEntity.status(HttpStatus.NOT_FOUND).build());
		
	}
	@PostMapping("/logar")
	public ResponseEntity<UsuarioLogin> autenticarUsuario(@RequestBody Optional<UsuarioLogin> usuarioLogin){
		return usuarioService.autenticarUsuario(usuarioLogin)
				.map(resposta->ResponseEntity.status(HttpStatus.OK).body(resposta))
				.orElse( ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
	}
}

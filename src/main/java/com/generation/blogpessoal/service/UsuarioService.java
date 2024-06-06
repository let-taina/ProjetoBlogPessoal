package com.generation.blogpessoal.service;

import java.util.Optional;


import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.model.UsuarioLogin;
import com.generation.blogpessoal.repository.UsuarioRepository;
import com.generation.blogpessoal.security.JwtService;

@Service //determina que a classe está tratando regras do negócio
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;//o segundo usuarioRepository poderia ser qlqr nome
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private AuthenticationManager authenticationManager; //faz a gestão de autenticação. permite acessar metodos q podem entregar ao objeto as suas autoridades concedidas
	
	public Optional<Usuario> cadastrarUsuario(Usuario usuario){ // valida as regras de negócio p permtir o cadastro do usario
		if(usuarioRepository.findByUsuario(usuario.getUsuario()).isPresent()) //valida se o usuario(email) existe, retornando T ou F
			return Optional.empty(); //caso já exista retorna vazio, evitando o cadastro c duplicidade. Cada usuario tem q ser único!
		
		usuario.setSenha(criptografarSenha(usuario.getSenha())); //caso nao exista dá opção p o usuario criar senha criptografada
		return Optional.of(usuarioRepository.save(usuario)); // o .save faz o isert into na tabela de banco de dados (persistencia) só depois da criptografia
	}
	
	//metodo p tratar a senha de forma q ela seja criptografada antes de ser persistida no banco de dados
	private String criptografarSenha(String senha) { //senha será digitada pelo usuario como string
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(); //método de criptografar
		return encoder.encode(senha); //especificando que o atributo senha é q será criptografado
	}
	
	//método para evitar que durante o update seja cadastrado/atualizado dois usuarios iguais/com mesmo email
	public Optional<Usuario> atualizarUsuario(Usuario usuario){
		if(usuarioRepository.findById(usuario.getId()).isPresent()){ //busca no db se o id já existe
			
			Optional<Usuario> buscaUsuario = usuarioRepository.findByUsuario(usuario.getUsuario()); //objeto optional pq pode ser preenchido ou não
			if(buscaUsuario.isPresent() && (buscaUsuario.get().getId()) != usuario.getId()) //compara o email passado c email registrado no banco de dados. se o objeto (atrelado ao email) for encontrado, estiver preenchido (id, foto, nome, etc), e o id a ser atualizado for igual à outro já existente vai p proxima linha do cod (badrequest)
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário já existe", null); //exceção para caso o id seja diferente e o email já existe
			
			usuario.setSenha(criptografarSenha(usuario.getSenha())); //email existe e o id é igual
			
			return Optional.ofNullable(usuarioRepository.save(usuario));
		}
		return Optional.empty();
	}
	
	//garantir as regras de negocio para o login
	public Optional<UsuarioLogin> autenticarUsuario(Optional<UsuarioLogin>usuarioLogin){
		var credenciais = new UsernamePasswordAuthenticationToken(usuarioLogin.get().getUsuario(),//criada a variavel credenciais q guarda o usuario e a senha q tenta logar
				usuarioLogin.get().getSenha());
		
		
		Authentication authentication = authenticationManager.authenticate(credenciais);
		
		if(authentication.isAuthenticated()){
			Optional<Usuario> usuario= usuarioRepository.findByUsuario(usuarioLogin.get().getUsuario());
			if(usuario.isPresent()) {
				usuarioLogin.get().setId(usuario.get().getId());
				usuarioLogin.get().setNome(usuario.get().getNome());
				usuarioLogin.get().setFoto(usuario.get().getFoto());
				usuarioLogin.get().setToken(gerarToken(usuarioLogin.get().getUsuario()));
				usuarioLogin.get().setSenha(""); //n sera informada aqui, será pelo usuario, e será criptografada
				
				
				return usuarioLogin;
			}
		}
		return Optional.empty();
	}
	
	private String gerarToken(String usuario) {
		return "Bearer "+jwtService.generateToken(usuario);
	}
}

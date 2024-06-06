package com.generation.blogpessoal.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;
 
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//objetivos dessa classe: trazer as validações do token feitas na JWTService; confirmar se o token está chegando pelo Header
@Component
public class JwtAuthFilter extends OncePerRequestFilter {
	
	//injeção de dependencias p validação do token
	@Autowired
	private JwtService jwtService;
	
	// injeção de dependencias da classe q conversa com o banco e valida se o usuario existe ou não
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	
	// metodo obrigatório que faz parte a onceperresquestfilter
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String authHeader = request.getHeader("Authorization");
		
		String token = null;
		
		String username = null;
		
		try {
			if(authHeader != null && authHeader.startsWith("Bearer")) {
				token = authHeader.substring(7); //conta e ignora os primeiros 7 caracteres da string do token = Bearer .Já q não precisa de tratamento, é só uma nomenclatura 
				username = jwtService.extractUsername(token); //o token sem o 7 primeiros vai p jwt e é extraido o username
							
			}
			if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) { //se o username extraido do token não for nulo && se as autorizações dos metodos get tb não são nulas
				
				UserDetails userDetails = userDetailsService.loadUserByUsername(username); //userdetails service verifica se o usuario ja existe pelo metodoloadbyusername
				
				if(jwtService.validateToken(token,  userDetails)) { //valida se o token tem as infos e se não expirou
					UsernamePasswordAuthenticationToken authToken=new
							UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
									authToken.setDetails(
									new WebAuthenticationDetailsSource().buildDetails(request));
							SecurityContextHolder.getContext().setAuthentication(authToken);
							
				}				
				}
			filterChain.doFilter(request,response); //se todos os itens acima derem certo será feito o filtro
			
		}catch(ExpiredJwtException | UnsupportedJwtException | MalformedJwtException // caso os itens acima deem algum "não" cairá nessas exceções
				| SignatureException | ResponseStatusException e) {
				response.setStatus(HttpStatus.FORBIDDEN.value());
		return;
	}
	}
}
		

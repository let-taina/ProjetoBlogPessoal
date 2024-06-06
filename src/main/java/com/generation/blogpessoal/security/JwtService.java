package com.generation.blogpessoal.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

// o objetivo da classe é criar e confirmar o token
@Component
public class JwtService {
	
	
	public static final String SECRET = "6a22ab77cf891126f62efa317fc3b816049d466b556f88c96e76b99db35dbb46"; // o "final" significa que o valor do atributo é costante, não será alterado após sua definição
	// o cod entre aspas foi gerado no kengen.io
	// o token é composto pelo email, data e horário que foi acessado e a assinatura. Essas informações são encodadas. O base64 é um site que faz esse msm processo de encodamento/decodificação
	//diferentes tipos de tokens fornecem tipos de acesso diferentes, como diferenciação de user e admin
	// aqui foi criada uma constante que irá gerar uma chave para encodar as infromações do token
	
	private Key getSignKey() { // processo de gerar e codar a assinatura do token
		byte[] keyBytes = Decoders.BASE64.decode(SECRET); //encoda a secret
		return Keys.hmacShaKeyFor(keyBytes); //retorna a chave tratada
	}
	
	// os Claims são classes do Java que carregam info/declarações contidas em algo, nesse caso solicitamos a validação das infos do token
	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(getSignKey()).build()
				.parseClaimsJws(token).getBody();
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) { //A assinatura extraida será tratada e tornada entendível
		final Claims claims = extractAllClaims(token); // o extractallclaims foi feito anteriormente (acima) e ele é chamado novamente aqui
		return claimsResolver.apply(claims);
	}
	
	//recupera os dados da parte sub do claim onde encontramos o email (usuario)
	public String extractUsername(String token) { 
		return extractClaim(token, Claims::getSubject); //novamente o metodo anterior (extractclaim) é puxado
	}
	
	//data que o token expira
	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}
	
	//valida se a data que o token expira esta dentro da validade ou seja a data atual ainda não atingiu essa data
	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
	
	// valida se o usuario que foi extraido do token condiz com o usuario que a userDetails tem e se o token ainda esta dentro da data de validade 
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
	
	//calcula o tempo de validade do token, forma o claim com as infos do token
	private String createToken(Map<String, Object> claims, String userName) {
		return Jwts.builder()
					.setClaims(claims) //formato do token: claimns
					.setSubject(userName) //username primeira parte do token
					.setIssuedAt(new Date(System.currentTimeMillis())) //consulta a data atual p incluir no token
					.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))//calcula e configura qdo a data do token expira, cada vez q expirar tem q ser feito um novo login. 1000> 1 milesimo de seg 60 = 60.000 milesimos de s = 1 min. 60.000 * 60 = 1h
					.signWith(getSignKey(), SignatureAlgorithm.HS256).compact(); // // assinatura do tipo HS256
	}
	
	//gera o token puxando os claims formados no metodo anterior
	public String generateToken(String userName) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, userName);
	}
}

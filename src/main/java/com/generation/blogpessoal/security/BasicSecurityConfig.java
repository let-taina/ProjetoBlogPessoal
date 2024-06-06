package com.generation.blogpessoal.security;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// o objetivo dessa classe é informar as configurações de segurança e liberar os links que não precisam de login
@Configuration // informa q essa classe é de configuração
@EnableWebSecurity //informa que as configurações se aplicam a toda a aplicação
public class BasicSecurityConfig {
	
	//injeção (tipo instanciar) de dependencias que traz a classe jwtAuthFilter q conversa com jwtservice e userdetails service, fazendo a validação do usuario e do token(via header)
	 @Autowired
	    private JwtAuthFilter authFilter;

	 //ajustes de usuario e senha
	 /*criptografia != codificação: a codificação pode ser feita e desfeita, traduzida. Como mudar a forma de escrever de uma mensagem
	A criptografia restringe o acesso a informação de forma q ela só fica legivel com acesso especifico e não pode ser desfeita*/
	 @Bean
	    UserDetailsService userDetailsService() {

	        return new UserDetailsServiceImpl();
	    }
	 	
	 //faz a criptografia da senha
	    @Bean
	    PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }

	    // usuario e senha já foram validados
	    @Bean
	    AuthenticationProvider authenticationProvider() {
	        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
	        authenticationProvider.setUserDetailsService(userDetailsService());
	        authenticationProvider.setPasswordEncoder(passwordEncoder());
	        return authenticationProvider;
	    }
	   
	    //implementa o gerenciamento de autenticação
	    @Bean
	    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
	            throws Exception {
	        return authenticationConfiguration.getAuthenticationManager();
	    }

	    @Bean
	    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

	    	http
		        .sessionManagement(management -> management
		                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		        		.csrf(csrf -> csrf.disable())
		        		.cors(withDefaults());

	    	http
		        .authorizeHttpRequests((auth) -> auth
		                .requestMatchers("/usuarios/logar").permitAll() //da autorização de acesso a tela de login sem login e token
		                .requestMatchers("/usuarios/cadastrar").permitAll() //da autorização de acesso a tela de cadastro sem login e token
		                .requestMatchers("/error/**").permitAll() //da autorização de acesso a tela de erro sem login e token
		                .requestMatchers(HttpMethod.OPTIONS).permitAll() //metodo http liberado: options
		                .anyRequest().authenticated())
		        .authenticationProvider(authenticationProvider())
		        .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
		        .httpBasic(withDefaults());

			return http.build();

	    }
		
		}



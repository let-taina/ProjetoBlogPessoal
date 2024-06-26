package com.generation.blogpessoal.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

//tdas as notações são importadas da biblioteca jakarta
@Entity // classe vai se tornar uma entidade do db
@Table (name = "tb_postagens")
public class Postagem {
	
	@Id //TORNAR O CAMPO uma chave primaria no db
	@GeneratedValue (strategy = GenerationType.IDENTITY) //tornando a chave primairia autoincrement
	private Long id;
	
	@NotBlank(message = "O atributo TITULO é obrigatório!") //notação notblank p validar o atributo p ele não ser nulo nem vazio
	@Size (min = 5, max = 100, message = "O atributo TITULO deve ter no mínimo 5 caracteres e no máximo 100.")
	private String titulo;
	
	@NotBlank (message = "O atributo TEXTO é obrigatório!")
	@Size (min = 10, max = 1000, message = "O atributo TEXTO deve ter no mínimo 10 caracteres e no máximo 1000 caracteres.")
	private String texto;

	@UpdateTimestamp //pega a data e hora do sistema e preenche no banco de dados
	private LocalDateTime data;
	
	
	@ManyToOne
	@JsonIgnoreProperties("postagem") ////// PARTE DE RELACIONAMENTO USUARIO C SEGURANÇA GERAR GETTER AND SETTERS!
	private Usuario usuario;
	
	@ManyToOne //mtas postagens podem pertencer a apenas um tema
	@JsonIgnoreProperties("postagem") //evita looping infinito (quebra do cod) n permitindo q o tema puxe a postagem e a postagem puxe o tema, e o tema puxe a post...
	private Tema tema; //o primeiro Tema é o tipo de dado, o nome da modelagem inteira (id, descrição, etc) e o segundo é oq vai ser trazido/nome do campo
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public LocalDateTime getData() {
		return data;
	}

	public void setData(LocalDateTime data) {
		this.data = data;
	}

	public Tema getTema() {
		return tema;
	}

	public void setTema(Tema tema) {
		this.tema = tema;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	
}

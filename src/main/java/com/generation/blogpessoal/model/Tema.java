package com.generation.blogpessoal.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table (name = "tb_temas")
public class Tema {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank (message = "O atributo descrição é obrigatório!")
	@Size (min = 4, max=50, message = "O atributo descrição precisa ter entre 4 e 50 caracteres.")
	private String descricao;
	
	@OneToMany (fetch = FetchType.LAZY, mappedBy = "tema", cascade = CascadeType.REMOVE) //ja q vai puxar as postagens o fetchLazy traz as postagens aos poucos,conforme forem solicitadas, s sobrecarregar a app.Ex contrário de Lazy:lista e contatos no cel
	// o fetch vai trazer MAPEADO(mappedby) de acordo c a tabela tema (lado esquerdo do innerjoin). Cascade: se o tema for deletado, as postagens tb serão deletadas, em cascata.
	@JsonIgnoreProperties ("tema")
	private List<Postagem> postagem; //lista pq são varios temas <postagem> pq é oq ta dentro dessa lista. O nome desse campo com lista de postagem é postagem
	
	public Long getId() {
		return this.id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescricao() {
	return this.descricao;
	}
		public void setDescricao(String descricao) {
			this.descricao = descricao;
		}
		public List<Postagem> getPostagem() {
			return postagem;
		}
		public void setPostagem(List<Postagem> postagem) {
			this.postagem = postagem;
		}
		
}

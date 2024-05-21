package com.generation.blogpessoal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.blogpessoal.model.Postagem;

// JPA REPOSITORY - classe jpa - metodos q vão realizar a query no banco
public interface PostagemRepository extends JpaRepository <Postagem, Long>{
	// vai fazer o select * from tb_postagens na query. Não precisa eu digitar

}

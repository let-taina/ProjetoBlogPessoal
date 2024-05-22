package com.generation.blogpessoal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.generation.blogpessoal.model.Postagem;

// JPA REPOSITORY - classe jpa - metodos q vão realizar a query no banco
public interface PostagemRepository extends JpaRepository <Postagem, Long>{
	// vai fazer o select * from tb_postagens na query. Não precisa eu digitar

	public List <Postagem> findAllByTituloContainingIgnoreCase (@Param("titulo")String titulo); // find =select All=* By=where titulo=atributo da classe postagem/campo especifico
						//o containing = contém. Traz uma busca não exata. Algo q contém mas nao necessariamente seja 100%igual. É o mesmo que %% no workbench
	// SELECT * FROM tb_postagens WHERE titulo LIKE "%Post%" = findAllByTituloContainingIgnoreCase (@Param("titulo")String titulo)
}
 
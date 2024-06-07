package com.generation.blogpessoal.configuration;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;

@Configuration //1 passo indica que a classe é de configuração
public class SwaggerConfig {
	
	@Bean
	OpenAPI springBlogPessoalOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("Projeto Blog Pessoal")
						.description("Projeto Blog Pessoal desenvolvido na Generation Brasil")
						.version("v0.0.1")
						.license(new License()
								.name("Letícia Silva") //p qual emmpresa ou pessoas está sendo esse projeto /nome integrantes grupo
								.url("www.linkedin.com/in/leticiatsilva"))
						.contact(new Contact()
								.name("Letícia Silva") //nome(s) do(s) desenvolvedor(es) q respondem pela aplicação
								.url("") //url do projeto
								.email("leticia.ts@outlook.com.br"))) //email do projeto
				.externalDocs(new ExternalDocumentation()
						.description("Github")
						.url("https://github.com/leticiasilva")); //link da aplicação no github (publico) ou link externo
	}
	@Bean
	OpenApiCustomizer customerGlobalHeaderOpenApiCustomiser() {
		return openApi -> {
			openApi.getPaths().values().forEach(pathItem -> pathItem.readOperations()
					.forEach(operation ->{
						ApiResponses apiResponses = operation.getResponses();
						// método p definir as mensagem de retorno para cada status HTML definidos nos métodos do insomnia
						apiResponses.addApiResponse("200", createApiResponse("Sucesso!"));
						apiResponses.addApiResponse("201", createApiResponse("Objeto persistido!"));
						apiResponses.addApiResponse("204", createApiResponse("Objeto excluído!"));
						apiResponses.addApiResponse("400", createApiResponse("Erro na requisição!"));
						apiResponses.addApiResponse("401", createApiResponse("Acesso não autorizado!"));
						apiResponses.addApiResponse("403", createApiResponse("Acesso proibido!"));
						apiResponses.addApiResponse("404", createApiResponse("Objeto não encontrado!"));
						apiResponses.addApiResponse("500", createApiResponse("Erro na aplicação!"));
					})
					);
		};
	}
	private ApiResponse createApiResponse(String message) {
		return new ApiResponse().description(message);
	}
}

package br.com.guntz.comments.comment.api.config.doc;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DocConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Guntz Comments - Microsserviço")
                        .description("Guntz Comments é um microsserviço de criação de comentários construído com Spring Boot. \n\n" +
                                "Esse microsserviço exibe cometários com recurso de páginação, além de pesquisa individual por ID, \n\n" +
                                "Válida e armazena apenas comentários aprovados através de comunicação síncrona com outro microsserviço (Guntz Moderation), \n\n" +
                                "Que por sua vez é responsável por toda inteligência de identificar palavras de baixo calão, ou termos utilizados em discurso de ódio.\n\n" +
                                "Uma vez identificado que se trata de uma palavra proíbida, ele reprova o comentário.")
                        .contact(new Contact()
                                .name("Guntz")
                                .email("rricardoguntzell@gmail.com"))
                        .license(new License()
                                .name("Guntz - Github")
                                .url("https://github.com/ricardoguntzell/guntz-guntzcomments-comment-service")));
    }
}

package br.com.seap.livraria.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

//@EnableSwagger2
@EnableWebMvc
@Configuration
public class SwaggerConfig {

//    @Bean
//    public Docket docket(){
//        return new Docket(DocumentationType.SWAGGER_2)
//                .select()
//                .apis( RequestHandlerSelectors.basePackage("br.com.seap.livraria.controllers") )
//                .paths(PathSelectors.any())
//                .build()
//                .apiInfo(apiInfo());
//    }
//
//    private ApiInfo apiInfo(){
//        return new ApiInfoBuilder()
//                .title("Api Biblioteca")
//                .description("Api do projeto de controle de aluguel de livros.")
//                .version("1.0")
//                .contact(contact())
//                .build();
//    }
//
//    private Contact contact(){
//        return new Contact("SDS",
//                             "sds.com",
//                           "sds@seap.ma.gov.br");
//    }
}

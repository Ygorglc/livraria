package br.com.seap.livraria.enums;

import org.springframework.beans.factory.annotation.Value;

public enum ProblemaEnum {

    ERRO_DE_SISTEMA("/erro-de-sistema", "Erro de sistema"),
    PARAMETRO_INVALIDO("/parametro-invalido", "Parâmetro inválido"),
    MENSAGEM_INCOMPREENSIVEL("/mensage-incompreensivel", "Mensagem incompreensivel"),
    RECURSO_NAO_ENCONTRADO("/recurso-naoencontrado","Entidade não encontrada"),
    ENTIDADE_EM_USO("/entidade-em-uso","Entidade em uso"),
    ERRO_NEGOCIO("/erro-negocio","Violação de regra de negócio");

    private String titulo;
    @Value("$siisp-seap.endpoint")
    private String uri;

    ProblemaEnum(String pasta, String titulo){
        this.uri = pasta;//Mudar a URI
        this.titulo = titulo;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getUri() {
        return uri;
    }

}
package br.com.seap.livraria.exceptions.handler;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Problema {

    private Integer status;
    private LocalDateTime horaEData;
    private String tipo;
    private String titulo;
    private String detalhe;
    private String mensagemDoUsuario;

    public Problema(ProblemaBuilder builder) {
        this.status = builder.status;
        this.horaEData = builder.horaEData;
        this.tipo = builder.tipo;
        this.titulo = builder.titulo;
        this.detalhe = builder.detalhe;
        this.mensagemDoUsuario = builder.mensagemDoUsuario;
    }

    public Integer getStatus() {
        return status;
    }

    public LocalDateTime getHoraEData() {
        return horaEData;
    }

    public String getTipo() {
        return tipo;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDetalhe() {
        return detalhe;
    }

    public String getMensagemDoUsuario() {
        return mensagemDoUsuario;
    }


    public static class ProblemaBuilder {
        private Integer status;
        private LocalDateTime horaEData;
        private String tipo;
        private String titulo;
        private String detalhe;
        private String mensagemDoUsuario;


        public ProblemaBuilder(){

        }
        public ProblemaBuilder status(Integer status){
            this.status = status;
            return this;
        }

        public ProblemaBuilder timestamp(LocalDateTime timestamp){
            this.horaEData = timestamp;
            return this;
        }

        public ProblemaBuilder tipo(String type){
            this.tipo = type;
            return this;
        }

        public ProblemaBuilder titulo(String title){
            this.titulo = title;
            return this;
        }

        public ProblemaBuilder detalhe(String detail){
            this.detalhe = detail;
            return this;
        }

        public ProblemaBuilder mensagemUsuario(String mensagemDoUsuario){
            this.mensagemDoUsuario = mensagemDoUsuario;
            return this;
        }

        public Problema build(){
            Problema problema = new Problema(this);
            return problema;
        }

    }

}

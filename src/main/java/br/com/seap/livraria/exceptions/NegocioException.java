package br.com.seap.livraria.exceptions;


public class NegocioException extends RuntimeException{

    public NegocioException(String mensagem){
        super(mensagem);
    }

}

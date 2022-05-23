package br.com.seap.livraria.exceptions;

public class EntidadeEmUsoException extends NegocioException{

    private static final long serialVersion = 1L;

    public EntidadeEmUsoException(String mensagem){
        super(mensagem);
    }
}

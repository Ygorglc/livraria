package br.com.seap.livraria.controllers.request;

public record EmprestimoRequest (
        String isbn,
        String cliente
) {
}

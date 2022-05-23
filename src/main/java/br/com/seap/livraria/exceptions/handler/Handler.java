package br.com.seap.livraria.exceptions.handler;

import br.com.seap.livraria.enums.ProblemaEnum;
import br.com.seap.livraria.exceptions.EntidadeEmUsoException;
import br.com.seap.livraria.exceptions.EntidadeNaoEncontradaException;
import br.com.seap.livraria.exceptions.NegocioException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class Handler extends ResponseEntityExceptionHandler {

    public static final String MSG_ERRO_GENERICA_USUARIO_FINAL
            = "Ocorreu um erro interno inesperado no sistema. Tente novamente e se "
            + "o problema persistir, entre em contato com o administrador do sistema.";

    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ResponseEntity<?> handleEntidadeNaoEncontrado(EntidadeNaoEncontradaException ex, WebRequest request){

        HttpStatus status = HttpStatus.NOT_FOUND;
        ProblemaEnum problemType = ProblemaEnum.PARAMETRO_INVALIDO;
        String detalhe = ex.getMessage();

        Problema problema = createProblemBuilder(status, problemType, detalhe)
                .mensagemUsuario(detalhe)
                .build();

        System.out.println(ex.getMessage());
        return handleExceptionInternal(ex, problema, new HttpHeaders(), status, request);

    }

    @ExceptionHandler(NegocioException.class)
    public ResponseEntity<?> handleNegocio(NegocioException ex, WebRequest request){

        HttpStatus status = HttpStatus.BAD_REQUEST;
        ProblemaEnum problemType = ProblemaEnum.ERRO_NEGOCIO;
        String detalhe = ex.getMessage();

        Problema problema = createProblemBuilder(status, problemType, detalhe)
                .mensagemUsuario(detalhe)
                .build();

        System.out.println(ex.getMessage());
        return handleExceptionInternal(ex, problema, new HttpHeaders(), status, request);

    }
    @ExceptionHandler(EntidadeEmUsoException.class)
    public ResponseEntity<?> handleEntidadeEmUso(EntidadeEmUsoException ex, WebRequest request){

        HttpStatus status = HttpStatus.CONFLICT;
        ProblemaEnum problemType = ProblemaEnum.ENTIDADE_EM_USO;
        String detalhe = ex.getMessage();

        Problema problema = createProblemBuilder(status, problemType, detalhe)
                .mensagemUsuario(detalhe)
                .build();

        System.out.println(ex.getMessage());
        return handleExceptionInternal(ex, problema, new HttpHeaders(), status, request);

    }

    private Problema.ProblemaBuilder createProblemBuilder(HttpStatus status, ProblemaEnum problemType, String detalhe) {
        return new Problema.ProblemaBuilder()
                .status(status.value())
                .tipo(problemType.getUri())
                .titulo(problemType.getTitulo())
                .detalhe(detalhe);
    }

    private String joinPath(List<JsonMappingException.Reference> referencias) {
        return referencias.stream().map(ref -> ref.getFieldName()).collect(Collectors.joining("."));
    }

}

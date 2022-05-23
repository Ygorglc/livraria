package br.com.seap.livraria.controllers.request;

import com.sun.istack.NotNull;
import org.springframework.lang.NonNull;

public record LivroRequest(
        Long id,

        @NotNull
        String titulo,

        @NotNull
        String autor,

        @NotNull
        String isbn
) {
}

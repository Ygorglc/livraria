package br.com.seap.livraria.controllers.response;

import com.sun.istack.NotNull;
import org.springframework.lang.NonNull;

public record LivroResponse(
        Long id,

        String titulo,

        String autor,

        String isbn
) {
}

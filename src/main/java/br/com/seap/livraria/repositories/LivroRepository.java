package br.com.seap.livraria.repositories;

import br.com.seap.livraria.models.Livro;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface LivroRepository extends JpaRepository<Livro, Long> {
    Livro save(Livro livro);

    Optional<Livro> findById(Long idLivro);
    Boolean existsByIsbn(String isbn);


}

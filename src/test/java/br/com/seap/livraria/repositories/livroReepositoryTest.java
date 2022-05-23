package br.com.seap.livraria.repositories;


import br.com.seap.livraria.models.Livro;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class livroReepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    LivroRepository livroRepository;

    @Test
    @DisplayName("Deve retornar verdadeiro quando existir um livro na base com isbn informado")
    public void retornaVerdadeiroCasoISBNExista(){
        String isbn = "123";
        Livro livro = new Livro("Novo","Costa",isbn);
        entityManager.persist(livro);

        boolean existe = livroRepository.existsByIsbn(isbn);

        assertThat(existe).isTrue();
    }

    @Test
    @DisplayName("Deve obter um livro por id.")
    public void findByIdTest(){
        Long id = 2L;
        Livro livro2 =criarLivro();

        entityManager.persist(livro2);
        Optional<Livro> livroEncontrado = livroRepository.findById(livro2.getId());
        assertThat(livroEncontrado.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Deve salvar um livro.")
    public void salvarNovoLivro(){
        Livro livroCriado = criarLivro();

        Livro livroSalvo = livroRepository.save(livroCriado);

        assertThat(livroSalvo.getId()).isNotNull();
    }

    @Test
    @DisplayName("Deve deletar um livro")
    public void deveDeletarLivro(){
        Livro livro = criarLivro();
        entityManager.persist(livro);
        Livro livroAchado = entityManager.find(Livro.class, livro.getId());

        livroRepository.delete(livroAchado);

        Livro livroDeletado = entityManager.find(Livro.class, livro.getId());
        assertThat(livroDeletado).isNull();
    }
    private Livro criarLivro(){
        return new Livro("Narnia","Costa","1234");
    }

}

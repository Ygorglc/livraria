package br.com.seap.livraria.services;

import br.com.seap.livraria.controllers.request.LivroRequest;
import br.com.seap.livraria.controllers.response.LivroResponse;
import br.com.seap.livraria.exceptions.EntidadeNaoEncontradaException;
import br.com.seap.livraria.exceptions.NegocioException;
import br.com.seap.livraria.models.Livro;
import br.com.seap.livraria.repositories.LivroRepository;
import br.com.seap.livraria.services.impl.LivroServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LivroServiceTest {

    @Mock
    LivroRepository livroRepository;
    @InjectMocks
    LivroServiceImpl livroService;

    LivroRequest livroRequest;
    Livro livro;

    @BeforeEach
    public void setUp() {
        livroRequest = new LivroRequest(12L,"Dark","jfs","2222");
        livro = new Livro(livroRequest.id(),livroRequest.titulo(),livroRequest.autor(),livroRequest.isbn());
        ReflectionTestUtils.setField(livro,"id", 12L);
    }
    @Test
    @DisplayName("Deve salvar um livro.")
    public void salvarLivroTest() {

        when(livroRepository.save(any(Livro.class))).thenReturn(livro);
        Livro livroSalvo = livroService.salvar(livro);

        assertThat(livroSalvo.getId()).isNotNull();
        assertThat(livroSalvo.getAutor()).isEqualTo("jfs");
        assertThat(livroSalvo.getTitulo()).isEqualTo("Dark");
        assertThat(livroSalvo.getIsbn()).isEqualTo("2222");
    }

    @Test
    @DisplayName("Develançar um erro de negocio ao tentar salvar um livro com isbn duplicado")
    public void naoPermiteSalvarLivrComISBNDuplicado() {
        when(livroRepository.existsByIsbn(Mockito.any(String.class))).thenReturn(true);

        Throwable exception = Assertions.catchThrowable(()->livroService.salvar(livro));

        assertThat(exception).isInstanceOf(NegocioException.class)
                .hasMessage("Isbn já cadastrado.");

        Mockito.verify(livroRepository, Mockito.never()).save(livro);
    }


    @Test
    @DisplayName("Deve obter um livro por id.")
    public void retornarLivroPorIdTest(){
        Long id = 11L;
        Livro livroRetorno = criarLivro();
        livroRetorno.setId(id);

        Mockito.when(livroRepository.findById(id)).thenReturn(Optional.of(livroRetorno));

        Optional<Livro> livroEncontrado = Optional.ofNullable(livroService.retornar(id));

        assertThat(livroEncontrado.isPresent()).isTrue();
        assertThat(livroEncontrado.get().getId()).isEqualTo(id);
        assertThat(livroEncontrado.get().getAutor()).isEqualTo(livroRetorno.getAutor());
        assertThat(livroEncontrado.get().getTitulo()).isEqualTo(livroRetorno.getTitulo());
        assertThat(livroEncontrado.get().getIsbn()).isEqualTo(livroRetorno.getIsbn());
    }

    @Test
    @DisplayName("Deve retornar uma exception caso o livro não exista.")
    public void livroNaoEncontradoTest() {
        Long id = 1L;
        when(livroRepository.findById(id)).thenReturn(Optional.empty());

        Throwable exception = Assertions.catchThrowable(()->livroService.retornar(id));

        assertThat(exception).isInstanceOf(EntidadeNaoEncontradaException.class)
                .hasMessage("Livro buscado não existe.");
    }

    @Test
    @DisplayName("Deve deletar um livro.")
    public void deletarLivro(){
        Livro livro3 = criarLivro();

        assertDoesNotThrow(()-> livroService.deletar(livro3));

        Mockito.verify(livroRepository, Mockito.times(1)).delete(livro3);
    }

//    @Test
//    @DisplayName("Deve ocorrer um erro ao tentar deletar um livro.")
//    public void erroAoDeletar() {
//        assertThrows(EntidadeEmUsoException.class, ()-> livroService.deletar(new Livro()));
//
//        Throwable exception = Assertions.catchThrowable(()->livroService.deletar(new Livro()));
//
//        assertThat(exception).isInstanceOf(EntidadeEmUsoException.class)
//                .hasMessage("O Livro está em uso no momento e não pode ser deletado.");
//    }

    @Test
    @DisplayName("Deve atualizar um livro")
    public void atualizaLivro(){
        Long id =13L;

        Livro livroAtualizar = criarLivro();
        livroAtualizar.setId(id);

        Livro livroAtualizado = criarLivro();
        livroAtualizado.setId(id);

        Mockito.when(livroRepository.save(livroAtualizar)).thenReturn(livroAtualizado);

        Livro livro4 = livroService.atualizar(livroAtualizar);

        assertThat(livro4.getId()).isEqualTo(livroAtualizado.getId());
        assertThat(livro4.getTitulo()).isEqualTo(livroAtualizado.getTitulo());
        assertThat(livro4.getAutor()).isEqualTo(livroAtualizado.getAutor());
        assertThat(livro4.getIsbn()).isEqualTo(livroAtualizado.getIsbn());
    }

    @Test
    @DisplayName("Deve filtrar livros pelas propriedades.")
    public void findBookTest(){
        Livro livro = criarLivro();

        PageRequest pageRequest = PageRequest.of(0,10);

        List<Livro> lista = Arrays.asList(livro);
        Page<Livro> page = new PageImpl<Livro>(lista, pageRequest, 1);
        when( livroRepository.findAll(Mockito.any(Example.class), Mockito.any(PageRequest.class)))
                .thenReturn(page);

        Page<Livro> resultado = livroService.filtrar(livro, pageRequest);

        assertThat(resultado.getTotalElements()).isEqualTo(1);
        assertThat(resultado.getContent()).isEqualTo(lista);
        assertThat(resultado.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(resultado.getPageable().getPageSize()).isEqualTo(10);
    }
    private LivroRequest criarLivroRequest() {
        return new LivroRequest(1L,"Narnia","Costa","1234");
    }

    private LivroResponse criarLivroResponse() {
        return new LivroResponse(1L,"Narnia","Costa","1234");
    }

    private Livro criarLivro() {
        return new Livro("Narnia","Costa","1234");
    }
}

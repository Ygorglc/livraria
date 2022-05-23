package br.com.seap.livraria.controllers;

import br.com.seap.livraria.controllers.request.LivroRequest;
import br.com.seap.livraria.controllers.response.LivroResponse;
import br.com.seap.livraria.exceptions.EntidadeNaoEncontradaException;
import br.com.seap.livraria.exceptions.NegocioException;
import br.com.seap.livraria.models.Livro;
import br.com.seap.livraria.repositories.LivroRepository;
import br.com.seap.livraria.services.impl.LivroServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import org.springframework.data.domain.Pageable;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = LivroController.class)
@AutoConfigureMockMvc
public class LivroControllerTest {

    static String LIVRO_API = "/v1/livro";

    @Autowired
    MockMvc mvc;

    @MockBean
    LivroServiceImpl livroService;

    @InjectMocks
    LivroController livroController;

    @Mock
    LivroRepository livroRepository;
    LivroRequest livroRequest;
    LivroResponse respostaLivro;
    Livro livro;

    Livro livro1;
    @BeforeEach
    public void setUp(){
        livroRequest = new LivroRequest(11L,"Novo","Carlos","12345");
        respostaLivro = new LivroResponse(12L,"Branca", "Autor","1234");
        livro = new Livro(respostaLivro.id(), respostaLivro.titulo(), respostaLivro.autor(), respostaLivro.isbn());
        ReflectionTestUtils.setField(livro,"id", 12L);
        livro1 = new Livro(10L,"Abc","Abc","123");
        ReflectionTestUtils.setField(livro1,"id", 10L);
    }

    @Test
    @DisplayName("Deve criar um livro com sucesso.")
    public void criandoLivroTest() throws Exception{


        BDDMockito.given(livroService.salvar(Mockito.any(Livro.class))).willReturn(livro);

        String json = new ObjectMapper().writeValueAsString(livroRequest);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(LIVRO_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("titulo").value("Branca"))
                .andExpect(jsonPath("autor").value("Autor"))
                .andExpect(jsonPath("isbn").value("1234"));
    }

    @Test
    @DisplayName("Deve tentar criar um livro invalido e retornar um erro.")
    public void criandoLivroInvalidoTest() throws Exception{

        String json = new ObjectMapper().writeValueAsString(new Livro(null,null,null,null));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(LIVRO_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isBadRequest());
//                .andExpect(jsonPath("errors", hasSize(1)));

    }

    @Test
    @DisplayName("Deve lançar um erro ao cadastrar livro caso o isbn já existir.")
    public void tentaCriarLivroComISBNDuplicadoTest() throws Exception {
        String json = new ObjectMapper().writeValueAsString(livroRequest);
        String mensagemErro = "Isbn já cadastrado.";

        BDDMockito.given(livroService.salvar(Mockito.any(Livro.class)))
                .willThrow(new NegocioException(mensagemErro));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(LIVRO_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isBadRequest());
//                .andExpect(jsonPath("errors", hasSize(1)))
//                .andExpect(jsonPath("errors[0]").value(mensagemErro));
    }


    @Test
    @DisplayName("Deve obter informações  de um livro.")
    public void retornaInformacoesLivro() throws Exception {
        Long id = 12l;

        BDDMockito.given(livroService.retornar(id)).willReturn(livro);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(LIVRO_API.concat("/"+id))
                .accept(MediaType.APPLICATION_JSON);

        System.out.println(livro.getId());
        System.out.println(livro.getTitulo());
        System.out.println(livro.getAutor());
        System.out.println(livro.getIsbn());

        mvc
                .perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("titulo").value(livro.getTitulo()))
                .andExpect(jsonPath("autor").value(livro.getAutor()))
                .andExpect(jsonPath("isbn").value(livro.getIsbn()));
    }

    @Test
    @DisplayName("Deve retornar resource not found quando livro procuradonão existir.")
    public void livroNaoEncontradoTest() throws Exception{
        BDDMockito.given( livroService.retornar(Mockito.anyLong())).willThrow(EntidadeNaoEncontradaException.class);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(LIVRO_API.concat("/" + 1))
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve deletar um livro.")
    public void deletarLivroTest() throws Exception {
        doNothing().when(livroService).deletar(any(Livro.class));
        BDDMockito.given(livroService.retornar(livro1.getId())).willReturn(livro1);

//        livroController.deletar(new LivroRequest(livro1.getId()
//                                                ,livro1.getTitulo()
//                                                ,livro1.getAutor()
//                                                ,livro1.getIsbn()));
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(LIVRO_API.concat("/"+livro1.getId()));

        mvc.perform(request)
                .andExpect(status().isNoContent());
    }

//    @Test
//    @DisplayName("Deve retornar conflict quando o livro estiver em uso e não puder ser deletado.")
//    public void conflitoAoDeletarLivroTest() throws Exception {
//        BDDMockito.given(livroRepository.findById(anyLong())).willReturn(Optional.empty());
//        //BDDMockito.doThrow(EntidadeEmUsoException.class).when(livroService).deletar(livro);
//        //        final Exception ex = new Exception();
////
////        EntidadeEmUsoException exception = assertThrows(EntidadeEmUsoException.class,()->
////        {   livroService.deletar(Mockito.any(Livro.class));
////        },"");
////
////        Assertions.assertEquals("",exception.getMessage());
//        //BDDMockito.doThrow(EntidadeEmUsoException.class).when(livroService).deletar(Mockito.any(Livro.class));
//        //doThrow(EntidadeEmUsoException.class).when(livroRepository).delete(any(Livro.class));
//
//        Livro livroDeletar = livroService.retornarLivro(10L);
//        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
//                .delete(LIVRO_API.concat("/"+10));
//
//        mvc.perform(request)
//                .andExpect(status().isConflict());
//    }

    @Test
    @DisplayName("Deve atualizar um livro")
    public void atualizaLivroTest() throws Exception {
        Long id = 9L;
        Livro livroAtualiza = new Livro("abc","abc","1234");
        ReflectionTestUtils.setField(livroAtualiza,"id", id);
        String json = new ObjectMapper().writeValueAsString(criarLivroRequest());

        BDDMockito.given(livroService.retornar(anyLong())).willReturn(livroAtualiza);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(LIVRO_API.concat("/"+id))
                .content(json)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mvc.perform( request )
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("titulo").value(criarLivroResponse().titulo()))
                .andExpect(jsonPath("autor").value(criarLivroResponse().autor()))
                .andExpect(jsonPath("isbn").value(criarLivroResponse().isbn()));
    }

    @Test
    @DisplayName("Deve filtrar livros.")
    public void filtrarLivroTest() throws Exception {
        Long id = 1L;

        Livro livro = criarLivro();
        livro.setId(id);

        LivroResponse livroResponse = new LivroResponse(livro.getId(),livro.getTitulo(),livro.getAutor(),livro.getIsbn());

        BDDMockito.given( livroService.filtrar(Mockito.any(Livro.class),Mockito.any(Pageable.class)) )
                .willReturn( new PageImpl<Livro>(Arrays.asList(livro), PageRequest.of(0,100), 1) );

        String queryString = String.format("?titulo=%s&autor=%s&page=0&size==100",
                livro.getTitulo(), livro.getAutor());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(LIVRO_API.concat(queryString))
                .accept(MediaType.APPLICATION_JSON);

        mvc
                .perform( request )
                .andExpect( status().isOk() )
                .andExpect( jsonPath("content", Matchers.hasSize(1)) )
                .andExpect( jsonPath("totalElements").value(1))
                .andExpect( jsonPath("pageable.pageSize").value(100))
                .andExpect( jsonPath("pageable.pageNumber").value(0));

    }

//    @Test
//    @DisplayName("Erro ao atualizar um livro")
//    public void erroAoAtualizaLivroTest() throws Exception {
//        Livro livroAtualiza = new Livro("abc","abc","12345");
//        String json = new ObjectMapper().writeValueAsString(livroAtualiza);
//
//        BDDMockito.given(livroService.retornarLivro(anyLong())).willReturn(any(Livro.class));
//
//        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
//                .put(LIVRO_API.concat("/"+13))
//                .content(json)
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON);
//
//        mvc.perform( request )
//                .andExpect(status().isOk());
//    }
    private LivroRequest criarLivroRequest(){
        return new LivroRequest(1L,"Narnia","Costa","1234");
    }
    private LivroResponse criarLivroResponse(){
        return new LivroResponse(1L,"Narnia","Costa","1234");
    }

    private Livro criarLivro(){
        return new Livro("Narnia","Costa","1234");
    }
}


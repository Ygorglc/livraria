package br.com.seap.livraria.controllers;

import br.com.seap.livraria.controllers.request.EmprestimoRequest;
import br.com.seap.livraria.services.LivroService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@WebMvcTest(controllers = EmprestimoController.class)
public class EmprestimoControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    private LivroService livroService;

    @Test
    @DisplayName("Deve realizar um emprestimo.")
    public void criandoEmprestimoTeste() throws JsonProcessingException {
        EmprestimoRequest emprestimoRequest = new EmprestimoRequest("123", "Costa");
        String json = new ObjectMapper().writeValueAsString(emprestimoRequest);
        //BDDMockito.given()
    }

}

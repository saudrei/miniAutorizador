package br.com.vr.autorizador.vrminiautorizador.controller;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.vr.autorizador.vrminiautorizador.domain.Cartao;
import br.com.vr.autorizador.vrminiautorizador.dto.CartaoDTO;
import br.com.vr.autorizador.vrminiautorizador.dto.TransacaoDTO;
import br.com.vr.autorizador.vrminiautorizador.helper.Helper;
import br.com.vr.autorizador.vrminiautorizador.repository.CartaoRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("staging")
@Sql(value = "/clean-database.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestMethodOrder(OrderAnnotation.class)
public class CartaoControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CartaoRepository cartaoRepository;

    @BeforeEach
    void criarCartao(){
        Cartao cartao = new Cartao();
        cartao.setNumeroCartao("6549873025634501");
        cartao.setSenha("1234");
        cartaoRepository.save(cartao);
    }
    
    @Test
    @DisplayName("[integration] - criacao de um cartão")
    @Order(1)
    void shouldCreatedOneCartaoAndReturnCode201() throws JsonProcessingException, Exception {
        CartaoDTO cartao = Helper.buildGenericCartaoDTO();
        cartao.setNumeroCartao("6549873025634588");
        mockMvc.perform(post("/cartoes").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartao)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("[integration] - verificação do saldo inicial do cartão recém-criado no valor de 500.00")
    @Order(2)
    void shouldReturnABalanceReturnSaldo500() throws JsonProcessingException, Exception {
        CartaoDTO cartao = Helper.buildGenericCartaoDTO();
        cartao.setNumeroCartao("9949873025634599");
        cartao.setSenha("91234567");
        mockMvc.perform(post("/cartoes").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cartao)));

        mockMvc.perform(get("/cartoes/"+cartao.getNumeroCartao()).contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().string("500.00"));
    }

    @Test
    @DisplayName("[integration] - retornar nao encontrado um cartão")
    @Order(3)
    void shouldReturnABalanceReturnSaldoNotFound() throws JsonProcessingException, Exception {
        CartaoDTO cartao = Helper.buildGenericCartaoDTO();
        cartao.setNumeroCartao("9949873025634666");
        cartao.setSenha("91234567");
        mockMvc.perform(post("/cartoes").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cartao)));

        mockMvc.perform(get("/cartoes/2213213213213").contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("[integration] - realizar transacao com sucesso")
    @Order(4)
    void shouldReturnARealizarTransacaoReturnCode200() throws JsonProcessingException, Exception {
        TransacaoDTO transacaoDTO = Helper.buildGenericTransacaoDTO();
        mockMvc.perform(post("/transacoes").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transacaoDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("[integration] - realizar transacao com retorno de cartão inexistente")
    @Order(7)
    void shouldReturnARealizarTransacaoReturnCode422CartaoInexistente() throws JsonProcessingException, Exception {
        TransacaoDTO transacaoDTO = Helper.buildGenericCartaoInexistenteTransacaoDTO();
        mockMvc.perform(post("/transacoes").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transacaoDTO)))
                .andExpect(content().string("CARTAO_INEXISTENTE"))
                .andExpect(status().isUnprocessableEntity());
    }
    
    
    @Test
    @DisplayName("[integration] - realizar transacao com retorno de saldo insuficiente")
    @Order(5)
    void shouldReturnARealizarTransacaoReturnCode422SaldoInsuficiente() throws JsonProcessingException, Exception {
    	TransacaoDTO transacaoDTO = Helper.buildGenericSaldoInsuficienteTransacaoDTO();
        mockMvc.perform(post("/transacoes").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transacaoDTO)))
                .andExpect(content().string("SALDO_INSUFICIENTE"))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("[integration] - realizar transacao com retorno de senha inválida")
    @Order(6)
    void shouldReturnARealizarTransacaoReturnCode422SenhaInvalida() throws JsonProcessingException, Exception {
    	TransacaoDTO transacaoDTO = Helper.buildGenericSenhaInvalidaTransacaoDTO();
        mockMvc.perform(post("/transacoes").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transacaoDTO)))
                .andExpect(content().string("SENHA_INVALIDA"))
                .andExpect(status().isUnprocessableEntity());
    }



}


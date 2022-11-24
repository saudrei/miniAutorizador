package br.com.vr.autorizador.vrminiautorizador.controller;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import br.com.vr.autorizador.vrminiautorizador.request.CartaoRequest;
import br.com.vr.autorizador.vrminiautorizador.request.TransacaoRequest;
import br.com.vr.autorizador.vrminiautorizador.response.CartaoResponse;


@ActiveProfiles("test")
@Sql(value = "/load-database.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/clean-database.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@DirtiesContext
@SpringBootTest 
@TestMethodOrder(OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
public class CartaoControllerIntegrationTest {
   

	private TestRestTemplate  restTemplate;
	
	@Value("${service.miniautorizador.url:http://localhost:8080}")
	private String url;
	
    @BeforeEach
    void setUp() {
        FixtureFactoryLoader.loadTemplates("br.com.vr.autorizador.vrminiautorizador.utils.fixture");
    }
    
    @Test
    @DisplayName("[integration] - criacao de um cartão")
    @Order(1)
    void shouldCreatedOneCartaoAndReturnCode201() {
    	restTemplate = new TestRestTemplate();
    	CartaoRequest cartaoRequest = Fixture.from(CartaoRequest.class).gimme("createCartao");
    	ResponseEntity<CartaoResponse> response = 
    			restTemplate.postForEntity(url + "/cartoes", cartaoRequest, CartaoResponse.class);
    	
    	CartaoResponse cartaoResponse = response.getBody();
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(cartaoRequest.getNumeroCartao(), cartaoResponse.getNumeroCartao());
    }
    
    @Test
    @DisplayName("[integration] - verificação do saldo inicial do cartão recém-criado no valor de 500.00")
    @Order(2)
    void shouldReturnABalanceReturnSaldo500() {
    	restTemplate = new TestRestTemplate();
    	CartaoRequest cartaoRequest = Fixture.from(CartaoRequest.class).gimme("createCartao");
    	ResponseEntity<CartaoResponse> responseCreate = 
    			restTemplate.postForEntity(url + "/cartoes", cartaoRequest, CartaoResponse.class);
    	CartaoResponse cartaoResponse = responseCreate.getBody();
        ResponseEntity<BigDecimal> response = restTemplate.getForEntity(url + "/cartoes/{id}", BigDecimal.class, cartaoResponse.getNumeroCartao());
        BigDecimal saldoResponse = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(new BigDecimal("500.00"), saldoResponse);
    }
    
    @Test
    @DisplayName("[integration] - realizar transacao com sucesso")
    @Order(3)
    void shouldReturnARealizarTransacaoReturnCode200() {
    	restTemplate = new TestRestTemplate();
    	CartaoRequest cartaoRequest = Fixture.from(CartaoRequest.class).gimme("createCartao");
    	ResponseEntity<CartaoResponse> responseCreate = 
    			restTemplate.postForEntity(url + "/cartoes", cartaoRequest, CartaoResponse.class);

    	TransacaoRequest transacaoRequest = Fixture.from(TransacaoRequest.class).gimme("transcaoCartaoSucesso");

    	ResponseEntity<?> responseRealizarTransacao = restTemplate.postForEntity(url + "/transacoes",transacaoRequest, String.class);
        assertEquals(HttpStatus.CREATED, responseRealizarTransacao.getStatusCode());
    }
    
    @Test
    @DisplayName("[integration] - realizar transacao com retorno de saldo insuficiente")
    @Order(4)
    void shouldReturnARealizarTransacaoReturnCode422SaldoInsuficiente() {
    	restTemplate = new TestRestTemplate();
    	CartaoRequest cartaoRequest = Fixture.from(CartaoRequest.class).gimme("createCartao");
    	ResponseEntity<CartaoResponse> responseCreate = 
    			restTemplate.postForEntity(url + "/cartoes", cartaoRequest, CartaoResponse.class);

    	TransacaoRequest transacaoRequest = Fixture.from(TransacaoRequest.class).gimme("transcaoCartaoSaldoInsuficiente");

    	ResponseEntity<?> responseRealizarTransacao = restTemplate.postForEntity(url + "/transacoes",transacaoRequest, String.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseRealizarTransacao.getStatusCode());
    }
    
    @Test
    @DisplayName("[integration] - realizar transacao com retorno de senha inválida")
    @Order(5)
    void shouldReturnARealizarTransacaoReturnCode422SenhaInvalida() {
    	restTemplate = new TestRestTemplate();
    	CartaoRequest cartaoRequest = Fixture.from(CartaoRequest.class).gimme("createCartao");
    	ResponseEntity<CartaoResponse> responseCreate = 
    			restTemplate.postForEntity(url + "/cartoes", cartaoRequest, CartaoResponse.class);

    	TransacaoRequest transacaoRequest = Fixture.from(TransacaoRequest.class).gimme("transcaoCartaoSenhaInvalida");

    	ResponseEntity<?> responseRealizarTransacao = restTemplate.postForEntity(url + "/transacoes",transacaoRequest, String.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseRealizarTransacao.getStatusCode());
    }
    
    @Test
    @DisplayName("[integration] - realizar transacao com retorno de cartão inexistente")
    @Order(6)
    void shouldReturnARealizarTransacaoReturnCode422CartaoInexistente() {
    	restTemplate = new TestRestTemplate();
    	CartaoRequest cartaoRequest = Fixture.from(CartaoRequest.class).gimme("createCartao");
    	ResponseEntity<CartaoResponse> responseCreate = 
    			restTemplate.postForEntity(url + "/cartoes", cartaoRequest, CartaoResponse.class);

    	TransacaoRequest transacaoRequest = Fixture.from(TransacaoRequest.class).gimme("transcaoCartaoCartaoInexistente");

    	ResponseEntity<?> responseRealizarTransacao = restTemplate.postForEntity(url + "/transacoes",transacaoRequest, String.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseRealizarTransacao.getStatusCode());
    }
    

}

package br.com.vr.autorizador.vrminiautorizador.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.vr.autorizador.vrminiautorizador.business.CartaoBusiness;
import br.com.vr.autorizador.vrminiautorizador.converter.CartaoConverter;
import br.com.vr.autorizador.vrminiautorizador.dto.CartaoDTO;
import br.com.vr.autorizador.vrminiautorizador.dto.CartaoOutDTO;
import br.com.vr.autorizador.vrminiautorizador.dto.TransacaoDTO;
import br.com.vr.autorizador.vrminiautorizador.exceptions.RecordNotFoundException;
import br.com.vr.autorizador.vrminiautorizador.exceptions.UnprocessableException;
import br.com.vr.autorizador.vrminiautorizador.helper.Helper;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = CartaoController.class)
@ActiveProfiles("test")
public class CartaoControllerIT {
	
	private static final String ENDPOINT_CARTOES = "/cartoes";
	private static final String ENDPOINT_TRANSACAO = "/transacao";
	
	private static final String SALDO_INSUFICIENTE = "SALDO_INSUFICIENTE";
	private static final String SENHA_INVALIDA = "SENHA_INVALIDA";
	private static final String CARTAO_INEXISTENTE = "CARTAO_INEXISTENTE";
	private static final String NUMEROCARTAO = "6549873025634501";
	private static final String NUMEROCARTAO_NOTEXIST = "56549873025634501";
	
	@MockBean
	private CartaoBusiness business;
	
	@MockBean
	private CartaoConverter converter;
	
	@Autowired
	private MockMvc mockMvc;
	
	private ObjectMapper mapper = new ObjectMapper();
	
	
	@Test
	public void testCreateCartaoSucessCode201() throws JsonProcessingException, Exception {
		CartaoDTO docMock = Helper.buildGenericCartaoDTO();
		CartaoOutDTO  cartaoOutDTO  = Helper.buildGenericCartaoOutDTO();
		when(business.createCartao(docMock)).thenReturn(cartaoOutDTO);
		mockMvc.perform(post(ENDPOINT_CARTOES)
				.content(mapper.writeValueAsString(docMock)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.numeroCartao").value(cartaoOutDTO.getNumeroCartao()));
	}
	
	@Test
	public void testCreateCartaoNumeroCartaoBadRequestCode400() throws JsonProcessingException, Exception {
		CartaoDTO docMock = Helper.buildGenericCartaoDTONumeroCartaoBadRequest();
		mockMvc.perform(post(ENDPOINT_CARTOES)
				.content(mapper.writeValueAsString
						(docMock)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.details").value("numeroCartao - Must be 13 or 16 digits"));
	}
	
	@Test
	public void testCreateCartaoSenhaBadRequestCode400() throws JsonProcessingException, Exception {
		CartaoDTO docMock = Helper.buildGenericCartaoDTOSenhaBadRequest();
		mockMvc.perform(post(ENDPOINT_CARTOES)
				.content(mapper.writeValueAsString(docMock)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.details").value("senha - The senha could not be null or empty."));
	}
	
	@Test
	public void testCreateCartaoJaExistCode422() throws JsonProcessingException, Exception {
		CartaoDTO docMock = Helper.buildGenericCartaoDTO();
		doThrow(new UnprocessableException("")).when(business).createCartao(docMock);
		mockMvc.perform(post(ENDPOINT_CARTOES)
				.content(mapper.writeValueAsString(docMock)).contentType(MediaType.APPLICATION_JSON))
		         .andExpect(status().isUnprocessableEntity());
		
	}
	
	@Test
	public void testObterSaldoComSucessoCode200() throws Exception {
		when(business.obterSaldo(NUMEROCARTAO)).thenReturn(new BigDecimal(500));
		ResultActions resultActions = mockMvc.perform(get(ENDPOINT_CARTOES+"/"+NUMEROCARTAO))
		 	.andDo(print())
			.andExpect(status().isOk());

		MvcResult result = resultActions.andReturn();
		String contentAsString = result.getResponse().getContentAsString();
		
		assertEquals(new BigDecimal("500"), new BigDecimal(contentAsString));

		verify(business).obterSaldo(NUMEROCARTAO);
	}
	
	@Test
	public void testObterSaldoCartaoNaoExisteCode404() throws Exception {
		doThrow(new RecordNotFoundException(CARTAO_INEXISTENTE)).when(business).obterSaldo(NUMEROCARTAO_NOTEXIST);

		mockMvc.perform(get(ENDPOINT_CARTOES+"/"+NUMEROCARTAO_NOTEXIST))
		 	.andDo(print())
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.details").value("CARTAO_INEXISTENTE"));;

		verify(business).obterSaldo(NUMEROCARTAO_NOTEXIST);
	}
	

 


}

package br.com.vr.autorizador.vrminiautorizador.business;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.test.context.ActiveProfiles;

import br.com.vr.autorizador.vrminiautorizador.converter.CartaoConverter;
import br.com.vr.autorizador.vrminiautorizador.domain.Cartao;
import br.com.vr.autorizador.vrminiautorizador.dto.CartaoDTO;
import br.com.vr.autorizador.vrminiautorizador.dto.CartaoOutDTO;
import br.com.vr.autorizador.vrminiautorizador.dto.TransacaoDTO;
import br.com.vr.autorizador.vrminiautorizador.exceptions.RecordNotFoundException;
import br.com.vr.autorizador.vrminiautorizador.exceptions.UnprocessableException;
import br.com.vr.autorizador.vrminiautorizador.helper.Helper;
import br.com.vr.autorizador.vrminiautorizador.service.CartaoService;

@RunWith(MockitoJUnitRunner.Silent.class)
@ActiveProfiles("test")
public class CartaoBusinessTest {
	
	@InjectMocks
	private CartaoBusiness business;
	@Mock
	private  CartaoService service;
	@Mock
	private  CartaoConverter converter;
	
	private final String numeroCartao = UUID.randomUUID().toString();
	
	

	@Test
	public void testCreateCartaoSucess() {
		
		CartaoDTO dockMockIn = Helper.buildGenericCartaoDTO();
		dockMockIn.setNumeroCartao(numeroCartao);
		Cartao dockMock = Helper.buildGenericCartao();
		dockMock.setNumeroCartao(numeroCartao);
		CartaoOutDTO dockMockOut = Helper.buildGenericCartaoOutDTO();
		dockMockOut.setNumeroCartao(numeroCartao);
		when(converter.convertCartaoDTOToCartao(dockMockIn)).thenReturn(dockMock);		
		when(converter.convertCartaoToCartaoOutDTO(dockMock)).thenReturn(dockMockOut);
		when(service.save(dockMock)).thenReturn(dockMock);
		CartaoOutDTO dockMockReturn = business.createCartao(dockMockIn);
		assertNotNull(dockMockReturn);		
	}
	
	@Test
	public void testCreateCartaoExist() {
		
		CartaoDTO dockMockIn = Helper.buildGenericCartaoDTO();
		dockMockIn.setNumeroCartao(numeroCartao);
		Cartao dockMock = Helper.buildGenericCartao();
		dockMock.setNumeroCartao(numeroCartao);
		CartaoOutDTO dockMockOut = Helper.buildGenericCartaoOutDTO();
		dockMockOut.setNumeroCartao(numeroCartao);
		Optional<Cartao>  dockMockOptional = Optional.of(Helper.buildGenericCartao());
		dockMockOptional.get().setNumeroCartao(numeroCartao);
		when(converter.convertCartaoDTOToCartao(dockMockIn)).thenReturn(dockMock);
		when(service.findById(dockMockOptional.get().getNumeroCartao())).thenReturn(dockMockOptional);
		when(converter.convertCartaoToCartaoOutDTO(dockMock)).thenReturn(dockMockOut);
		when(service.save(dockMock)).thenReturn(dockMock);
		try{
			business.createCartao(dockMockIn);
		}catch (Exception ex){	
			assertEquals("CARTAO_EXISTENTE" , ex.getMessage());
		}	
	}

	
	@Test
	public void testRealizarTransacaoSucess() throws NotFoundException {
		TransacaoDTO dockMock =  Helper.buildGenericTransacaoDTO();
		dockMock.setNumeroCartao(numeroCartao);		
		Optional<Cartao>  dockMockOptional = Optional.of(Helper.buildGenericCartao());
		dockMockOptional.get().setNumeroCartao(numeroCartao);
		when(service.findById(numeroCartao)).thenReturn(dockMockOptional);
		business.realizarTransacao(dockMock);
	}
	
	@Test
	public void testRealizarTransacaoCartaoNaoExist() {
		TransacaoDTO dockMock =  Helper.buildGenericTransacaoDTO();
		Optional<Cartao>  dockMockOptional = Optional.of(Helper.buildGenericCartao());
		dockMockOptional.get().setNumeroCartao(numeroCartao);
		when(service.findById(numeroCartao)).thenReturn(dockMockOptional);
		try{
			business.realizarTransacao(dockMock);
		}catch (Exception ex){	
			assertEquals(UnprocessableException.CARTAO_INEXISTENTE , ex.getMessage());
			assertInstanceOf(UnprocessableException.class, ex);
		}
		
	}
	
	@Test
	public void testRealizarTransacaoCartaoSaldoInsuficente() {
		TransacaoDTO dockMock =  Helper.buildGenericTransacaoDTO();
		dockMock.setValor(new BigDecimal(100000));
		dockMock.setNumeroCartao(numeroCartao);	
		Optional<Cartao>  dockMockOptional = Optional.of(Helper.buildGenericCartao());
		dockMockOptional.get().setNumeroCartao(numeroCartao);
		when(service.findById(numeroCartao)).thenReturn(dockMockOptional);
		try{
			business.realizarTransacao(dockMock);
		}catch (Exception ex){	
			assertEquals(UnprocessableException.SALDO_INSUFICIENTE, ex.getMessage());
			assertInstanceOf(UnprocessableException.class, ex);
		}
		
	}
	
	@Test
	public void testRealizarTransacaoCartaoSenhaInvalida() {
		TransacaoDTO dockMock =  Helper.buildGenericTransacaoDTO();
		dockMock.setNumeroCartao(numeroCartao);
		Optional<Cartao>  dockMockOptional = Optional.of(Helper.buildGenericCartao());
		dockMockOptional.get().setNumeroCartao(numeroCartao);
		dockMockOptional.get().setSenha(UUID.randomUUID().toString());
		when(service.findById(numeroCartao)).thenReturn(dockMockOptional);
		try{
			business.realizarTransacao(dockMock);
		}catch (Exception ex){	
			assertEquals(UnprocessableException.SENHA_INVALIDA, ex.getMessage());
			assertInstanceOf(UnprocessableException.class, ex);
		}
		
	}
	
	@Test
	public void testObterSaldoSucess() {
		Cartao dockMock =  Helper.buildGenericCartao();
		dockMock.setNumeroCartao(numeroCartao);
		BigDecimal saldoIn = dockMock.getSaldo();
		Optional<Cartao>  dockMockOptional = Optional.of(dockMock);
		when(service.findById(numeroCartao)).thenReturn(dockMockOptional);
		BigDecimal saldoOut = business.obterSaldo(numeroCartao);
		assertEquals(saldoIn, saldoOut);
	}
	
	@Test(expected = RecordNotFoundException.class)
	public void testObterSaldoCartaoNaoExist() {
		when(service.findById(numeroCartao)).thenReturn( Optional.empty());
       	business.obterSaldo(numeroCartao);
	}
}

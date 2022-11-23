package br.com.vr.autorizador.vrminiautorizador.convert;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import br.com.vr.autorizador.vrminiautorizador.converter.TransacaoConverter;
import br.com.vr.autorizador.vrminiautorizador.domain.Transacao;
import br.com.vr.autorizador.vrminiautorizador.dto.TransacaoDTO;
import br.com.vr.autorizador.vrminiautorizador.helper.Helper;


@RunWith(MockitoJUnitRunner.class)
public class TransacaoConverterTest {
	
	@InjectMocks
	private TransacaoConverter converter;
	
	@Test
	public void convertCartaoDTOToCartaoTest() {
		TransacaoDTO dockMockIn  = Helper.buildGenericTransacaoDTO();
		Transacao dockMockOut = converter.convertCartaoDTOToCartao(dockMockIn);
		assertEquals(dockMockIn.getNumeroCartao(),dockMockOut.getNumeroCartao());
	
	}
	
	

}

package br.com.vr.autorizador.vrminiautorizador.convert;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import br.com.vr.autorizador.vrminiautorizador.converter.CartaoConverter;
import br.com.vr.autorizador.vrminiautorizador.domain.Cartao;
import br.com.vr.autorizador.vrminiautorizador.dto.CartaoDTO;
import br.com.vr.autorizador.vrminiautorizador.dto.CartaoOutDTO;
import br.com.vr.autorizador.vrminiautorizador.helper.Helper;

@RunWith(MockitoJUnitRunner.class)
public class CartaoConverterTest {
	
	@InjectMocks
	private CartaoConverter converter;
	
	@Test
	public void convertCartaoDTOToCartaoTest() {
		CartaoDTO dockMockIn  = Helper.buildGenericCartaoDTO();
		Cartao dockMockOut = converter.convertCartaoDTOToCartao(dockMockIn);
		assertEquals(dockMockIn.getNumeroCartao(),dockMockOut.getNumeroCartao());
		
	}
	
	@Test
	public void convertCartaoToCartaoOutDTOTest() {
		Cartao dockMockIn  = Helper.buildGenericCartao();
		CartaoOutDTO dockMockOut =  converter.convertCartaoToCartaoOutDTO(dockMockIn);
		assertEquals(dockMockIn.getNumeroCartao(),dockMockOut.getNumeroCartao());
	}
	
	
	@Test
	public void convertCartaoDTOToCartaoOutDTOTest() {
		CartaoDTO dockMockIn  = Helper.buildGenericCartaoDTO();
		CartaoOutDTO dockMockOut = converter.convertCartaoDTOToCartaoOutDTO(dockMockIn);
		assertEquals(dockMockIn.getNumeroCartao(),dockMockOut.getNumeroCartao());
	}

}

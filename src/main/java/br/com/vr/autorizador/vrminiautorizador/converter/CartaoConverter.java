package br.com.vr.autorizador.vrminiautorizador.converter;


import org.springframework.stereotype.Component;

import br.com.vr.autorizador.vrminiautorizador.domain.Cartao;
import br.com.vr.autorizador.vrminiautorizador.dto.CartaoDTO;
import br.com.vr.autorizador.vrminiautorizador.dto.CartaoOutDTO;

@Component
public class CartaoConverter {
	
	public CartaoConverter() {
		super();
	}
	
	public Cartao convertCartaoDTOToCartao(CartaoDTO entity) {
		return Cartao.builder().numeroCartao(entity.getNumeroCartao())
			.senha(entity.getSenha())
			.build();
	}
	
	public CartaoOutDTO convertCartaoToCartaoOutDTO(Cartao entity) {
		return CartaoOutDTO.builder().numeroCartao(entity.getNumeroCartao())
			.senha(entity.getSenha())
			.build();
	}
	
	public CartaoOutDTO convertCartaoDTOToCartaoOutDTO(CartaoDTO entity) {
		return CartaoOutDTO.builder().numeroCartao(entity.getNumeroCartao())
			.senha(entity.getSenha())
			.build();
	}


}

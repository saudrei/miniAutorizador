package br.com.vr.autorizador.vrminiautorizador.converter;

import br.com.vr.autorizador.vrminiautorizador.domain.Transacao;
import br.com.vr.autorizador.vrminiautorizador.dto.TransacaoDTO;

public class TransacaoConverter {
	
	public TransacaoConverter() {
		super();
	}
	
	public Transacao convertCartaoDTOToCartao(TransacaoDTO entity) {
		return Transacao.builder().numeroCartao(entity.getNumeroCartao())
				.senhaCartao(entity.getSenhaCartao())
				.valor(entity.getValor())
				.build();
	}

}

package br.com.vr.autorizador.vrminiautorizador.helper;

import java.math.BigDecimal;

import br.com.vr.autorizador.vrminiautorizador.domain.Cartao;
import br.com.vr.autorizador.vrminiautorizador.dto.CartaoDTO;
import br.com.vr.autorizador.vrminiautorizador.dto.CartaoOutDTO;
import br.com.vr.autorizador.vrminiautorizador.dto.TransacaoDTO;

public class Helper {
	
	public static CartaoDTO buildGenericCartaoDTO() {
        return CartaoDTO.builder()
                .numeroCartao("6549873025634501")
                .senha("1234").build();
	}
	
	public static CartaoDTO buildGenericCartaoDTONumeroCartaoBadRequest() {
        return CartaoDTO.builder()
                .numeroCartao("6549873025")
                .senha("1234555").build();
	}
	
	public static CartaoDTO buildGenericCartaoDTOSenhaBadRequest() {
        return CartaoDTO.builder()
                .numeroCartao("6549873025634501")
                .build();
	}
	
	public static CartaoOutDTO buildGenericCartaoOutDTO() {
        return CartaoOutDTO.builder()
                .numeroCartao("6549873025634501")
                .senha("1234").build();
	}
	
	public static Cartao buildGenericCartao() {
        return Cartao.builder()
                .numeroCartao("6549873025634501")
                .senha("1234")
                .saldo(new BigDecimal("500")).build();
	}
	
	public static TransacaoDTO buildGenericTransacaoDTO() {
        return TransacaoDTO.builder()
                .numeroCartao("6549873025634501")
                .senhaCartao("1234")
                .valor(new BigDecimal(300))
                .build();
	}

}

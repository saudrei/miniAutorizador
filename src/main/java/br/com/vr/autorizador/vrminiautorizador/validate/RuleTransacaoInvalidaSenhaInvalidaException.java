package br.com.vr.autorizador.vrminiautorizador.validate;

import br.com.vr.autorizador.vrminiautorizador.domain.Cartao;
import br.com.vr.autorizador.vrminiautorizador.dto.TransacaoDTO;
import br.com.vr.autorizador.vrminiautorizador.exceptions.UnprocessableException;

public class RuleTransacaoInvalidaSenhaInvalidaException implements Rule<Cartao, TransacaoDTO> {

	@Override
	public boolean validate(Cartao cartao, TransacaoDTO transacaoDTO) {
		return cartao!= null && !cartao.getSenha().equals(transacaoDTO.getSenhaCartao());
	}

	@Override
	public void error() {
		throw new UnprocessableException(UnprocessableException.SENHA_INVALIDA);			
	}

}

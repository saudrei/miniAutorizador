package br.com.vr.autorizador.vrminiautorizador.validate;

import br.com.vr.autorizador.vrminiautorizador.domain.Cartao;
import br.com.vr.autorizador.vrminiautorizador.dto.TransacaoDTO;
import br.com.vr.autorizador.vrminiautorizador.exceptions.UnprocessableException;

public class RuleTransacaoInvalidaCartaoNaoExisteException implements Rule<Cartao, TransacaoDTO> {

	@Override
	public boolean validate(Cartao cartao, TransacaoDTO transacaoDTO) {
		return cartao == null;
	}

	@Override
	public void error() {
		throw new UnprocessableException(UnprocessableException.CARTAO_INEXISTENTE);			
	}

}

package br.com.vr.autorizador.vrminiautorizador.validate;

import br.com.vr.autorizador.vrminiautorizador.domain.Cartao;
import br.com.vr.autorizador.vrminiautorizador.exceptions.UnprocessableException;

public class RuleCartaoNaoExist implements Rule<Cartao, String> {

	@Override
	public boolean validate(Cartao cartao, String comparavel) {
		return cartao == null;
	}

	@Override
	public void error() {
		throw new UnprocessableException(UnprocessableException.CARTAO_INEXISTENTE);		
	}
	



}

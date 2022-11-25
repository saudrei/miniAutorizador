package br.com.vr.autorizador.vrminiautorizador.validate;

import br.com.vr.autorizador.vrminiautorizador.domain.Cartao;
import br.com.vr.autorizador.vrminiautorizador.exceptions.UnprocessableException;

public class RuleCartaoJaExistException  implements Rule<Cartao, Cartao> {
	
	Cartao cartao;

	@Override
	public boolean validate(Cartao objeto, Cartao comparavel) {
        this.cartao = comparavel;
        return cartao != null;
	}
	
    @Override
    public void error() {
        throw new UnprocessableException(UnprocessableException.CARTAO_EXISTENTE);
    }


}

package br.com.vr.autorizador.vrminiautorizador.validate;

import br.com.vr.autorizador.vrminiautorizador.domain.Cartao;
import br.com.vr.autorizador.vrminiautorizador.dto.TransacaoDTO;

public class ValidateFactory {

    public Validate<Cartao, TransacaoDTO> validateTransacao(){
        Validate<Cartao, TransacaoDTO> validate = new Validate();
        validate.addRule(new RuleTransacaoInvalidaCartaoNaoExisteException());
        validate.addRule(new RuleTransacaoInvalidaSaldoInsuficienteException());
        validate.addRule(new RuleTransacaoInvalidaSenhaInvalidaException());
        return validate;
    }

    public Validate<Cartao, Cartao> validateNovoCartao(){
    	Validate<Cartao, Cartao>  validate = new Validate();
    	validate.addRule(new RuleCartaoJaExistException());
        return validate;
    }


}

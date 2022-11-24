package br.com.vr.autorizador.vrminiautorizador.business;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.vr.autorizador.vrminiautorizador.converter.CartaoConverter;
import br.com.vr.autorizador.vrminiautorizador.domain.Cartao;
import br.com.vr.autorizador.vrminiautorizador.dto.CartaoDTO;
import br.com.vr.autorizador.vrminiautorizador.dto.CartaoOutDTO;
import br.com.vr.autorizador.vrminiautorizador.dto.TransacaoDTO;
import br.com.vr.autorizador.vrminiautorizador.exceptions.RecordNotFoundException;
import br.com.vr.autorizador.vrminiautorizador.exceptions.UnprocessableException;
import br.com.vr.autorizador.vrminiautorizador.service.CartaoService;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class CartaoBusiness {
	
	private final CartaoService cartaoService;
	private final CartaoConverter cartaoConverter;
	


	public CartaoBusiness(CartaoService cartaoService,CartaoConverter cartaoConverter) {
		super();
		this.cartaoService = cartaoService;
		this.cartaoConverter = cartaoConverter;
	}
	
	
	@Transactional
	public CartaoOutDTO createCartao(CartaoDTO cartaoDTO ) {
		validatorCartaoExist(cartaoConverter.convertCartaoDTOToCartao(cartaoDTO));
		return cartaoConverter.convertCartaoToCartaoOutDTO(cartaoService.save(cartaoConverter.convertCartaoDTOToCartao(cartaoDTO)));
	}
	
	public String realizarTransacao(TransacaoDTO transacaoDTO) throws NotFoundException {
		Optional<Cartao> cartao = cartaoService.findById(transacaoDTO.getNumeroCartao());
		if (cartao.isPresent()) {
			validatorCartaoSenhaInvalid(cartao,transacaoDTO);
			validatorInsufficientFunds(cartao,transacaoDTO);
			newBalanceCartao(cartao,transacaoDTO);
		} else {
			validatorCartaoNotExist(transacaoDTO.getNumeroCartao());
		}
		//return "\"OK\"";
		return "OK";
	}


	private void newBalanceCartao(Optional<Cartao> cartao, TransacaoDTO transacaoDTO) {
        BigDecimal novoSaldo = cartao.get().getSaldo();
        novoSaldo = novoSaldo.subtract(transacaoDTO.getValor());
        cartao.get().setSaldo(novoSaldo);
        cartaoService.save(cartao.get());	
        log.info(cartao.get().getNumeroCartao().toString() + "|" +  transacaoDTO.getValor());
	}


	private void validatorCartaoExist(Cartao entity) {
		Optional<Cartao> validatorCartao = cartaoService.findById(entity.getNumeroCartao());
		if(validatorCartao.isPresent()) {
			log.info(entity.getNumeroCartao().toString() + "|" +  UnprocessableException.CARTAO_EXISTENTE);
			throw new UnprocessableException(UnprocessableException.CARTAO_EXISTENTE); 
		}
	}
	
	private void validatorCartaoNotExist(String numeroCartao) {
		log.info(numeroCartao + "|" + UnprocessableException.CARTAO_INEXISTENTE);
		throw new UnprocessableException(UnprocessableException.CARTAO_INEXISTENTE); 
	}
	
	private void validatorCartaoSenhaInvalid(Optional<Cartao> cartao, TransacaoDTO transacaoDTO) {
        if(!cartao.get().getSenha().equals(transacaoDTO.getSenhaCartao())){
        	log.info(cartao.get().getNumeroCartao().toString() + "|" + UnprocessableException.SENHA_INVALIDA);
        	throw new UnprocessableException(UnprocessableException.SENHA_INVALIDA);
        }
	}
	
	private void validatorInsufficientFunds(Optional<Cartao> cartao, TransacaoDTO transacaoDTO) {
        if(transacaoDTO.getValor().compareTo(cartao.get().getSaldo()) == 1){
        	log.info(cartao.get().getNumeroCartao().toString() + "| Saldo:" + cartao.get().getSaldo() + "|Valor: " + transacaoDTO.getValor() + "|" + UnprocessableException.SALDO_INSUFICIENTE);
            throw new UnprocessableException(UnprocessableException.SALDO_INSUFICIENTE);
        }
	}


	public BigDecimal obterSaldo(String numeroCartao) {
        Optional<Cartao> cartao = cartaoService.findById(numeroCartao);
        if(!cartao.isPresent()) {
        	throw new RecordNotFoundException();
        }
       	return cartao.get().getSaldo();

	}

}

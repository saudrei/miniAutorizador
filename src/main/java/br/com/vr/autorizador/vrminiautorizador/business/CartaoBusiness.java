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
import br.com.vr.autorizador.vrminiautorizador.service.CartaoService;
import br.com.vr.autorizador.vrminiautorizador.validate.ValidateFactory;
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
		Optional<Cartao> cartaoOptional = cartaoService.findById(transacaoDTO.getNumeroCartao());
		Cartao cartao = cartaoOptional.orElse(null);
		new ValidateFactory().validateTransacao().validateRule(cartao, transacaoDTO);
		novoSaldoCartao(cartao,transacaoDTO);
		return "OK";
	}


	private void novoSaldoCartao(Cartao cartao, TransacaoDTO transacaoDTO) {
        BigDecimal novoSaldo = cartao.getSaldo();
        novoSaldo = novoSaldo.subtract(transacaoDTO.getValor());
        cartao.setSaldo(novoSaldo);
        cartaoService.save(cartao);	
        log.info(cartao.getNumeroCartao().toString() + "|" +  transacaoDTO.getValor());
	}


	private void validatorCartaoExist(Cartao entity) {
		Cartao validatorCartao = cartaoService.findById(entity.getNumeroCartao()).orElse(null);
		new ValidateFactory().validateNovoCartao().validateRule(entity, validatorCartao);

	}
	
	public Optional<BigDecimal> obterSaldo(String numeroCartao) {
       	return cartaoService.obterSaldo(numeroCartao);
	}

}

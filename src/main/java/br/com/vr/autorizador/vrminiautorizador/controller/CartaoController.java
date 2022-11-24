package br.com.vr.autorizador.vrminiautorizador.controller;

import java.math.BigDecimal;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.vr.autorizador.vrminiautorizador.business.CartaoBusiness;
import br.com.vr.autorizador.vrminiautorizador.converter.CartaoConverter;
import br.com.vr.autorizador.vrminiautorizador.dto.CartaoDTO;
import br.com.vr.autorizador.vrminiautorizador.dto.CartaoOutDTO;
import br.com.vr.autorizador.vrminiautorizador.dto.TransacaoDTO;
import br.com.vr.autorizador.vrminiautorizador.exceptions.UnprocessableException;


@RestController
@Validated
@CrossOrigin
public class CartaoController {
	
	private final CartaoBusiness cartaoBusiness;
	private final CartaoConverter cartaoConverter;

	public CartaoController(CartaoBusiness cartaoBusiness,CartaoConverter cartaoConverter) {
		super();
		this.cartaoBusiness = cartaoBusiness;
		this.cartaoConverter = cartaoConverter;
	}
	
	@PostMapping("/cartoes")
	public ResponseEntity<CartaoOutDTO> createCartao(@RequestBody @Valid CartaoDTO cartao) {
		try {
			return ResponseEntity.status(HttpStatus.CREATED).body(cartaoBusiness.createCartao(cartao));
		} catch (UnprocessableException ex) {
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(cartaoConverter.convertCartaoDTOToCartaoOutDTO(cartao));
		}		
	}
	
	@GetMapping("/cartoes/{numeroCartao}")
	public ResponseEntity<BigDecimal> obterSaldo(@PathVariable(name = "numeroCartao") String numeroCartao) {
		return ResponseEntity.status(HttpStatus.OK).body(cartaoBusiness.obterSaldo(numeroCartao));
	}
	
	@PostMapping(path="/transacoes")
	public ResponseEntity<String> realizarTransacao(@RequestBody @Valid TransacaoDTO transacaoDTO) throws Exception {
		try {
			return new ResponseEntity<String>(cartaoBusiness.realizarTransacao(transacaoDTO), HttpStatus.CREATED);
		} catch (UnprocessableException ex) {
			return new ResponseEntity<String>(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

}

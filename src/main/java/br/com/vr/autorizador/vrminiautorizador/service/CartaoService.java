package br.com.vr.autorizador.vrminiautorizador.service;



import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.vr.autorizador.vrminiautorizador.domain.Cartao;
import br.com.vr.autorizador.vrminiautorizador.repository.CartaoRepository;


@Service
public class CartaoService {
	
	private final CartaoRepository repository;

	public CartaoService(CartaoRepository repository) {
		super();
		this.repository = repository;
	}
	
	public Cartao save(Cartao entity) {
		return repository.save(entity);
	}
	
	
	public Optional<Cartao> findById(String id) {
		return repository.findById(id);
	}
	
	public boolean existsById(String id) {
		return repository.existsById(id);
	}
	
	public void update(Cartao cartaoToUpdate) {
		repository.save(cartaoToUpdate);
	}

	public Optional<BigDecimal> obterSaldo(String numeroCartao) {		
		return repository.obterSaldoDoCartao(numeroCartao);
	}
	

}

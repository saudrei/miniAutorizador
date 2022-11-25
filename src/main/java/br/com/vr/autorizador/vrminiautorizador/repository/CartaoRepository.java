package br.com.vr.autorizador.vrminiautorizador.repository;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.vr.autorizador.vrminiautorizador.domain.Cartao;

@Repository
public interface CartaoRepository  extends CrudRepository<Cartao, String> {
    @Query("SELECT c.saldo FROM Cartao c WHERE c.numeroCartao like :numeroDeCartaoExistente")
    Optional<BigDecimal> obterSaldoDoCartao(String numeroDeCartaoExistente);
}

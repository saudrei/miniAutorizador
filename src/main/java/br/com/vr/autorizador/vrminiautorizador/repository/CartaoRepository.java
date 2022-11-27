package br.com.vr.autorizador.vrminiautorizador.repository;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.vr.autorizador.vrminiautorizador.domain.Cartao;

@Repository
public interface CartaoRepository  extends JpaRepository<Cartao, String> {
    @Query("SELECT c.saldo FROM Cartao c WHERE c.numeroCartao like :numeroDeCartaoExistente")
    Optional<BigDecimal> obterSaldoDoCartao(String numeroDeCartaoExistente);
}

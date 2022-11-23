package br.com.vr.autorizador.vrminiautorizador.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.vr.autorizador.vrminiautorizador.domain.Cartao;

@Repository
public interface CartaoRepository  extends CrudRepository<Cartao, String> {

}

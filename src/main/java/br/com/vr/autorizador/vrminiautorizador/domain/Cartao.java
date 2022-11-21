package br.com.vr.autorizador.vrminiautorizador.domain;


import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cartao {
	
    @Id
    private String numeroCartao;
    @Column(nullable = false)
    private String senha;
    @Builder.Default
    @Column(nullable = false)
    private BigDecimal saldo = new BigDecimal("500");

}

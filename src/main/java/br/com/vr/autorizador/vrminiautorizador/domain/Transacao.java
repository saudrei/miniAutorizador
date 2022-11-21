package br.com.vr.autorizador.vrminiautorizador.domain;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Transacao {
    private String numeroCartao;
    private String senhaCartao;
    private BigDecimal valor;

}

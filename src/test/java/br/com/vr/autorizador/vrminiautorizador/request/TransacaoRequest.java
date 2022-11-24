package br.com.vr.autorizador.vrminiautorizador.request;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransacaoRequest  {
	private String numeroCartao;
    private String senhaCartao;
    private BigDecimal valor;

}

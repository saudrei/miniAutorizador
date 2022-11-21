package br.com.vr.autorizador.vrminiautorizador.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotEmpty;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class TransacaoDTO {
	@NotEmpty(message = "The numeroCartao could not be null or empty.")
    private String numeroCartao;
	@NotEmpty(message = "The senhaCartao could not be null or empty.")
    private String senhaCartao;
	@NotEmpty(message = "The valor could not be null or empty.")
    private BigDecimal valor;

}

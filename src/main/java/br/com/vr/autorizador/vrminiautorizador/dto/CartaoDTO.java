package br.com.vr.autorizador.vrminiautorizador.dto;


import javax.validation.constraints.NotEmpty;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class CartaoDTO {
	@NotEmpty(message = "The numeroCartao could not be null or empty.")
    private String numeroCartao;
	@NotEmpty(message = "The senha could not be null or empty.")
    private String senha;

}

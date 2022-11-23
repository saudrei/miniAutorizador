package br.com.vr.autorizador.vrminiautorizador.dto;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@EqualsAndHashCode
public class CartaoDTO {
	@NotEmpty(message = "{NotEmpty.NumeroCartao}")
	@Pattern(regexp = "[0-9]{13}(?:[0-9]{3})?$", message = "{Valid.NumeroCartao}")
    private String numeroCartao;
	@NotEmpty(message = "{NotEmpty.Senha}")
    private String senha;

}

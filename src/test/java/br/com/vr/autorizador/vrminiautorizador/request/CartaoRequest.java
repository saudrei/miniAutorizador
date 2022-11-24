package br.com.vr.autorizador.vrminiautorizador.request;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartaoRequest implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String numeroCartao;
	private String senha;
	private BigDecimal saldo;

}

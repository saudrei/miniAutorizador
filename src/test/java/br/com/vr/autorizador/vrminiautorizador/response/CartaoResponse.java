package br.com.vr.autorizador.vrminiautorizador.response;

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
public class CartaoResponse  implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String numeroCartao;
	private String senha;
	private BigDecimal saldo;

}

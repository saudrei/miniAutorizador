package br.com.vr.autorizador.vrminiautorizador.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;




@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class UnprocessableException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	public static String SALDO_INSUFICIENTE = "SALDO_INSUFICIENTE";
	public static String SENHA_INVALIDA = "SENHA_INVALIDA";
	public static String CARTAO_INEXISTENTE = "CARTAO_INEXISTENTE";
	public static String CARTAO_EXISTENTE = "CARTAO_EXISTENTE";

	public UnprocessableException(String message) {
		super(message);
	}
}

package br.com.vr.autorizador.vrminiautorizador;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class VrMiniAutorizadorApplication {

	public static void main(String[] args) {
		SpringApplication.run(VrMiniAutorizadorApplication.class, args);
	}

}

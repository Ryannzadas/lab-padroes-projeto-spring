package one.digitalinnovation.gof;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

/**
 * API de clientes com endereço preenchido a partir do CEP, usada para explorar
 * padrões de projeto (GoF) com Spring.
 *
 * @author falvojr (versão original)
 */
@OpenAPIDefinition(info = @Info(
		title = "Padrões de Projeto com Spring",
		version = "2.0",
		description = "Cadastro de clientes com endereço consultado pelo CEP (ViaCEP com fallback na BrasilAPI)."))
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}

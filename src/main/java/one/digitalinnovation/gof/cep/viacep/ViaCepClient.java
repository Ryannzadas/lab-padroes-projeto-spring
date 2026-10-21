package one.digitalinnovation.gof.cep.viacep;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

/**
 * Client HTTP declarativo (HTTP Interface do Spring) para a API do ViaCEP.
 * Substitui o OpenFeign da versão original sem depender do Spring Cloud.
 *
 * @see <a href="https://viacep.com.br">ViaCEP</a>
 */
public interface ViaCepClient {

	@GetExchange("/{cep}/json/")
	ViaCepResponse consultarCep(@PathVariable String cep);

}

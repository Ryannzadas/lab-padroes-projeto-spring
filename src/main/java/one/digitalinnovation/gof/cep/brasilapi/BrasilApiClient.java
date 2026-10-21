package one.digitalinnovation.gof.cep.brasilapi;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

/**
 * Client HTTP declarativo para a API de CEP da BrasilAPI.
 *
 * @see <a href="https://brasilapi.com.br/docs#tag/CEP">BrasilAPI · CEP</a>
 */
public interface BrasilApiClient {

	@GetExchange("/cep/v1/{cep}")
	BrasilApiResponse consultarCep(@PathVariable String cep);

}

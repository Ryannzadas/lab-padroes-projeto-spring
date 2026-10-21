package one.digitalinnovation.gof.cep.brasilapi;

import java.util.Optional;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import one.digitalinnovation.gof.cep.Cep;
import one.digitalinnovation.gof.cep.CepProvider;
import one.digitalinnovation.gof.model.Endereco;

/**
 * Segunda estratégia da corrente: BrasilAPI, usada quando o ViaCEP falha ou não
 * conhece o CEP.
 */
@Component
@Order(2)
public class BrasilApiProvider implements CepProvider {

	private final BrasilApiClient client;

	public BrasilApiProvider(BrasilApiClient client) {
		this.client = client;
	}

	@Override
	public String nome() {
		return "BrasilAPI";
	}

	@Override
	public Optional<Endereco> consultar(Cep cep) {
		try {
			BrasilApiResponse resposta = client.consultarCep(cep.valor());
			return Optional.ofNullable(resposta).map(BrasilApiAdapter::paraEndereco);
		} catch (HttpClientErrorException.NotFound e) {
			// A BrasilAPI responde 404 para CEP inexistente.
			return Optional.empty();
		}
	}

}

package one.digitalinnovation.gof.cep.viacep;

import java.util.Optional;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import one.digitalinnovation.gof.cep.Cep;
import one.digitalinnovation.gof.cep.CepProvider;
import one.digitalinnovation.gof.model.Endereco;

/**
 * Primeira estratégia da corrente: ViaCEP.
 */
@Component
@Order(1)
public class ViaCepProvider implements CepProvider {

	private final ViaCepClient client;

	public ViaCepProvider(ViaCepClient client) {
		this.client = client;
	}

	@Override
	public String nome() {
		return "ViaCEP";
	}

	@Override
	public Optional<Endereco> consultar(Cep cep) {
		ViaCepResponse resposta = client.consultarCep(cep.valor());
		if (resposta == null || resposta.naoEncontrado()) {
			return Optional.empty();
		}
		Endereco endereco = new Endereco();
		endereco.setLogradouro(resposta.logradouro());
		endereco.setComplemento(resposta.complemento());
		endereco.setBairro(resposta.bairro());
		endereco.setLocalidade(resposta.localidade());
		endereco.setUf(resposta.uf());
		endereco.setIbge(resposta.ibge());
		endereco.setDdd(resposta.ddd());
		return Optional.of(endereco);
	}

}

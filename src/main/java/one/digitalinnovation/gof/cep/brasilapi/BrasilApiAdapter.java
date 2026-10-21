package one.digitalinnovation.gof.cep.brasilapi;

import one.digitalinnovation.gof.model.Endereco;

/**
 * <b>Adapter</b>: converte o formato da BrasilAPI ({@code state}, {@code city},
 * {@code street}...) para o {@link Endereco} do domínio. O resto do sistema
 * nunca vê o formato externo.
 */
public final class BrasilApiAdapter {

	private BrasilApiAdapter() {
	}

	public static Endereco paraEndereco(BrasilApiResponse resposta) {
		Endereco endereco = new Endereco();
		endereco.setLogradouro(resposta.street());
		endereco.setBairro(resposta.neighborhood());
		endereco.setLocalidade(resposta.city());
		endereco.setUf(resposta.state());
		return endereco;
	}

}

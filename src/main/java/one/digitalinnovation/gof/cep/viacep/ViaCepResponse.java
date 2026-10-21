package one.digitalinnovation.gof.cep.viacep;

/**
 * Resposta do ViaCEP. Para um CEP inexistente ele responde 200 com {@code "erro": true}
 * (ou {@code "erro": "true"}, nas versões mais novas), por isso o campo aceita os dois.
 */
public record ViaCepResponse(
		String cep,
		String logradouro,
		String complemento,
		String bairro,
		String localidade,
		String uf,
		String ibge,
		String ddd,
		Object erro) {

	public boolean naoEncontrado() {
		return erro != null && Boolean.parseBoolean(erro.toString());
	}

}

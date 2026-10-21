package one.digitalinnovation.gof.cep.brasilapi;

/**
 * Resposta da BrasilAPI: mesmo conteúdo do ViaCEP, mas com nomes em inglês.
 */
public record BrasilApiResponse(
		String cep,
		String state,
		String city,
		String neighborhood,
		String street) {

}

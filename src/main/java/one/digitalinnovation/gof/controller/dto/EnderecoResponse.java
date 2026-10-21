package one.digitalinnovation.gof.controller.dto;

import one.digitalinnovation.gof.cep.Cep;
import one.digitalinnovation.gof.model.Endereco;

public record EnderecoResponse(
		String cep,
		String logradouro,
		String complemento,
		String bairro,
		String localidade,
		String uf,
		String fonte) {

	public static EnderecoResponse de(Endereco endereco) {
		return new EnderecoResponse(
				new Cep(endereco.getCep()).formatado(),
				endereco.getLogradouro(),
				endereco.getComplemento(),
				endereco.getBairro(),
				endereco.getLocalidade(),
				endereco.getUf(),
				endereco.getFonte());
	}

}

package one.digitalinnovation.gof.controller.dto;

import one.digitalinnovation.gof.model.Cliente;

public record ClienteResponse(Long id, String nome, EnderecoResponse endereco) {

	public static ClienteResponse de(Cliente cliente) {
		return new ClienteResponse(cliente.getId(), cliente.getNome(), EnderecoResponse.de(cliente.getEndereco()));
	}

}

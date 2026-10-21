package one.digitalinnovation.gof.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import one.digitalinnovation.gof.controller.dto.EnderecoResponse;
import one.digitalinnovation.gof.service.EnderecoService;

@Tag(name = "CEP")
@RestController
@RequestMapping("ceps")
public class CepRestController {

	private final EnderecoService enderecoService;

	public CepRestController(EnderecoService enderecoService) {
		this.enderecoService = enderecoService;
	}

	@Operation(summary = "Consulta um CEP", description = "O campo \"fonte\" mostra qual serviço respondeu.")
	@GetMapping("/{cep}")
	public EnderecoResponse consultar(@PathVariable String cep) {
		return EnderecoResponse.de(enderecoService.buscarPorCep(cep));
	}

}

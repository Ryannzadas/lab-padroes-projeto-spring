package one.digitalinnovation.gof.controller;

import java.net.URI;
import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import one.digitalinnovation.gof.controller.dto.ClienteRequest;
import one.digitalinnovation.gof.controller.dto.ClienteResponse;
import one.digitalinnovation.gof.model.Cliente;
import one.digitalinnovation.gof.service.ClienteService;

/**
 * Esse {@link RestController} representa nossa <b>Facade</b>, pois abstrai toda
 * a complexidade de integrações (banco de dados H2, ViaCEP e BrasilAPI) em uma
 * interface simples e coesa (API REST).
 *
 * @author falvojr (versão original)
 */
@Tag(name = "Clientes")
@RestController
@RequestMapping("clientes")
public class ClienteRestController {

	private final ClienteService clienteService;

	public ClienteRestController(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	@Operation(summary = "Lista todos os clientes")
	@GetMapping
	public List<ClienteResponse> buscarTodos() {
		Iterable<Cliente> clientes = clienteService.buscarTodos();
		return StreamSupport.stream(clientes.spliterator(), false).map(ClienteResponse::de).toList();
	}

	@Operation(summary = "Busca um cliente pelo id")
	@GetMapping("/{id}")
	public ClienteResponse buscarPorId(@PathVariable Long id) {
		return ClienteResponse.de(clienteService.buscarPorId(id));
	}

	@Operation(summary = "Cadastra um cliente; o endereço é preenchido pelo CEP")
	@PostMapping
	public ResponseEntity<ClienteResponse> inserir(@Valid @RequestBody ClienteRequest request) {
		Cliente cliente = clienteService.inserir(request.nome(), request.cep());
		return ResponseEntity.created(URI.create("/clientes/" + cliente.getId())).body(ClienteResponse.de(cliente));
	}

	@Operation(summary = "Atualiza o nome e o CEP de um cliente")
	@PutMapping("/{id}")
	public ClienteResponse atualizar(@PathVariable Long id, @Valid @RequestBody ClienteRequest request) {
		return ClienteResponse.de(clienteService.atualizar(id, request.nome(), request.cep()));
	}

	@Operation(summary = "Remove um cliente")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletar(@PathVariable Long id) {
		clienteService.deletar(id);
		return ResponseEntity.noContent().build();
	}
}

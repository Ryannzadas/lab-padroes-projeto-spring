package one.digitalinnovation.gof.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import one.digitalinnovation.gof.exception.ClienteNaoEncontradoException;
import one.digitalinnovation.gof.model.Cliente;
import one.digitalinnovation.gof.model.ClienteRepository;
import one.digitalinnovation.gof.service.ClienteService;
import one.digitalinnovation.gof.service.EnderecoService;

/**
 * Implementação da <b>Strategy</b> {@link ClienteService}. Como é um
 * {@link Service}, o Spring cria uma única instância (<b>Singleton</b>) e a
 * injeta onde for preciso.
 * <p>
 * É também a <b>Facade</b> do domínio: quem cadastra um cliente só informa nome e
 * CEP, sem saber de banco, cache de endereços ou serviços externos.
 *
 * @author falvojr (versão original)
 */
@Service
public class ClienteServiceImpl implements ClienteService {

	// Singleton: dependências injetadas pelo construtor, uma instância de cada.
	private final ClienteRepository clienteRepository;
	private final EnderecoService enderecoService;

	public ClienteServiceImpl(ClienteRepository clienteRepository, EnderecoService enderecoService) {
		this.clienteRepository = clienteRepository;
		this.enderecoService = enderecoService;
	}

	@Override
	@Transactional(readOnly = true)
	public Iterable<Cliente> buscarTodos() {
		return clienteRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Cliente buscarPorId(Long id) {
		return clienteRepository.findById(id).orElseThrow(() -> new ClienteNaoEncontradoException(id));
	}

	@Override
	@Transactional
	public Cliente inserir(String nome, String cep) {
		Cliente cliente = new Cliente();
		return salvarClienteComCep(cliente, nome, cep);
	}

	@Override
	@Transactional
	public Cliente atualizar(Long id, String nome, String cep) {
		// Altera o cliente que já existe. Na versão original, o cliente vindo da
		// requisição era salvo sem o id e acabava criando um registro novo.
		Cliente cliente = buscarPorId(id);
		return salvarClienteComCep(cliente, nome, cep);
	}

	@Override
	@Transactional
	public void deletar(Long id) {
		clienteRepository.delete(buscarPorId(id));
	}

	private Cliente salvarClienteComCep(Cliente cliente, String nome, String cep) {
		cliente.setNome(nome.strip());
		cliente.setEndereco(enderecoService.buscarPorCep(cep));
		return clienteRepository.save(cliente);
	}

}

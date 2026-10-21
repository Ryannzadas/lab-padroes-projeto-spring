package one.digitalinnovation.gof.service;

import one.digitalinnovation.gof.model.Cliente;

/**
 * Interface que define o padrão <b>Strategy</b> no domínio de cliente. Com
 * isso, se necessário, podemos ter múltiplas implementações dessa mesma
 * interface.
 *
 * @author falvojr (versão original)
 */
public interface ClienteService {

	Iterable<Cliente> buscarTodos();

	/** @throws one.digitalinnovation.gof.exception.ClienteNaoEncontradoException */
	Cliente buscarPorId(Long id);

	Cliente inserir(String nome, String cep);

	/** @throws one.digitalinnovation.gof.exception.ClienteNaoEncontradoException */
	Cliente atualizar(Long id, String nome, String cep);

	/** @throws one.digitalinnovation.gof.exception.ClienteNaoEncontradoException */
	void deletar(Long id);

}

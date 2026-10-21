package one.digitalinnovation.gof.cep;

import java.util.Optional;

import one.digitalinnovation.gof.model.Endereco;

/**
 * <b>Strategy</b>: cada serviço de CEP é uma estratégia intercambiável de consulta.
 * Para adicionar um novo serviço, basta criar outra implementação anotada com
 * {@code @Component} e {@code @Order}; a {@link ConsultaCepChain} passa a usá-la sozinha.
 */
public interface CepProvider {

	/** Nome exibido na resposta e nos logs. */
	String nome();

	/**
	 * @return o endereço, ou vazio se o serviço respondeu que o CEP não existe.
	 * @throws RuntimeException se o serviço falhou (fora do ar, timeout, resposta inválida).
	 */
	Optional<Endereco> consultar(Cep cep);

}

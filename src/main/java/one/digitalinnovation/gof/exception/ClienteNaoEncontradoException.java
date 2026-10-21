package one.digitalinnovation.gof.exception;

public class ClienteNaoEncontradoException extends RuntimeException {

	public ClienteNaoEncontradoException(Long id) {
		super("Cliente %s não encontrado.".formatted(id));
	}

}

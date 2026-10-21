package one.digitalinnovation.gof.exception;

import one.digitalinnovation.gof.cep.Cep;

public class CepNaoEncontradoException extends RuntimeException {

	public CepNaoEncontradoException(Cep cep) {
		super("O CEP %s não existe.".formatted(cep));
	}

}

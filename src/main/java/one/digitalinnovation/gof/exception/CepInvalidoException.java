package one.digitalinnovation.gof.exception;

public class CepInvalidoException extends RuntimeException {

	public CepInvalidoException(String cep) {
		super("CEP inválido: '%s'. Informe 8 dígitos, com ou sem hífen (ex.: 55610-000).".formatted(cep));
	}

}

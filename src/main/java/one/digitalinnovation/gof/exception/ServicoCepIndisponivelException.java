package one.digitalinnovation.gof.exception;

import one.digitalinnovation.gof.cep.Cep;

public class ServicoCepIndisponivelException extends RuntimeException {

	public ServicoCepIndisponivelException(Cep cep) {
		super("Nenhum serviço de CEP respondeu ao consultar %s. Tente novamente em instantes.".formatted(cep));
	}

}

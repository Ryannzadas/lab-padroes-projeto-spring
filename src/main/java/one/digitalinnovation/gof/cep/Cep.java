package one.digitalinnovation.gof.cep;

import one.digitalinnovation.gof.exception.CepInvalidoException;

/**
 * CEP já validado e normalizado (8 dígitos). Aceita "55610-000", "55.610-000"
 * ou "55610000"; qualquer outra coisa é recusada antes de chegar nas APIs externas.
 */
public record Cep(String valor) {

	public Cep {
		if (valor == null || !valor.matches("\\d{8}")) {
			throw new CepInvalidoException(valor);
		}
	}

	public static Cep de(String texto) {
		if (texto == null) {
			throw new CepInvalidoException(null);
		}
		String digitos = texto.replaceAll("[.\\-\\s]", "");
		if (!digitos.matches("\\d{8}")) {
			throw new CepInvalidoException(texto);
		}
		return new Cep(digitos);
	}

	/** No formato 00000-000. */
	public String formatado() {
		return valor.substring(0, 5) + "-" + valor.substring(5);
	}

	@Override
	public String toString() {
		return formatado();
	}

}

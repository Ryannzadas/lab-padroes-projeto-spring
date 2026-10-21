package one.digitalinnovation.gof.cep;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import one.digitalinnovation.gof.exception.CepInvalidoException;

class CepTest {

	@ParameterizedTest
	@ValueSource(strings = { "55610000", "55610-000", "55.610-000", " 55610-000 " })
	void aceitaFormatosComunsENormaliza(String texto) {
		Cep cep = Cep.de(texto);

		assertThat(cep.valor()).isEqualTo("55610000");
		assertThat(cep.formatado()).isEqualTo("55610-000");
	}

	@ParameterizedTest
	@NullSource
	@ValueSource(strings = { "", "5561000", "556100000", "5561O000", "abc" })
	void recusaCepInvalido(String texto) {
		assertThatThrownBy(() -> Cep.de(texto)).isInstanceOf(CepInvalidoException.class);
	}

}

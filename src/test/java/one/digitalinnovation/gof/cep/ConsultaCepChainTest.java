package one.digitalinnovation.gof.cep;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

import one.digitalinnovation.gof.cep.brasilapi.BrasilApiAdapter;
import one.digitalinnovation.gof.cep.brasilapi.BrasilApiResponse;
import one.digitalinnovation.gof.exception.CepNaoEncontradoException;
import one.digitalinnovation.gof.exception.ServicoCepIndisponivelException;
import one.digitalinnovation.gof.model.Endereco;

class ConsultaCepChainTest {

	private static final Cep CEP = new Cep("55610000");

	@Test
	void usaOPrimeiroServicoQueResponder() {
		var primeiro = new ProviderFake("A", () -> Optional.of(endereco("Rua A")));
		var segundo = new ProviderFake("B", () -> Optional.of(endereco("Rua B")));

		Endereco endereco = new ConsultaCepChain(List.of(primeiro, segundo)).consultar(CEP);

		assertThat(endereco.getLogradouro()).isEqualTo("Rua A");
		assertThat(endereco.getFonte()).isEqualTo("A");
		assertThat(endereco.getCep()).isEqualTo("55610000");
		assertThat(segundo.chamadas).isZero();
	}

	@Test
	void passaParaOProximoQuandoUmServicoFalha() {
		var foraDoAr = new ProviderFake("A", () -> {
			throw new IllegalStateException("timeout");
		});
		var reserva = new ProviderFake("B", () -> Optional.of(endereco("Rua B")));

		Endereco endereco = new ConsultaCepChain(List.of(foraDoAr, reserva)).consultar(CEP);

		assertThat(endereco.getFonte()).isEqualTo("B");
	}

	@Test
	void passaParaOProximoQuandoUmServicoNaoConheceOCep() {
		var semOCep = new ProviderFake("A", Optional::empty);
		var reserva = new ProviderFake("B", () -> Optional.of(endereco("Rua B")));

		assertThat(new ConsultaCepChain(List.of(semOCep, reserva)).consultar(CEP).getFonte()).isEqualTo("B");
	}

	@Test
	void cepNaoEncontradoQuandoAlgumServicoRespondeuENenhumConhece() {
		var semOCep = new ProviderFake("A", Optional::empty);
		var foraDoAr = new ProviderFake("B", () -> {
			throw new IllegalStateException("timeout");
		});

		assertThatThrownBy(() -> new ConsultaCepChain(List.of(semOCep, foraDoAr)).consultar(CEP))
				.isInstanceOf(CepNaoEncontradoException.class);
	}

	@Test
	void servicoIndisponivelQuandoTodosFalham() {
		var foraDoAr = new ProviderFake("A", () -> {
			throw new IllegalStateException("timeout");
		});

		assertThatThrownBy(() -> new ConsultaCepChain(List.of(foraDoAr)).consultar(CEP))
				.isInstanceOf(ServicoCepIndisponivelException.class);
	}

	@Test
	void adapterConverteOFormatoDaBrasilApi() {
		var resposta = new BrasilApiResponse("55610000", "PE", "Vitória de Santo Antão", "Centro", "Rua Imperial");

		Endereco endereco = BrasilApiAdapter.paraEndereco(resposta);

		assertThat(endereco.getUf()).isEqualTo("PE");
		assertThat(endereco.getLocalidade()).isEqualTo("Vitória de Santo Antão");
		assertThat(endereco.getBairro()).isEqualTo("Centro");
		assertThat(endereco.getLogradouro()).isEqualTo("Rua Imperial");
	}

	private static Endereco endereco(String logradouro) {
		Endereco endereco = new Endereco();
		endereco.setLogradouro(logradouro);
		return endereco;
	}

	private static final class ProviderFake implements CepProvider {

		private final String nome;
		private final Supplier<Optional<Endereco>> resposta;
		private int chamadas;

		ProviderFake(String nome, Supplier<Optional<Endereco>> resposta) {
			this.nome = nome;
			this.resposta = resposta;
		}

		@Override
		public String nome() {
			return nome;
		}

		@Override
		public Optional<Endereco> consultar(Cep cep) {
			chamadas++;
			return resposta.get();
		}

	}

}

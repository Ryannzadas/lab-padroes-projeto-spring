package one.digitalinnovation.gof.cep;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import one.digitalinnovation.gof.exception.CepNaoEncontradoException;
import one.digitalinnovation.gof.exception.ServicoCepIndisponivelException;
import one.digitalinnovation.gof.model.Endereco;

/**
 * <b>Chain of Responsibility</b>: passa a consulta pelos {@link CepProvider} em
 * ordem (ViaCEP, depois BrasilAPI) até um deles responder. Se um serviço cai ou
 * não conhece o CEP, o próximo da corrente tenta. Quem chama não sabe quantos
 * serviços existem nem em que ordem são tentados.
 */
@Component
public class ConsultaCepChain {

	private static final Logger log = LoggerFactory.getLogger(ConsultaCepChain.class);

	private final List<CepProvider> providers;

	/** O Spring injeta os providers já ordenados pelo {@code @Order}. */
	public ConsultaCepChain(List<CepProvider> providers) {
		this.providers = List.copyOf(providers);
	}

	/**
	 * @throws CepNaoEncontradoException se pelo menos um serviço respondeu e nenhum conhece o CEP.
	 * @throws ServicoCepIndisponivelException se todos os serviços falharam.
	 */
	public Endereco consultar(Cep cep) {
		boolean algumRespondeu = false;
		for (CepProvider provider : providers) {
			try {
				Optional<Endereco> endereco = provider.consultar(cep);
				if (endereco.isPresent()) {
					Endereco encontrado = endereco.get();
					encontrado.setCep(cep.valor());
					encontrado.setFonte(provider.nome());
					return encontrado;
				}
				algumRespondeu = true;
				log.info("{} não encontrou o CEP {}, tentando o próximo serviço", provider.nome(), cep);
			} catch (RuntimeException e) {
				log.warn("{} falhou ao consultar o CEP {}: {}", provider.nome(), cep, e.getMessage());
			}
		}
		if (algumRespondeu) {
			throw new CepNaoEncontradoException(cep);
		}
		throw new ServicoCepIndisponivelException(cep);
	}

}

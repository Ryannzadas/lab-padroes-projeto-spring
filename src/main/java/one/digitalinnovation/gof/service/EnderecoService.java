package one.digitalinnovation.gof.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import one.digitalinnovation.gof.cep.Cep;
import one.digitalinnovation.gof.cep.ConsultaCepChain;
import one.digitalinnovation.gof.model.Endereco;
import one.digitalinnovation.gof.model.EnderecoRepository;

/**
 * Busca o endereço de um CEP: primeiro no banco (cache) e, só se ainda não
 * existir, na corrente de serviços externos, salvando o resultado.
 */
@Service
public class EnderecoService {

	private final EnderecoRepository enderecoRepository;
	private final ConsultaCepChain consultaCepChain;

	public EnderecoService(EnderecoRepository enderecoRepository, ConsultaCepChain consultaCepChain) {
		this.enderecoRepository = enderecoRepository;
		this.consultaCepChain = consultaCepChain;
	}

	@Transactional
	public Endereco buscarPorCep(String cep) {
		Cep cepValido = Cep.de(cep);
		return enderecoRepository.findById(cepValido.valor())
				.orElseGet(() -> enderecoRepository.save(consultaCepChain.consultar(cepValido)));
	}

}

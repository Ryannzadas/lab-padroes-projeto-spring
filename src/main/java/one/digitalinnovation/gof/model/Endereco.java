package one.digitalinnovation.gof.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

/**
 * Endereço de um CEP. Fica salvo no banco e funciona como cache: o mesmo CEP
 * não é consultado duas vezes nas APIs externas.
 *
 * @see <a href="https://viacep.com.br">ViaCEP</a>
 * @see <a href="https://brasilapi.com.br">BrasilAPI</a>
 */
@Entity
public class Endereco {

	/** Só os 8 dígitos, sem hífen. */
	@Id
	private String cep;
	private String logradouro;
	private String complemento;
	private String bairro;
	private String localidade;
	private String uf;
	private String ibge;
	private String ddd;
	/** Qual serviço respondeu a consulta (ViaCEP ou BrasilAPI). */
	private String fonte;

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getLocalidade() {
		return localidade;
	}

	public void setLocalidade(String localidade) {
		this.localidade = localidade;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getIbge() {
		return ibge;
	}

	public void setIbge(String ibge) {
		this.ibge = ibge;
	}

	public String getDdd() {
		return ddd;
	}

	public void setDdd(String ddd) {
		this.ddd = ddd;
	}

	public String getFonte() {
		return fonte;
	}

	public void setFonte(String fonte) {
		this.fonte = fonte;
	}

}

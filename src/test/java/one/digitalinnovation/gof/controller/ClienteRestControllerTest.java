package one.digitalinnovation.gof.controller;

import static org.hamcrest.Matchers.matchesPattern;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.ResourceAccessException;

import com.jayway.jsonpath.JsonPath;

import one.digitalinnovation.gof.cep.brasilapi.BrasilApiClient;
import one.digitalinnovation.gof.cep.brasilapi.BrasilApiResponse;
import one.digitalinnovation.gof.cep.viacep.ViaCepClient;
import one.digitalinnovation.gof.cep.viacep.ViaCepResponse;

/**
 * Testa a API de ponta a ponta (controller, serviços, corrente de CEP e banco H2).
 * Só os clients HTTP são simulados, para os testes não dependerem da internet.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ClienteRestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private ViaCepClient viaCepClient;

	@MockitoBean
	private BrasilApiClient brasilApiClient;

	@Test
	void cadastraClienteComEnderecoDoViaCep() throws Exception {
		viaCepResponde("55610000", "Rua Imperial");

		mockMvc.perform(post("/clientes").contentType(MediaType.APPLICATION_JSON)
						.content(json("João Ryan", "55610-000")))
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", matchesPattern("/clientes/\\d+")))
				.andExpect(jsonPath("$.nome").value("João Ryan"))
				.andExpect(jsonPath("$.endereco.cep").value("55610-000"))
				.andExpect(jsonPath("$.endereco.logradouro").value("Rua Imperial"))
				.andExpect(jsonPath("$.endereco.fonte").value("ViaCEP"));
	}

	@Test
	void usaABrasilApiQuandoOViaCepEstaForaDoAr() throws Exception {
		when(viaCepClient.consultarCep(anyString())).thenThrow(new ResourceAccessException("timeout"));
		when(brasilApiClient.consultarCep("55610001"))
				.thenReturn(new BrasilApiResponse("55610001", "PE", "Vitória de Santo Antão", "Centro", "Rua Nova"));

		mockMvc.perform(get("/ceps/55610001"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.logradouro").value("Rua Nova"))
				.andExpect(jsonPath("$.uf").value("PE"))
				.andExpect(jsonPath("$.fonte").value("BrasilAPI"));
	}

	@Test
	void naoConsultaDeNovoUmCepJaSalvo() throws Exception {
		viaCepResponde("55610002", "Rua Velha");

		cadastrar("Ana", "55610-002");
		cadastrar("Bia", "55610002");

		verify(viaCepClient, times(1)).consultarCep("55610002");
		verify(brasilApiClient, never()).consultarCep(anyString());
	}

	@Test
	void atualizaOMesmoClienteEmVezDeCriarOutro() throws Exception {
		viaCepResponde("55610003", "Rua Um");
		viaCepResponde("55610004", "Rua Dois");
		String id = cadastrar("Carlos", "55610003");

		mockMvc.perform(put("/clientes/" + id).contentType(MediaType.APPLICATION_JSON)
						.content(json("Carlos Silva", "55610004")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(Integer.parseInt(id)))
				.andExpect(jsonPath("$.nome").value("Carlos Silva"))
				.andExpect(jsonPath("$.endereco.logradouro").value("Rua Dois"));

		mockMvc.perform(get("/clientes"))
				.andExpect(jsonPath("$.length()").value(1));
	}

	@Test
	void removeCliente() throws Exception {
		viaCepResponde("55610005", "Rua Cinco");
		String id = cadastrar("Dani", "55610005");

		mockMvc.perform(delete("/clientes/" + id)).andExpect(status().isNoContent());
		mockMvc.perform(get("/clientes/" + id)).andExpect(status().isNotFound());
	}

	@Test
	void clienteInexistenteResponde404ComProblemDetail() throws Exception {
		mockMvc.perform(get("/clientes/999"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.title").value("Cliente não encontrado"))
				.andExpect(jsonPath("$.detail").value("Cliente 999 não encontrado."));
		mockMvc.perform(put("/clientes/999").contentType(MediaType.APPLICATION_JSON).content(json("X", "55610000")))
				.andExpect(status().isNotFound());
		mockMvc.perform(delete("/clientes/999")).andExpect(status().isNotFound());
	}

	@Test
	void validaOsDadosAntesDeConsultarOCep() throws Exception {
		mockMvc.perform(post("/clientes").contentType(MediaType.APPLICATION_JSON).content(json("", "")))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.detail").value("cep: informe o CEP; nome: informe o nome"));

		mockMvc.perform(post("/clientes").contentType(MediaType.APPLICATION_JSON).content(json("Eva", "123")))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.title").value("CEP inválido"));

		mockMvc.perform(post("/clientes").contentType(MediaType.APPLICATION_JSON).content("{nome:"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.title").value("JSON inválido"));

		verify(viaCepClient, never()).consultarCep(anyString());
	}

	@Test
	void cepInexistenteResponde422() throws Exception {
		when(viaCepClient.consultarCep("99999999"))
				.thenReturn(new ViaCepResponse(null, null, null, null, null, null, null, null, "true"));
		when(brasilApiClient.consultarCep("99999999")).thenReturn(null);

		mockMvc.perform(get("/ceps/99999-999"))
				.andExpect(status().isUnprocessableContent())
				.andExpect(jsonPath("$.detail").value("O CEP 99999-999 não existe."));
	}

	@Test
	void todosOsServicosForaDoArResponde503() throws Exception {
		when(viaCepClient.consultarCep(anyString())).thenThrow(new ResourceAccessException("timeout"));
		when(brasilApiClient.consultarCep(anyString())).thenThrow(new ResourceAccessException("timeout"));

		mockMvc.perform(get("/ceps/55610006")).andExpect(status().isServiceUnavailable());
	}

	private void viaCepResponde(String cep, String logradouro) {
		when(viaCepClient.consultarCep(cep)).thenReturn(new ViaCepResponse(
				cep, logradouro, "", "Centro", "Vitória de Santo Antão", "PE", "2616407", "81", null));
	}

	private String cadastrar(String nome, String cep) throws Exception {
		MvcResult resultado = mockMvc.perform(post("/clientes").contentType(MediaType.APPLICATION_JSON)
						.content(json(nome, cep)))
				.andExpect(status().isCreated())
				.andReturn();
		return JsonPath.read(resultado.getResponse().getContentAsString(), "$.id").toString();
	}

	private static String json(String nome, String cep) {
		return """
				{"nome": "%s", "cep": "%s"}
				""".formatted(nome, cep);
	}

}

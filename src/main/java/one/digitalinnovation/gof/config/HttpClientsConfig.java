package one.digitalinnovation.gof.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import one.digitalinnovation.gof.cep.brasilapi.BrasilApiClient;
import one.digitalinnovation.gof.cep.viacep.ViaCepClient;

/**
 * Cria os clients HTTP de CEP a partir das interfaces anotadas com {@code @GetExchange}.
 * Timeouts curtos evitam que um serviço lento segure a requisição: a corrente
 * desiste dele e tenta o próximo.
 */
@Configuration
public class HttpClientsConfig {

	@Bean
	ViaCepClient viaCepClient(RestClient.Builder builder, @Value("${cep.viacep.url}") String url) {
		return criar(builder, url, ViaCepClient.class);
	}

	@Bean
	BrasilApiClient brasilApiClient(RestClient.Builder builder, @Value("${cep.brasilapi.url}") String url) {
		return criar(builder, url, BrasilApiClient.class);
	}

	private static <T> T criar(RestClient.Builder builder, String baseUrl, Class<T> tipo) {
		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		requestFactory.setConnectTimeout(Duration.ofSeconds(2));
		requestFactory.setReadTimeout(Duration.ofSeconds(4));
		RestClient restClient = builder.clone()
				.baseUrl(baseUrl)
				.requestFactory(requestFactory)
				.build();
		return HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient))
				.build()
				.createClient(tipo);
	}

}

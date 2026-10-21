package one.digitalinnovation.gof.controller;

import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import one.digitalinnovation.gof.exception.CepInvalidoException;
import one.digitalinnovation.gof.exception.CepNaoEncontradoException;
import one.digitalinnovation.gof.exception.ClienteNaoEncontradoException;
import one.digitalinnovation.gof.exception.ServicoCepIndisponivelException;

/**
 * Traduz as exceções em respostas HTTP no formato Problem Details (RFC 9457),
 * em vez do erro 500 genérico da versão original.
 */
@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(ClienteNaoEncontradoException.class)
	ProblemDetail clienteNaoEncontrado(ClienteNaoEncontradoException e) {
		return problema(HttpStatus.NOT_FOUND, "Cliente não encontrado", e.getMessage());
	}

	@ExceptionHandler(CepInvalidoException.class)
	ProblemDetail cepInvalido(CepInvalidoException e) {
		return problema(HttpStatus.BAD_REQUEST, "CEP inválido", e.getMessage());
	}

	@ExceptionHandler(CepNaoEncontradoException.class)
	ProblemDetail cepNaoEncontrado(CepNaoEncontradoException e) {
		return problema(HttpStatus.UNPROCESSABLE_CONTENT, "CEP não encontrado", e.getMessage());
	}

	@ExceptionHandler(ServicoCepIndisponivelException.class)
	ProblemDetail servicoIndisponivel(ServicoCepIndisponivelException e) {
		return problema(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de CEP indisponível", e.getMessage());
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		String detalhe = ex.getBindingResult().getFieldErrors().stream()
				.map(erro -> erro.getField() + ": " + erro.getDefaultMessage())
				.sorted()
				.collect(Collectors.joining("; "));
		return ResponseEntity.badRequest().body(problema(HttpStatus.BAD_REQUEST, "Dados inválidos", detalhe));
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		return ResponseEntity.badRequest().body(problema(HttpStatus.BAD_REQUEST, "JSON inválido",
				"Não foi possível ler o corpo da requisição. Envie um JSON válido em UTF-8."));
	}

	private static ProblemDetail problema(HttpStatus status, String titulo, String detalhe) {
		ProblemDetail problema = ProblemDetail.forStatusAndDetail(status, detalhe);
		problema.setTitle(titulo);
		return problema;
	}

}

package one.digitalinnovation.gof.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClienteRequest(
		@Schema(example = "João Ryan")
		@NotBlank(message = "informe o nome")
		@Size(max = 100, message = "use no máximo 100 caracteres")
		String nome,

		@Schema(example = "55610-000")
		@NotBlank(message = "informe o CEP")
		String cep) {

}

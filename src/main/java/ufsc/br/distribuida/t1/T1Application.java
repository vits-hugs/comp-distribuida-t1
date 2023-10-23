package ufsc.br.distribuida.t1;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@SpringBootApplication
@RestController
public class T1Application {

	public static void main(String[] args) {
		SpringApplication.run(T1Application.class, args);
	}


	@Operation(
			summary = "teste",
			description = "testa")
	@Tag(name = "teste")
	@ApiResponse(responseCode = "200", description = "")
	@GetMapping(value = "/Get", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map> doget( @Parameter(description = "int") @RequestParam(defaultValue = "0",value = "num") int num) {
		Teste teste = new Teste("test",num);
		ObjectMapper objectMapper = new ObjectMapper();

		return new ResponseEntity<Map>(objectMapper.convertValue(teste, Map.class),HttpStatus.OK);
	}


}

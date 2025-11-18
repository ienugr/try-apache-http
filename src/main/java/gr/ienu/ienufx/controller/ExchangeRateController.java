package gr.ienu.ienufx.controller;

import gr.ienu.ienufx.dto.ExchangeRateResponse;
import gr.ienu.ienufx.service.ExchangeRateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/exchange-rate")
public class ExchangeRateController {

	private final ExchangeRateService exchangeRateService;

	public ExchangeRateController(ExchangeRateService exchangeRateService) {
		this.exchangeRateService = exchangeRateService;
	}

	@GetMapping
	public Mono<ResponseEntity<ExchangeRateResponse>> getExchangeRate(@RequestParam String from,
			@RequestParam String to) {

		// Validate input parameters
		if (from == null || from.trim().isEmpty() || to == null || to.trim().isEmpty()) {
			return Mono.just(ResponseEntity.badRequest().build());
		}

		return this.exchangeRateService.getExchangeRate(from.toUpperCase(), to.toUpperCase())
				.map(ResponseEntity::ok)
				.onErrorResume(error -> Mono.just(ResponseEntity.badRequest().build()));
	}

	@GetMapping("/health")
	public ResponseEntity<String> health() {
		return ResponseEntity.ok("Foreign Exchange Service is running");
	}

}

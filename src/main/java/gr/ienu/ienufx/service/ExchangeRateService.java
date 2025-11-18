package gr.ienu.ienufx.service;

import gr.ienu.ienufx.config.ExchangeRateProperties;
import gr.ienu.ienufx.dto.ExchangeRateResponse;
import gr.ienu.ienufx.dto.FrankfurterResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
public class ExchangeRateService {

	private static final String DEFAULT_BASE_URL = "https://api.frankfurter.app";

	private final WebClient webClient;

	public ExchangeRateService(WebClient.Builder webClientBuilder, ExchangeRateProperties properties) {
		String configuredBaseUrl = resolveBaseUrl(properties);
		this.webClient = webClientBuilder.baseUrl(configuredBaseUrl).build();
	}

	private String resolveBaseUrl(ExchangeRateProperties properties) {
		if (properties == null || !StringUtils.hasText(properties.getBaseUrl())) {
			return DEFAULT_BASE_URL;
		}
		return properties.getBaseUrl().trim();
	}

	public Mono<ExchangeRateResponse> getExchangeRate(String fromCurrency, String toCurrency) {
		if (fromCurrency == null || toCurrency == null) {
			return Mono.error(new IllegalArgumentException("Currency parameters cannot be null"));
		}

		return this.webClient.get()
				.uri("/latest?from={from}&to={to}", fromCurrency, toCurrency)
				.retrieve()
				.bodyToMono(FrankfurterResponse.class)
				.map(response -> {
					if (response.rates() == null) {
						throw new RuntimeException("No exchange rate data available");
					}
					BigDecimal rate = response.rates().get(toCurrency);
					if (rate == null) {
						throw new RuntimeException("Exchange rate not found for currency: "
								+ toCurrency);
					}
					return new ExchangeRateResponse(fromCurrency, toCurrency, rate,
							response.date());
				})
				.onErrorMap(throwable -> new RuntimeException("Failed to fetch exchange rate: "
						+ throwable.getMessage()));
	}

}

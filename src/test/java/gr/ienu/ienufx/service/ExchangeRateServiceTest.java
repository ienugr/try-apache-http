package gr.ienu.ienufx.service;

import gr.ienu.ienufx.config.ExchangeRateProperties;
import gr.ienu.ienufx.dto.ExchangeRateResponse;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExchangeRateServiceTest {

	private MockWebServer mockWebServer;
	private ExchangeRateService exchangeRateService;

	@BeforeEach
	void setUp() throws IOException {
		this.mockWebServer = new MockWebServer();
		this.mockWebServer.start();

		ExchangeRateProperties properties = new ExchangeRateProperties();
		properties.setBaseUrl(this.mockWebServer.url("/").toString());

		this.exchangeRateService = new ExchangeRateService(WebClient.builder(), properties);
	}

	@AfterEach
	void tearDown() throws IOException {
		this.mockWebServer.shutdown();
	}

	@Test
	void fetchesExchangeRateFromConfiguredBaseUrl() throws InterruptedException {
		this.mockWebServer.enqueue(new MockResponse()
				.setHeader("Content-Type", "application/json")
				.setBody("""
						{
						  "amount": 1,
						  "base": "USD",
						  "date": "2025-01-18",
						  "rates": {
						    "EUR": 0.86
						  }
						}
						"""));

		ExchangeRateResponse response = this.exchangeRateService.getExchangeRate("USD", "EUR").block();

		assertNotNull(response);
		assertEquals("USD", response.fromCurrency());
		assertEquals("EUR", response.toCurrency());
		assertEquals(new BigDecimal("0.86"), response.rate());

		RecordedRequest recordedRequest = this.mockWebServer.takeRequest();
		assertEquals("/latest?from=USD&to=EUR", recordedRequest.getPath());
	}

	@Test
	void wrapsRemoteErrorsWithMeaningfulMessage() {
		this.mockWebServer.enqueue(new MockResponse().setResponseCode(500));

		StepVerifier.create(this.exchangeRateService.getExchangeRate("USD", "EUR"))
				.expectErrorSatisfies(error -> {
					assertNotNull(error.getMessage());
					assertTrue(error.getMessage().contains("Failed to fetch exchange rate"));
				})
				.verify();
	}

}

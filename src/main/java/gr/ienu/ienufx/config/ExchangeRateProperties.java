package gr.ienu.ienufx.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "exchange-rate")
public class ExchangeRateProperties {

	private static final String DEFAULT_BASE_URL = "https://api.frankfurter.app";

	private String baseUrl = DEFAULT_BASE_URL;

	public String getBaseUrl() {
		return this.baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = (baseUrl == null || baseUrl.trim().isEmpty())
				? DEFAULT_BASE_URL
				: baseUrl;
	}

}

package gr.ienu.ienufx.dto;

import java.math.BigDecimal;

public record ExchangeRateResponse(
		String fromCurrency,
		String toCurrency,
		BigDecimal rate,
		String date) {

}

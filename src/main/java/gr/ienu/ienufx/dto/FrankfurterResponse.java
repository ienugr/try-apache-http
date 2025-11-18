package gr.ienu.ienufx.dto;

import java.math.BigDecimal;
import java.util.Map;

public record FrankfurterResponse(
		BigDecimal amount,
		String base,
		String date,
		Map<String, BigDecimal> rates) {

}

package net.currencyservice.domain.dto.response;

import java.util.Currency;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CurrencyRateResponseDto {

    private Currency currency;

    private Map<String, Double> rates;
}

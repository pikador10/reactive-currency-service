package net.currencyservice.domain.dto.response;

import java.math.BigDecimal;
import java.util.Currency;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyConvertResponseDto {

    private Double rate;

    private Currency sourceCurrency;

    private Currency targetCurrency;

    private BigDecimal amount;
}

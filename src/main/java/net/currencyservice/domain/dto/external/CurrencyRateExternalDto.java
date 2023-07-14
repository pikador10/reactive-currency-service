package net.currencyservice.domain.dto.external;

import java.time.LocalDate;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyRateExternalDto {

    private boolean success;

    private long timestamp;

    private String base;

    private LocalDate date;

    private Map<String, Double> rates;
}

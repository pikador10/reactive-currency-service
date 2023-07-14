package net.currencyservice.domain;

import java.time.LocalDate;
import java.util.Currency;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Document("currencyRate")
public class CurrencyRate {

    @Id
    private String id;

    private Currency currency;

    private LocalDate date;

    private Map<String, Double> rates;
}

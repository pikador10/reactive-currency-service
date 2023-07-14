package net.currencyservice.property;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@NoArgsConstructor
@ConfigurationProperties("currency.external")
public class CurrencyRateProperty {

    private String baseUrl;

    private String latestUrl;

    private String updateRateCron;

    private String accessKey;
}

package net.currencyservice.config;

import lombok.RequiredArgsConstructor;
import net.currencyservice.property.CurrencyRateProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    private final CurrencyRateProperty currencyRateProperty;

    @Bean
    public WebClient getWebClient() {
        return WebClient.builder()
            .defaultHeader("apikey", currencyRateProperty.getAccessKey())
            .build();
    }
}

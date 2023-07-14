package net.currencyservice.busines;

import net.currencyservice.busines.impl.CurrencyRateServiceImpl;
import net.currencyservice.domain.CurrencyRate;
import net.currencyservice.domain.dto.external.CurrencyRateExternalDto;
import net.currencyservice.domain.dto.response.CurrencyConvertResponseDto;
import net.currencyservice.domain.dto.response.CurrencyRateResponseDto;
import net.currencyservice.mapper.CurrencyRateMapper;
import net.currencyservice.repository.CurrencyRateRepository;
import net.currencyservice.util.CurrencyRateUtil;
import net.currencyservice.validation.CurrencyRateValidationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class CurrencyRateServiceImplTest {

    @Mock
    private CurrencyRateRepository repository;

    @Mock
    private CurrencyRateClient currencyRateClient;

    @Mock
    private CurrencyRateValidationService currencyRateValidationService;

    @Spy
    private CurrencyRateMapper currencyRateMapper = Mappers.getMapper(CurrencyRateMapper.class);

    @InjectMocks
    private CurrencyRateServiceImpl currencyRateService;

    @Test
    void getCurrencyRates() {
        // Arrange
        var currencyRate1 = new CurrencyRate("1", Currency.getInstance("USD"), LocalDate.now(), new HashMap<>());
        var currencyRate2 = new CurrencyRate("2", Currency.getInstance("EUR"), LocalDate.now(), new HashMap<>());

        when(repository.findAll()).thenReturn(Flux.just(currencyRate1, currencyRate2));

        // Act
        var result = currencyRateService.getCurrencyRates();

        // Assert
        assertEquals(2, result.count().block());
        verify(repository).findAll();
        verify(currencyRateMapper).toCurrencyRateResponseDto(currencyRate1);
        verify(currencyRateMapper).toCurrencyRateResponseDto(currencyRate2);
    }

    @Test
    void getCurrencyRate_whenEntityFoundInDB() {
        // Arrange
        var currency = Currency.getInstance("USD");
        var currencyRate = new CurrencyRate("1", currency, LocalDate.now(), Map.of());
        var responseDto = new CurrencyRateResponseDto(currency, new HashMap<>());

        when(currencyRateValidationService.validateForBaseCurrency(currency)).thenReturn(currency);
        when(repository.findByCurrency(currency)).thenReturn(Mono.just(currencyRate));

        // Act
        var result = currencyRateService.getCurrencyRate(currency);

        // Assert
        assertEquals(responseDto, result.block());
        verify(currencyRateValidationService).validateForBaseCurrency(currency);
        verify(repository).findByCurrency(currency);
        verify(currencyRateMapper).toCurrencyRateResponseDto(currencyRate);
        verify(currencyRateMapper, never()).toCurrencyRate(any());
    }

    @Test
    void getCurrencyRate_whenEntityNotFoundInDB() {
        // Arrange
        var currency = Currency.getInstance("USD");
        var now = LocalDate.now();
        var responseDto = new CurrencyRateResponseDto(currency, Map.of("EUR", 25d));
        var currencyRateEntity = new CurrencyRate("1", currency, now, Map.of("EUR", 25d));
        var currencyRate = new CurrencyRate(null, currency, now, Map.of("EUR", 25d));
        var usd = CurrencyRateExternalDto.builder()
            .rates(Map.of("EUR", 25d))
            .base("USD")
            .success(true)
            .timestamp(111)
            .date(now)
            .build();

        when(currencyRateValidationService.validateForBaseCurrency(currency)).thenReturn(currency);
        when(currencyRateClient.getCurrencyRate(currency)).thenReturn(Mono.just(usd));
        when(repository.findByCurrency(currency)).thenReturn(Mono.empty());
        when(repository.save(any())).thenReturn(Mono.just(currencyRateEntity));

        // Act
        var result = currencyRateService.getCurrencyRate(currency);

        // Assert
        assertEquals(responseDto, result.block());
        verify(currencyRateValidationService).validateForBaseCurrency(currency);
        verify(repository).findByCurrency(currency);
        verify(repository).save(currencyRate);
        verify(currencyRateMapper).toCurrencyRateResponseDto(currencyRateEntity);
        verify(currencyRateMapper).toCurrencyRate(usd);
    }


    @Test
    void convertCurrency() {
        // Arrange
        var sourceCurrency = Currency.getInstance("USD");
        var targetCurrency = Currency.getInstance("EUR");
        var amount = BigDecimal.valueOf(100);
        var sourceRate = 1.2;

        var sourceCurrencyRate = new CurrencyRate("1", sourceCurrency, LocalDate.now(), Map.of(targetCurrency.getCurrencyCode(), sourceRate));

        when(currencyRateValidationService.validateForBaseCurrency(sourceCurrency)).thenReturn(sourceCurrency);
        when(repository.findByCurrency(sourceCurrency)).thenReturn(Mono.just(sourceCurrencyRate));

        var convertedAmount = CurrencyRateUtil.convert(amount, BigDecimal.valueOf(sourceRate));
        var expectedResponse = new CurrencyConvertResponseDto(sourceRate, sourceCurrency, targetCurrency, convertedAmount);

        // Act
        var result = currencyRateService.convertCurrency(sourceCurrency, targetCurrency, amount);

        // Assert
        assertEquals(expectedResponse, result.block());
        verify(repository).findByCurrency(sourceCurrency);
    }

    @Test
    void updateCurrencyRate() {
        // Arrange
        var currencyRate1 = new CurrencyRate("1", Currency.getInstance("USD"), LocalDate.now(), new HashMap<>());
        var currencyRate2 = new CurrencyRate("2", Currency.getInstance("EUR"), LocalDate.now(), new HashMap<>());
        var usd = CurrencyRateExternalDto.builder()
            .rates(new HashMap<>())
            .base("USD")
            .success(true)
            .timestamp(111)
            .date(LocalDate.now())
            .build();
        var eur = CurrencyRateExternalDto.builder()
            .rates(new HashMap<>())
            .base("EUR")
            .success(true)
            .timestamp(111)
            .date(LocalDate.now())
            .build();

        when(repository.findAll()).thenReturn(Flux.just(currencyRate1, currencyRate2));
        when(currencyRateClient.getCurrencyRate(currencyRate1.getCurrency())).thenReturn(Mono.just(usd));
        when(currencyRateClient.getCurrencyRate(currencyRate2.getCurrency())).thenReturn(Mono.just(eur));
        when(repository.save(currencyRate1)).thenReturn(Mono.just(currencyRate1));
        when(repository.save(currencyRate2)).thenReturn(Mono.just(currencyRate2));

        // Act
        currencyRateService.updateCurrencyRate();

        // Assert
        verify(repository).findAll();
        verify(currencyRateClient).getCurrencyRate(currencyRate1.getCurrency());
        verify(currencyRateClient).getCurrencyRate(currencyRate2.getCurrency());
        verify(currencyRateMapper).toCurrencyRate(usd);
        verify(currencyRateMapper, times(1)).toCurrencyRate(eur);
        verify(repository).save(currencyRate1);
        verify(repository).save(currencyRate2);
    }
}

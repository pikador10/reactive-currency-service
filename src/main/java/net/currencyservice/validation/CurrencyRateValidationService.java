package net.currencyservice.validation;

import static java.util.Objects.isNull;
import static net.currencyservice.util.constant.CurrencyRate.BASE_CURRENCY;

import java.util.Currency;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrencyRateValidationService {

    public Currency validateForBaseCurrency(Currency currency) {
        return isNull(currency) ? BASE_CURRENCY : currency;
    }
}

package net.currencyservice.util;

import java.math.BigDecimal;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CurrencyRateUtil {

    public static BigDecimal convert(BigDecimal amount, BigDecimal rate) {
        return amount.multiply(rate);
    }
}

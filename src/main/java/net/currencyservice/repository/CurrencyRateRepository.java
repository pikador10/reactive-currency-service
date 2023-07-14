package net.currencyservice.repository;

import java.util.Currency;

import net.currencyservice.domain.CurrencyRate;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CurrencyRateRepository extends ReactiveMongoRepository<CurrencyRate, Long> {

    Mono<CurrencyRate> findByCurrency(Currency currency);
}

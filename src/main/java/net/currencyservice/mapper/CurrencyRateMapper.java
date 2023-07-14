package net.currencyservice.mapper;

import net.currencyservice.domain.CurrencyRate;
import net.currencyservice.domain.dto.external.CurrencyRateExternalDto;
import net.currencyservice.domain.dto.response.CurrencyRateResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CurrencyRateMapper {

    @Mapping(target = "currency", source = "base")
    CurrencyRate toCurrencyRate(CurrencyRateExternalDto currencyRateExternalDto);

    CurrencyRateResponseDto toCurrencyRateResponseDto(CurrencyRate currencyRate);
}

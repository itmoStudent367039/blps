package ru.ifmo.common.dto.hotels;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TariffDto {

    private String tariffName;
    private Double price;
    private List<TariffOptionDto> includeOptions;
    private List<TariffOptionDto> excludeOptions;
}

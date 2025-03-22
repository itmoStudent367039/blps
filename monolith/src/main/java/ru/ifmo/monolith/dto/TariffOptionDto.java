package ru.ifmo.monolith.dto;

import lombok.Builder;
import lombok.Data;
import ru.ifmo.monolith.domain.entity.TariffOptionName;

@Data
@Builder
public class TariffOptionDto {

    private TariffOptionName optionName;
}

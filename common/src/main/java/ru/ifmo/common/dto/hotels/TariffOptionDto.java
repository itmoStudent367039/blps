package ru.ifmo.common.dto.hotels;

import lombok.Builder;
import lombok.Data;
import ru.ifmo.common.domain.entity.TariffOptionName;

@Data
@Builder
public class TariffOptionDto {

    private TariffOptionName optionName;
}

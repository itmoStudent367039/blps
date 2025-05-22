package ru.ifmo.common.dto.hotels;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CityDto {

    private String name;
    private Integer hotelsCount;
}

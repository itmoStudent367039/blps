package ru.ifmo.monolith.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CityDto {

    private String name;
    private Long hotelsCount;
}

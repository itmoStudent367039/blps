package ru.ifmo.monolith.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class NumberDto {

    private Integer numberOfRooms;
    private Integer maxOccupancy;
    private Integer numberOfSingleBeds;
    private Integer numberOfPairBeds;
    private String name;
    private List<TariffDto> tariffs;
}

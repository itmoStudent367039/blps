package ru.ifmo.common.dto.hotels;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetHotelsResponse {

    private Integer totalPrice;
    private Integer pricePerDay;
    private Integer rating;
    private Double distanceFromCenter;
    private String name;
}



package ru.ifmo.common.dto.internal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PricePerDayRequest {

    private String hotelName;
    private String numberName;
    private String tariffName;
}

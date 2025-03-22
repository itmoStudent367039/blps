package ru.ifmo.monolith.dto;

import lombok.Builder;
import lombok.Data;
import ru.ifmo.common.dto.PaymentResponse;

@Data
@Builder
public class BookingResponseDto {

    private PaymentResponse paymentResponse;
}

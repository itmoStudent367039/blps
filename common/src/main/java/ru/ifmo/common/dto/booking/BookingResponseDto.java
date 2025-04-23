package ru.ifmo.common.dto.booking;

import lombok.Builder;
import lombok.Data;
import ru.ifmo.common.dto.internal.PaymentResponse;

@Data
@Builder
public class BookingResponseDto {

    private PaymentResponse paymentResponse;
}

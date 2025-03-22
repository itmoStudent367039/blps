package ru.ifmo.payment.dto;

import lombok.Data;

@Data
public class PaymentHookDto {

    private Boolean success;
    private Integer bookingId;
}

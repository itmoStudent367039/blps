package ru.ifmo.payment.dto;

import lombok.Data;

@Data
public class PaymentHookDto {

    private String type;
    private String event;
    private Object object;
}

package ru.ifmo.hotels.domain.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class TariffOptionId implements Serializable {

    private Integer tariffId;

    private Integer optionId;
}
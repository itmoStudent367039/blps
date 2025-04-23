package ru.ifmo.hotels.domain.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class TariffExcludeOption {

    @EmbeddedId
    private TariffOptionId tariffOptionId;
}

package ru.ifmo.monolith.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class TariffOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TariffOptionName optionName;

    @ManyToMany(mappedBy = "includeOptions")
    private List<Tariff> tariffsIncluded;

    @ManyToMany(mappedBy = "excludeOptions")
    private List<Tariff> tariffsExcluded;
}

package ru.ifmo.monolith.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Tariff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tariffName;

    private Double price;

    @ManyToMany
    @JoinTable(
            name = "tariff_include_option",
            joinColumns = @JoinColumn(name = "tariff_id"),
            inverseJoinColumns = @JoinColumn(name = "option_id")
    )
    private List<TariffOption> includeOptions;

    @ManyToMany
    @JoinTable(
            name = "tariff_exclude_option",
            joinColumns = @JoinColumn(name = "tariff_id"),
            inverseJoinColumns = @JoinColumn(name = "option_id")
    )
    private List<TariffOption> excludeOptions;

    @ManyToOne
    @JoinColumn(name = "number_id")
    private Number number;
}
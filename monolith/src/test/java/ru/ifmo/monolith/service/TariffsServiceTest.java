package ru.ifmo.monolith.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.ifmo.monolith.domain.entity.Tariff;
import ru.ifmo.monolith.domain.repository.AbstractDatabaseTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class TariffsServiceTest extends AbstractDatabaseTest {

    @Autowired
    private TariffsService tariffsService;

    @Test
    void getPricePerDayTest() {
        assertThat(tariffsService.getPricePerDay(
                "Hotel Moscow",
                "Standard Room",
                "Basic"))
                .isPresent()
                .get()
                .isEqualTo(50.);
    }

    @Test
    void getTariffByNamesTest() {
        Optional<Tariff> byNames = tariffsService.findByNames(
                "Hotel Moscow",
                "Standard Room",
                "Basic");
        assertThat(byNames.isPresent()).isTrue();
        Tariff actual = byNames.get();
        assertThat(actual).hasFieldOrPropertyWithValue("tariffName", "Basic")
                .hasFieldOrPropertyWithValue("price", 50.);
    }
}
package ru.ifmo.monolith.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.ifmo.monolith.domain.entity.Tariff;

import java.util.Optional;

@Repository
public interface TariffRepository extends JpaRepository<Tariff, Integer> {

    @Query("""
                SELECT t.price
                FROM Tariff t
                JOIN t.number n
                JOIN n.hotel h
                WHERE t.tariffName = :tariffName
                AND n.name = :numberName
                AND h.name = :hotelName
            """)
    Optional<Double> getPricePerDay(String hotelName, String numberName, String tariffName);

    @Query("""
                SELECT t FROM Tariff t
                JOIN FETCH t.number n
                JOIN FETCH n.hotel h
                WHERE h.name = :hotelName
                AND n.name = :numberName
                AND t.tariffName = :tariffName
            """)
    Optional<Tariff> findByNames(@Param("hotelName") String hotelName,
                                 @Param("numberName") String numberName,
                                 @Param("tariffName") String tariffName);
}

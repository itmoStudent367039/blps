package ru.ifmo.monolith.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.ifmo.monolith.domain.entity.Number;
import ru.ifmo.monolith.dto.GetAvailableNumbersRequest;

import java.util.List;

@Repository
// todo: jpa specification
public interface NumbersRepository extends JpaRepository<Number, Integer> {

    @Query("""
            SELECT n FROM Number n
            JOIN Hotel h ON h.id = n.hotelId
            WHERE h.name = :#{#request.hotelName}
              AND n.maxOccupancy >= :#{#request.guestsCount}
              AND NOT EXISTS (
                SELECT 1 FROM Booking b
                WHERE b.hotelName = h.name
                  AND b.hotelNumberName = n.name
                  AND b.status IN ('PENDING', 'CONFIRMED')
                  AND b.startDate < :#{#request.endBookingDate}
                  AND b.endDate > :#{#request.startBookingDate}
              )
            ORDER BY n.id DESC
            """)
    Page<Number> findAvailableNumbers(@Param("request") GetAvailableNumbersRequest request, Pageable pageable);


    List<Number> findAllByHotelId(Integer hotelId);
}

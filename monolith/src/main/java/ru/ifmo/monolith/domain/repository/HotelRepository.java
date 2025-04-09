package ru.ifmo.monolith.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.ifmo.monolith.domain.entity.Hotel;
import ru.ifmo.monolith.dto.GetHotelsRequest;
import ru.ifmo.monolith.dto.HotelDto;

import java.util.List;

@Repository
// todo: jpa specification
public interface HotelRepository extends JpaRepository<Hotel, Integer> {

    @Query("""
            SELECT new ru.ifmo.monolith.dto.HotelDto(c.name, h.name)
            FROM Hotel h
            JOIN City c ON c.id = h.cityId
            WHERE h.name LIKE CONCAT('%', :name, '%')
            ORDER BY h.name DESC
            """)
    List<HotelDto> findAllByNameIsLike(@Param("name") String name);

    @Query("""
            SELECT h FROM Hotel h
            JOIN City c ON c.id = h.cityId
            WHERE c.name = :#{#request.cityName}
              AND EXISTS (
                SELECT 1 FROM Number n
                WHERE n.hotelId = h.id
                  AND n.maxOccupancy >= :#{#request.guestsCount}
                  AND NOT EXISTS (
                    SELECT 1 FROM Booking b
                    WHERE b.hotelName = h.name
                      AND b.hotelNumberName = n.name
                      AND b.status IN ('PENDING', 'CONFIRMED')
                      AND b.startDate < :#{#request.endBookingDate}
                      AND b.endDate > :#{#request.startBookingDate}
                  )
              )
            ORDER BY h.name DESC
            """)
    Page<Hotel> findAvailableHotels(@Param("request") GetHotelsRequest request, Pageable pageable);
}

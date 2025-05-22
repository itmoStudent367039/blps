package ru.ifmo.hotels.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.ifmo.common.dto.hotels.GetHotelsRequest;
import ru.ifmo.common.dto.hotels.HotelDto;
import ru.ifmo.hotels.domain.entity.Hotel;

import java.util.List;
import java.util.Optional;

@Repository
// todo: jpa specification
public interface HotelRepository extends JpaRepository<Hotel, Integer> {

    @Query("""
            SELECT new ru.ifmo.common.dto.hotels.HotelDto(c.name, h.name)
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
              )
            ORDER BY h.name DESC
            """)
    Page<Hotel> findAvailableHotels(@Param("request") GetHotelsRequest request, Pageable pageable);

    Optional<Hotel> findByName(String name);
}

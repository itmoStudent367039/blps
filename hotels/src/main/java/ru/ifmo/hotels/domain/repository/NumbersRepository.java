package ru.ifmo.hotels.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.ifmo.common.dto.hotels.GetAvailableNumbersRequest;
import ru.ifmo.hotels.domain.entity.Number;

import java.util.List;

@Repository
// todo: jpa specification
public interface NumbersRepository extends JpaRepository<Number, Integer> {

    @Query("""
            SELECT n FROM Number n
            JOIN Hotel h ON h.id = n.hotelId
            WHERE h.name = :#{#request.hotelName}
              AND n.maxOccupancy >= :#{#request.guestsCount}
            ORDER BY n.id DESC
            """)
    Page<Number> findAvailableNumbers(@Param("request") GetAvailableNumbersRequest request, Pageable pageable);


    List<Number> findAllByHotelId(Integer hotelId);
}

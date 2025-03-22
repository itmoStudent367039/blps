package ru.ifmo.monolith.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.ifmo.monolith.booking.Booking;
import ru.ifmo.monolith.dto.BookingRequestDto;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    @Query("""
            SELECT COUNT(b) = 0 FROM Booking b
            JOIN b.room r
            JOIN r.hotel h
            JOIN b.tariff t
            WHERE h.name = :#{#request.bookingInfo.hotelName}
            AND r.name = :#{#request.bookingInfo.hotelNumberName}
            AND t.tariffName = :#{#request.bookingInfo.tariffName}
            AND b.status IN ('CONFIRMED', 'PENDING')
            AND b.startDate < :#{#request.bookingInfo.endBookingDate}
            AND b.endDate > :#{#request.bookingInfo.startBookingDate}
            """)
    Boolean isRoomAvailable(@Param("request") BookingRequestDto requestDto);
}

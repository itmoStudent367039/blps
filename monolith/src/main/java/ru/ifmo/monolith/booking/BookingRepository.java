package ru.ifmo.monolith.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.ifmo.common.dto.booking.BookingRequestDto;

@Repository
// todo: jpa specification
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    @Query("""
            SELECT COUNT(b) = 0 FROM Booking b
            WHERE b.hotelName = :#{#request.bookingInfo.hotelName}
              AND b.hotelNumberName = :#{#request.bookingInfo.hotelNumberName}
              AND b.tariffName = :#{#request.bookingInfo.tariffName}
              AND b.status IN ('CONFIRMED', 'PENDING')
              AND b.startDate < :#{#request.bookingInfo.endBookingDate}
              AND b.endDate > :#{#request.bookingInfo.startBookingDate}
            """)
    Boolean isRoomAvailable(@Param("request") BookingRequestDto requestDto);
}

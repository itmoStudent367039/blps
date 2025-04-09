package ru.ifmo.monolith.domain.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.ifmo.monolith.booking.Booking;
import ru.ifmo.monolith.domain.entity.Number;
import ru.ifmo.monolith.domain.entity.Tariff;
import ru.ifmo.monolith.dto.BookingRequestDto;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.ifmo.monolith.booking.BookingStatus.PENDING;

class BookingRepositoryTest extends AbstractDatabaseTest {

    @Autowired
    BookingRepository bookingRepository;

    @Test
    void checkAvailableAndBook() {
        var requestDto = createValidBookingRequest();
        var bookingInfo = requestDto.getBookingInfo();

        var isRoomAvailable = bookingRepository.isRoomAvailable(requestDto);
        assertThat(isRoomAvailable).isTrue();

        var booking = createBooking(bookingInfo, requestDto);
        var savedBooking = bookingRepository.save(booking);

        assertThat(savedBooking.getId()).isEqualTo(1);
        assertThat(savedBooking.getStatus()).isEqualTo(PENDING);
    }

    private Booking createBooking(BookingRequestDto.BookingInfoDto bookingInfo,
                                  BookingRequestDto requestDto) {
        return Booking.builder()
                .startDate(bookingInfo.getStartBookingDate())
                .endDate(bookingInfo.getEndBookingDate())
                .guestsNumber(bookingInfo.getGuestsNumber())
                .firstName(requestDto.getFirstName())
                .lastName(requestDto.getLastName())
                .email(requestDto.getEmail())
                .hotelName("Hotel Moscow")
                .tariffName("Basic")
                .status(PENDING)
                .hotelNumberName("Standard Room")
                .build();
    }

    private BookingRequestDto createValidBookingRequest() {
        return BookingRequestDto.builder()
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .bookingInfo(BookingRequestDto.BookingInfoDto.builder()
                        .hotelName("Hotel Moscow")
                        .hotelNumberName("Standard Room")
                        .tariffName("Basic")
                        .startBookingDate(LocalDate.now().plusDays(1))
                        .endBookingDate(LocalDate.now().plusDays(3))
                        .guestsNumber(1)
                        .build())
                .build();
    }
}
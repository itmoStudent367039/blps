package ru.ifmo.monolith.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.ifmo.common.dto.PaymentRequest;
import ru.ifmo.common.dto.PaymentResponse;
import ru.ifmo.monolith.booking.Booking;
import ru.ifmo.monolith.booking.BookingStatus;
import ru.ifmo.monolith.domain.entity.Tariff;
import ru.ifmo.monolith.domain.repository.BookingRepository;
import ru.ifmo.monolith.dto.BookingRequestDto;
import ru.ifmo.monolith.dto.BookingResponseDto;
import ru.ifmo.monolith.exception.MonolithException;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static ru.ifmo.monolith.booking.BookingStatus.PENDING;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final TariffsService tariffsService;
    private final PaymentService paymentService;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public BookingResponseDto resolveBooking(BookingRequestDto requestDto) {
        var bookingInfo = requestDto.getBookingInfo();
        var tariffByNames = getTariffByNames(bookingInfo);
        var isRoomAvailable = bookingRepository.isRoomAvailable(requestDto);
        var id = createBookingAndReturnId(requestDto, bookingInfo, tariffByNames);
        if (!isRoomAvailable) {
            throw new MonolithException("Room is already booked for the selected dates", CONFLICT);
        }
        var pricePerDay = tariffsService.getPricePerDay(
                        bookingInfo.getHotelName(),
                        bookingInfo.getHotelNumberName(),
                        bookingInfo.getTariffName())
                .orElseThrow(() -> new MonolithException("Can't find tariff", NOT_FOUND));
        var paymentRequest = buildPaymentRequest(pricePerDay, bookingInfo, id);
        var paymentDto = paymentService.resolvePayment(paymentRequest);
        return buildBookingResponse(paymentDto);
    }

    @Transactional
    public void setStatus(Integer bookingId, BookingStatus status) {
        var booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new MonolithException(
                        "Can't set status for id: " + bookingId + "; the selected booking: " + status,
                        CONFLICT));
        booking.setStatus(status);
    }

    public BookingStatus getStatus(Integer bookingId) {
        var booking = bookingRepository.findById(bookingId);
        if (booking.isEmpty()) {
            throw new MonolithException("Can't get status for id: " + bookingId, NOT_FOUND);
        }
        return booking.get().getStatus();
    }

    private BookingResponseDto buildBookingResponse(PaymentResponse paymentDto) {
        return BookingResponseDto.builder()
                .paymentResponse(paymentDto)
                .build();
    }

    private PaymentRequest buildPaymentRequest(Double pricePerDay,
                                               BookingRequestDto.BookingInfoDto bookingInfo,
                                               Integer id) {
        return PaymentRequest.builder()
                .amount(countAmount(pricePerDay, bookingInfo))
                .bookingId(id)
                .build();
    }

    private Tariff getTariffByNames(BookingRequestDto.BookingInfoDto bookingInfo) {
        return tariffsService.findByNames(
                        bookingInfo.getHotelName(),
                        bookingInfo.getHotelNumberName(),
                        bookingInfo.getTariffName())
                .orElseThrow(() -> new MonolithException("Tariff wasn't found", NOT_FOUND));
    }

    private Integer createBookingAndReturnId(BookingRequestDto requestDto,
                                             BookingRequestDto.BookingInfoDto bookingInfo,
                                             Tariff tariffByNames) {
        var booking = Booking.builder()
                .startDate(bookingInfo.getStartBookingDate())
                .endDate(bookingInfo.getEndBookingDate())
                .guestsNumber(bookingInfo.getGuestsNumber())
                .firstName(requestDto.getFirstName())
                .lastName(requestDto.getLastName())
                .email(requestDto.getEmail())
                .tariff(tariffByNames)
                .status(PENDING)
                .room(tariffByNames.getNumber())
                .build();
        return bookingRepository.save(booking).getId();
    }

    private Double countAmount(Double pricePerDay, BookingRequestDto.BookingInfoDto bookingInfo) {
        var startDate = bookingInfo.getStartBookingDate();
        var endDate = bookingInfo.getEndBookingDate();
        var daysCount = DAYS.between(startDate, endDate);
        return pricePerDay * daysCount;
    }
}

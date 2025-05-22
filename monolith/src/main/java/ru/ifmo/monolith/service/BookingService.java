package ru.ifmo.monolith.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ifmo.common.dto.booking.BookingModel;
import ru.ifmo.common.dto.booking.BookingRequestDto;
import ru.ifmo.common.dto.booking.BookingResponseDto;
import ru.ifmo.common.dto.internal.PaymentRequest;
import ru.ifmo.common.dto.internal.PaymentResponse;
import ru.ifmo.monolith.booking.Booking;
import ru.ifmo.monolith.booking.BookingStatus;
import ru.ifmo.monolith.booking.BookingRepository;
import ru.ifmo.monolith.exception.MonolithException;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static ru.ifmo.monolith.booking.BookingStatus.PENDING;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final TariffsService tariffsService;
    private final PaymentService paymentService;
    private final FavoritesService favoritesService;

    @Transactional
    public BookingResponseDto resolveBooking(BookingRequestDto requestDto, String user) {
        var bookingInfo = requestDto.getBookingInfo();
        boolean isTariffExists = tariffsService.exists(
                bookingInfo.getHotelName(),
                bookingInfo.getHotelNumberName(),
                bookingInfo.getTariffName());
        if (!isTariffExists) {
            throw new MonolithException("Tarriff doesn't exists", NOT_FOUND);
        }
        boolean isRoomAvailable = bookingRepository.isRoomAvailable(requestDto);
        if (!isRoomAvailable) {
            throw new MonolithException("Room is already booked for the selected dates", CONFLICT);
        }
        int id = createBookingAndReturnId(requestDto, bookingInfo, user);
        favoritesService.save(user, bookingInfo.getHotelName());
        var pricePerDay = tariffsService.getPricePerDay(
                bookingInfo.getHotelName(),
                bookingInfo.getHotelNumberName(),
                bookingInfo.getTariffName());
        var paymentRequest = buildPaymentRequest(pricePerDay, bookingInfo, id);
        var paymentDto = paymentService.resolvePayment(paymentRequest);
        paymentDto.setBookingId(id);
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

    public BookingModel getById(Integer bookingId) {
        return bookingRepository.findById(bookingId)
                .map(this::toModel)
                .orElseThrow(() -> new MonolithException("Booking not found by id: " + bookingId, NOT_FOUND));
    }

    private BookingModel toModel(Booking booking) {
        return BookingModel.builder()
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .guestsNumber(booking.getGuestsNumber())
                .firstName(booking.getFirstName())
                .lastName(booking.getLastName())
                .tariffName(booking.getTariffName())
                .email(booking.getEmail())
                .hotelNumberName(booking.getHotelNumberName())
                .tariffName(booking.getTariffName())
                .hotelName(booking.getHotelName())
                .isPayed(true)
                .build();
    }

    public BookingStatus getStatus(Integer bookingId, UserDetails user) {
        var bookingOptional = bookingRepository.findById(bookingId);
        if (bookingOptional.isEmpty()) {
            throw new MonolithException("Can't get status for id: " + bookingId, NOT_FOUND);
        }
        var booking = bookingOptional.get();
        if (hasRoleUser(user)) {
            if (!booking.getUsername().equals(user.getUsername())) {
                throw new MonolithException("User doesn't belong to this booking", FORBIDDEN);
            }
        }
        return booking.getStatus();
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

    private Integer createBookingAndReturnId(BookingRequestDto requestDto,
                                             BookingRequestDto.BookingInfoDto bookingInfo,
                                             String user) {
        var booking = Booking.builder()
                .startDate(bookingInfo.getStartBookingDate())
                .endDate(bookingInfo.getEndBookingDate())
                .guestsNumber(bookingInfo.getGuestsNumber())
                .firstName(requestDto.getFirstName())
                .lastName(requestDto.getLastName())
                .email(requestDto.getEmail())
                .tariffName(bookingInfo.getTariffName())
                .hotelName(bookingInfo.getHotelName())
                .status(PENDING)
                .hotelNumberName(bookingInfo.getHotelNumberName())
                .username(user)
                .build();
        return bookingRepository.save(booking).getId();
    }

    private Double countAmount(Double pricePerDay, BookingRequestDto.BookingInfoDto bookingInfo) {
        var startDate = bookingInfo.getStartBookingDate();
        var endDate = bookingInfo.getEndBookingDate();
        var daysCount = DAYS.between(startDate, endDate);
        return pricePerDay * daysCount;
    }

    private boolean hasRoleUser(UserDetails user) {
        return user.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER"));
    }
}

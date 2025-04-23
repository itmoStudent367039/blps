package ru.ifmo.monolith.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.ifmo.common.dto.booking.BookingRequestDto;
import ru.ifmo.common.dto.booking.BookingResponseDto;
import ru.ifmo.monolith.booking.BookingStatus;
import ru.ifmo.monolith.service.BookingService;

import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
import static ru.ifmo.monolith.api.constant.ApiUri.BOOKING_URI;

@RestController
@RequiredArgsConstructor
@RequestMapping(BOOKING_URI)
public class BookingController {

    private final BookingService bookingService;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<BookingResponseDto> resolveBooking(@RequestBody @Valid BookingRequestDto requestDto,
                                                             @AuthenticationPrincipal UserDetails user) {

        return ok(bookingService.resolveBooking(requestDto, user.getUsername()));
    }

    @GetMapping("/check-status")
    public ResponseEntity<BookingStatus> checkStatus(@RequestParam("id") Integer id,
                                                     @AuthenticationPrincipal UserDetails user) {
        return ok(bookingService.getStatus(id, user));
    }
}

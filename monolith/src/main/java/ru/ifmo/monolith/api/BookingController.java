package ru.ifmo.monolith.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.ifmo.monolith.booking.BookingStatus;
import ru.ifmo.monolith.dto.BookingRequestDto;
import ru.ifmo.monolith.dto.BookingResponseDto;
import ru.ifmo.monolith.service.BookingService;

import static org.springframework.http.ResponseEntity.ok;
import static ru.ifmo.monolith.api.constant.ApiUri.BOOKING_URI;

@RestController
@RequiredArgsConstructor
@RequestMapping(BOOKING_URI)
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponseDto> resolveBooking(
            @RequestBody @Valid BookingRequestDto requestDto) {
        return ok(bookingService.resolveBooking(requestDto));
    }

    @GetMapping("/check-status")
    public ResponseEntity<BookingStatus> checkStatus(@RequestParam("id") Integer id) {
        return ok(bookingService.getStatus(id));
    }
}

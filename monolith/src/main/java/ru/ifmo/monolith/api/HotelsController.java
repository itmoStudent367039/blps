package ru.ifmo.monolith.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ifmo.monolith.dto.GetHotelsRequest;
import ru.ifmo.monolith.dto.GetHotelsResponse;
import ru.ifmo.monolith.exception.MonolithException;
import ru.ifmo.monolith.service.HotelsService;

import java.time.LocalDate;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static ru.ifmo.monolith.api.constant.ApiUri.HOTELS_URI;

@RestController
@RequiredArgsConstructor
@RequestMapping(HOTELS_URI)
public class HotelsController {

    private final HotelsService hotelsService;

    @PostMapping
    public ResponseEntity<Page<GetHotelsResponse>> getHotelsByCity(@RequestBody @Valid GetHotelsRequest request,
                                                                   @RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "10") int size) {
        if (!isBookingDateValid(request)) {
            throw new MonolithException("Not valid booking dates!", BAD_REQUEST);
        }
        var pageable = PageRequest.of(page, size);
        var hotels = hotelsService.findAllHotels(request, pageable);
        return ResponseEntity.ok(hotels);
    }

    public boolean isBookingDateValid(GetHotelsRequest request) {
        boolean isEndDateAfter = request.getEndBookingDate().isAfter(request.getStartBookingDate());
        boolean isStartDateValid = request.getStartBookingDate().isAfter(LocalDate.now()) ||
                request.getStartBookingDate().isEqual(LocalDate.now());
        return isEndDateAfter && isStartDateValid;
    }
}

package ru.ifmo.hotels.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.ifmo.common.dto.hotels.GetHotelsRequest;
import ru.ifmo.common.dto.hotels.GetHotelsResponse;
import ru.ifmo.common.validator.BookingDateValidator;
import ru.ifmo.hotels.exception.HotelsException;
import ru.ifmo.hotels.service.HotelsService;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static ru.ifmo.hotels.api.constant.ApiUri.HOTELS_URI;

@RestController
@RequiredArgsConstructor
@RequestMapping(HOTELS_URI)
public class HotelsController {

    private final HotelsService hotelsService;
    private final BookingDateValidator dateValidator;

    @PostMapping
    public ResponseEntity<Page<GetHotelsResponse>> getHotelsByCity(@RequestBody @Valid GetHotelsRequest request,
                                                                   @RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "10") int size) {
        if (!dateValidator.isValid(request.getStartBookingDate(), request.getEndBookingDate())) {
            throw new HotelsException("Not valid booking dates!", BAD_REQUEST);
        }
        var pageable = PageRequest.of(page, size);
        var hotels = hotelsService.findAllHotels(request, pageable);
        return ResponseEntity.ok(hotels);
    }
}

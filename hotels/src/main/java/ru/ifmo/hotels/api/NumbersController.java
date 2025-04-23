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
import ru.ifmo.common.dto.hotels.GetAvailableNumbersRequest;
import ru.ifmo.common.dto.hotels.NumberDto;
import ru.ifmo.common.validator.BookingDateValidator;
import ru.ifmo.hotels.exception.HotelsException;
import ru.ifmo.hotels.service.NumbersService;


import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static ru.ifmo.hotels.api.constant.ApiUri.NUMBERS_URI;

@RestController
@RequiredArgsConstructor
@RequestMapping(NUMBERS_URI)
public class NumbersController {

    private final NumbersService numbersService;
    private final BookingDateValidator dateValidator;

    @PostMapping
    public ResponseEntity<Page<NumberDto>> getNumbers(@RequestBody @Valid GetAvailableNumbersRequest request,
                                                      @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size) {
        if (!dateValidator.isValid(request.getStartBookingDate(), request.getEndBookingDate())) {
            throw new HotelsException("Not valid booking dates!", BAD_REQUEST);
        }
        var pageable = PageRequest.of(page, size);
        var hotels = numbersService.findAllByHotelName(request, pageable);
        return ResponseEntity.ok(hotels);
    }
}

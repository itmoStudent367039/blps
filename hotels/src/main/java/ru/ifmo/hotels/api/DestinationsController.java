package ru.ifmo.hotels.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ifmo.common.dto.hotels.DestinationsResponse;
import ru.ifmo.hotels.service.DestinationsService;

import static ru.ifmo.hotels.api.constant.ApiUri.DESTINATIONS_URI;

@RestController
@RequiredArgsConstructor
@RequestMapping(DESTINATIONS_URI)
public class DestinationsController {

    private final DestinationsService destinationsService;

    @GetMapping("/{destinationName}")
    public ResponseEntity<DestinationsResponse> getDestinations(@PathVariable String destinationName) {
        return ResponseEntity.ok(destinationsService.getAllByName(destinationName));
    }
}

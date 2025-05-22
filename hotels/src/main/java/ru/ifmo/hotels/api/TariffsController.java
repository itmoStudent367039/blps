package ru.ifmo.hotels.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ifmo.common.dto.internal.ExistsByNameTariffRequest;
import ru.ifmo.common.dto.internal.ExistsByNameTariffResponse;
import ru.ifmo.common.dto.internal.PricePerDayRequest;
import ru.ifmo.common.dto.internal.PricePerDayResponse;
import ru.ifmo.hotels.exception.HotelsException;
import ru.ifmo.hotels.service.TariffsService;

import static ru.ifmo.hotels.api.constant.ApiUri.TARIFFS_URI;

@RestController
@RequestMapping(TARIFFS_URI)
@RequiredArgsConstructor
public class TariffsController {

    private final TariffsService tariffsService;

    @PostMapping("/exists")
    public ResponseEntity<ExistsByNameTariffResponse> exists(@RequestBody ExistsByNameTariffRequest request) {
        boolean exists = tariffsService.findByNames(
                        request.getHotelName(),
                        request.getNumberName(),
                        request.getTariffName())
                .isPresent();
        var body = ExistsByNameTariffResponse.builder()
                .exists(exists)
                .build();
        return ResponseEntity.ok(body);
    }

    @PostMapping("/price")
    public ResponseEntity<PricePerDayResponse> exists(@RequestBody PricePerDayRequest request) {
        var optionalPrice = tariffsService.getPricePerDay(
                request.getHotelName(),
                request.getNumberName(),
                request.getTariffName());
        if (optionalPrice.isEmpty()) {
            throw new HotelsException("Tariff is not found", HttpStatus.NOT_FOUND);
        }
        var body = PricePerDayResponse.builder()
                .pricePerDay(optionalPrice.get())
                .build();
        return ResponseEntity.ok(body);
    }
}

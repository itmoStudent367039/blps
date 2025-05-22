package ru.ifmo.monolith.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.ifmo.common.dto.internal.ExistsByNameTariffRequest;
import ru.ifmo.common.dto.internal.ExistsByNameTariffResponse;
import ru.ifmo.common.dto.internal.PricePerDayRequest;
import ru.ifmo.common.dto.internal.PricePerDayResponse;
import ru.ifmo.monolith.exception.IntegrationException;

import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpStatus.BAD_GATEWAY;

@Slf4j
@Service
@RequiredArgsConstructor
public class TariffsService {

    private final RestTemplate restTemplate;

    @Value("${tariffs.exists.url}")
    private String tariffsExistsUrl;

    @Value("${tariffs.price.url}")
    private String tariffsPriceUrl;

    public Double getPricePerDay(String hotelName,
                                 String numberName,
                                 String tariffName) {
        try {
            var request = PricePerDayRequest.builder()
                    .hotelName(hotelName)
                    .numberName(numberName)
                    .tariffName(tariffName)
                    .build();
            return ofNullable(restTemplate.postForObject(
                    tariffsPriceUrl,
                    request,
                    PricePerDayResponse.class
            )).orElseThrow(() -> new IntegrationException("Hotels service is unavailable", BAD_GATEWAY))
                    .getPricePerDay();
        } catch (RestClientException e) {
            throw new IntegrationException("Hotels service is unavailable", BAD_GATEWAY);
        }
    }

    public boolean exists(String hotelName,
                          String numberName,
                          String tariffName) {
        try {
            var request = ExistsByNameTariffRequest.builder()
                    .hotelName(hotelName)
                    .numberName(numberName)
                    .tariffName(tariffName)
                    .build();
            return ofNullable(restTemplate.postForObject(
                    tariffsExistsUrl,
                    request,
                    ExistsByNameTariffResponse.class
            )).orElseThrow(() -> new IntegrationException("Hotels service is unavailable", BAD_GATEWAY))
                    .isExists();
        } catch (RestClientException e) {
            throw new IntegrationException("Hotels service is unavailable", BAD_GATEWAY);
        }
    }
}

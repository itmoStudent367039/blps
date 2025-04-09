package ru.ifmo.monolith.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;
import ru.ifmo.common.dto.PaymentRequest;
import ru.ifmo.common.dto.PaymentResponse;
import ru.ifmo.monolith.domain.repository.AbstractDatabaseTest;
import ru.ifmo.monolith.dto.BookingRequestDto;
import ru.ifmo.monolith.dto.BookingResponseDto;

import java.time.LocalDate;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@EnableWireMock({
        @ConfigureWireMock(
                name = "payment",
                port = 8081)
})
class BookingServiceTest extends AbstractDatabaseTest {

    @Autowired
    BookingService bookingService;

    @MockBean
    PaymentService paymentService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void resolveBookingTest() throws JsonProcessingException {
        PaymentResponse paymentResponse = new PaymentResponse("link");
        BookingRequestDto request = buildBookingRequest();
        PaymentRequest paymentRequest = buildPaymentRequest();
        BookingResponseDto expected = buildBookingResponse(paymentResponse);

        stubFor(com.github.tomakehurst.wiremock.client.WireMock.post(urlEqualTo("/payment/api"))
                .withRequestBody(equalToJson(objectMapper.writeValueAsString(paymentRequest)))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(paymentResponse))));
        doReturn(paymentResponse).when(paymentService)
                .resolvePayment(paymentRequest);

        assertThat(bookingService.resolveBooking(request))
                .isEqualTo(expected);
    }

    private static BookingResponseDto buildBookingResponse(PaymentResponse paymentResponse) {
        return BookingResponseDto.builder()
                .paymentResponse(paymentResponse)
                .build();
    }

    private PaymentRequest buildPaymentRequest() {
        return PaymentRequest.builder()
                .amount(50.)
                .bookingId(1)
                .build();
    }

    private BookingRequestDto buildBookingRequest() {
        return BookingRequestDto.builder()
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .bookingInfo(BookingRequestDto.BookingInfoDto.builder()
                        .hotelName("Hotel Moscow")
                        .hotelNumberName("Standard Room")
                        .tariffName("Basic")
                        .startBookingDate(LocalDate.now())
                        .endBookingDate(LocalDate.now().plusDays(1))
                        .guestsNumber(1)
                        .build())
                .build();
    }
}
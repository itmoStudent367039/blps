package ru.ifmo.monolith.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;
import ru.ifmo.common.dto.PaymentRequest;
import ru.ifmo.common.dto.PaymentResponse;
import ru.ifmo.monolith.domain.repository.AbstractDatabaseTest;
import ru.ifmo.monolith.dto.BookingRequestDto;

import java.time.LocalDate;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@EnableWireMock({
        @ConfigureWireMock(
                name = "payment",
                port = 8081)
})
class BookingControllerTest extends AbstractDatabaseTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeAll
    static void setup() {
    }

    @Test
    void resolveBookingTest() throws Exception {
        BookingRequestDto requestDto = buildBookingRequest();
        PaymentResponse paymentResponse = buildPaymentResponse();
        PaymentRequest paymentRequest = buildPaymentRequest();

        stubFor(com.github.tomakehurst.wiremock.client.WireMock.post(urlEqualTo("/payment/api"))
                .withRequestBody(equalToJson(objectMapper.writeValueAsString(paymentRequest)))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(paymentResponse))));

        mockMvc.perform(post("/api/booking")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.paymentResponse").value(paymentResponse));
    }

    private PaymentRequest buildPaymentRequest() {
        return PaymentRequest.builder()
                .bookingId(1)
                .amount(2100.)
                .build();
    }

    private PaymentResponse buildPaymentResponse() {
        return PaymentResponse.builder()
                .paymentLink("link")
                .build();
    }

    private BookingRequestDto buildBookingRequest() {
        return BookingRequestDto.builder()
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .bookingInfo(BookingRequestDto.BookingInfoDto.builder()
                        .hotelName("Hotel Moscow")
                        .hotelNumberName("Single Room")
                        .tariffName("Deluxe")
                        .startBookingDate(LocalDate.of(2025, 4, 4))
                        .endBookingDate(LocalDate.of(2025, 4, 25))
                        .guestsNumber(1)
                        .build())
                .build();
    }
}
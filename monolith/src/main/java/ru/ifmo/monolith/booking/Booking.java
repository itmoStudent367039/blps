package ru.ifmo.monolith.booking;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ifmo.monolith.domain.entity.Number;
import ru.ifmo.monolith.domain.entity.Tariff;

import java.time.LocalDate;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer guestsNumber;

    private String firstName;

    private String lastName;

    private String email;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Number room;

    @ManyToOne
    @JoinColumn(name = "tariff_id")
    private Tariff tariff;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;
}

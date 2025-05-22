package ru.ifmo.mailsender.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.ifmo.common.dto.booking.BookingModel;

@Service
@Slf4j
public class SimpleEmailSender {

    private final JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    public SimpleEmailSender(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Async
    public void sendMail(BookingModel model) throws MailException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("Payment Confirmation");
        message.setFrom(from);
        message.setTo(model.getEmail());
        message.setText(buildMessage(model));
        emailSender.send(message);
        log.info("Send email to - {}", model.getEmail());
    }

    private String buildMessage(BookingModel booking) {
        if (booking.isPayed()) {
            return String.format("""
                            Уважаемый(ая) %s %s,
                            
                            Ваше бронирование успешно подтверждено:
                            
                            Отель: %s
                            Номер: %s
                            Тариф: %s
                            Даты: %s по %s
                            Кол-во гостей: %d
                            
                            Спасибо за выбор нашего сервиса!
                            """,
                    booking.getFirstName(),
                    booking.getLastName(),
                    booking.getHotelName(),
                    booking.getHotelNumberName(),
                    booking.getTariffName(),
                    booking.getStartDate(),
                    booking.getEndDate(),
                    booking.getGuestsNumber()
            );
        } else {
            return String.format("""
                            Уважаемый(ая) %s %s,
                            
                            Ваше бронирование не было подтверждено из-за ошибки оплаты.
                            
                            Пожалуйста, повторите попытку или свяжитесь с поддержкой.
                            
                            Отель: %s
                            Номер: %s
                            Даты: %s по %s
                            
                            С уважением, команда бронирования.
                            """,
                    booking.getFirstName(),
                    booking.getLastName(),
                    booking.getHotelName(),
                    booking.getHotelNumberName(),
                    booking.getStartDate(),
                    booking.getEndDate()
            );
        }
    }
}

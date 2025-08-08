package pl.coderslab.hotelpriceapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendPriceAlert(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("hotel.pricealert@gmail.com");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            System.out.println("E-mail wysłany do: " + to);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Błąd wysyłki: " + e.getMessage());
        }
    }


    public void sendTestEmail(String to) {
        String subject = "Test wiadomości z HotelPriceApp";
        String body = "To jest testowa wiadomość wysłana z Twojej aplikacji Spring Boot.";
        sendPriceAlert(to, subject, body);
    }
}

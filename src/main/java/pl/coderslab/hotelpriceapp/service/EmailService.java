package pl.coderslab.hotelpriceapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import pl.coderslab.hotelpriceapp.model.EmailLog;
import pl.coderslab.hotelpriceapp.repository.EmailLogRepository;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final EmailLogRepository emailLogRepository;

    public void sendPriceAlert(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("hotel.pricealert@gmail.com");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);

            // LOG — sukces
            emailLogRepository.save(EmailLog.builder()
                    .recipient(to)
                    .subject(subject)
                    .body(body)
                    .success(true)
                    .build());

            System.out.println("E-mail wysłany do: " + to);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Błąd wysyłki: " + e.getMessage());

            // LOG — błąd
            emailLogRepository.save(EmailLog.builder()
                    .recipient(to)
                    .subject(subject)
                    .body(body)
                    .success(false)
                    .errorMessage(e.getMessage())
                    .build());
        }
    }

    public void sendTestEmail(String to) {
        String subject = "Test wiadomości z HotelPriceApp";
        String body = "To jest testowa wiadomość wysłana z Twojej aplikacji Spring Boot.";
        sendPriceAlert(to, subject, body);
    }
}

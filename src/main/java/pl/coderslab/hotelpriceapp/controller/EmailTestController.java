package pl.coderslab.hotelpriceapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.hotelpriceapp.service.EmailService;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class EmailTestController {

    private final EmailService emailService;

    @GetMapping("/email")
    public ResponseEntity<String> testEmail(@RequestParam String to) {
        System.out.println(">>> Wywołano endpoint testEmail z parametrem: " + to);
        try {
            emailService.sendTestEmail(to);
            return ResponseEntity.ok("Wysłano testowego maila na adres: " + to);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Błąd wysyłki: " + e.getMessage());
        }
    }
}

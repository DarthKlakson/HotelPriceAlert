package pl.coderslab.hotelpriceapp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.hotelpriceapp.dto.HotelDTO;
import pl.coderslab.hotelpriceapp.service.HotelService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/hotels")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;

    @GetMapping
    public ResponseEntity<List<HotelDTO>> getUserHotels(Authentication auth) {
        return ResponseEntity.ok(hotelService.getHotelsForUser(auth.getName()));
    }

    @PostMapping
    public ResponseEntity<String> addHotel(@Valid @RequestBody HotelDTO dto,
                                           Authentication auth) {
        try {
            hotelService.addHotelForUser(dto, auth.getName());
            return ResponseEntity.ok("Hotel dodany.");
        } catch (IOException e) {
            e.printStackTrace(); // 👈 pomocne przy debugowaniu
            return ResponseEntity.status(500).body("Błąd przy pobieraniu danych z Itaki");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteHotel(@PathVariable Long id, Authentication auth) {
        hotelService.deleteHotelForUser(id, auth.getName());
        return ResponseEntity.ok("Hotel usunięty.");
    }
}

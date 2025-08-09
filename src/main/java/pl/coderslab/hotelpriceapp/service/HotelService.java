package pl.coderslab.hotelpriceapp.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.coderslab.hotelpriceapp.dto.HotelDTO;
import pl.coderslab.hotelpriceapp.model.Hotel;
import pl.coderslab.hotelpriceapp.model.PriceHistory;
import pl.coderslab.hotelpriceapp.model.User;
import pl.coderslab.hotelpriceapp.repository.HotelRepository;
import pl.coderslab.hotelpriceapp.repository.PriceHistoryRepository;
import pl.coderslab.hotelpriceapp.repository.UserRepository;
import pl.coderslab.hotelpriceapp.scraper.ItakaPriceScraper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HotelService {

    private final HotelRepository        hotelRepo;
    private final UserRepository         userRepo;
    private final PriceHistoryRepository histRepo;
    private final ItakaPriceScraper      scraper;

    @Transactional
    public void addHotelForUser(HotelDTO dto, String username) throws IOException {
        User u = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Użytkownik nie znaleziony"));

        var res = scraper.scrapeNameAndLowestPrice(dto.getUrl());

        // Zapisujemy hotel
        Hotel h = Hotel.builder()
                .url(dto.getUrl())
                .name(res.name())
                .user(u)
                .build();
        hotelRepo.save(h);

        // Pierwszy pomiar
        if (res.price() != null) {
            histRepo.save(PriceHistory.builder()
                    .hotel(h)
                    .price(res.price().toPlainString())
                    .timestamp(LocalDateTime.now())
                    .build());
        }
    }

    // Pobiera DTO hoteli zalogowanego użytkownika.
    public List<HotelDTO> getHotelsForUser(String username) {
        User u = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Użytkownik nie znaleziony"));
        return hotelRepo.findAllByUser(u).stream()
                .map(h -> HotelDTO.builder()
                        .id(h.getId())
                        .url(h.getUrl())
                        .name(h.getName())
                        .lastKnownPrice(h.getLastKnownPrice())
                        .build())
                .collect(Collectors.toList());
    }

    // Usuwa hotel o podanym ID należący do zalogowanego użytkownika.
    @Transactional
    public void deleteHotelForUser(Long hotelId, String username) {
        User u = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Użytkownik nie znaleziony"));
        hotelRepo.findByIdAndUser(hotelId, u)
                .ifPresent(hotelRepo::delete);
    }
}

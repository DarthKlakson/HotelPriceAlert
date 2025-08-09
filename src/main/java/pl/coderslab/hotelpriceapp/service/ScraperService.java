package pl.coderslab.hotelpriceapp.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.coderslab.hotelpriceapp.model.Hotel;
import pl.coderslab.hotelpriceapp.model.PriceHistory;
import pl.coderslab.hotelpriceapp.repository.HotelRepository;
import pl.coderslab.hotelpriceapp.repository.PriceHistoryRepository;
import pl.coderslab.hotelpriceapp.scraper.ItakaPriceScraper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScraperService {

    private final HotelRepository hotelRepo;
    private final PriceHistoryRepository histRepo;
    private final EmailService emailService;
    private final ItakaPriceScraper scraper;

    // 60 minut (600_000 ms * 6). Zmieniaj w razie potrzeby.
    @Scheduled(fixedRate = 200_00)
    @Transactional
    public void checkHotelPrices() {
        List<Hotel> hotels = hotelRepo.findAll();
        for (Hotel hotel : hotels) {
            try {
                var res = scraper.scrapeNameAndLowestPrice(hotel.getUrl());

                // Aktualizacja nazwy, jeżeli się zmieniła
                if (res.name() != null && !res.name().equals(hotel.getName())) {
                    hotel.setName(res.name());
                    hotelRepo.save(hotel);
                }

                if (res.price() == null) {
                    // nie udało się pobrać ceny – pomijamy
                    continue;
                }

                BigDecimal last = hotel.getLastKnownPrice() != null
                        ? new BigDecimal(hotel.getLastKnownPrice())
                        : null;

                if (last == null) {
                    // Pierwszy pomiar
                    hotel.setLastKnownPrice(res.price().toPlainString());
                    hotelRepo.save(hotel);

                    histRepo.save(PriceHistory.builder()
                            .hotel(hotel)
                            .price(res.price().toPlainString())
                            .timestamp(LocalDateTime.now())
                            .build());

                    emailService.sendPriceAlert(
                            hotel.getUser().getEmail(),
                            "Pierwsza cena dla: " + hotel.getName(),
                            "Zapisana cena: " + res.price() + " zł\nURL: " + hotel.getUrl()
                    );

                } else if (res.price().compareTo(last) < 0) {
                    // Cena spadła
                    hotel.setLastKnownPrice(res.price().toPlainString());
                    hotelRepo.save(hotel);

                    histRepo.save(PriceHistory.builder()
                            .hotel(hotel)
                            .price(res.price().toPlainString())
                            .timestamp(LocalDateTime.now())
                            .build());

                    emailService.sendPriceAlert(
                            hotel.getUser().getEmail(),
                            "Cena spadła: " + hotel.getName(),
                            "Nowa niższa cena: " + res.price() + " zł\nURL: " + hotel.getUrl()
                    );

                } else if (res.price().compareTo(last) > 0) {
                    // Cena wzrosła
                    hotel.setLastKnownPrice(res.price().toPlainString());
                    hotelRepo.save(hotel);

                    emailService.sendPriceAlert(
                            hotel.getUser().getEmail(),
                            "Cena wzrosła: " + hotel.getName(),
                            "Nowa wyższa cena: " + res.price() + " zł\nURL: " + hotel.getUrl()
                    );
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}

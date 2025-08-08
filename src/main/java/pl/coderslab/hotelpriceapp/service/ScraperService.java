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


    @Scheduled(fixedRate = 600_000 * 6) // co 200 sek
    @Transactional
    public void checkHotelPrices() {
        List<Hotel> hotels = hotelRepo.findAll();
        for (Hotel hotel : hotels) {
            try {
                String url      = hotel.getUrl();
                String newName  = scraper.scrapeName(url);
                BigDecimal price = scraper.scrapeLowestPrice(url);

                // Aktualizacja nazwy, jeżeli się zmieniła
                if (!newName.equals(hotel.getName())) {
                    hotel.setName(newName);
                    hotelRepo.save(hotel);
                }

                if (price == null) {
                    // nie udało się pobrać ceny
                    continue;
                }

                // Pobranie ostatniej zapisanej ceny
                BigDecimal last = hotel.getLastKnownPrice() != null
                        ? new BigDecimal(hotel.getLastKnownPrice())
                        : null;

                // Pierwszy pomiar
                if (last == null) {
                    hotel.setLastKnownPrice(price.toPlainString());
                    hotelRepo.save(hotel);

                    histRepo.save(PriceHistory.builder()
                            .hotel(hotel)
                            .price(price.toPlainString())
                            .timestamp(LocalDateTime.now())
                            .build());

                    emailService.sendPriceAlert(
                            hotel.getUser().getEmail(),
                            "Pierwsza cena dla: " + hotel.getName(),
                            "Zapisana cena: " + price + " zł\nURL: " + url
                    );

                    // 4) Cena spadła
                } else if (price.compareTo(last) < 0) {
                    hotel.setLastKnownPrice(price.toPlainString());
                    hotelRepo.save(hotel);

                    histRepo.save(PriceHistory.builder()
                            .hotel(hotel)
                            .price(price.toPlainString())
                            .timestamp(LocalDateTime.now())
                            .build());

                    emailService.sendPriceAlert(
                            hotel.getUser().getEmail(),
                            "Cena spadła: " + hotel.getName(),
                            "Nowa niższa cena: " + price + " zł\nURL: " + url
                    );

                    // 5) Cena wzrosła
                } else if (price.compareTo(last) > 0) {
                    hotel.setLastKnownPrice(price.toPlainString());
                    hotelRepo.save(hotel);

                    emailService.sendPriceAlert(
                            hotel.getUser().getEmail(),
                            "Cena wzrosła: " + hotel.getName(),
                            "Nowa wyższa cena: " + price + " zł\nURL: " + url
                    );
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}

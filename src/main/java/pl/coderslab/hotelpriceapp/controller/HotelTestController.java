package pl.coderslab.hotelpriceapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.coderslab.hotelpriceapp.model.Hotel;
import pl.coderslab.hotelpriceapp.model.User;
import pl.coderslab.hotelpriceapp.repository.HotelRepository;
import pl.coderslab.hotelpriceapp.repository.UserRepository;

@RestController
@RequiredArgsConstructor
public class HotelTestController {

    private final HotelRepository hotelRepository;
    private final UserRepository userRepository;

    @PostMapping("/test/add-hotel")
    public String addTestHotel() {
        User user = userRepository.findByUsername("testuser")
                .orElseThrow(() -> new RuntimeException("Użytkownik nie istnieje"));

        Hotel hotel = Hotel.builder()
                .name("Hotel Testowy")
                .url("https://www.itaka.pl/wyniki-wyszukiwania/wakacje/turcja/z-katowic/?dateFrom=2025-09-01&dateTo=2025-09-30&participants%5B0%5D%5Badults%5D=2&departuresByPlane=KTW")
                .user(user)
                .build();

        hotelRepository.save(hotel);
        return "Hotel dodany!";
    }
}

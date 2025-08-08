package pl.coderslab.hotelpriceapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coderslab.hotelpriceapp.model.Hotel;
import pl.coderslab.hotelpriceapp.model.User;

import java.util.List;
import java.util.Optional;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
    List<Hotel> findAllByUser(User user);
    Optional<Hotel> findByIdAndUser(Long id, User user);
}

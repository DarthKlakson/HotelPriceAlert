package pl.coderslab.hotelpriceapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coderslab.hotelpriceapp.model.PriceHistory;

public interface PriceHistoryRepository extends JpaRepository<PriceHistory, Long> {
}

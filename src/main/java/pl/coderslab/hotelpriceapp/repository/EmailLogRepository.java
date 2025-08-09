package pl.coderslab.hotelpriceapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coderslab.hotelpriceapp.model.EmailLog;

public interface EmailLogRepository extends JpaRepository<EmailLog, Long> {
}

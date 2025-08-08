package pl.coderslab.hotelpriceapp.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor @AllArgsConstructor @Builder
public class PriceHistory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String price;
    private LocalDateTime timestamp;

    @ManyToOne @JoinColumn(name = "hotel_id")
    private Hotel hotel;
}

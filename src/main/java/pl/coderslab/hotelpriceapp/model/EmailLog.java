package pl.coderslab.hotelpriceapp.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class EmailLog {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String recipient;

    @Column(nullable = false)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String body;

    @Builder.Default
    private boolean success = false;

    private String errorMessage;

    @Builder.Default
    private LocalDateTime sentAt = LocalDateTime.now();
}

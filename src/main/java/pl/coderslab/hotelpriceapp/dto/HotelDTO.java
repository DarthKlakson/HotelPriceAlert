package pl.coderslab.hotelpriceapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "HotelDTO", description = "Transport object for hotel data")
public class HotelDTO {
    @Schema(description = "Hotel ID", example = "1")
    private Long id;

    @Schema(description = "Itaka offer URL", example = "https://www.itaka.pl/wakacje/...")
    private String url;

    @Schema(description = "Hotel/offer name", example = "Hotel Testowy 5*")
    private String name;

    @Schema(description = "Last known price (numeric string)", example = "2789")
    private String lastKnownPrice;
}

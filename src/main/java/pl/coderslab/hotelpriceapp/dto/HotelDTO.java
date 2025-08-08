package pl.coderslab.hotelpriceapp.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotelDTO {
    private Long   id;
    private String url;
    private String name;
    private String lastKnownPrice;
}

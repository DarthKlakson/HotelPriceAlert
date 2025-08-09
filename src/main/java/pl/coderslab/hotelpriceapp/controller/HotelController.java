package pl.coderslab.hotelpriceapp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.hotelpriceapp.dto.HotelDTO;
import pl.coderslab.hotelpriceapp.service.HotelService;

import java.io.IOException;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.media.*;

@RestController
@RequestMapping("/api/hotels")
@RequiredArgsConstructor
@Tag(name = "Hotels", description = "Manage hotels of the current user")
@SecurityRequirement(name = "bearerAuth")
public class HotelController {

    private final HotelService hotelService;

    @GetMapping
    @Operation(summary = "List hotels for current user")
    @ApiResponse(responseCode = "200", description = "List of hotels",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = HotelDTO.class))))
    public ResponseEntity<List<HotelDTO>> getUserHotels(Authentication auth) {
        return ResponseEntity.ok(hotelService.getHotelsForUser(auth.getName()));
    }

    @PostMapping
    @Operation(summary = "Add a hotel (Itaka URL)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Hotel added",
                    content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Scraping error",
                    content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<String> addHotel(@Valid @RequestBody HotelDTO dto,
                                           Authentication auth) {
        try {
            hotelService.addHotelForUser(dto, auth.getName());
            return ResponseEntity.ok("Hotel dodany.");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Błąd przy pobieraniu danych z Itaki");
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a hotel by ID")
    @ApiResponse(responseCode = "200", description = "Hotel deleted",
            content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class)))
    public ResponseEntity<String> deleteHotel(@PathVariable Long id, Authentication auth) {
        hotelService.deleteHotelForUser(id, auth.getName());
        return ResponseEntity.ok("Hotel usunięty.");
    }
}

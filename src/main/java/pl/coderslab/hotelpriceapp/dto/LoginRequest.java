package pl.coderslab.hotelpriceapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(name = "LoginRequest")
public class LoginRequest {
    @NotBlank
    @Schema(example = "john")
    private String username;

    @NotBlank
    @Schema(example = "Secret123!")
    private String password;
}

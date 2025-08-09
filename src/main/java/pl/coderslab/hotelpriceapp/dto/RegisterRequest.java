package pl.coderslab.hotelpriceapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(name = "RegisterRequest")
public class RegisterRequest {
    @NotBlank
    @Schema(example = "john")
    private String username;

    @NotBlank
    @Schema(example = "Secret123!")
    private String password;

    @Email
    @Schema(example = "john@example.com")
    private String email;
}

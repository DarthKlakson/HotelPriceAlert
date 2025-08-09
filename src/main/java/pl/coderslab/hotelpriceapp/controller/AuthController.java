package pl.coderslab.hotelpriceapp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.hotelpriceapp.dto.LoginRequest;
import pl.coderslab.hotelpriceapp.dto.RegisterRequest;
import pl.coderslab.hotelpriceapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "User registration and login (public)")
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Registered",
                            content = @Content(mediaType = "text/plain",
                                    schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "400", description = "Validation error")
            })
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest req) {
        userService.registerUser(req);
        return ResponseEntity.ok("Zarejestrowano pomyślnie");
    }

    @PostMapping("/login")
    @Operation(summary = "Login and get JWT token",
            description = "Returns raw JWT as plain text in the response body.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "JWT token",
                            content = @Content(mediaType = "text/plain",
                                    schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")
            })
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest req) {
        String jwt = userService.authenticateAndGenerateToken(req);
        return ResponseEntity.ok(jwt);
    }
}

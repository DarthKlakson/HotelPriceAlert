package pl.coderslab.hotelpriceapp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.hotelpriceapp.dto.LoginRequest;
import pl.coderslab.hotelpriceapp.dto.RegisterRequest;
import pl.coderslab.hotelpriceapp.service.UserService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest req) {
        userService.registerUser(req);
        return ResponseEntity.ok("Zarejestrowano pomyślnie");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest req) {
        String jwt = userService.authenticateAndGenerateToken(req);
        return ResponseEntity.ok(jwt);
    }
}

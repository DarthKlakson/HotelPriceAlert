package pl.coderslab.hotelpriceapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.coderslab.hotelpriceapp.dto.LoginRequest;
import pl.coderslab.hotelpriceapp.dto.RegisterRequest;
import pl.coderslab.hotelpriceapp.model.User;
import pl.coderslab.hotelpriceapp.repository.UserRepository;
import pl.coderslab.hotelpriceapp.security.JwtUtil;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    public void registerUser(RegisterRequest req) {
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new RuntimeException("Użytkownik już istnieje");
        }
        User u = User.builder()
                .username(req.getUsername())
                .password(passwordEncoder.encode(req.getPassword()))
                .email(req.getEmail())
                .role("USER")
                .build();
        userRepository.save(u);
    }

    public String authenticateAndGenerateToken(LoginRequest req) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
        );
        User u = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new RuntimeException("Brak użytkownika"));
        return jwtUtil.generateToken(u);
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Użytkownik nie znaleziony"));
    }
}

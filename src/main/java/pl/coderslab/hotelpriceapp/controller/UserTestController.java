package pl.coderslab.hotelpriceapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.coderslab.hotelpriceapp.model.User;
import pl.coderslab.hotelpriceapp.repository.UserRepository;

@RestController
@RequiredArgsConstructor
public class UserTestController {

    private final UserRepository userRepository;

    @PostMapping("/test/add-user")
    public String addTestUser() {
        User user = User.builder()
                .username("testuser")
                .email("test@example.com")
                .password("hashedpassword") // pamiętaj o zaszyfrowaniu w realnym scenariuszu
                .role("USER")
                .build();

        userRepository.save(user);
        return "Użytkownik zapisany!";
    }
}

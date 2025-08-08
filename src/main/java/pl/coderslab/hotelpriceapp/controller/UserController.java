package pl.coderslab.hotelpriceapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.hotelpriceapp.model.User;
import pl.coderslab.hotelpriceapp.service.UserService;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<User> getLoggedInUser(Authentication auth) {
        User user = userService.getByUsername(auth.getName());
        return ResponseEntity.ok(user);
    }
}

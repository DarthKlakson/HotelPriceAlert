package pl.coderslab.hotelpriceapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.hotelpriceapp.model.EmailLog;
import pl.coderslab.hotelpriceapp.repository.EmailLogRepository;

import java.util.List;

@RestController
@RequestMapping("/api/email-logs")
@RequiredArgsConstructor
@Tag(name = "email-log-controller", description = "View email sending logs")
public class EmailLogController {

    private final EmailLogRepository emailLogRepository;

    @GetMapping
    @Operation(summary = "Get all email logs")
    public ResponseEntity<List<EmailLog>> getAll() {
        return ResponseEntity.ok(emailLogRepository.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get email log by ID")
    public ResponseEntity<EmailLog> getById(@PathVariable Long id) {
        return emailLogRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}

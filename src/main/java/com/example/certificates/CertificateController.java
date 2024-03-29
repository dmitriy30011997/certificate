package com.example.certificates;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/certificates")
@Slf4j
@RequiredArgsConstructor
public class CertificateController {

    private final TokenService tokenService;

    @GetMapping
    public ResponseEntity<String> createToken(CertificateRequest request) {
        return ResponseEntity.ok("JWT Token created successfully: " + tokenService.createToken(request));
    }

    @GetMapping
    @RequestMapping("/verify")
    public ResponseEntity<Boolean> verifyToken(String token) {
        return ResponseEntity.ok(tokenService.verifyJwtToken(token));
    }
}

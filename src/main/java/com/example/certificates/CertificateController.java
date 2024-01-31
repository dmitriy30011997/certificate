package com.example.certificates;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/certificates")
@Slf4j
@RequiredArgsConstructor
public class CertificateController {

    private final TokenService tokenService;

    @PostMapping
    public ResponseEntity<String> createToken(CertificateRequest request) {
        return tokenService.createToken(request);
    }
}

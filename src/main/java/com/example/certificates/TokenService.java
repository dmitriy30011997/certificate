package com.example.certificates;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

    private final CertificationCenterService certificationCenterService;

    public ResponseEntity<String> createToken(CertificateRequest request) {
        try {
            if (CertificationCenterService.verifyCertificate(request.getPublicKey())) {
                String jwtToken = generateJwtToken(request.getUsername());
                return ResponseEntity.ok("JWT Token created successfully: " + jwtToken);
            } else {
                return ResponseEntity.badRequest().body("Invalid certificate or signature");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to generate JWT token: " + e.getMessage());
        }
    }
    public boolean verifyJwtToken(String token) {
        try {
            PublicKey publicKey = certificationCenterService.getPublicKey();
            JWT.require(Algorithm.RSA256((RSAPublicKey) publicKey)).build().verify(token);
            return true;
        } catch (Exception e) {
            log.error("JWT token verification failed", e);
            return false;
        }
    }
    public String generateJwtToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                .sign(null);
    }
}

package com.example.certificates;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/certificates")
public class CertificateController {

    private final CertificateService certificateService;
    private final CertificationCenterService certificationCenterService;

    @Autowired
    public CertificateController(CertificateService certificateService, CertificationCenterService certificationCenterService) {
        this.certificateService = certificateService;
        this.certificationCenterService = certificationCenterService;
    }

    @PostMapping
    public ResponseEntity<String> createCertificate(@RequestBody CertificateRequest request) {
        try {
            certificateService.createCertificate(request.getUsername(), request.getPublicKey());

            if (certificationCenterService.verifyCertificate()) {
                String jwtToken = certificationCenterService.generateJwtToken(request.getUsername());
                return ResponseEntity.ok("Certificate created successfully. JWT Token: " + jwtToken);
            } else {
                return ResponseEntity.badRequest().body("Invalid certificate or signature");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to create certificate: " + e.getMessage());
        }
    }

    @GetMapping("/{username}")
    public ResponseEntity<String> getCertificate(@PathVariable String username) {
        Optional<CertificateEntity> certificate = certificateService.getCertificateByUsername(username);
        return certificate.map(cert -> ResponseEntity.ok("Public Key: " + cert.getPublicKey()))
                .orElse(ResponseEntity.notFound().build());
    }
}

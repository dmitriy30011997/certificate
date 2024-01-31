package com.example.certificates;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/certificates")
@Slf4j
public class CertificateController {

    private final CertificateService certificateService;
    private final CertificationCenterService certificationCenterService;


    @Autowired
    public CertificateController(CertificateService certificateService, CertificationCenterService certificationCenterService) {
        this.certificateService = certificateService;
        this.certificationCenterService = certificationCenterService;
    }

    @PostMapping
    public ResponseEntity<String> createToken(@RequestBody CertificateRequest request) {
        try {
            CertificateEntity certificate = certificateService.getCertificateByUsername(request.getUsername());

            if (CertificationCenterService.verifyCertificate(certificate.getPublicKey())) {
                String jwtToken = certificationCenterService.generateJwtToken(request.getUsername());
                return ResponseEntity.ok("JWT Token created successfully: " + jwtToken);
            } else {
                return ResponseEntity.badRequest().body("Invalid certificate or signature");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to generate JWT token: " + e.getMessage());
        }
    }

    @GetMapping("/{username}")
    public ResponseEntity<String> getCertificate(@PathVariable String username) {
        CertificateEntity certificate = certificateService.getCertificateByUsername(username);
        return ResponseEntity.ok("Public Key: " + certificate.getPublicKey());
    }
}

package com.example.sertificates;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/certificates")
public class CertificateController {

    private final CertificateService certificateService;

    private final CertificationCenterService certificationCenterService;

    public CertificateController(CertificateService certificateService, CertificationCenterService certificationCenterService) {
        this.certificateService = certificateService;
        this.certificationCenterService = certificationCenterService;
    }

    @PostMapping
    public ResponseEntity<String> createCertificate(@RequestBody CertificateRequest request) {
        if (certificationCenterService.validatePublicKey(request.getPublicKey(), request.getSignature())) {
            String decryptedPublicKey = certificationCenterService.decryptPublicKey(request.getEncryptedPublicKey());
            certificateService.createCertificate(request.getUsername(), decryptedPublicKey);
            return ResponseEntity.ok("Certificate created successfully");
        } else {
            return ResponseEntity.badRequest().body("Invalid public key or signature");
        }
    }

    @GetMapping("/{username}")
    public ResponseEntity<String> getCertificate(@PathVariable String username) {
        String certificate = certificateService.getCertificate(username);
        return ResponseEntity.ok(certificate);
    }
}


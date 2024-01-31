package com.example.certificates;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class CertificateService {

    private final CertificateRepository certificateRepository;

    @Autowired
    public CertificateService(CertificateRepository certificateRepository) {
        this.certificateRepository = certificateRepository;
    }

    public void createCertificate(String username, String publicKey) {
        CertificateEntity certificateEntity = new CertificateEntity();
        certificateEntity.setUsername(username);
        certificateEntity.setPublicKey(publicKey);
        certificateRepository.save(certificateEntity);

        log.info("Certificate created for user {}", username);
    }

    public Optional<CertificateEntity> getCertificateByUsername(String username) {
        return certificateRepository.findByUsername(username);
    }
}

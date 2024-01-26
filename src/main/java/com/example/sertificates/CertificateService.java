package com.example.sertificates;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
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
    }

    public String getCertificate(String username) {
        return certificateRepository.findByUsername(username)
                .map(CertificateEntity::getPublicKey)
                .orElse("Certificate not found");
    }
}

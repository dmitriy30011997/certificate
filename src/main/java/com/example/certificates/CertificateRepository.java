package com.example.certificates;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CertificateRepository extends JpaRepository<CertificateEntity, Long> {
    CertificateEntity findByUsername(String username);
}
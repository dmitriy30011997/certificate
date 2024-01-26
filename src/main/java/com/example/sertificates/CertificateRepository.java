package com.example.sertificates;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CertificateRepository extends JpaRepository<CertificateEntity, Long> {
    Optional<CertificateEntity> findByUsername(String username);
}
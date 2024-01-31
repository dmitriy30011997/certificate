package com.example.certificates;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CertificateService {

    private final CertificateRepository certificateRepository;

    public CertificateEntity getCertificateByUsername(String username) {
        return certificateRepository.findByUsername(username);
    }
}

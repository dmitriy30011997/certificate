package com.example.certificates;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CertificateRequest {

    private String username;
    private String encryptedPublicKey;
    private String publicKey;
    private String signature;
}

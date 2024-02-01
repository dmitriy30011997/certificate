package com.example.certificates;

import lombok.SneakyThrows;
import org.mockito.Mockito;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import static org.mockito.Mockito.when;

public class MockDataHelper {

    @SneakyThrows
    public static PrivateKey createMockPrivateKey() {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return keyPair.getPrivate();
    }

    @SneakyThrows
    public static PublicKey createMockPublicKey() {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return keyPair.getPublic();
    }
    public static CertificateRequest createMockCertificateRequest() {
        CertificateRequest request = Mockito.mock(CertificateRequest.class);
        when(request.getUsername()).thenReturn("testUser");
        return request;
    }
}
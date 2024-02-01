package com.example.certificates;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource(properties = {"keystore.password=changeit"})
class TokenServiceTests {

    @Mock
    private CertificationCenterService certificationCenterService;

    @InjectMocks
    private TokenService tokenService;
    String username = "testUser";

    @Test
    void testVerifyJwtToken_Positive() {
        KeyPair keyPair = MockDataHelper.createMockKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        when(certificationCenterService.getPrivateKey()).thenReturn(privateKey);
        when(certificationCenterService.getPublicKey()).thenReturn(publicKey);

        String tok = tokenService.generateJwtToken(username);

        boolean result = tokenService.verifyJwtToken(tok);
        assertTrue(result);
    }

    @Test
    void testVerifyJwtToken_Negative() {
        PublicKey publicKey = MockDataHelper.createMockPublicKey();
        PrivateKey privateKey = MockDataHelper.createMockPrivateKey();

        when(certificationCenterService.getPrivateKey()).thenReturn(privateKey);
        when(certificationCenterService.getPublicKey()).thenReturn(publicKey);

        String tok = tokenService.generateJwtToken(username);

        when(certificationCenterService.getPublicKey()).thenReturn(MockDataHelper.createMockPublicKey());
        when(certificationCenterService.getPrivateKey()).thenReturn(MockDataHelper.createMockPrivateKey());

        boolean result = tokenService.verifyJwtToken(tok);
        assertFalse(result);
    }


    @Test
    void testGenerateJwtToken() {
        when(certificationCenterService.getPrivateKey()).thenReturn(MockDataHelper.createMockPrivateKey());
        String token = tokenService.generateJwtToken(username);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }
}
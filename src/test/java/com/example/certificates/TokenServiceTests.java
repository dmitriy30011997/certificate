package com.example.certificates;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

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

    @Test
    void testCreateToken() {
        CertificateRequest request = new CertificateRequest();
        String fakeToken = "fakeToken";
        when(certificationCenterService.publicKey()).thenReturn(MockDataHelper.createMockPublicKey());
        when(certificationCenterService.privateKey()).thenReturn(MockDataHelper.createMockPrivateKey());
        when(tokenService.generateJwtToken(request.getUsername())).thenReturn(fakeToken);

        String token = tokenService.createToken(request);

        assertNotNull(token);
        assertEquals(fakeToken, token);
    }

    @Test
    void testVerifyJwtToken_Positive() {
        String fakeToken = "fakeToken";

        PublicKey publicKey = MockDataHelper.createMockPublicKey();
        when(certificationCenterService.publicKey()).thenReturn(publicKey);
        when(tokenService.verifyJwtToken(fakeToken)).thenCallRealMethod();

        boolean result = tokenService.verifyJwtToken(fakeToken);

        assertTrue(result);
    }

    @Test
    void testVerifyJwtToken_Negative() {
        String fakeToken = "fakeToken";

        PublicKey publicKey = MockDataHelper.createMockPublicKey();
        when(certificationCenterService.publicKey()).thenReturn(publicKey);
        when(tokenService.verifyJwtToken(fakeToken)).thenCallRealMethod();

        fakeToken = fakeToken + "modified";

        boolean result = tokenService.verifyJwtToken(fakeToken);

        assertFalse(result);
    }

    @Test
    void testGenerateJwtToken() {
        String username = "testUser";

        when(certificationCenterService.privateKey()).thenReturn(MockDataHelper.createMockPrivateKey());
        String token = tokenService.generateJwtToken(username);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }
}
package com.example.certificates;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

    private final CertificationCenterService certificationCenterService;

    public String createToken(CertificateRequest request) throws CertificateException, KeyStoreException, NoSuchAlgorithmException, IOException {
        return generateJwtToken(request.getUsername());
        }
    public boolean verifyJwtToken(String token) {
        try {
            PublicKey publicKey = certificationCenterService.publicKey();
            JWT.require(Algorithm.RSA256((RSAPublicKey) publicKey)).build().verify(token);
            return true;
        } catch (Exception e) {
            log.error("JWT token verification failed", e);
            return false;
        }
    }
    public String generateJwtToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                .sign(Algorithm.RSA256(null, (RSAPrivateKey) certificationCenterService.privateKey()));
    }
}

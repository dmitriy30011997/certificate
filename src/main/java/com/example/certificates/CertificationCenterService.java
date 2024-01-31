package com.example.certificates;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

@Service
@Slf4j
public class CertificationCenterService {

    private final CertificateService certificateService;

    @Value("${certificate.privateKey}")
    private String privateKeyString;

    @Value("${certificate.publicKey}")
    private String publicKeyString;

    @Autowired
    public CertificationCenterService(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    private PublicKey getPublicKey() throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyString);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        return keyFactory.generatePublic(keySpec);
    }


    private PrivateKey getPrivateKey() throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyString);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        return keyFactory.generatePrivate(keySpec);
    }

    public boolean verifyJwtToken(String token) {
        try {
            PublicKey publicKey = getPublicKey();
            Jwts.parserBuilder().setSigningKey(publicKey).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.error("JWT token verification failed", e);
            return false;
        }
    }

    public String generateJwtToken(String username) {
        try {
            PrivateKey privateKey = getPrivateKey();
            return Jwts.builder()
                    .setSubject(username)
                    .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 час
                    .signWith(privateKey, SignatureAlgorithm.RS256)
                    .compact();
        } catch (Exception e) {
            log.error("Failed to generate JWT token", e);
            return null;
        }
    }
    //посмотреть какое исключение
    public boolean verifyCertificate() throws Exception {
        // Верификация сертификата должна быть с использованием публичного ключа
        //String publicKey = certificate.getPublicKey();
        // Логика верификации...
        return true;
    }
}

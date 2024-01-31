package com.example.certificates;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;

@Service
@Slf4j
public class CertificationCenterService {

    private final CertificateService certificateService;

    @Value("${keystore.path}")
    private Resource keystoreResource;

    @Value("${keystore.password}")
    private String keystorePassword;

    @Value("${certificateFilePath}")
    private static String certificateFilePath;

    @Autowired
    public CertificationCenterService(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    private PrivateKey getPrivateKey() throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, UnrecoverableKeyException {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        try (InputStream inputStream = keystoreResource.getInputStream()) {
            keyStore.load(inputStream, keystorePassword.toCharArray());
        }
        return (PrivateKey) keyStore.getKey("privateKeyAlias", keystorePassword.toCharArray());
    }

    private PublicKey getPublicKey() throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        try (InputStream inputStream = keystoreResource.getInputStream()) {
            keyStore.load(inputStream, keystorePassword.toCharArray());
        }
        Certificate certificate = keyStore.getCertificate("publicKeyAlias");
        return certificate.getPublicKey();
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
                    .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hour
                    .signWith(privateKey, SignatureAlgorithm.RS256)
                    .compact();
        } catch (Exception e) {
            log.error("Failed to generate JWT token", e);
            return null;
        }
    }
    public static boolean verifyCertificate(String publicKeyString) {
        try {
            FileInputStream fileInputStream = new FileInputStream(certificateFilePath);
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(fileInputStream);

            PublicKey publicKeyFromCert = certificate.getPublicKey();

            return publicKeyFromCert.toString().equals(publicKeyString);
        } catch (Exception e) {
            log.error("Certificate verification failed", e);
            return false;
        }
    }
}

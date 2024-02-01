package com.example.certificates;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

@Service
@Slf4j
@RequiredArgsConstructor
public class CertificationCenterService {

    private String keystoreResource = "C:\\Users\\dmitr\\IdeaProjects\\sertificates\\src\\keystore.jks";

    @Value("${keystore.password}")
    private String keystorePassword;

    private static String certificateFilePath = "src/main/resources/certificate.crt";

    @Value("${alias}")
    private String alias;

    @SneakyThrows
    PrivateKey privateKey() {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(new FileInputStream(keystoreResource), keystorePassword.toCharArray());

        return (PrivateKey) keyStore.getKey(alias, keystorePassword.toCharArray());
    }


    @SneakyThrows
    public PublicKey publicKey() {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(new FileInputStream(keystoreResource), keystorePassword.toCharArray());

        Certificate certificate = keyStore.getCertificate(alias);
        return certificate.getPublicKey();
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


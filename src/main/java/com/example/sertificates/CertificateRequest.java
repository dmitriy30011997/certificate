package com.example.sertificates;

public class CertificateRequest {

    private String username;
    private String encryptedPublicKey;
    private String publicKey;
    private String signature;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEncryptedPublicKey() {
        return encryptedPublicKey;
    }

    public void setEncryptedPublicKey(String encryptedPublicKey) {
        this.encryptedPublicKey = encryptedPublicKey;
    }


    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}

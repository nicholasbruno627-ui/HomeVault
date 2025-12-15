package com.homevault.server.crypto;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class EncryptedPayload {

    @Column(name = "payload_ciphertext", nullable = false)
    private byte[] ciphertext;

    @Column(name = "payload_iv", nullable = false)
    private byte[] iv;

    @Column(name = "payload_salt", nullable = false)
    private byte[] salt;

    @Column(name = "payload_alg", nullable = false, length = 50)
    private String alg;

    public EncryptedPayload() {}

    public void ensureAlgDefault(String defaultAlg) {
        if (this.alg == null || this.alg.isBlank()) {
            this.alg = (defaultAlg == null || defaultAlg.isBlank())
                    ? "AES_GCM_256"
                    : defaultAlg;
        }
    }

    public byte[] getCiphertext() {
        return ciphertext;
    }

    public void setCiphertext(byte[] ciphertext) {
        this.ciphertext = ciphertext;
    }

    public byte[] getIv() {
        return iv;
    }

    public void setIv(byte[] iv) {
        this.iv = iv;
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    public String getAlg() {
        return alg;
    }

    public void setAlg(String alg) {
        this.alg = alg;
    }
}

package com.homevault.server.crypto;

import jakarta.persistence.*;

@Embeddable
public class EncryptedPayload {

  @Column(name = "payload_cipher_text", nullable = false, length = 8192)
  private String cipherTextBase64; //encrypted data

  @Column(name = "payload_iv", nullable = false, length = 64)
  private String ivBase64; //ensures plain-text is encrypted differently each time

  @Column(name = "payload_salt", nullable = false, length = 64)
  private String saltBase64; //makes password-based keys unique

  @Column(name = "payload_alg", nullable = false, length = 32)
  private String algorithm = "AES-GCM"; //modern encryption mode

  @Column(name = "payload_kdf", nullable = false, length = 32)
  private String kdf = "PBKDF2-HMAC-SHA256"; //key derivative function that derives the encryption key

  @Column(name = "payload_ver", nullable = false)
  private int version = 1; //version number for encryption scheme if i ever decide to change algorithms

  
  //getters and setters
  public String getCipherTextBase64() { return cipherTextBase64; }
  public void setCipherTextBase64(String v) { this.cipherTextBase64 = v; }
  public String getIvBase64() { return ivBase64; }
  public void setIvBase64(String v) { this.ivBase64 = v; }
  public String getSaltBase64() { return saltBase64; }
  public void setSaltBase64(String v) { this.saltBase64 = v; }
  public String getAlgorithm() { return algorithm; }
  public void setAlgorithm(String algorithm) { this.algorithm = algorithm; }
  public String getKdf() { return kdf; }
  public void setKdf(String kdf) { this.kdf = kdf; }
  public int getVersion() { return version; }
  public void setVersion(int version) { this.version = version; }
}

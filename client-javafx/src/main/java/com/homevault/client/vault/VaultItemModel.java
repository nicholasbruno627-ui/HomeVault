package com.homevault.client.vault;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

//display items in table with, returned by server's VaultItemResponse
public class VaultItemModel {

    private UUID id;

    private String title;

    //login username
    @SerializedName(value = "username", alternate = {"loginUsername"})
    private String username;

    // Server might call this "secret" or "decryptedSecret"
    @SerializedName(value = "secret", alternate = {"decryptedSecret"})
    private String secret;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}

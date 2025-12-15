package com.homevault.server.vault;

import java.util.UUID;

public class VaultItemNotFoundException extends RuntimeException {

    public VaultItemNotFoundException(UUID id) {
        super("Vault item not found: " + id);
    }
}

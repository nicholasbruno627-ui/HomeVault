package com.homevault.server.vault;

import com.homevault.server.crypto.EncryptedPayload;
import com.homevault.server.user.User;
import com.homevault.server.vault.dto.VaultItemCreateRequest;
import com.homevault.server.vault.dto.VaultItemUpdateRequest;
import com.homevault.server.vault.dto.VaultItemResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class VaultItemService {

    private static final String DEFAULT_ALG = "AES_GCM_256";

    private final VaultItemRepository vaultItemRepository;

    public VaultItemService(VaultItemRepository vaultItemRepository) {
        this.vaultItemRepository = vaultItemRepository;
    }

   
    public VaultItemResponse createVaultItem(UUID userId, VaultItemCreateRequest request) {

        VaultItem item = new VaultItem();
        item.setUserId(userId);
        item.setTitle(request.getTitle());
        item.setLoginUsername(request.getUsername());

        String secret = request.getSecret() == null ? "" : request.getSecret();

        EncryptedPayload payload = new EncryptedPayload();
        payload.setCiphertext(("cipher-" + secret).getBytes(StandardCharsets.UTF_8));
        payload.setIv("iv-dummy".getBytes(StandardCharsets.UTF_8));
        payload.setSalt("salt-dummy".getBytes(StandardCharsets.UTF_8));
        payload.setAlg(DEFAULT_ALG);
        payload.ensureAlgDefault(DEFAULT_ALG);

        item.setPayload(payload);

        VaultItem saved = vaultItemRepository.save(item);
        return toResponse(saved);
    }

  
    public VaultItemResponse updateVaultItem(UUID userId, UUID itemId, VaultItemUpdateRequest request) {

        VaultItem item = vaultItemRepository
                .findByIdAndUserId(itemId, userId)
                .orElseThrow(() -> new VaultItemNotFoundException(itemId));

        if (request.getTitle() != null) {
            item.setTitle(request.getTitle());
        }
        if (request.getUsername() != null) {
            item.setLoginUsername(request.getUsername());
        }

        if (request.getSecret() != null) {
            EncryptedPayload payload = item.getPayload();
            if (payload == null) {
                payload = new EncryptedPayload();
                item.setPayload(payload);
            }

            payload.setCiphertext(("cipher-" + request.getSecret()).getBytes(StandardCharsets.UTF_8));
            payload.setIv("iv-updated".getBytes(StandardCharsets.UTF_8));
            payload.setSalt("salt-updated".getBytes(StandardCharsets.UTF_8));
            payload.setAlg(DEFAULT_ALG);
            payload.ensureAlgDefault(DEFAULT_ALG);
        }

        VaultItem saved = vaultItemRepository.save(item);
        return toResponse(saved);
    }


    @Transactional(readOnly = true)
    public List<VaultItemResponse> listItemsForUser(UUID userId) {
        return vaultItemRepository.findByUserIdOrderByCreatedAtAsc(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

 
    @Transactional(readOnly = true)
    public VaultItemResponse getItem(UUID userId, UUID itemId) {
        VaultItem item = vaultItemRepository
                .findByIdAndUserId(itemId, userId)
                .orElseThrow(() -> new VaultItemNotFoundException(itemId));

        return toResponse(item);
    }

    
    public void deleteItem(UUID userId, UUID itemId) {
        VaultItem item = vaultItemRepository
                .findByIdAndUserId(itemId, userId)
                .orElseThrow(() -> new VaultItemNotFoundException(itemId));

        vaultItemRepository.delete(item);
    }

   
    @Transactional(readOnly = true)
    public String getDecryptedSecretForItem(UUID itemId) {

        //extract user identity from security 
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();

        UUID currentUserId;

        
        if (principal instanceof User u) {
            currentUserId = u.getId();
        }
        
        else if (principal instanceof UUID uuid) {
            currentUserId = uuid;
        }
        else {
            throw new IllegalStateException("Unexpected principal type: " + principal);
        }

        //load the vault item and ensure it's owned by this user
        VaultItem item = vaultItemRepository
                .findByIdAndUserId(itemId, currentUserId)
                .orElseThrow(() -> new IllegalArgumentException("Vault item not found"));

        String cipher = new String(item.getPayload().getCiphertext(), StandardCharsets.UTF_8);

        if (cipher.startsWith("cipher-")) {
            return cipher.substring("cipher-".length());
        }

        return "(no secret stored)";
    }
    
    private VaultItemResponse toResponse(VaultItem item) {
        return new VaultItemResponse(
                item.getId(),
                item.getTitle(),
                item.getLoginUsername(),
                item.getCreatedAt(),
                item.getUpdatedAt()
        );
    }
}

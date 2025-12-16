package com.homevault.server.vault;

import com.homevault.server.vault.dto.VaultItemCreateRequest;
import com.homevault.server.vault.dto.VaultItemResponse;
import com.homevault.server.vault.dto.VaultItemUpdateRequest;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/vault")
public class VaultItemController {

    private final VaultItemService vaultItemService;

    public VaultItemController(VaultItemService vaultItemService) {
        this.vaultItemService = vaultItemService;
    }

   
    @GetMapping
    public List<VaultItemResponse> list(@RequestParam("userId") UUID userId) {
        return vaultItemService.listItemsForUser(userId);
    }

    
    @PostMapping
    public VaultItemResponse create(@RequestParam("userId") UUID userId,
                                    @RequestBody VaultItemCreateRequest request) {
        return vaultItemService.createVaultItem(userId, request);
    }


    @PutMapping("/{id}")
    public VaultItemResponse update(@PathVariable("id") UUID id,
                                    @RequestParam("userId") UUID userId,
                                    @RequestBody VaultItemUpdateRequest request) {
        return vaultItemService.updateVaultItem(userId, id, request);
    }

    
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") UUID id,
                       @RequestParam("userId") UUID userId) {
        vaultItemService.deleteItem(userId, id);
    }

   
    @GetMapping("/{id}/secret")
    public Map<String, String> getSecret(@PathVariable("id") UUID itemId) {

        String secret = vaultItemService.getDecryptedSecretForItem(itemId);

        Map<String, String> body = new HashMap<>();
        body.put("secret", secret);

        return body;
    }
}

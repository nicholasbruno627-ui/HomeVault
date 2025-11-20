package com.homevault.client.service;

import com.homevault.client.vault.VaultItemModel;
import com.homevault.client.backup.BackupModel;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.http.*;
import java.net.URI;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ApiClient {

    private static final String BASE_URL = "http://localhost:8080/api";

    private final HttpClient http = HttpClient.newHttpClient();


    public UUID login(String email, String password) {
        try {
            JSONObject body = new JSONObject()
                    .put("email", email)
                    .put("password", password);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/auth/login"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                    .build();

            HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JSONObject json = new JSONObject(response.body());
                String idString = json.getString("userId");
                return UUID.fromString(idString);
            }

            throw new RuntimeException("Login failed: " + response.body());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public List<VaultItemModel> getVaultItems(UUID userId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/vault-items"))
                    .header("X-User-Id", userId.toString())
                    .GET()
                    .build();

            HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());
            JSONArray arr = new JSONArray(response.body());

            List<VaultItemModel> items = new ArrayList<>();

            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);
                items.add(new VaultItemModel(
                        UUID.fromString(o.getString("id")),
                        o.getString("title"),
                        o.getString("username"),
                        o.getString("secret")
                ));
            }

            return items;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void createVaultItem(UUID userId, String title, String username, String secret) {
        try {
            JSONObject body = new JSONObject()
                    .put("title", title)
                    .put("username", username)
                    .put("secret", secret);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/vault-items"))
                    .header("X-User-Id", userId.toString())
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                    .build();

            http.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateVaultItem(UUID userId, UUID itemId, String title, String username, String secret) {
        try {
            JSONObject body = new JSONObject()
                    .put("title", title)
                    .put("username", username)
                    .put("secret", secret);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/vault-items/" + itemId))
                    .header("X-User-Id", userId.toString())
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(body.toString()))
                    .build();

            http.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteVaultItem(UUID userId, UUID itemId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/vault-items/" + itemId))
                    .header("X-User-Id", userId.toString())
                    .DELETE()
                    .build();

            http.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public List<BackupModel> getBackups(UUID userId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/backups"))
                    .header("X-User-Id", userId.toString())
                    .GET()
                    .build();

            HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());
            JSONArray arr = new JSONArray(response.body());

            List<BackupModel> backups = new ArrayList<>();

            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);
                backups.add(new BackupModel(
                        UUID.fromString(o.getString("id")),
                        o.getString("location"),
                        o.getLong("sizeBytes"),
                        o.getString("status"),
                        Instant.parse(o.getString("createdAt"))
                ));
            }

            return backups;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void createBackup(UUID userId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/backups"))
                    .header("X-User-Id", userId.toString())
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            http.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void restoreBackup(UUID userId, UUID backupId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/backups/" + backupId + "/restore"))
                    .header("X-User-Id", userId.toString())
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            http.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

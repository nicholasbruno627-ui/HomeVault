package com.homevault.client.service;

import com.homevault.client.backup.BackupModel;
import com.homevault.client.vault.VaultItemModel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class ApiClient {

    private static final String BASE_URL = "http://localhost:8080/api";
    private static final String TOKEN_FIELD = "token";

    private static String authToken;

    public static String getAuthToken() {
        return authToken;
    }

 
    public static void login(String email, String password) throws IOException {
        String endpoint = BASE_URL + "/auth/login";
        URL url = new URL(endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String jsonBody =
                "{\"email\":\"" + escapeJson(email) +
                "\",\"password\":\"" + escapeJson(password) + "\"}";

        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonBody.getBytes(StandardCharsets.UTF_8));
        }

        int status = conn.getResponseCode();
        String body = readBody(status, conn);

        if (status != HttpURLConnection.HTTP_OK) {
            throw new IOException("Login failed: " + body);
        }

        String token = extractJsonField(body, TOKEN_FIELD);
        String userIdStr = extractJsonField(body, "userId");

        authToken = token;

        AuthSession session = AuthSession.getInstance();
        session.setToken(token);

        if (userIdStr != null && !userIdStr.isBlank()) {
            session.setUserId(UUID.fromString(userIdStr));
        }
    }
    
    public static void register(String email, String displayName, String password) throws IOException {

        String endpoint = BASE_URL + "/auth/register";
        URL url = new URL(endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String jsonBody = "{"
                + "\"email\":\"" + escapeJson(email) + "\","
                + "\"displayName\":\"" + escapeJson(displayName) + "\","
                + "\"password\":\"" + escapeJson(password) + "\""
                + "}";

        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonBody.getBytes(StandardCharsets.UTF_8));
        }

        int status = conn.getResponseCode();
        String response = readBody(status, conn);

        System.out.println("REGISTER STATUS = " + status);
        System.out.println("REGISTER BODY = " + response);

        
        if (status == HttpURLConnection.HTTP_CONFLICT) {
            throw new IOException("Email already exists");
        }

        
        if (status != HttpURLConnection.HTTP_OK &&
            status != HttpURLConnection.HTTP_CREATED) {
            throw new IOException("Registration failed");
        }

        
    }



    
    public static List<VaultItemModel> getVaultItems(UUID userId) {
        try {
            HttpURLConnection conn = openAuthorizedConnection(
                    BASE_URL + "/vault?userId=" + userId,
                    "GET"
            );

            int status = conn.getResponseCode();
            String body = readBody(status, conn);

            if (status != HttpURLConnection.HTTP_OK) {
                System.err.println("Failed to load vault items. HTTP " + status +
                        " body: " + body);
                return Collections.emptyList();
            }

            return parseVaultItems(body);

        } catch (Exception ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }

  
    public static void createVaultItem(UUID userId, VaultItemModel item) {
        try {
            HttpURLConnection conn = openAuthorizedConnection(
                    BASE_URL + "/vault?userId=" + userId,
                    "POST"
            );

            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String json =
                "{ \"title\":\"" + escapeJson(item.getTitle()) + "\"," +
                "\"username\":\"" + escapeJson(item.getUsername()) + "\"," +
                "\"secret\":\"" + escapeJson(item.getSecret()) + "\" }";

            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }

            int status = conn.getResponseCode();
            String body = readBody(status, conn);

            if (status != HttpURLConnection.HTTP_CREATED &&
                status != HttpURLConnection.HTTP_OK) {

                System.err.println("Failed to create vault item. HTTP " + status +
                        " body: " + body);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    
    public static void updateVaultItem(UUID itemId, VaultItemModel item) {
        UUID userId = AuthSession.getInstance().getUserId();

        try {
            HttpURLConnection conn = openAuthorizedConnection(
                    BASE_URL + "/vault/" + itemId + "?userId=" + userId,
                    "PUT"
            );

            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String json =
                "{ \"title\":\"" + escapeJson(item.getTitle()) + "\"," +
                "\"username\":\"" + escapeJson(item.getUsername()) + "\"," +
                "\"secret\":\"" + escapeJson(item.getSecret()) + "\" }";

            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }

            int status = conn.getResponseCode();
            String body = readBody(status, conn);

            if (status != HttpURLConnection.HTTP_OK) {
                System.err.println("Failed to update vault item. HTTP " + status +
                        " body: " + body);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public static void deleteVaultItem(UUID itemId) {
        UUID userId = AuthSession.getInstance().getUserId();

        try {
            HttpURLConnection conn = openAuthorizedConnection(
                    BASE_URL + "/vault/" + itemId + "?userId=" + userId,
                    "DELETE"
            );

            int status = conn.getResponseCode();
            String body = readBody(status, conn);

            if (status != HttpURLConnection.HTTP_NO_CONTENT &&
                status != HttpURLConnection.HTTP_OK) {

                System.err.println("Failed to delete vault item. HTTP " + status +
                        " body: " + body);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //get secret field
    public static String getVaultItemSecret(UUID itemId) {
        try {
            HttpURLConnection conn = openAuthorizedConnection(
                    BASE_URL + "/vault/" + itemId + "/secret",
                    "GET"
            );

            int status = conn.getResponseCode();
            String body = readBody(status, conn);

            if (status != HttpURLConnection.HTTP_OK) {
                System.err.println("Failed to fetch secret. HTTP " + status +
                        " body: " + body);
                return null;
            }

            return extractJsonField(body, "secret");

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }


    public static List<BackupModel> getBackups(UUID userId) {
        try {
            HttpURLConnection conn = openAuthorizedConnection(
                    BASE_URL + "/backup",
                    "GET"
            );

            int status = conn.getResponseCode();
            String body = readBody(status, conn);

            if (status != HttpURLConnection.HTTP_OK) {
                System.err.println("Failed to load backups. HTTP " + status +
                        " body: " + body);
                return Collections.emptyList();
            }

            return parseBackups(body);

        } catch (Exception ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }

    //parsers
    private static List<VaultItemModel> parseVaultItems(String json) {

        List<VaultItemModel> list = new ArrayList<>();
        if (json == null || json.isBlank()) return list;

        json = json.trim();

        if (!json.startsWith("[")) {
            System.err.println("Invalid vault items JSON: " + json);
            return list;
        }

       
        String inner = json.substring(1, json.length() - 1).trim();
        if (inner.isEmpty()) return list;

        String[] objects = inner.split("\\},\\{");

        for (String obj : objects) {

            if (!obj.startsWith("{")) obj = "{" + obj;
            if (!obj.endsWith("}")) obj = obj + "}";

            String idStr = extractJsonField(obj, "id");
            String title = extractJsonField(obj, "title");
            String username = extractJsonField(obj, "username");

            if (idStr == null) continue;

            VaultItemModel model = new VaultItemModel();
            model.setId(UUID.fromString(idStr));
            model.setTitle(title != null ? title : "");
            model.setUsername(username != null ? username : "");
            model.setSecret("(hidden)");

            list.add(model);
        }

        return list;
    }

    private static List<BackupModel> parseBackups(String json) {
        return new ArrayList<>();
    }

  
    private static HttpURLConnection openAuthorizedConnection(String urlStr, String method) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);

        if (authToken == null || authToken.isBlank()) {
            String sessionToken = AuthSession.getInstance().getToken();
            if (sessionToken != null && !sessionToken.isBlank()) {
                authToken = sessionToken;
            }
        }

        if (authToken != null && !authToken.isBlank()) {
            conn.setRequestProperty("Authorization", "Bearer " + authToken);
        }

        return conn;
    }

    private static String readBody(int status, HttpURLConnection conn) {
        try {
            InputStream is =
                    (status >= 200 && status < 300)
                            ? conn.getInputStream()
                            : conn.getErrorStream();

            if (is == null) return null;

            byte[] bytes = is.readAllBytes();
            return new String(bytes, StandardCharsets.UTF_8);

        } catch (IOException ex) {
            return null;
        }
    }

    private static String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private static String extractJsonField(String json, String field) {
        if (json == null) return null;

        String pattern = "\"" + field + "\"";
        int idx = json.indexOf(pattern);
        if (idx < 0) return null;

        int colon = json.indexOf(':', idx + pattern.length());
        if (colon < 0) return null;

        int firstQuote = json.indexOf('"', colon + 1);
        if (firstQuote < 0) return null;

        int secondQuote = json.indexOf('"', firstQuote + 1);
        if (secondQuote < 0) return null;

        return json.substring(firstQuote + 1, secondQuote);
    }
}

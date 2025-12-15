package com.homevault.server.auth;

//authentication endpoints
public final class AuthDtos {

    private AuthDtos() {
        // utility holder, no instances
    }

    //carries the user’s registration information from the client to server
    public static record RegisterRequest(
            String email,
            String displayName,
            String password
    ) {}

    //carries the user’s log in information from the client to server
    public static record LoginRequest(
            String email,
            String password
    ) {}

    //sends authentication data back to the client
    public static record AuthResponse(
            String token,
            String email,
            String displayName
    ) {}
}

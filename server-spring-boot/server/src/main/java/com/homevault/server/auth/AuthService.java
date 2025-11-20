package com.homevault.server.auth;

import com.homevault.server.user.User;
import com.homevault.server.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Service
public class AuthService {

    private final UserRepository users;

    public AuthService(UserRepository users) {
        this.users = users;
    }

    public AuthResponse register(RegisterRequest req) {
        //if email already used, error
        if (users.findByEmail(req.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
        }

        User u = new User();
        u.setEmail(req.getEmail());
        //password hash
        u.setPasswordHash(req.getPassword());
        u.setDisplayName(req.getDisplayName());

        u = users.save(u);

        AuthResponse resp = new AuthResponse();
        resp.setUserId(u.getId());
        resp.setEmail(u.getEmail());
        resp.setDisplayName(u.getDisplayName());
        return resp;
    }

    public AuthResponse login(LoginRequest req) {
        User u = users.findByEmail(req.getEmail())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        //compare plain passwords to hashed password
        if (!Objects.equals(u.getPasswordHash(), req.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        AuthResponse resp = new AuthResponse();
        resp.setUserId(u.getId());
        resp.setEmail(u.getEmail());
        resp.setDisplayName(u.getDisplayName());
        return resp;
    }
}

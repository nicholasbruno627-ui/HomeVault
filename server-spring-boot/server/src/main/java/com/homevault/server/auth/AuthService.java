package com.homevault.server.auth;

import com.homevault.server.user.User;
import com.homevault.server.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

  private final UserRepository users;
  private final JWTService jwt;

  public AuthService(UserRepository users, JWTService jwt) {
    this.users = users;
    this.jwt = jwt;
  }

  @Transactional
  public AuthResponse register(RegisterRequest req) {
    if (req.getEmail() == null || req.getPassword() == null || req.getName() == null) {
      throw new AuthException("Missing required fields");
    }

    String email = req.getEmail().trim().toLowerCase();
    String displayName = req.getName().trim();
    String password = req.getPassword(); // plain text for demo

    users.findByEmailIgnoreCase(email).ifPresent(u -> {
      throw new AuthException("Email already in use");
    });

    User u = new User();
    u.setEmail(email);
    u.setDisplayName(displayName);
    u.setPasswordHash(password);

    User saved = users.save(u);

    String token = jwt.generateToken(saved.getId(), saved.getEmail(), saved.getDisplayName());

    return new AuthResponse(
        saved.getId(),
        saved.getEmail(),
        saved.getDisplayName(),
        token
    );
  }

  @Transactional(readOnly = true)
  public AuthResponse login(LoginRequest req) {
    if (req.getEmail() == null || req.getPassword() == null) {
      throw new AuthException("Missing credentials");
    }

    String email = req.getEmail().trim().toLowerCase();
    String password = req.getPassword();

    User u = users
        .findByEmailIgnoreCase(email)
        .orElseThrow(() -> new AuthException("Invalid credentials"));

    if (!password.equals(u.getPasswordHash())) {
      throw new AuthException("Invalid credentials");
    }

    String token = jwt.generateToken(u.getId(), u.getEmail(), u.getDisplayName());

    return new AuthResponse(
        u.getId(),
        u.getEmail(),
        u.getDisplayName(),
        token
    );
  }
}

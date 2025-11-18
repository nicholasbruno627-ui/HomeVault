package com.homevault.server.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin //allows javafx to call the auth controller
public class AuthController {

  private final AuthService auth;

  public AuthController(AuthService auth) {
    this.auth = auth;
  }

  @PostMapping("/register")
  public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
    AuthResponse resp = auth.register(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(resp);
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
    AuthResponse resp = auth.login(request);
    return ResponseEntity.ok(resp);
  }

  //test /me
  @GetMapping("/me")
  public ResponseEntity<String> me() {
    return ResponseEntity.ok("Auth is not using sessions/tokens right now.");
  }
}

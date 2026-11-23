package com.geof.app.controller;
import com.geof.app.dto.AuthDtos.*; import com.geof.app.model.User; import com.geof.app.repository.UserRepository; import com.geof.app.security.JwtService;
import jakarta.validation.Valid; import org.springframework.beans.factory.annotation.Value; import org.springframework.http.*; import org.springframework.security.authentication.*; import org.springframework.security.core.Authentication; import org.springframework.security.crypto.password.PasswordEncoder; import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/auth") @CrossOrigin
public class AuthController {
  private final AuthenticationManager authManager; private final UserRepository users; private final PasswordEncoder encoder; private final JwtService jwt; private final long exp;
  public AuthController(AuthenticationManager a, UserRepository u, PasswordEncoder e, JwtService j, @Value("${app.jwt.expiration-ms}") long exp){authManager=a; users=u; encoder=e; jwt=j; this.exp=exp;}
  // POST /api/auth/login  -> for EXTERNAL apps + web UI
  @PostMapping("/login") public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req){
    try{ Authentication auth=authManager.authenticate(new UsernamePasswordAuthenticationToken(req.username(), req.password())); }
    catch(BadCredentialsException ex){ return ResponseEntity.status(401).body(new MessageResponse("Invalid username or password")); }
    User u=users.findByUsername(req.username()).orElseThrow();
    return ResponseEntity.ok(new AuthResponse(jwt.generate(u.getUsername(),u.getRole()),"Bearer",u.getUsername(),u.getRole(),exp));
  }
  // POST /api/auth/register -> create user (hashes password). Disable in prod or restrict to ADMIN.
  @PostMapping("/register") public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req){
    if(users.existsByUsername(req.username())) return ResponseEntity.badRequest().body(new MessageResponse("Username already taken"));
    User u=new User(); u.setUsername(req.username()); u.setEmail(req.email()); u.setPassword(encoder.encode(req.password())); u.setRole("USER"); users.save(u);
    return ResponseEntity.status(201).body(new MessageResponse("User created. Now POST /api/auth/login"));
  }
  // POST /api/auth/logout -> JWT is stateless, client just deletes token. This endpoint lets UI/external apps confirm + (optional) add to blocklist later.
  @PostMapping("/logout") public ResponseEntity<MessageResponse> logout(){ return ResponseEntity.ok(new MessageResponse("Logged out. Delete the token client-side.")); }
  // GET /api/auth/me -> validate token + get profile. External apps call this to verify a token.
  @GetMapping("/me") public ResponseEntity<?> me(Authentication auth){
    if(auth==null) return ResponseEntity.status(401).body(new MessageResponse("No token"));
    User u=users.findByUsername(auth.getName()).orElseThrow();
    return ResponseEntity.ok(new MeResponse(u.getId(),u.getUsername(),u.getEmail(),u.getRole()));
  }
}

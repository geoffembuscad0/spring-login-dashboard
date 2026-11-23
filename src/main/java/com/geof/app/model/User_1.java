package com.geof.app.model;
import jakarta.persistence.*; import java.time.LocalDateTime;
@Entity @Table(name="users") // <-- maps to YOUR existing table. Rename if yours differs.
public class User {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(nullable=false, unique=true, length=100) private String username; // or email
  @Column(nullable=false, unique=true, length=150) private String email;
  @Column(nullable=false) private String password; // BCrypt hash, NEVER plain text
  @Column(length=50) private String role = "USER"; // USER, ADMIN
  @Column(name="created_at") private LocalDateTime createdAt = LocalDateTime.now();
  // getters/setters
  public Long getId(){return id;} public void setId(Long id){this.id=id;}
  public String getUsername(){return username;} public void setUsername(String u){this.username=u;}
  public String getEmail(){return email;} public void setEmail(String e){this.email=e;}
  public String getPassword(){return password;} public void setPassword(String p){this.password=p;}
  public String getRole(){return role;} public void setRole(String r){this.role=r;}
  public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime c){this.createdAt=c;}
}

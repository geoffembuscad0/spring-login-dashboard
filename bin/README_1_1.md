# Spring Boot Login + Dashboard + External Auth API
## Run
1. Update `src/main/resources/application.properties` with YOUR MySQL DB (url/user/pass). If your table is not named `users`, change `@Table(name="users")` in `User.java` and column names to match.
2. Set env: `JWT_SECRET=<64+ char random>` (or keep dev default, change in prod)
3. `mvn spring-boot:run` -> http://localhost:8080/login.html
## External API (for other apps)
- POST /api/auth/register {username,email,password} -> 201
- POST /api/auth/login {username,password} -> {token, tokenType:"Bearer", username, role, expiresInMs}
- GET  /api/auth/me  Header: Authorization: Bearer <token> -> validates token + returns profile
- POST /api/auth/logout Header: Authorization: Bearer <token> -> client deletes token (JWT is stateless)
- GET  /api/dashboard/summary Header: Bearer <token> -> protected example
## Curl
curl -X POST http://localhost:8080/api/auth/login -H 'Content-Type: application/json' -d '{"username":"geof","password":"secret123"}'
curl http://localhost:8080/api/auth/me -H 'Authorization: Bearer <TOKEN>'
## Notes
- Passwords are BCrypt (12 rounds). Never store plain text. If your existing users have plain/MD5 passwords, migrate: on first login verify old hash, then re-hash with BCrypt.
- JWT expiry default 24h via JWT_EXPIRATION_MS.
- For true server-side logout/revocation, add a token blocklist table (jti + expiry) - ask me and I'll add it.

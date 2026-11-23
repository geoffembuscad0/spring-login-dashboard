-- ONLY for local testing. NEVER ship this to prod.
-- Password is BCrypt(12) of 'secret123' - generate your own with: https://bcrypt-generator.com (cost 12)
-- Or via Spring: new BCryptPasswordEncoder(12).encode("secret123")
INSERT INTO users (username, email, password, role)
VALUES ('geof', 'geof@test.local', '$2a$12$R9h/cIPz0gi.URNNX3kh2OPST9/PgBkqquzi.Sw06UzvS9qOv6a', 'ADMIN')
ON DUPLICATE KEY UPDATE username=username;
-- Login with: geof / secret123

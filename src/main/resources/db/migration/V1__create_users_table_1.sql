-- Equivalent of Laravel: php artisan make:migration create_users_table
-- Laravel: Schema::create('users', fn(Blueprint $t){ $t->id(); $t->string('username')->unique(); ... })
-- Flyway runs this automatically on startup, in order V1 -> V2 -> ...
CREATE TABLE IF NOT EXISTS users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(100) NOT NULL UNIQUE,
  email VARCHAR(150) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL COMMENT 'BCrypt hash, never plain text',
  role VARCHAR(50) NOT NULL DEFAULT 'USER',
  created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_users_username (username),
  INDEX idx_users_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

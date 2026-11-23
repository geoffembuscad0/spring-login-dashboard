# Login & Dashboard — Spring Boot + JWT Auth API

![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3.4-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-JWT-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Flyway](https://img.shields.io/badge/Flyway-Migrations-CC0200?style=for-the-badge&logo=flyway&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-Build-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)
![PRs Welcome](https://img.shields.io/badge/PRs-Welcome-brightgreen?style=for-the-badge)

A responsive login + empty dashboard web app built with **Java 17 / Spring Boot 3 / Spring Security / MySQL**,
with a reusable **Login & Logout API** that other external applications can use.

> **Web UI and external apps share the same auth API.** No duplicate login logic.

---

## ✨ What You Get

| Feature | Details |
|---|---|
| 🔐 Login UI | Responsive login page at `/login.html` |
| 📊 Dashboard UI | Responsive sidebar navigation at `/dashboard.html` — collapses to hamburger + overlay on mobile |
| 🔑 Auth API | `POST /api/auth/login`, `POST /api/auth/logout`, `GET /api/auth/me`, `POST /api/auth/register` |
| 🛡️ JWT | Stateless Bearer tokens, BCrypt (12 rounds) password hashing |
| 🗄️ Migrations | Flyway — the Laravel-migrations equivalent for Java (see `MIGRATIONS.md`) |
| 🧪 Safe Testing | Separate `test` profile + Docker MySQL so you never touch your real DB |

---

## 📁 Project Structure

```
login-dashboard/
├── README.md                          ← You are here
├── MIGRATIONS.md                      ← Laravel vs Flyway cheat sheet
├── docker-compose.yml                ← Local test MySQL (Docker)
├── pom.xml                            ← Maven dependencies (Spring Boot, Security, JPA, Flyway, JWT)
└── src/main/
    ├── java/com/geof/app/
    │   ├── LoginDashboardApplication.java
    │   ├── model/User.java            ← Maps to YOUR existing `users` table
    │   ├── repository/UserRepository.java
    │   ├── security/
    │   │   ├── JwtService.java        ← Create / validate JWT tokens
    │   │   ├── JwtAuthFilter.java     ← Reads `Authorization: Bearer <token>`
    │   │   └── SecurityConfig.java    ← Who can access what, CORS
    │   ├── controller/
    │   │   ├── AuthController.java    ← /api/auth/* — login, logout, me, register
    │   │   └── DashboardController.java ← /api/dashboard/summary — empty widgets, extend me
    │   └── dto/AuthDtos.java
    └── resources/
        ├── application.properties     ← PROD / default config (your real MySQL)
        ├── application-test.properties← TEST config (throwaway test DB)
        ├── db/migration/
        │   ├── V1__create_users_table.sql
        │   └── V2__seed_test_user.sql ← TEST ONLY — delete before prod
        └── static/
            ├── login.html
            ├── dashboard.html
            └── css/app.css
```

---

## ✅ Prerequisites

Before you start, make sure you have:

| Tool | Version | Check it with | Download if missing |
|---|---|---|---|
| Java (JDK) | 17 or newer | `java -version` | https://adoptium.net |
| Maven | 3.8+ | `mvn -version` | https://maven.apache.org/download.cgi |
| Docker Desktop *(recommended for testing)* | Any recent | `docker --version` | https://www.docker.com/products/docker-desktop/ |
| Git *(optional)* | Any | `git --version` | https://git-scm.com |

> **No Docker?** You can also use a local MySQL you already have installed — just point the
> test config at it (see Step 2, Option B).

---

## 🚀 How to Run Locally (Step-by-Step)

### Step 1 — Download & Open the Project

```bash
# If you got this as a .zip, unzip it, then:
cd login-dashboard
```

Open it in your IDE (IntelliJ IDEA / VS Code / Eclipse).

### Step 2 — Start a Test Database (Pick ONE option)

#### ✅ Option A — Docker (Recommended, isolated, nothing to install)

```bash
docker compose up -d
```

This starts a throwaway MySQL on your machine:

| Setting | Value |
|---|---|
| Host / Port | `localhost:3307` |
| Database | `login_dashboard_test` |
| Username | `root` |
| Password | `yourpassword` |

Check it's running:

```bash
docker ps
# You should see: login-dashboard-mysql-test
```

> This DB is **only for testing**. Your real MySQL is never touched.

#### Option B — Use Your Own Local MySQL

If you already have MySQL running locally, create a test DB manually:

```sql
CREATE DATABASE login_dashboard_test;
```

Then edit `src/main/resources/application-test.properties` so the URL matches
your MySQL (port `3306` instead of `3307`, your own username/password).

### Step 3 — Run the App in TEST Mode

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=test
```

Wait until you see something like:

```
Started LoginDashboardApplication in 3.2 seconds
Flyway: Migrating schema to version 1 - create users table
Flyway: Migrating schema to version 2 - seed test user
Tomcat started on port 8080
```

**What just happened?**

1. Flyway ran `V1__create_users_table.sql` → created the `users` table in the test DB.
2. Flyway ran `V2__seed_test_user.sql` → inserted one test user.
3. The app started on `http://localhost:8080`.

> If port `8080` is already in use, stop the other app or change
> `server.port=8080` in `application.properties`.

### Step 4 — Log In via the Web UI

1. Open your browser: **http://localhost:8080/login.html**
2. Log in with the seeded test account:

   | Field | Value |
   |---|---|
   | Username | `geof` |
   | Password | `secret123` |

3. You should land on **http://localhost:8080/dashboard.html** — sidebar on the left,
   empty dashboard cards in the middle.
4. Click **Logout** — you return to the login page and your token is deleted.

**Try it on mobile width:** shrink your browser window below ~820px — the sidebar
collapses into a hamburger menu (☰) with a dark overlay. Tap it to open/close.

### Step 5 — Test the Auth API (For External Apps)

The same login powers external apps. Use `curl`, Postman, or any HTTP client.

#### 5a. Login → Get a Token

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"username":"geof","password":"secret123"}'
```

Response:

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "tokenType": "Bearer",
  "username": "geof",
  "role": "ADMIN",
  "expiresInMs": 3600000
}
```

Copy the `token` value. In test mode it expires in **1 hour**.

#### 5b. Validate the Token → Get Profile

External apps call this to check if a token is still valid:

```bash
curl http://localhost:8080/api/auth/me \
  -H 'Authorization: Bearer PASTE_TOKEN_HERE'
```

Response:

```json
{
  "id": 1,
  "username": "geof",
  "email": "geof@test.local",
  "role": "ADMIN"
}
```

If the token is missing / expired, you get `401 Unauthorized`.

#### 5c. Call a Protected Endpoint

```bash
curl http://localhost:8080/api/dashboard/summary \
  -H 'Authorization: Bearer PASTE_TOKEN_HERE'
```

#### 5d. Logout

```bash
curl -X POST http://localhost:8080/api/auth/logout \
  -H 'Authorization: Bearer PASTE_TOKEN_HERE'
```

> **JWT is stateless**, so logout means: the client deletes the token.
> The server confirms logout, but does not store a session to destroy.
> If you need true server-side revocation (logout from all devices / admin kick),
> add a token blocklist — ask for it and it can be added.

#### 5e. Register a New User (Optional)

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H 'Content-Type: application/json' \
  -d '{"username":"juan","email":"juan@test.local","password":"secret123"}'
```

Then log in as `juan` with the same login call above.

### Step 6 — Stop Everything

In the terminal where the app is running, press:

```
Ctrl + C
```

To stop the test database:

```bash
docker compose down
# Add -v to also delete the test data: docker compose down -v
```

---

## 🔌 API Reference (For External Applications)

Base URL (local): `http://localhost:8080`

| Method | Endpoint | Auth Required? | Description |
|---|---|---|---|
| `POST` | `/api/auth/register` | No | Create a user. Body: `{username, email, password}` (min 8 chars). Password is BCrypt-hashed. |
| `POST` | `/api/auth/login` | No | Log in. Body: `{username, password}`. Returns JWT token. |
| `GET` | `/api/auth/me` | Yes — `Bearer <token>` | Validate token + return profile. |
| `POST` | `/api/auth/logout` | Yes — `Bearer <token>` | Confirm logout. Client must delete the token. |
| `GET` | `/api/dashboard/summary` | Yes — `Bearer <token>` | Example protected endpoint. Extend with real widgets. |

**How external apps must call protected endpoints:**

```
Authorization: Bearer <TOKEN>
```

Example (JavaScript / fetch):

```js
const res = await fetch('http://localhost:8080/api/auth/me', {
  headers: { 'Authorization': 'Bearer ' + token }
});
```

---

## 🗄️ Migrations — Laravel Users, Read This

This project does **not** use Laravel (PHP) migrations. It uses **Flyway**, which is the
same concept for Java. Full comparison in [`MIGRATIONS.md`](MIGRATIONS.md).

| Laravel | This Project (Flyway) |
|---|---|
| `php artisan make:migration ...` | Create `V3__add_avatar_to_users.sql` in `src/main/resources/db/migration/` |
| `php artisan migrate` | Just run the app — Flyway auto-runs on startup |
| `php artisan migrate:rollback` | Write a new forward migration to undo it |
| `database/migrations/*.php` | `src/main/resources/db/migration/V*.sql` |

**Rules:**

1. Name files `V{number}__{description}.sql` — e.g. `V3__add_avatar_to_users.sql`
2. **Never edit** a migration that has already run on a shared / prod DB. Add a new one.
3. Example new migration:

   ```sql
   -- V3__add_avatar_to_users.sql
   ALTER TABLE users ADD COLUMN avatar_url VARCHAR(255) NULL AFTER email;
   ```

---

## 🏭 Connecting to YOUR Real / Existing MySQL `users` Table

The test setup above uses a throwaway DB. When you're ready for your real DB:

### 1. Edit `src/main/resources/application.properties`

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/your_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Manila
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=validate
spring.flyway.enabled=true
```

> `ddl-auto=validate` means JPA will **not** alter your tables. Flyway owns the schema.

### 2. Match `User.java` to Your Real Table

In `src/main/java/com/geof/app/model/User.java`:

```java
@Entity
@Table(name="users") // ← change if yours is e.g. tbl_users / app_users
public class User {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(unique=true) private String username;
  @Column(unique=true) private String email;
  private String password; // must be a BCrypt hash
  private String role;
}
```

Rename the table / columns to match yours exactly.

### 3. If You Already Have a `users` Table — Do NOT Run V1 Against It

`V1__create_users_table.sql` will conflict with an existing table.

Instead, create a baseline migration that only **adds missing columns**, e.g.:

```sql
-- V1__baseline_existing_users.sql (replace V1 for existing DBs)
ALTER TABLE users ADD COLUMN IF NOT EXISTS role VARCHAR(50) NOT NULL DEFAULT 'USER';
-- Add only what is missing. Do not recreate the table.
```

If you share `SHOW CREATE TABLE users;`, the exact baseline migration can be written for you.

### 4. Passwords Must Be BCrypt

Existing users with plain-text / MD5 passwords will **not** be able to log in until migrated.
Safe migration pattern: on first login, verify against the old hash, then re-hash with
BCrypt and save. Ask for this migration code if your current passwords are not BCrypt.

### 5. Set a Real JWT Secret in Prod

```bash
export JWT_SECRET=<at-least-64-characters-random-secret>
export JWT_EXPIRATION_MS=86400000
```

Never commit the dev default secret to a real deployment.

### 6. Delete the Test Seed Before Prod

Delete `src/main/resources/db/migration/V2__seed_test_user.sql` — it contains a known
test login (`geof / secret123`) and must never ship to production.

---

## 🧪 Troubleshooting

| Problem | Fix |
|---|---|
| `Port 8080 already in use` | Stop the other app, or change `server.port=8080` in `application.properties` |
| `Access denied for user 'root'` | Wrong MySQL user/password in `application-test.properties` / `application.properties` |
| `Unknown database 'login_dashboard_test'` | With Docker, it auto-creates. Without Docker, run `CREATE DATABASE login_dashboard_test;` |
| `Flyway: Found non-empty schema without history table` | Your DB already has tables but no Flyway history. Either use a fresh test DB, or set `spring.flyway.baseline-on-migrate=true` for a one-time baseline |
| Login returns `401 Invalid username or password` | Check username/password. Test login is `geof / secret123`. Passwords are case-sensitive. |
| `/api/auth/me` returns `401` | Token missing, expired, or `Authorization` header not exactly `Bearer <token>` |
| `docker: command not found` | Install Docker Desktop, or use Option B (your own local MySQL) |
| Frontend shows blank / 404 | Make sure you opened `/login.html`, not just `/` — and that the app is running |

---

## 🔒 Security Notes

- Passwords are hashed with **BCrypt (cost 12)**. Never store plain-text passwords.
- JWT secret must be long + random in prod — keep it in an environment variable, not in code.
- Test seed user (`geof / secret123`) is **test-only**. Delete `V2__seed_test_user.sql` before prod.
- CORS allowed origins are set in `application.properties` (`app.cors.allowed-origins`). Restrict to your real frontend domains in prod.

---

## ➡️ What to Build Next

The dashboard is intentionally empty. Good next steps:

1. Wire real widgets in `DashboardController.summary()` — e.g. `UserRepository.count()`
2. Add role-based sidebar items — `ADMIN` sees Users/Settings, `USER` does not
3. Add token blocklist / refresh tokens for true server-side logout
4. Add password migration for existing plain-text / MD5 users

---

## 🤝 Contributing

Want to help improve this project? See **[CONTRIBUTING.md](CONTRIBUTING.md)** — it covers how to set up, branch, commit, test, and open a PR, plus the code style and security rules.

---

## 📄 License

Free to use for your own projects. Adapt as needed.

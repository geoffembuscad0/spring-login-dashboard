# Contributing to Login & Dashboard

Thanks for wanting to contribute! This guide is for anyone who wants to fix a bug,
add a feature, improve docs, or just try the project and report what breaks.

> **New here?** Start with the [README](README.md) — it has the full step-by-step
> to run the project locally. This file is about *how to contribute back*.

---

## Table of Contents

- [Code of Conduct](#-code-of-conduct)
- [Quick Start for Contributors](#-quick-start-for-contributors)
- [How to Contribute](#-how-to-contribute)
- [Branch & Commit Rules](#-branch--commit-rules)
- [Pull Request Checklist](#-pull-request-checklist)
- [Code Style](#-code-style)
- [Testing](#-testing)
- [Database / Migration Rules](#-database--migration-rules)
- [Security Rules — Read Before You Code](#-security-rules--read-before-you-code)
- [Reporting Bugs](#-reporting-bugs)
- [Requesting Features](#-requesting-features)
- [Questions?](#-questions)

---

## 🤝 Code of Conduct

- Be kind and respectful. No harassment, insults, or discrimination.
- Assume good intent. Give clear, constructive feedback.
- Stay on topic — keep PRs focused on one change.
- If you disagree, disagree with the idea, not the person.

If someone violates this, report it to the maintainer (the repo owner) privately.

---

## ⚡ Quick Start for Contributors

You need: **Java 17+, Maven, Docker Desktop** (for the test DB).

```bash
# 1. Fork the repo on GitHub, then clone YOUR fork
git clone https://github.com/<your-username>/login-dashboard.git
cd login-dashboard

# 2. Add the original repo as upstream (so you can stay in sync)
git remote add upstream https://github.com/<original-owner>/login-dashboard.git

# 3. Start the test database (throwaway, never your real DB)
docker compose up -d

# 4. Run in TEST mode
mvn spring-boot:run -Dspring-boot.run.profiles=test

# 5. Open http://localhost:8080/login.html
#    Login: geof / secret123
```

> Full details, troubleshooting, and API examples are in the [README](README.md).

---

## 🛠️ How to Contribute

### 1. Find or Create an Issue

- Check existing **Issues** first — your bug / idea may already be there.
- If not, open a new issue with a clear title and what you want to change / fix.
- For a first contribution, look for issues labelled `good first issue` or `help wanted`.

### 2. Sync Your Fork

```bash
git checkout main
git pull upstream main
git push origin main
```

### 3. Create a Branch (Never Work Directly on `main`)

```bash
git checkout -b feat/add-user-search
# or
git checkout -b fix/login-401-error
```

See [Branch & Commit Rules](#-branch--commit-rules) for naming.

### 4. Make Your Change

- Keep it small and focused — one PR = one feature / one fix.
- Update docs (`README.md` / `MIGRATIONS.md`) if your change affects setup, API, or DB.
- Add a Flyway migration if you change the DB schema — see [Migration Rules](#-database--migration-rules).

### 5. Test It

```bash
# Run the app in test mode and manually verify
mvn spring-boot:run -Dspring-boot.run.profiles=test

# If tests exist, run them
mvn test
```

Manually check at minimum:
- [ ] Login works in the UI (`geof / secret123`)
- [ ] Dashboard loads
- [ ] Logout returns you to login
- [ ] `POST /api/auth/login` returns a token
- [ ] `GET /api/auth/me` with that token returns the profile

### 6. Commit, Push & Open a PR

```bash
git add .
git commit -m "feat: add user search to dashboard"
git push origin feat/add-user-search
```

Then open a Pull Request on GitHub from your branch → `main`.
Fill in the PR template / checklist below.

---

## 🌿 Branch & Commit Rules

### Branch names

| Type | Format | Example |
|---|---|---|
| Feature | `feat/<short-description>` | `feat/add-user-search` |
| Bug fix | `fix/<short-description>` | `fix/login-401-error` |
| Docs | `docs/<short-description>` | `docs/update-readme` |
| Chore / refactor | `chore/<short-description>` | `chore/upgrade-spring-boot` |

Use lowercase, hyphens, no spaces.

### Commit messages — Conventional Commits

```
<type>: <short description in present tense>
```

| Type | Use for |
|---|---|
| `feat:` | New feature |
| `fix:` | Bug fix |
| `docs:` | Docs only (README, CONTRIBUTING, comments) |
| `chore:` | Build, deps, config, no code change |
| `refactor:` | Code change, no behaviour change |
| `test:` | Adding / fixing tests |

Examples:

```bash
git commit -m "feat: add avatar column to users table"
git commit -m "fix: return 401 instead of 500 on bad login"
git commit -m "docs: clarify Docker setup in README"
```

Keep the first line under ~72 characters. Add a longer body if needed:

```bash
git commit -m "feat: add token blocklist" -m "Adds a revoked_tokens table. Logout now revokes the token server-side."
```

---

## ✅ Pull Request Checklist

Before you mark your PR ready, check:

- [ ] My branch is up to date with `main` (`git pull upstream main`)
- [ ] My change is focused — one feature / fix per PR
- [ ] I tested locally in TEST mode (`mvn spring-boot:run -Dspring-boot.run.profiles=test`)
- [ ] Login / dashboard / logout still work
- [ ] Auth API still works (`/api/auth/login`, `/api/auth/me`, `/api/auth/logout`)
- [ ] If I changed the DB, I added a **new** Flyway migration (did NOT edit an old one)
- [ ] If I changed setup / API / config, I updated `README.md`
- [ ] I did NOT commit secrets, passwords, `.env`, or real DB credentials
- [ ] I did NOT commit the test seed user to a prod migration, or real user data
- [ ] My code follows the [Code Style](#-code-style) below

**PR title:** use the same Conventional Commits style, e.g. `feat: add user search to dashboard`

**PR description should include:**

1. **What** changed and **why**
2. **How to test** (exact steps / curl commands)
3. **Screenshots** if you changed the UI (desktop + mobile width if possible)
4. Any **breaking changes** / migration notes

### Review process

- A maintainer will review, ask questions, or request changes.
- Please respond to review comments — push new commits to the same branch, don't open a new PR.
- Once approved, the maintainer will merge. Please don't merge your own PR unless asked to.

---

## 🎨 Code Style

### Java

- Java 17, Spring Boot 3 conventions.
- 2-space or 4-space indent — match the file you're editing, stay consistent in new files.
- Class / method names: `PascalCase` for classes, `camelCase` for methods/variables.
- Keep controllers thin — business logic belongs in a service / repository, not in the controller.
- Use `record` for DTOs where it makes sense (see `AuthDtos.java`).
- No `System.out.println` in production code — use a logger if you need logging.

### Frontend (HTML / CSS / JS in `src/main/resources/static/`)

- Keep it dependency-free (plain HTML/CSS/JS) unless the PR discusses adding a framework.
- Responsive first: test at desktop **and** at < 820px width (sidebar must collapse).
- No inline secrets / tokens in JS. Tokens live in `localStorage` only for this demo app.

### SQL / Flyway

- One migration = one logical change.
- Use `IF NOT EXISTS` / `ADD COLUMN IF NOT EXISTS` defensively where MySQL supports it.
- Always include a clear comment at the top of the migration.

---

## 🧪 Testing

Currently the project is verified by manual testing (see checklist above) + `mvn test` if tests exist.

If you add a feature, please add or update tests where practical:

- **Controller / API tests:** use `@SpringBootTest` + `MockMvc` or `TestRestTemplate`.
- **Security tests:** verify unauthenticated requests to `/api/dashboard/*` get `401`, and bad logins get `401` (not `500`).
- **Migration tests:** run the app on a fresh test DB and confirm Flyway migrates `V1 → V2 → …` cleanly.

Never run tests against a real / production database. Always use the `test` profile + Docker test DB.

---

## 🗄️ Database / Migration Rules

This project uses **Flyway** — the Laravel-migrations equivalent for Java.
Full comparison in [`MIGRATIONS.md`](MIGRATIONS.md).

**Golden rules:**

1. **Never edit** a migration that has already been merged / run on a shared DB.
   Always add a new file: `V{next}__{description}.sql`
2. Name it clearly, e.g. `V3__add_avatar_to_users.sql`
3. One migration = one change.
4. Test it on a fresh test DB:

   ```bash
   docker compose down -v
   docker compose up -d
   mvn spring-boot:run -Dspring-boot.run.profiles=test
   # Flyway should log: Migrating schema to version 3 ...
   ```

Example:

```sql
-- V3__add_avatar_to_users.sql
ALTER TABLE users ADD COLUMN avatar_url VARCHAR(255) NULL AFTER email;
```

---

## 🔒 Security Rules — Read Before You Code

These are non-negotiable. A PR that breaks them will not be merged.

- [ ] **Never commit secrets.** No JWT secrets, DB passwords, API keys, `.env` files.
      Use environment variables (`JWT_SECRET`, etc.).
- [ ] **Never store plain-text passwords.** Always BCrypt-hash. Never log passwords or tokens.
- [ ] **Never commit real user data.** No dumps, no real emails, no production DB exports.
- [ ] **Never ship the test seed user to prod.** `V2__seed_test_user.sql` (`geof / secret123`)
      is test-only. Do not copy it into a prod migration.
- [ ] **Validate input.** Use `@Valid` / Bean Validation on request DTOs.
- [ ] **Keep protected endpoints protected.** Don't add `permitAll()` to `/api/**` without
      discussing it in the PR — explain why it's safe.
- [ ] **Don't weaken CORS / CSRF blindly.** If you change `SecurityConfig`, explain the impact.

If you find a security issue, **do not** open a public issue with exploit details.
Contact the maintainer privately instead.

---

## 🐛 Reporting Bugs

Open an issue with:

```markdown
**What happened:**
**Expected:**
**Steps to reproduce:**
1.
2.
3.

**Environment:** OS / Java version (`java -version`) / Browser / Docker or local MySQL?
**Logs / error message:** (paste the relevant lines, remove secrets)
**Screenshots:** (if UI)
```

---

## 💡 Requesting Features

Open an issue with:

```markdown
**Problem:** What are you trying to do?
**Proposal:** What should change?
**Alternatives:** What else did you consider?
**Would you build it?** Yes / No / Need help
```

---

## ❓ Questions?

- Setup / run problems → check the [README Troubleshooting](README.md#-troubleshooting) first.
- Migrations → check [`MIGRATIONS.md`](MIGRATIONS.md).
- Still stuck? Open an issue labelled `question` — include what you tried and the exact error.

---

Thanks again for contributing! 🙌

# Laravel Migrations vs This Project (Spring + Flyway)
| Laravel (PHP) | This Project (Java) | What it does |
|---|---|---|
| php artisan make:migration create_users_table | Create V1__create_users_table.sql in src/main/resources/db/migration/ | New migration file |
| php artisan migrate | Just run the app - Flyway auto-runs on startup | Applies pending migrations in order |
| php artisan migrate:rollback | NOT automatic - write a new migration to undo (e.g. V3__drop_column.sql) | Flyway Teams/undo aside, forward-only is safest |
| php artisan migrate:fresh | Drop test DB + restart app | Fresh test DB |
| database/migrations/*.php | src/main/resources/db/migration/V*.sql | Migration files |

Naming: V{number}__{description}.sql  e.g. V3__add_avatar_to_users.sql
Rules: NEVER edit a migration already run on shared/prod DB. Always add a new V-file.
Example V3:
  ALTER TABLE users ADD COLUMN avatar_url VARCHAR(255) NULL AFTER email;

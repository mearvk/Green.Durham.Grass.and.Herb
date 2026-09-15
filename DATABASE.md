# Database

The application database is documented as `green_durham_grass_and_herb`.

## Core tables

- `ethical` — ethical concerns
- `labor` — labor concerns
- `moral` — moral concerns
- `mortality` — mortality-related concerns
- `2000_iq` — questions received by Appree
- `listeners_*` — regional connection records

## Sensitive fields

The Appree question record is documented with fields including `remote_address`, `ip`, `dns`, `name`, `geo`, and `national_id`. These fields require access control, retention rules, and appropriate protection at rest.

## Database security

- Use a dedicated application database account.
- Do not grant administrative privileges to the application account.
- Store credentials outside committed XML and source files.
- Use prepared statements or an equivalent parameterized database API.
- Record schema changes as migrations where practical.
- Backups must be encrypted and access-controlled.

## Data lifecycle

Collection should be limited to information necessary for the declared service purpose. Retention and deletion schedules should be documented in `DATA-GOVERNANCE.md` and implemented rather than relying on indefinite database growth.

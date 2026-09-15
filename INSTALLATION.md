# Installation

## Requirements

- JDK 21 or later.
- MySQL 8.x or a compatible supported database configuration.
- Required application dependencies described by the build/install system.
- Optional local GeoLite2 data for geolocation.

## Installation sequence

1. Review `SECURITY.md`, `NETWORK.md`, and `DATA-GOVERNANCE.md`.
2. Configure a dedicated database and least-privilege application account.
3. Supply secrets through the deployment environment or an approved secret store.
4. Review listener exposure and bind addresses.
5. Initialize the database using the authoritative schema/install procedure.
6. Validate configuration before starting listeners.
7. Start the service and verify health, logs, and database connectivity.

## Configuration safety

Production configuration must not contain committed passwords or private keys. XML files should be suitable for templating from environment variables or deployment secrets.

## Installation services

The repository currently contains root and nested installation material. Consolidation should preserve compatibility while establishing `installation/` as the canonical location for maintained installers and migration scripts.

## Verification

An installation is complete only after configuration validation, database connectivity, listener checks, and a controlled service test succeed.

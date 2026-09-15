# Data Governance

Green.Durham.Grass.and.Herb should collect, retain, and disclose operational data according to necessity, security, and documented purpose.

## Data classes

**Operational:** connection metadata, service status, listener events, and diagnostic logs.

**Contact:** public contact and reference material stored under `contacts/`.

**Research:** political, labor, ethical, moral, mortality, and related reference material.

**Sensitive operational data:** names, IP addresses, DNS values, geolocation, NationalID-related values, and question records.

## Rules

1. Collect only what the service needs.
2. Restrict access by role and least privilege.
3. Encrypt sensitive data in transit and protect it at rest.
4. Define retention periods before production use.
5. Do not silently transmit IP information to third parties.
6. Keep research/reference material separate from private operational records.
7. Provide a documented deletion or purge process where appropriate.

## Third-party geolocation

The remote `ip-api.com` fallback is an external disclosure of an IP address. Production deployments should prefer local geolocation data and require an explicit configuration decision before enabling remote lookup.

## Auditability

Changes to collection fields, database schemas, external data processors, retention rules, or access controls should be documented in the repository and reviewed before deployment.

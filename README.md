# Green.Durham.Grass.and.Herb

Durham, North Carolina-focused political, public-contact, labor, research, and Java network-service project. The repository combines the Appree service with organized public-reference material and supporting research.

## Project purpose

The project provides a structured technical and research environment for public-policy, labor, ethical, moral, mortality, contact, and network-service work. Political or research material is informational unless a document explicitly states otherwise; it does not itself confer government authority or administrative access.

## Architecture

- `source-code/` — Java application, Appree service, listeners, and domain components
- `configuration/` — runtime configuration; production secrets stay outside Git
- `contacts/` — public-contact and jurisdictional reference material
- `data/` — operational/reference data and optional GeoLite2 data
- `research/` — research material as the repository is consolidated
- `political/` — political/public-policy material as the repository is consolidated
- `installation/` — canonical maintained installation material
- `install/` — existing installation compatibility area
- `rhetoric/` — project rhetoric/reference logs
- `labor-concerns/` — labor, intelligence, and democracy references

See `ARCHITECTURE.md` for the system model and `CONFIGURATION.md` for deployment rules.

## Network

| Port | Service |
|------:|---------|
| 2000 | Module installation/listener service |
| 20000 | Base Appree contact service |
| 40002 | East Coast listener |
| 40003 | West Coast listener |
| 40007 | Texas listener |
| 49152 | Registration server |

Extended values described by the module documentation (for example 70000 or 99152) are application-level identifiers and are not ordinary TCP/IPv4 ports. See `NETWORK.md`.

## Database

The documented database is `green_durham_grass_and_herb` with ethical, labor, moral, mortality, Appree question, and listener-log tables. Fields such as `ip`, `dns`, `name`, `geo`, and `national_id` require controlled access and documented retention.

See `DATABASE.md` and `DATA-GOVERNANCE.md`.

## Geolocation

Local GeoLite2 data is preferred. A remote `ip-api.com` fallback represents an external disclosure of an IP address and should be disabled by default for privacy-sensitive deployments or enabled only through explicit configuration. See `SECURITY.md`.

## Security

Security controls include least-privilege database access, externalized secrets, controlled listener exposure, authentication/authorization separation, bounded retries, sensitive-data protection, and documented incident response. See `SECURITY.md`.

## Installation

JDK 21+ and the supported database environment are required. Review `INSTALLATION.md` before deployment and validate database connectivity, listener exposure, configuration, and service health after installation.

## Documentation map

- `ARCHITECTURE.md` — system architecture and separation of concerns
- `CONFIGURATION.md` — runtime configuration and secret handling
- `NETWORK.md` — listeners, ports, and exposure policy
- `DATABASE.md` — schema responsibilities and database security
- `DATA-GOVERNANCE.md` — collection, retention, and sensitive-data rules
- `SECURITY.md` — threat model and security controls
- `CONTACTS.md` — contact/reference organization
- `POLITICAL-MODULE.md` — separation of political research from infrastructure
- `INSTALLATION.md` — deployment and verification
- `MODULE.txt` — historical/module integration specification
- `STRUCTURE.txt` — repository structure reference

## Development rule

Documentation is treated as an engineering contract. Changes to listeners, data collection, authentication, database schemas, external services, or installation behavior should update the corresponding specification and tests in the same development cycle.

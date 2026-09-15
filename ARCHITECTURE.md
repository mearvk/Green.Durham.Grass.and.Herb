# Architecture

**Green.Durham.Grass.and.Herb** is organized as a modular Java service with a clear separation between application infrastructure, data, research, political material, contacts, and installation.

## Architectural layers

1. **Application layer** — `source-code/` contains the Java entry point, Appree service, listeners, and domain components.
2. **Configuration layer** — `configuration/` contains runtime XML configuration. Secrets must not be committed.
3. **Database layer** — database schema and migration material belong under `database/` when consolidated; existing `schema.sql` remains authoritative until migration is completed.
4. **Installation layer** — `installation/` and the root installer provide reproducible setup.
5. **Research and political layer** — political, labor, ethical, moral, mortality, and related reference material is informational and must remain distinct from network infrastructure.
6. **Contacts layer** — `contacts/` contains organized public-contact/reference material.
7. **Operational data layer** — `data/` and logs contain runtime or reference data and require explicit retention rules.

## Network model

The current module documents physical listeners at ports 2000, 20000, 40002, 40003, 40007, and 49152. Any extended/virtual port mapping must be treated as an application namespace rather than assumed to be an operating-system TCP port.

The implementation should keep listener registration, application routing, authentication, authorization, and persistence as separate responsibilities.

## Design rule

Documentation is the contract. Code changes should update the relevant architecture, security, network, database, or installation document in the same change when behavior changes.

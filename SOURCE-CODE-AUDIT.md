# Source Code and Runtime Audit

**Repository:** Green.Durham.Grass.and.Herb  
**Branch:** `main`  
**Audit date:** 2026-09-15  
**Owner:** Max Rupplin / MEARVK LLC

## 1. Purpose

This document records the implementation-level review performed against the repository as it exists on `main`. It supplements `ARCHITECTURE.md`, `SECURITY.md`, `NETWORK.md`, `DATABASE.md`, and `INSTALLATION.md`.

The review deliberately distinguishes between **source that is actually present** and source that is described by repository documentation. No missing Java implementation is being invented or substituted.

## 2. Current Findings

### 2.1 Java implementation availability

The repository documentation describes a `source-code/` tree containing `Main.java`, Appree, listeners, labor, ethical, moral, and mortality components. The current `main` tree does not expose those Java source files through the repository contents/code index used for this audit.

This is now treated as a release-integrity issue: documentation must not imply that an executable implementation is present when the corresponding source cannot be retrieved from the branch.

**Action:** restore or identify the authoritative Java source before performing source-level refactoring.

### 2.2 Installation privileges

The existing installer installs MySQL and initializes the database as the local MySQL `root` account. That is appropriate only as an installation/bootstrap operation; the running application should not use `root` as its database identity.

**Required implementation rule:** create a dedicated application database principal with only the privileges required by this module. Production credentials must come from the environment or a protected secret mechanism, not committed XML.

### 2.3 Database configuration

The existing database configuration identifies `root` as the database user. This creates an unnecessary privilege boundary failure even when the password is empty for local development.

The intended model is:

1. bootstrap administrator performs schema creation;
2. installer creates a dedicated application account;
3. runtime receives credentials from protected configuration;
4. runtime account cannot create/drop unrelated databases or administer MySQL;
5. sample configuration contains placeholders only.

### 2.4 Listener configuration

The Appree configuration currently contains enabled East Coast, West Coast, and Texas listeners and disabled Wyoming and Indiana listener entries. These entries must remain explicitly documented as configured/disabled rather than being silently interpreted as active services.

Every listener implementation should validate:

- configured port;
- bind address;
- maximum concurrent connections;
- socket/read timeout;
- maximum request size;
- authentication state;
- shutdown behavior;
- error logging policy.

### 2.5 Network exposure

No listener should become externally reachable merely because a configuration file contains a port number. Binding, firewall exposure, TLS, authentication, and authorization must be explicit deployment decisions.

Extended or virtual port identifiers described by the module documentation are application-level identifiers and must not be confused with TCP/UDP port numbers accepted by the operating system.

## 3. Source-Level Improvement Plan

Once the authoritative Java source is restored, the following changes are the first implementation pass:

### A. Runtime configuration

- Replace hard-coded credentials with environment-backed configuration.
- Validate required configuration at startup.
- Fail closed when security-sensitive configuration is absent.
- Provide safe development defaults only where they cannot expose protected services.

### B. Database access

- Use a dedicated application account.
- Use prepared statements for all variable SQL values.
- Centralize connection creation.
- Configure connection and query timeouts.
- Close connections/statements/result sets deterministically.
- Add bounded retry behavior for transient startup failures.
- Avoid logging credentials or sensitive database values.

### C. Listener lifecycle

- Centralize listener startup/shutdown.
- Make startup idempotent.
- Detect bind failures clearly.
- Install a controlled shutdown hook.
- Stop accepting connections before closing dependent resources.
- Prevent unbounded thread creation.
- Apply connection/request limits.

### D. Input handling

All externally supplied values must be treated as untrusted input, including:

- questions;
- names;
- IP and DNS values;
- NationalID values;
- installer identifiers;
- file names;
- listener requests;
- XML/configuration values.

Inputs require length limits, character validation where appropriate, and safe persistence. No externally supplied value should be used to construct SQL or shell commands directly.

### E. Logging

Logging should use structured levels and must distinguish operational diagnostics from sensitive data. NationalID, credentials, full connection strings, and unnecessary personally identifying information should not appear in ordinary logs.

### F. Geolocation

Local GeoLite2 data should remain the preferred lookup mechanism. Any remote geolocation provider must be opt-in, timeout-limited, documented, and treated as a third-party disclosure of the queried network address.

### G. Authentication and authorization

The installer identity, NationalID, moral rating, IQ value, and file metadata must not be treated as sufficient authorization merely because they exist in a request. Authorization decisions should be explicit, auditable, and separated from ordinary contact/question processing.

## 4. Database Hardening Direction

The existing schema is a useful starting point but should be extended with appropriate indexes and constraints after the authoritative Java access patterns are recovered. Schema changes must follow the application data model rather than inventing columns solely to satisfy documentation.

The `2000_iq` and `module_installs` tables contain potentially sensitive identifiers and network metadata. Retention, access, deletion, and audit requirements therefore belong in the database migration and governance process.

## 5. Release Integrity Rule

A release is not considered implementation-complete when only documentation describes a component. For each documented executable component, `main` should contain either:

- the authoritative source required to build it; or
- a clearly documented external dependency with an immutable/versioned reference.

This rule prevents the repository from appearing more complete than its actual executable contents.

## 6. Next Engineering Pass

The next code pass should begin only after locating/restoring the authoritative Java source. At that point the implementation should be reviewed against this document and the existing security/network/database specifications, followed by compilation and runtime tests.

**Status:** audit documented; source-level Java changes intentionally deferred until the real source is available.

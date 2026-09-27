# PIXEL.md

## Purpose

This document provides a concise, repository-grounded description of **Green.Durham.Grass.and.Herb**: what is made, what is included, and how to document the project accurately as its technical and research components evolve.

The repository describes itself as a Durham, North Carolina-focused project combining public-reference and research material with the **Appree** Java network-service architecture. Because the repository contains political and public-policy material, PIXEL describes those materials as information and software/data structures rather than treating the repository as governmental authority or as an instruction to adopt any political position.

## 1. What's Made

### 1.1 Appree Network-Service Platform

The repository contains a Java-oriented service architecture centered on Appree, with listeners, domain components, configuration, installation material, and network documentation.

The documented network surface includes:

- module installation/listener service on TCP port 2000;
- base Appree contact service on TCP port 20000;
- East Coast listener on TCP port 40002;
- West Coast listener on TCP port 40003;
- Texas listener on TCP port 40007;
- registration server on TCP port 49152.

The repository also distinguishes application-level identifiers from ordinary TCP/IPv4 ports.

### 1.2 Public Contact and Reference Material

The repository contains structured contact/reference material under `contacts/`, including jurisdictional and institutional reference collections.

These materials should be treated as reference data. Their presence in the repository does not itself establish current accuracy, official affiliation, authorization, or governmental status.

### 1.3 Research and Public-Policy Organization

The repository provides dedicated areas for research and political/public-policy material, including:

- `research/`
- `political/`
- `political/`-related documentation such as `POLITICAL-MODULE.md`
- `labor-concerns/`
- `rhetoric/`

The technical architecture keeps these informational domains distinct from core infrastructure.

### 1.4 Database and Data-Governance Layer

The documented database is `green_durham_grass_and_herb`, with tables and responsibilities covering ethical, labor, moral, mortality, Appree-question, and listener-log data.

The project explicitly identifies potentially sensitive fields such as IP, DNS, name, geographic information, and national identifiers as requiring controlled access and documented retention.

### 1.5 Geolocation and External-Service Boundary

The project documents local GeoLite2 data as the preferred geolocation source and identifies an external IP-geolocation fallback as a configurable privacy boundary.

The architecture therefore treats external IP disclosure as an explicit deployment consideration rather than an invisible implementation detail.

### 1.6 Security and Operational Controls

The repository documents controls including:

- least-privilege database access;
- externalized secrets;
- controlled listener exposure;
- authentication/authorization separation;
- bounded retries;
- sensitive-data protection;
- incident-response documentation.

These are documented mechanisms and design requirements; their existence in documentation should not be interpreted as proof that every deployment has been independently security-audited.

## 2. What's Included

### 2.1 Core Documentation

Major top-level documentation includes:

- `README.md`
- `ARCHITECTURE.md`
- `CONFIGURATION.md`
- `NETWORK.md`
- `DATABASE.md`
- `DATA-GOVERNANCE.md`
- `SECURITY.md`
- `CONTACTS.md`
- `POLITICAL-MODULE.md`
- `INSTALLATION.md`
- `MODULE.txt`
- `STRUCTURE.txt`
- `SOURCE-CODE-AUDIT.md`

### 2.2 Source and Configuration

The repository includes:

- `source-code/`
- `configuration/`
- `META-INF/`

Configuration includes Appree, AI-interpreter, database, labor, moral, mortality, GitHub-polling, JWSTF-J21 integration, and known-port material.

### 2.3 Contacts and Jurisdictional Data

The `contacts/` hierarchy contains structured public-reference material organized by jurisdiction and institution.

Because this material can become stale, future updates should identify source dates, provenance, and validation status where appropriate.

### 2.4 Data and Research Areas

The repository includes dedicated areas for:

- `data/`
- `research/`
- `political/`
- `labor-concerns/`
- `rhetoric/`

These should remain distinguishable from executable infrastructure.

### 2.5 Installation and Compatibility Material

Installation-related material is organized under:

- `installation/`
- `install/`

The repository documents JDK 21+ as the required Java runtime baseline and provides deployment/verification guidance.

### 2.6 Build and Project Metadata

The repository also contains project metadata and supporting artifacts, including IntelliJ project configuration, Java module metadata, and a Windows cleanup helper.

## 3. How to Write This Kind of Stuff

### 3.1 Start With the Architecture

Explain the relationship between:

`source-code/` → services/listeners → configuration → database/data → documentation/operations

Then identify the research and public-reference layers separately.

### 3.2 Separate Software From Information

A contact record, political research document, database table, listener, and Java service are different classes of artifact.

PIXEL should make those boundaries explicit.

### 3.3 Treat Public-Policy Material Neutrally

For political or policy-related content:

- describe what a document contains;
- identify the relevant source or provenance;
- distinguish data from interpretation;
- avoid turning repository organization into political endorsement;
- do not present the software as possessing governmental authority.

### 3.4 Record Network Interfaces Precisely

Use the actual service name, port, and protocol status. Do not describe application-level identifiers as TCP ports merely because they contain port-like numbers.

### 3.5 Treat Sensitive Data as a Separate Concern

When documenting fields such as IP addresses, names, geographic data, DNS information, or national identifiers, describe access, retention, provenance, and purpose rather than merely listing the fields.

### 3.6 Distinguish Documentation From Verification

A documented security control is not automatically a tested security property.

Likewise:

- a listed listener is not proof that it is currently running;
- a schema is not proof that a production database is correctly configured;
- an installation guide is not proof that deployment has succeeded;
- a research reference is not proof that its contents remain current.

### 3.7 Keep External Services Visible

Document any external API, geolocation provider, GitHub polling, database dependency, or other network boundary so deployments can make deliberate decisions about availability and privacy.

## 4. Recommended PIXEL Pattern

For future revisions, keep the document organized around:

1. **Purpose**
2. **What's Made**
3. **What's Included**
4. **How to Write This Kind of Stuff**
5. **Current Project Snapshot**
6. **Maintenance Rule**

Use exact repository paths and update the document whenever a major architecture, data-governance, network, installation, or research boundary changes.

## 5. Current Project Snapshot

| Area | Current repository evidence |
|---|---|
| Project | Green.Durham.Grass.and.Herb |
| Primary application direction | Java / Appree network-service platform |
| Java baseline | JDK 21+ documented |
| Network model | TCP listeners plus application-level identifiers |
| Database | `green_durham_grass_and_herb` |
| Configuration | XML configuration under `configuration/` |
| Contact/reference data | `contacts/` |
| Research material | `research/` |
| Public-policy material | `political/` |
| Labor/intelligence/democracy references | `labor-concerns/` |
| Security documentation | `SECURITY.md` |
| Data governance | `DATA-GOVERNANCE.md` |
| Installation | `INSTALLATION.md`, `installation/`, `install/` |
| Architecture | `ARCHITECTURE.md` |
| Network specification | `NETWORK.md` |
| Source audit | `SOURCE-CODE-AUDIT.md` |

## 6. Maintenance Rule

**PIXEL follows the repository.**

When Green.Durham.Grass.and.Herb changes:

- update architecture descriptions with the actual source paths;
- synchronize documented listeners and ports;
- keep database and data-governance documentation synchronized;
- identify external services and privacy boundaries;
- distinguish public-reference data from executable infrastructure;
- keep political/public-policy descriptions informational and source-grounded;
- record tests actually performed rather than inferred validation;
- identify stale or historical reference material when known;
- never treat documentation alone as proof of production readiness or official authority.

The objective is to make the project easier to understand while preserving clear boundaries between **software, data, research, public-reference material, and operational claims**.

# Political Module

This repository contains political, labor, ethical, moral, mortality, and public-contact material alongside a networked Java application. The political/research layer is intentionally separated from infrastructure and security controls.

## Scope

Political material may include public-office references, policy research, labor references, democracy material, and public contact information. It should be treated as research/reference content unless a document explicitly states another purpose.

## Separation of concerns

- `political/` and `research/` are informational domains.
- `contacts/` is public-contact/reference data.
- `source-code/` is executable application code.
- `configuration/` is deployment configuration.
- `data/` contains operational/reference data.
- `network/` or `NETWORK.md` describes service exposure.

Political content must not implicitly change listener privileges, database permissions, authentication, or administrative access.

## Durham context

The project identifies Durham, North Carolina as a principal context and references public policy and local political concerns. Those references should remain clearly labeled as project research or commentary rather than being represented as government instructions or official statements.

## Change control

New political datasets should identify their source, jurisdiction, date, and intended use. Sensitive personal information should not be introduced merely for analytical convenience.

# Security

Security is a first-class requirement for Green.Durham.Grass.and.Herb. The service handles network connections, logs, names, IP-related information, geolocation information, and NationalID-related fields; those capabilities require least privilege and deliberate data governance.

## Security boundaries

- Do not commit passwords, API keys, database credentials, private certificates, or production secrets.
- Configuration examples must use placeholders such as `${DB_USER}` and `${DB_PASSWORD}`.
- Database accounts should have only the privileges required by the service.
- Administrative and installation operations must require explicit authorization.
- Listener ports should bind only where operationally required; local-only services should bind to loopback.
- TLS should be used for any sensitive traffic crossing an untrusted network.

## Identity and authorization

Authentication and authorization are separate controls. A valid identity does not automatically grant access to administrative, database, installation, or political-research functions.

NationalID fields must be treated as sensitive identifiers. They must not be used as a substitute for authentication, and access to them must be restricted and logged.

## Logging

Operational logs should record security-relevant events without unnecessarily reproducing sensitive payloads. IP addresses, names, DNS values, geolocation, and identifiers require documented retention and access rules.

## Geolocation

Local GeoLite2 data is preferred where available. The remote `ip-api.com` fallback should be disabled by default for privacy-sensitive deployments, or explicitly configured with a timeout and documented disclosure that an external service receives the IP address.

## Threat model

Primary concerns include credential disclosure, unauthorized database access, exposed listeners, malicious configuration, dependency compromise, log leakage, and unauthorized access to sensitive contact or identity information.

## Incident response

If credentials or sensitive data are exposed: revoke or rotate the affected secret, restrict the affected service, preserve relevant audit evidence, identify the affected files/records, remediate the exposure, and document the corrective action.

## Secure-development rule

Security-sensitive behavior must be documented before deployment. Tests should cover authentication, authorization, listener exposure, configuration validation, database permissions, and failure handling.

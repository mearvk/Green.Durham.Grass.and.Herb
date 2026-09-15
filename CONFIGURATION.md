# Configuration

Configuration defines runtime behavior without embedding secrets in source control.

## Rules

- Keep production credentials outside Git.
- Use environment variables or a deployment secret store for passwords, API keys, and private keys.
- Maintain sanitized `.example` configuration files for reproducible setup.
- Validate XML and required fields before service startup.
- Fail closed when required security settings are missing.
- Document listener addresses, ports, database endpoints, retry behavior, and optional external services.

## Database configuration

Database credentials must be supplied at deployment time. The application should use a dedicated account with the minimum required schema permissions.

## Geolocation configuration

Local GeoLite2 lookup is preferred. Remote `ip-api.com` lookup should be an explicit opt-in with timeout/error handling and a documented privacy impact.

## Listener configuration

Configured listener ports must match `NETWORK.md`. A configuration entry must not be considered an active service until the implementation actually registers the listener.

## Change control

Any configuration setting that changes authentication, authorization, network exposure, data collection, or external data transmission requires a corresponding security or data-governance review.

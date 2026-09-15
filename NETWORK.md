# Network

## Declared listener ports

| Port | Role |
|---:|---|
| 2000 | Module installation/listener service |
| 20000 | Base Appree contact service |
| 40002 | East Coast listener |
| 40003 | West Coast listener |
| 40007 | Texas listener |
| 49152 | Registration server |

These are the ports currently described by the module documentation. `STRUCTURE.txt` has historically mentioned Wyoming and Indiana listener tables; those names must not be treated as active network listeners until the implementation and configuration agree.

## Extended namespace

The module documentation describes virtual mappings such as 70000→20000, 70002→40002, 70003→40003, 70007→40007, 80000→2000, and 99152→49152. Values above 65535 are application-level/extended identifiers, not ordinary IPv4 TCP ports.

## Exposure policy

- Bind public listeners only when required.
- Prefer loopback for administrative and installation services.
- Apply authentication and authorization before sensitive operations.
- Apply connection timeouts, rate limits, and bounded retries.
- Do not expose database ports as part of the application listener model.

## Configuration authority

Runtime XML configuration and the actual Java listener implementation must agree with this document. When a listener is added, removed, renamed, or moved, update `NETWORK.md`, `MODULE.txt`, configuration, and tests together.

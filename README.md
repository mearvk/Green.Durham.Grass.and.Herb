# Green.Durham.Grass.and.Herb

Are All The Millionaires Called Out? Does the Socialism Rely on National Standard? Does it Rely on Strong Socialism? Durham, NC Mayor Slot up For Grabs! 2028. 919.923.4239 - Connecting!

## Bitcoin Conjegeum

![](https://github.com/mearvk/Ubuntu.Determinant.Beta.Restricted/blob/main/images/Bitcoin_and_wallet_in_slots_2K_202609042306%20(1).jpeg)

bc1qs6v4q9zsw70t0umk3m0quhvf9dr6cdeskl28dh

US Democratic and US Policy.

## About

Appree is a careful and concernful program all the way down to the Mayor of Chapel Hill and her study of students of the County. This is subtly an Appree project riding on NC Labor Laws & Organization.

## Structure

- `source-code/Main.java` — Entry point with DecisionMaker and XmlReader classes
- `source-code/appree/` — Appree contact server, listener configuration, question storage
- `source-code/listeners/` — Coast listeners, base port 20000 server (geo + NationalID + DB), connection scripts
- `source-code/labor/` — Labor concerns DB, AI interpreter for labor inquiries
- `source-code/ethical/` — Ethical concerns DB connection
- `source-code/moral/` — Moral concerns DB connection
- `source-code/mortality/` — Mortal concerns DB connection
- `install/` — Installation scripts and SQL
- `data/` — US trades logs, optional GeoLite2 database
- `rhetoric/` — US trades rhetoric logs
- `labor-concerns/` — Labor, intelligence, and democracy references

## Ports

| Port | Service |
|------|---------|
| 20000 | Base public contact server (Appree) — geo lookup, NationalID gate, DB logging |
| 40002 | East Coast listener |
| 40003 | West Coast listener |
| 40007 | Texas listener |
| 49152 | Registration server |

## Database

All data stored in MySQL database `green_durham_grass_and_herb`.

| Table | Purpose |
|-------|---------|
| ethical | Ethical concerns |
| labor | Labor concerns |
| moral | Moral concerns |
| mortality | Mortality concerns |
| 2000_iq | All questions asked to Appree (Mayor's separate table) |
| listeners_* | Per-region connection logs |

### 2000_iq Fields

`id`, `question`, `remote_address`, `ip`, `dns`, `name`, `geo`, `national_id`, `created_at`

Geo is resolved via local GeoLite2 jar (if present in `data/`) or remote ip-api.com fallback.

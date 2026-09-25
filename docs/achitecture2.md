# Architecture — Current State (Local)

## Overview

CivicPulse is currently a local Spring Boot application. It has no
external dependencies yet — no database, no cloud services — by
design, so the domain logic and API contract could be built and
tested in isolation before introducing infrastructure concerns.

## Layers


## Endpoints

| Method | Path                     | Purpose                              |
|--------|--------------------------|---------------------------------------|
| POST   | /incidents               | Create a new incident report          |
| GET    | /incidents               | List all incidents                    |
| GET    | /incidents/{id}          | Get one incident by id                |
| PATCH  | /incidents/{id}/status   | Transition an incident's status       |

## Domain model

- `Incident` — title, description, location, priority, system-generated
  id, and a status that starts at `OPEN`.
- `Status` — `OPEN → IN_PROGRESS → RESOLVED`, enforced as a one-way,
  one-step-at-a-time transition (an incident cannot jump straight
  from `OPEN` to `RESOLVED`).
- `Priority` — `LOW`, `MEDIUM`, `HIGH`.

## Why separate `domain` from `api`?

The `domain` package (Incident, Status, Priority, IncidentRepository)
has no knowledge of HTTP, JSON, or Spring's web layer. The `api`
package (IncidentController, StatusUpdateRequest) is the only layer
that knows this is a web application at all. This means the business
rules — what counts as a valid status transition, what makes an
incident invalid — can be tested and reasoned about independently of
how they're exposed to the outside world.

## Why in-memory storage right now?

`IncidentRepository` currently stores incidents in a `Map<String,
Incident>` that lives only in application memory — nothing survives a
restart. This was a deliberate choice to get the domain model and API
contract fully correct and tested before introducing a real
persistence layer. Swapping this for a real datastore (see the
roadmap in the README) will not require any change to the controller
or domain model — only to `IncidentRepository`'s internals.

## Known limitations at this stage

- No persistence beyond process memory.
- No authentication or authorization — anyone can create or update
  any incident.
- No photo/file attachment support yet.
- No asynchronous processing (notifications, audit logging) — all
  work happens synchronously within the request.

## Roadmap
- DynamoDB persistence (DynamoDbIncidentRepository) is implemented but
  untested against live AWS — account verification did not complete
  before the submission deadline.
- Planned next: S3 for photo attachments, SQS for async notification
  processing, EventBridge for event-driven workflows, Lambda-based
  deployment, CloudWatch monitoring, IAM least-privilege hardening.
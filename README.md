# CivicPulse
CivicPulse is a cloud-based platform for reporting and tracking infrastructure problems in a local community.

Cloud Computing Verification

WTC-9T5FYZCR

# CivicPulse

A cloud-native incident reporting and community response platform.

## Problem

Communities often report service delivery problems — broken
infrastructure, safety hazards, service failures — through scattered
channels: phone calls, emails, WhatsApp messages, or in-person visits
to municipal offices. This makes reports easy to lose, hard to track,
and impossible to see patterns in (the same pothole might be reported
ten times by ten different people with no visibility into each other).

CivicPulse gives communities a single platform to submit and track
incidents, and gives staff — who are themselves community members —
a clear, structured way to see, prioritize, and resolve them.

## Solution & Architecture





### Endpoints

| Method | Path                     | Purpose                         |
|--------|--------------------------|----------------------------------|
| POST   | /incidents               | Create a new incident report     |
| GET    | /incidents               | List all incidents               |
| GET    | /incidents/{id}          | Get one incident by id           |
| PATCH  | /incidents/{id}/status   | Transition an incident's status  |

### Domain model

- **Incident** — title, description, location, priority, a
  system-generated id, and a status that starts at `OPEN`.
- **Status** — `OPEN → IN_PROGRESS → RESOLVED`, enforced as a
  one-way, one-step-at-a-time transition. An invalid transition
  (e.g. `OPEN → RESOLVED` directly) returns `409 Conflict`.
- **Priority** — `LOW`, `MEDIUM`, `HIGH`.

The `domain` package has no knowledge of HTTP, JSON, or Spring's web
layer — only the `api` package knows this is a web application. This
keeps business rules testable independently of how they're exposed.

## Why AWS?

A single in-memory `Map` works for development and testing, but
loses all data on restart and can't be shared across multiple running
instances of the app — both real limitations for a system meant to
serve a community over time. `IncidentRepository` was deliberately
built as an interface so persistence could be swapped without
touching the API or domain layer at all.

**Current status:** `DynamoDbIncidentRepository` is fully implemented
using the AWS SDK, but has not yet been tested against a live AWS
account — account identity verification did not complete before the
submission deadline (a delay outside my control, not a technical
blocker). `InMemoryIncidentRepository` is the active, fully tested
implementation shipped in this submission (`@Primary`); DynamoDB
activates via the `dynamodb` Spring profile once account access is
available.

### AWS services

| Service   | Why it's used                                                  |
|-----------|------------------------------------------------------------------|
| DynamoDB  | Persistent storage for incident records, keyed by id — replaces the in-memory Map with something that survives restarts and scales beyond one process |

## How to run locally

The API is available at `http://localhost:8080/incidents`.

Run the test suite: mvn spring-boot:run
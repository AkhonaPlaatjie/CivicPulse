# Design Decisions

A running log of real decisions made while building CivicPulse, and
the reasoning behind them. Written as they were made, not
reconstructed afterward.

## Priority as an enum, not a String
A String priority (`"HIGH"`) allows typos like `"Hihg"` to compile
and fail silently at runtime. An enum (`Priority.HIGH`) makes invalid
values impossible to construct in the first place — the compiler
catches the mistake instead of a user seeing broken behavior later.

## Id is generated internally, not passed in
A caller should never get to choose an incident's identity — that's
the system's job. The constructor generates a UUID internally
(`UUID.randomUUID().toString()`) rather than accepting an id
parameter. The id field is also `final`: once assigned, it cannot
change.

## Status transitions are explicit methods, not a setter
There is no generic `setStatus(Status s)`. Instead, `markInProgress()`
and `markResolved()` each enforce a specific, valid transition
(`OPEN → IN_PROGRESS → RESOLVED`, one step at a time) and throw
`IllegalStateException` if called out of order. A plain setter would
let any code jump straight from `OPEN` to `RESOLVED`, bypassing the
workflow the system is meant to enforce.

## Map, not array or List, for in-memory storage
The repository needs to look incidents up by id. An array/List would
require looping through every element to find a match by id — a
`Map<String, Incident>` keyed by id gives direct lookup instead
(`incidents.get(id)`), while `.values()` still provides "all
incidents" when needed.

## findById returns Optional<Incident>, not a nullable Incident
Returning `null` when nothing is found makes "this might not exist" an
implicit trap a caller can forget to check. `Optional` makes the
possibility of absence part of the method's signature, forcing the
caller to explicitly decide what happens when nothing is found.

## save() returns the saved Incident
Chosen so the controller has something to hand back in the response
body immediately after creation (specifically the generated id) —
rather than the caller already needing to have a reference to what it
just saved.

## POST /incidents returns the full created object (201, not empty)
A caller creating an incident has no way to know its generated id
unless the response includes the full object. This follows REST
convention: a successful creation returns `201 Created` with the
resource in the body.

## PATCH /incidents/{id}/status uses a dedicated endpoint and request
## body, not a general-purpose update endpoint
A generic `PUT /incidents/{id}` that accepted a full incident object
(status included) would let a client overwrite status directly,
bypassing the same transition rules `Incident` already enforces
internally. A separate `StatusUpdateRequest` body
(`{"status": "IN_PROGRESS"}`) keeps status changes routed through
`markInProgress()`/`markResolved()`, so the validation can't be
skipped from the API layer.

## 409 Conflict for an invalid status transition, not 400 or 500
An invalid transition (e.g. `OPEN → RESOLVED` directly) is not
malformed input (`400`) — the request is well-formed — and it should
never surface as an unhandled `500`. `409 Conflict` communicates
that the request is valid but conflicts with the resource's current
state, which is precisely what an illegal transition is.

## domain package has no knowledge of HTTP or Spring
`Incident`, `Status`, `Priority`, and `IncidentRepository` don't
import anything from `org.springframework.web`. Only the `api`
package (`IncidentController`, `StatusUpdateRequest`) knows this is a
web application. This keeps business rules testable and reasoned
about independently of how they're exposed externally.

## Tests use @Import(IncidentRepository.class), not @MockBean
`@WebMvcTest` only loads the web layer by default and doesn't
automatically wire up `@Repository` beans. `@Import` was chosen over
mocking so the real repository is exercised in controller tests —
useful here since the repository has no external dependencies of its
own (no database yet), so there's no cost to using the real thing.

## Repository tests use @BeforeEach with deleteAll()
`@WebMvcTest` reuses the same Spring context (and therefore the same
repository instance) across test methods for speed. Without an
explicit reset, one test's saved data could leak into another and
cause order-dependent failures. `deleteAll()` before every test
guarantees isolation regardless of run order.

## Scope cut for the 25 September deadline
The original plan included S3 (photos), SQS (async notifications),
EventBridge, and Lambda. Given the actual time available, only a
DynamoDB swap for persistence is being completed for this submission.
The rest is documented as a roadmap rather than attempted and left
half-working — treating this as Phase 1 of an ongoing project rather
than a single deliverable.
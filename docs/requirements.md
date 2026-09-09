# CivicPulse — Requirements

## 1. Users

CivicPulse has two primary users:

### Community Member

A community member can report infrastructure problems and track the
progress of their reports.

### Administrator

An administrator manages infrastructure reports and updates their
progress.

---

## 2. Functional Requirements

### FR-01 — Create an Incident

The system must allow a community member to create an infrastructure
incident.

A report must contain:

- Title
- Description
- Category
- Location
- Priority

A report may optionally contain photographic evidence.

---

### FR-02 — Generate an Incident ID

The system must assign a unique identifier to every incident.

The identifier will allow users and administrators to refer to a
specific report.

---

### FR-03 — View an Incident

The system must allow a user to retrieve a specific incident using
its unique identifier.

The returned incident must include its current status.

---

### FR-04 — List Incidents

The system must allow an administrator to retrieve multiple incidents.

The administrator must be able to identify the current status and
priority of each incident.

---

### FR-05 — Update an Incident

The system must allow an administrator to update an incident.

Initially, administrators must be able to:

- Change the status
- Change the priority
- Update the description

---

### FR-06 — Track Incident Status

Every incident must have a status.

The initial statuses are:

- OPEN
- IN_PROGRESS
- RESOLVED
- REJECTED

The system must record the current status of each incident.

---

### FR-07 — Categorise Incidents

Every incident must belong to a category.

Initial categories are:

- ROAD_DAMAGE
- STREETLIGHT
- WATER_LEAK
- DRAINAGE
- WASTE
- PUBLIC_FACILITY
- OTHER

---

### FR-08 — Prioritise Incidents

Every incident must have a priority.

Initial priorities are:

- LOW
- MEDIUM
- HIGH
- CRITICAL

---

### FR-09 — Attach Evidence

The system must allow a community member to attach an image to an
incident.

Images will be treated separately from the incident's structured
information.

---

### FR-10 — Record Timestamps

The system must record when an incident was created and when it was
last updated.

---

## 3. Non-Functional Requirements

### NFR-01 — Availability

The system should remain available without requiring the developer
to manually start or maintain a physical server.

---

### NFR-02 — Scalability

The system should be capable of handling an increase in the number
of incident reports without requiring a complete redesign.

---

### NFR-03 — Security

Users and system components must only have access to the resources
and operations they require.

---

### NFR-04 — Reliability

A failure in background processing should not cause the original
incident report to be lost.

---

### NFR-05 — Observability

The system must provide sufficient logging and monitoring information
to investigate application failures and system behaviour.

---

### NFR-06 — Maintainability

The system should be organised into components with clear
responsibilities so that individual components can be changed without
requiring unnecessary changes elsewhere.

---

## 4. MVP Boundary

The initial version of CivicPulse will focus on the complete incident
reporting workflow:

Community Member:

    Create incident
        ↓
    Receive incident ID
        ↓
    View incident
        ↓
    Track status

Administrator:

    View incidents
        ↓
    Inspect incident
        ↓
    Update priority/status
        ↓
    Resolve or reject incident

Cloud functionality will be introduced incrementally as the system
develops.
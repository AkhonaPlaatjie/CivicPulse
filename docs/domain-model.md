# CivicPulse — Domain Model

## 1. Core Domain

The central concept in CivicPulse is an `IncidentReport`.

An incident represents an infrastructure problem reported by a
community member.

Examples include:

- A pothole
- A broken streetlight
- A water leak
- Blocked drainage
- Illegal dumping
- Damaged public facilities

---

## 2. IncidentReport

An incident contains the following information:

| Field | Description |
|---|---|
| id | Unique identifier for the incident |
| title | Short description of the problem |
| description | Detailed explanation of the problem |
| category | Type of infrastructure problem |
| location | Where the problem occurred |
| priority | Urgency of the problem |
| status | Current state of the incident |
| imageKey | Reference to photographic evidence |
| createdAt | Time the incident was created |
| updatedAt | Time the incident was last updated |

---

## 3. Category

An incident belongs to one category.

Initial categories:

```text
ROAD_DAMAGE
STREETLIGHT
WATER_LEAK
DRAINAGE
WASTE
PUBLIC_FACILITY
OTHER
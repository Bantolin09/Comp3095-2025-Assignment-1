# COMP3095 – Assignment 1 Status Checklist Report
**Team:** Uzma, Ben, Zaki  
**Date:** November 06, 2025

---

## 1. Completed Requirements

| Requirement | Status | Notes |
|-----------|--------|-------|
| Event Service CRUD | Completed | Full REST |
| Register/Unregister | Completed | Capacity enforced |
| Filter by date/location | Completed | `?date=`, `?location=` |
| PostgreSQL + JPA | Completed | `eventdb` |
| Inter-service call | Completed | `GET /events/{id}/resources` |
| TestContainers test | Completed | Spins up PostgreSQL |
| Docker Compose | Completed | All containers |
| GitLab Branch | Completed | `uzma-event-service` → `main` |

---


## API Endpoints for Events

| Endpoint                                           | Method | Description               |
|----------------------------------------------------|--------|---------------------------|
| `http://localhost:8082/events`                     | POST   | Create new event          |
| `http://localhost:8082/events`                     | GET    | Get all events            |
| `http://localhost:8082/events/{id}`                | GET    | Get event by ID           |
| `http://localhost:8082/events/{id}`                | PUT    | Update event by ID        |
| `http://localhost:8082/events/{id}`                | DELETE | Delete event by ID        |
| `http://localhost:8082/events/date/{date}`         | GET    | Filter events by date     |
| `http://localhost:8082/events/location/{location}` | GET    | Filter events by location |
| `http://localhost:8082/events/category/{category}` | GET    | Filter events by category |

### Student Registration

| Endpoint                                       | Method | Description          |
|------------------------------------------------|--------|----------------------|
| `http://localhost:8082/events/{id}/register`   | POST   | Register a student   |
| `http://localhost:8082/events/{id}/unregister` | GET    | Unregister a student |

### Resource Linking

| Endpoint                                      | Method | Description   |
|-----------------------------------------------|--------|---------------|
| `http://localhost:8082/events/{id}/resources` | GET    | Get Resources |


- Default port: `8082`

## Example Create Event JSON

{
"title": "Yoga Workshop",
"description": "Relax and stretch with mindfulness",
"date": "2025-12-05",
"location": "GBC Gym",
"capacity": 20,
"category": "mindfulness"
}

## 2. GitLab Repository

**URL:** `https://gitlab.com/Bantolin09/Comp3095-2025-Assignment-1`  
**Professor Access:** Added as **Reporter**

---

## 3. Video Demonstration

- **Duration:** 7 minutes
- **Uploaded to Brightspace**
- **Includes:** Intro slide, Docker, Postman, DB, TestContainers, reflection

---

**All services run via Docker. No IntelliJ used.**

**Reflection:**
> "Challenge: Spring Boot 3.5.7 Docker Compose bug. Favorite: Real-time resource suggestions."

---

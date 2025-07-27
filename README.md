# 🚀 SpaceX Dragon Rockets Repository

## Overview

This is a simple **Java library** that manages SpaceX **Dragon rockets** and their **missions** using in-memory data structures. It is designed for educational and technical assessment purposes, following best practices in object-oriented design.
Chat GPT was used for refactoring and adding tests

✅ Clean Code  
✅ SOLID Principles  
✅ TDD (Test-Driven Development)  
✅ No frameworks, no databases, no REST APIs — just pure Java
✅ Based on NOT thread-safe collections, not transactional

---

## 🧠 Functionality

The system supports:

- Adding new rockets (initial status: `On ground`)
- Adding new missions (initial status: `Scheduled`)
- Assigning rockets to missions (one rocket → one mission, mission → many rockets)
- Changing rocket status
- Auto-updating mission status based on the assigned rocket statuses
- Ending a mission (removes all assigned rockets)
- Mission summary ordered by:
  - Number of rockets assigned (descending)
  - Name (descending alphabetical order in case of ties)

---

## 🚦 Statuses

### Rocket statuses:

- `OnGround` (default)
- `InSpace`
- `InRepair` (triggers mission status to `PENDING`)
- `InBuild` (for non-human builds, included as an edge case)

### Mission statuses:

- `Scheduled` – no rockets assigned
- `Pending` – at least one assigned rocket is `IN_REPAIR`
- `InProgress` – at least one rocket assigned, none are in repair
- `Ended` – mission completed, no further rocket assignments allowed

---

---

## 🔧 Requirements

- Java 17+
- Maven 3.8+

---

## 🚀 How to Run

1. Compile the project:

```bash
mvn clean compile

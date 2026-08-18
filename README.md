# AnimoRegistry

A centralized recruitment management system for DLSU student organizations, built with Spring Boot 3, Java 17, Maven, and an in-memory H2 database.

## Team
- Johanna Rae C. Castor
- Gian Gabriel S. Fadriquela
- Sean Matthew E. Ortiz

## How to import into IntelliJ IDEA

1. Unzip the project folder anywhere on your machine.
2. Open IntelliJ IDEA → **File → Open** → select the unzipped `animoregistry` folder (the one with `pom.xml`).
3. IntelliJ will detect it as a Maven project and prompt to load it — click **Load Maven Project** (or it may auto-import).
4. Make sure the Project SDK is set to **Java 17**: File → Project Structure → Project → SDK.
5. Let Maven download the dependencies (needs an internet connection the first time).
6. Run the app by opening `AnimoRegistryApplication.java` and clicking the green Run arrow, or run:
   ```
   mvn spring-boot:run
   ```
7. The app starts on **http://localhost:8080**. Sample data is loaded automatically on startup (see console output).

## Using the website

Open **http://localhost:8080** in your browser — that loads the actual site (`index.html`), not raw JSON. Pages:

- `index.html` — browse all organizations, filter by category, search
- `org.html?id={id}` — organization profile + apply form (students only)
- `register.html` — create a student or officer account
- `login.html` — log in as a student or officer
- `dashboard.html` — student view: track your own applications and their status
- `officer.html` — officer view: toggle recruitment open/closed, set membership cap, edit the org profile, collect membership fees, review applicants and accept/reject/schedule interviews

It's plain HTML/CSS/JS under `src/main/resources/static/`, served directly by Spring Boot — no build step, no framework. It calls the REST API below with `fetch()`.

**Test accounts (from the seeded data), password for all is `password123`:**
- Student: `juan_delacruz@dlsu.edu.ph`
- Officer: `anna_reyes@dlsu.edu.ph` (VP for Membership, LSCS)

Login sessions are just stored in `localStorage` — there's no real authentication (no hashing, no tokens). That's fine for a school project demo, but flag it if anyone asks whether this is production-ready.

## Database

Uses H2 in-memory database — no setup required, resets every restart.
- H2 Console: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:animoregistry`
- Username: `sa`, Password: *(blank)*

## Project structure

```
src/main/java/com/dlsu/animoregistry/
├── model/          # Entities: DLSUUser (abstract), LasallianStudent, OrgOfficer,
│                   # Organization, ApplicationForm, ApplicationStatus, PaymentMethod (+ impls)
├── repository/     # Spring Data JPA repositories
├── service/        # Business logic (StudentService, OrgOfficerService,
│                   # OrganizationService, ApplicationService)
├── controller/     # REST controllers
├── dto/            # Request bodies for the API
├── exception/      # Custom exceptions + global handler
└── config/         # DataSeeder (loads sample data on startup)
```

## Where the four OOP pillars live

- **Encapsulation** — `DLSUUser.setIdNumber()` and `setDlsuEmail()` validate every value before it's stored (8-digit ID, `@dlsu.edu.ph` email only). `Organization.setMembershipCap()` also guards against setting a cap below the current member count.
- **Inheritance** — `LasallianStudent` and `OrgOfficer` both extend the abstract `DLSUUser`, inheriting its credentials and validation while adding their own role-specific fields.
- **Polymorphism** — `displayDashboard()` is declared abstract in `DLSUUser` and overridden differently in `LasallianStudent` (applicant view) and `OrgOfficer` (officer view).
- **Abstraction** — `PaymentMethod` is an interface with two implementations, `CashPayment` and `DigitalBankPayment`. Callers (like `Organization.collectMembershipFee()`) only depend on the interface, never on the concrete payment logic.

## Sample API endpoints

### Organizations
- `POST /api/organizations` — create an organization
- `GET /api/organizations` — list all (supports `?category=` and `?openOnly=true`)
- `GET /api/organizations/{id}` — get one
- `PUT /api/organizations/{id}/profile` — edit description/logo/social handle/category
- `PATCH /api/organizations/{id}/registration-status` — body: `{ "open": false }`
- `PATCH /api/organizations/{id}/membership-cap` — body: `{ "membershipCap": 60 }`
- `POST /api/organizations/{id}/collect-fee` — body: `{ "payerName": "Juan Dela Cruz" }`

### Students
- `POST /api/students/register` — register a student
- `GET /api/students` — list all
- `GET /api/students/{id}/dashboard` — polymorphic dashboard summary

### Officers
- `POST /api/officers/register/{organizationId}` — register an officer under an org
- `GET /api/officers/{id}/dashboard` — polymorphic dashboard summary

### Applications
- `POST /api/applications` — submit an application: `{ "applicantId": 1, "organizationId": 1, "answers": "..." }`
- `GET /api/applications/student/{studentId}` — a student's applications + statuses
- `GET /api/applications/organization/{organizationId}` — an org's applicants (optionally `?status=PENDING`)
- `PATCH /api/applications/{id}/interview` — body: `{ "interviewSchedule": "2026-09-01T14:00:00" }`
- `PATCH /api/applications/{id}/status` — body: `{ "status": "ACCEPTED" }` (or `REJECTED`, `PENDING`, `INTERVIEW_SCHEDULED`)

### Login
- `POST /api/students/login` — body: `{ "dlsuEmail": "...", "password": "..." }`
- `POST /api/officers/login` — same shape

## Notes / next steps

This covers the recruitment workflow end to end (browse orgs, apply, interview, accept/reject, track slots and status) with a working UI on top. Not yet in scope: real authentication (the login is a plaintext password check, not hashed, and sessions live in `localStorage`), and there's no way for an officer to create a brand-new organization from the UI yet (only via `POST /api/organizations` directly) — registering an officer currently requires the org to already exist. A natural next step would be adding Spring Security and a "create your org" flow for the first officer of a new organization.

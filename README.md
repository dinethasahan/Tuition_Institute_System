# Tuition Institute Management System - Backend

Production-ready, modular monolithic backend for the Tuition Institute Management System built with **Spring Boot 3.3.5** on **Java 21 LTS**, **Spring Security 6**, **Spring Data JPA (Hibernate ORM)**, **MySQL 8 (InnoDB, utf8mb4)**, and **Flyway**.

---

## 1. Quick Start

### Prerequisites
- **Java 21 LTS** installed (`java -version`)
- **MySQL 8.x** running on port 3306

### Database Setup
Log into your MySQL server and create the database with UTF-8 Unicode support:
```sql
CREATE DATABASE IF NOT EXISTS tuition CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### Running the Application

You can run the application directly using Maven or using the packaged standalone JAR:

#### Option A: Using Maven
```powershell
mvn spring-boot:run
```

#### Option B: Running the Packaged JAR
```powershell
java -jar target/tuition-institute-backend-1.0.0.jar
```

*Flyway will automatically execute migrations `V1__init.sql` and `V2__seed_data.sql` upon startup, and the `DatabaseSeeder` will initialize the verified demo accounts.*

---

## 2. Interactive API Documentation (Swagger UI)

Once running, access Swagger UI in your browser:
- **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **OpenAPI 3 JSON Specification**: [http://localhost:8080/api-docs](http://localhost:8080/api-docs)

---

## 3. Pre-Seeded Demo Credentials

All seed accounts are pre-configured with the standard password: `Password@123`

| Role | Name | Email Identifier | Password | Access Scope |
| :--- | :--- | :--- | :--- | :--- |
| **Admin** (`ROLE_ADMIN`) | Dr. Alistair Vance | `admin@apex-institute.edu` | `Password@123` | Full institute overview, staff management, academic years, grading scales, audit logs |
| **Teacher** (`ROLE_TEACHER`) | Prof. Sarah Jenkins | `teacher@apex-institute.edu` | `Password@123` | Assigned classes, sessions, QR attendance scanning, marks entry, materials upload |
| **Student** (`ROLE_STUDENT`) | Alex Morgan | `student@apex-institute.edu` | `Password@123` | Own digital QR pass, class schedule, attendance records, exam results, PDF report card |
| **Parent** (`ROLE_PARENT`) | David Morgan | `parent@apex-institute.edu` | `Password@123` | Linked children overview, child attendance & marks, fee invoices, online payment & PDF receipts |

---

## 4. Key Endpoints Catalogue

### A. Authentication & Session (`/api/v1/auth`)
- `POST /api/v1/auth/login` - Authenticate with email/phone and password; returns JWT access token and sets HttpOnly refresh cookie.
- `POST /api/v1/auth/refresh` - Rotate refresh token and issue a fresh 15-minute access token.
- `POST /api/v1/auth/logout` - Invalidate refresh token and clear session cookies.
- `GET /api/v1/auth/me` - Retrieve authenticated user profile and roles.

### B. Institute Administration (`/api/v1/admin`)
- `GET /api/v1/admin/dashboard` - Operational metrics (active students, staff count, total collections, pending dues).
- `GET /api/v1/admin/staff` - List faculty and administrative staff with assigned permissions.
- `GET /api/v1/admin/classes` - List all institute classes, batches, capacity, and enrollment numbers.
- `GET /api/v1/admin/fees` - Financial registry of all issued tuition fee invoices and statuses.
- `GET /api/v1/admin/settings/academic-year` - Academic calendar terms and configurations.
- `GET /api/v1/admin/settings/grading-scale` - Active grading scales and mark thresholds.
- `GET /api/v1/admin/settings/sms-templates` - SMS notification templates with template variables.
- `GET /api/v1/admin/audit-logs` - Security, academic, and financial audit trails.

### C. Faculty & Teaching (`/api/v1/teacher`)
- `GET /api/v1/teacher/classes` - List classes assigned to the logged-in teacher.
- `GET /api/v1/teacher/sessions/today` - Today's scheduled class sessions.
- `POST /api/v1/teacher/attendance/sessions/{sessionId}/scan` - Instant QR attendance scan check-in.
- `POST /api/v1/teacher/attendance/sessions/{sessionId}` - Bulk roll-call attendance saving.
- `GET /api/v1/teacher/exams` - List exams and assessment evaluations.
- `POST /api/v1/teacher/exams` - Schedule a new exam.
- `POST /api/v1/teacher/exams/{examId}/results` - Enter marks and feedback for students (auto-grades A+, A, B, etc.).
- `GET /api/v1/teacher/materials` - List study materials.
- `POST /api/v1/teacher/materials` - Upload study notes, worksheets, and syllabus documents (multipart).

### D. Enrolled Student Portal (`/api/v1/student`)
- `GET /api/v1/student/dashboard` - Quick summary of student attendance rate, upcoming exam, notes count.
- `GET /api/v1/student/qr` - Retrieve digital student ID pass and opaque QR token for entrance scanning.
- `GET /api/v1/student/schedule` - Weekly class timetable and exam schedule.
- `GET /api/v1/student/attendance` - Detailed attendance history records.
- `GET /api/v1/student/results` - Continuous assessment grades and percentages.
- `GET /api/v1/student/report-card` - Download official PDF report card.
- `GET /api/v1/student/materials` - Access course lecture notes and downloads.

### E. Parent & Guardian Portal (`/api/v1/parent`)
- `GET /api/v1/parent/dashboard` - Overview of linked children, dues, and alerts.
- `GET /api/v1/parent/children` - List linked children.
- `GET /api/v1/parent/children/{childId}/attendance` - Attendance logs for a linked child.
- `GET /api/v1/parent/children/{childId}/results` - Marks and performance for a linked child.
- `GET /api/v1/parent/fees` - Tuition fee invoices for linked children.
- `POST /api/v1/parent/fees/{invoiceId}/pay` - Settle tuition invoice (Card / Direct Bank Transfer).
- `GET /api/v1/parent/fees/{invoiceId}/receipt` - Download official payment receipt (PDF).

### F. Real-Time Chat & Communications
- `GET /api/v1/classes/{classId}/chat` - Class discussion message history.
- `POST /api/v1/classes/{classId}/chat` - Post message to class discussion (with profanity filter).
- `WS /ws` - STOMP WebSocket endpoint subscribed to `/topic/class.{classId}`.
- `GET /api/v1/announcements` - Institute-wide and class-level notices.
- `GET /api/v1/notifications` - In-app notification centre alerts.

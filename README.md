# School Management System

A JavaFX desktop application that brings together three core systems — School, Fee, and Library — under a unified interface with role-based access for students, teachers, admins, and librarians.

---

## Systems

### School System
Handles the core of the application. Admins can enroll and remove students and teachers, manage departments, and oversee the entire platform. Teachers can manage attendance and enter student results. Students can view their result cards, attendance, and change departments.

### Fee Management System
Manages student fee records with automatic enrollment sync — when a new student is added to the school system, a fee record is created automatically. Students can view their current semester fee, payment status, late fee (calculated automatically past the due date), and download a fee challan as a PDF.

### Library System
A full library catalog accessible to all users. Students can search books by title, author, category, or year, and issue books directly from the catalog. The librarian dashboard supports adding, removing, and returning books, with availability tracking across all copies.

---

## Features

- Role-based login — Student, Teacher, Admin, Librarian
- Auto fee enrollment when a new student is registered
- Late fee auto-calculation past due date
- PDF challan generation for fee payments
- Real-time book search with availability tracking
- Dark and light theme toggle in the library
- Attendance and result management per semester
- File-based persistence for all data

---

## Tech Stack

- Java 25
- JavaFX 25
- Maven
- File I/O (txt-based storage)

---

## Project Structure

```
src/main/java/
├── com.school       — core school system (login, dashboards, students, teachers)
├── com.fee          — fee management system
├── com.library      — library system
└── com.util         — shared utilities (SceneManager, SchoolDataStore, etc.)

src/main/resources/
├── school/          — school FXML, CSS, data
├── fee/             — fee FXML, CSS, data
└── library/         — library FXML, CSS, data
```

---

## Contributors

| Name | Role |
|------|------|
| [HananShafay](https://github.com/hananshafay72442) | School System |
| [Mohid Ahmed](https://github.com/MohidAhmed6115) | Fee System (partial), Library System |
| Umair Hassan | Fee System (partial) |

---

## Status

> Active development — admin fee panel and payment history in progress.

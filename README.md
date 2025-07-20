# Job Application Tracker

A full-stack Spring Boot application for tracking job applications with secure user authentication, email verification, and user-specific data access.

## Features
- User registration with email verification
- JWT-based authentication and authorization
- Forgot password and password reset via email
- Users can create, view, update, and delete their own job applications
- Secure endpoints: users can only access their own data
- Pagination and filtering support for job applications (extendable)

## Tech Stack
- Java 17+
- Spring Boot
- Spring Security (JWT)
- Spring Data JPA (Hibernate)
- H2 (in-memory) or any SQL database
- JavaMailSender (for email)

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven

### Configuration
1. Clone the repository.
2. Configure your email settings in `src/main/resources/application.properties`:
   ```properties
   spring.mail.host=smtp.gmail.com
   spring.mail.port=587
   spring.mail.username=${MAIL_USERNAME}
   spring.mail.password=${MAIL_PASSWORD}
   spring.mail.properties.mail.smtp.auth=true
   spring.mail.properties.mail.smtp.starttls.enable=true
   ```
3. Set up .env variables for email credentials:
   ```
    MAIL_USERNAME=your_email@gmail.com
    MAIL_PASSWORD=your_app_password
   ```

### Running the Application
```sh
./mvnw spring-boot:run
```

The app will start on [http://localhost:8080](http://localhost:8080)

## API Endpoints

### Authentication
- `POST /api/auth/register` — Register a new user (username, password, email)
- `GET /api/auth/verify?token=...` — Verify email address
- `POST /api/auth/login` — Login and receive JWT token
- `POST /api/auth/forgot-password?email=...` — Request password reset
- `POST /api/auth/reset-password?token=...&newPassword=...` — Reset password

### Job Applications (JWT required)
- `GET /api/job-applications` — List current user's job applications
- `POST /api/job-applications` — Create a new job application
- `GET /api/job-applications/{id}` — Get a specific job application (if owned)
- `PUT /api/job-applications/{id}` — Update a job application (if owned)
- `DELETE /api/job-applications/{id}` — Delete a job application (if owned)

### Usage Notes
- All job application endpoints require a valid JWT token in the `Authorization: Bearer <token>` header.
- Only verified users can log in and access protected endpoints.
- Password reset and email verification links are sent via email.

## Extending
- Add pagination and filtering to job application endpoints by extending repository and service methods.
- Switch to a persistent database by updating the datasource configuration in `application.properties`.
- Add more user profile features, roles, or admin controls as needed.

## License
MIT


# 🎬 Movie Booking Application

A full-stack Movie Ticket Booking application built using Java Spring Boot, Spring Security, REST APIs, and a responsive JavaScript frontend.

---

## 🚀 Key Features

* **Spring Security Integration:** Protected endpoints with form-based authentication.
* **Custom Business Rule Validation:** Enforces booking limits using custom domain exceptions (`InvalidBookingYearException` for bookings beyond 2027).
* **Global Exception Handling:** Centralized exception mapping using `@RestControllerAdvice`.
* **Session-Managed Booking Updates:** Implemented restrictions allowing maximum 3 ticket updates within an active edit window.
* **Self-Contained Deployment:** Configured external static resource handling via `WebMvcConfigurer` to serve frontend assets directly.

---

## 🛠️ Tech Stack

* **Backend:** Java, Spring Boot, Spring Security, Spring MVC, Spring Data JPA
* **Frontend:** HTML5, CSS3, JavaScript (Fetch API)
* **Build Tool:** Maven

---

## 💻 How to Run Locally

1. Clone the repository:
   ```bash
   git clone [https://github.com/joita-ghoshal/movie-booking-application.git](https://github.com/joita-ghoshal/movie-booking-application.git)

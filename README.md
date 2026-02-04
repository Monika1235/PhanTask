🚀 Running Phantask Locally
---------------------------

Phantask is a full-stack application with a Spring Boot backend powering a React frontend. Both layers work independently but come together seamlessly to deliver a smooth task management experience.

🧠 Backend Execution (Spring Boot)
----------------------------------
The backend is the brain of Phantask 🧠 — handling authentication, authorization, business logic, and data persistence.

⚙️ Tech Stack
-------------
Java + Spring Boot
Spring Security (JWT-based authentication 🔐)
RESTful APIs
JPA / Hibernate
MySQL / PostgreSQL (configurable)

▶️ How to Run the Backend
-------------------------
Navigate to the backend project directory
Configure your database credentials in application.properties

Run the application:
--------------------
./mvnw spring-boot:run
or directly from your IDE ▶️

🌐 Backend Highlights
---------------------
Secure login & role-based access (ADMIN / USER)
Clean layered architecture (Controller → Service → Repository)
DTO-based responses for safe data transfer
Stateless JWT authentication for scalability 🚀

Once started, the backend runs on:
👉 http://localhost:8080

🔗 How Frontend & Backend Work Together
---------------------------------------

💡 The magic happens here:
React sends API requests using Axios
Spring Boot validates JWT tokens 🔐
Backend responds with structured JSON data
Frontend updates UI instantly ⚡

Both applications can run:
-------------------------
On the same machine
On different devices (as long as they’re on the same network 🌐)

🏆 Why Phantask?
----------------
✔ Real-world full-stack architecture
✔ Secure and scalable backend
✔ Modern frontend workflow
✔ Easy to run, easy to extend
✔ Perfect for learning and showcasing skills 💼

Phantask isn’t just a project — it’s a production-style full-stack experience.

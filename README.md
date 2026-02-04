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

🎨 Frontend Execution (React)
-----------------------------
The frontend is where Phantask comes to life ✨ — fast, responsive, and user-friendly.

⚙️ Tech Stack
-------------
React
Vite (⚡ lightning-fast dev server)
Axios for API calls
Modern component-based UI

▶️ How to Run the Frontend
--------------------------
Navigate to the frontend project directory
Install dependencies:
npm install
Start the development server:
npm run dev

🌟 Frontend Highlights
----------------------
Clean and responsive UI 📱💻
Real-time interaction with backend APIs
Secure token-based communication
Smooth UX designed for productivity

Frontend runs on:
----------------
👉 http://localhost:5173 (default Vite port)

🔗 How Frontend & Backend Work Together
---------------------------------------
💡 The magic happens here:
React sends API requests using Axios
Spring Boot validates JWT tokens 🔐
Backend responds with structured JSON data
Frontend updates UI instantly ⚡

Both applications can run:
-------------------------
1. On the same machine
2. On different devices (as long as they’re on the same network 🌐)

🏆 Why Phantask?
----------------
✔ Real-world full-stack architecture
✔ Secure and scalable backend
✔ Modern frontend workflow
✔ Easy to run, easy to extend
✔ Perfect for learning and showcasing skills 💼

Phantask isn’t just a project — it’s a production-style full-stack experience.

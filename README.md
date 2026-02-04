🚀 Running Phantask Locally
---------------------------

Phantask is a full-stack application with a Spring Boot backend powering a React frontend. Both layers work independently but come together seamlessly to deliver a smooth task management experience.

🧠 Backend Execution (Spring Boot)
----------------------------------
The backend is the brain of Phantask 🧠 — handling authentication, authorization, business logic, and data persistence.

⚙️ Tech Stack
-------------
1. Java + Spring Boot
2. Spring Security (JWT-based authentication 🔐)
3. RESTful APIs
4. JPA / Hibernate
5. MySQL / PostgreSQL (configurable)

▶️ How to Run the Backend
-------------------------
1. Navigate to the backend project directory
2. Configure your database credentials in application.properties

Run the application:
--------------------
./mvnw spring-boot:run
or directly from your IDE ▶️

🌐 Backend Highlights
---------------------
1. Secure login & role-based access (ADMIN / USER)
2. Clean layered architecture (Controller → Service → Repository)
3. DTO-based responses for safe data transfer
4. Stateless JWT authentication for scalability 🚀

Once started, the backend runs on:
---------------------------------
👉 http://localhost:8080

🎨 Frontend Execution (React)
-----------------------------
The frontend is where Phantask comes to life ✨ — fast, responsive, and user-friendly.

⚙️ Tech Stack
-------------
1. React
2. Vite (⚡ lightning-fast dev server)
3. Axios for API calls
4. Modern component-based UI

▶️ How to Run the Frontend
--------------------------
1. Navigate to the frontend project directory
2. Install dependencies: npm install
3. Start the development server: npm run dev

🌟 Frontend Highlights
----------------------
1. Clean and responsive UI 📱💻
2. Real-time interaction with backend APIs
3. Secure token-based communication
4. Smooth UX designed for productivity

Frontend runs on:
----------------
👉 http://localhost:5173 (default Vite port)

🔗 How Frontend & Backend Work Together
---------------------------------------
💡 The magic happens here:
1. React sends API requests using Axios
2. Spring Boot validates JWT tokens 🔐
3. Backend responds with structured JSON data
4. Frontend updates UI instantly ⚡

Both applications can run:
-------------------------
1. On the same machine
2. On different devices (as long as they’re on the same network 🌐)

🏆 Why Phantask?
----------------
1. Real-world full-stack architecture
2. Secure and scalable backend
3. Modern frontend workflow
4. Easy to run, easy to extend
5. Perfect for learning and showcasing skills 💼

Phantask isn’t just a project — it’s a production-style full-stack experience.

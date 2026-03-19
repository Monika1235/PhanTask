# 🚀 Phantask - Full Stack Task Management Application

Phantask is a full-stack application with a Spring Boot backend powering a React frontend. Both layers work independently but come together seamlessly to deliver a smooth task management experience.

---

## 🧱 Architecture Overview

Frontend (React - Vite :5173)
        ↓
Backend (Spring Boot :9090 inside container)
        ↓
MySQL (3306 inside container)

---

## 🧠 Backend (Spring Boot)
The backend is the brain of Phantask 🧠 — handling authentication, authorization, business logic, and data persistence.
### ⚙️ Tech Stack
- Java8 + Spring Boot
- Spring Security (JWT-based authentication 🔐)
- RESTful APIs
- JPA / Hibernate
- MySQL
- Docker
---
### 🐳 Running with Docker

- Step 1: Start all services
```bash
$sudo docker-compose up --build

- Step 2: Verify containers

$sudo docker ps

#### Expected containers:
- phantask-mysql
- phantask-backend
- phantask-frontend

- Step 3: Check backend logs
  
$sudo docker logs phantask-backend

#### Look for:
Tomcat initialized with port 9090

👉 This confirms backend is running correctly inside container

---
### 🌐 Backend Highlights

- Secure login & role-based access (ADMIN / USER)
- Clean layered architecture (Controller → Service → Repository)
- DTO-based responses for safe data transfer
- Stateless JWT authentication for scalability

---

🎨 Frontend Execution (React)
-----------------------------
The frontend is where Phantask comes to life ✨ — fast, responsive, and user-friendly.

⚙️ Tech Stack
-------------
1. React
2. Vite (⚡ lightning-fast dev server)
3. Axios for API calls
4. Modern component-based UI

🌟 Frontend Highlights
----------------------
1. Clean and responsive UI 📱💻
2. Real-time interaction with backend APIs
3. Secure token-based communication
4. Smooth UX designed for productivity

Open:
----
http://localhost:5173

Test:
-----
Signup

Login

API calls in browser DevTools → Network tab

👉 Find your private IP:
-----------------------
Linux / macOS: ip a or ifconfig
Windows: ipconfig

Open:
----
http://<your-IP>:5173

Mobile Testing:
---------------
Signup

Login

check logs via: 

chrome://inspect/#devices

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

Built with care and real-world practices.
If Phantask added value to you, don’t forget to ⭐ the repository!

Also added the workflow script to automate Build, Test, and Deploy -> maven.yml

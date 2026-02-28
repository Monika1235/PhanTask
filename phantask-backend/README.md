Use Case 1: Login (with first-login / password change check)
------------------------------------------------------------
Title: Force password change on first login

Actors:
------
 Primary Actor: User(whose account is created)
 System: Authentication Service

Preconditions:
--------------
 1.User exists in the database
 2.User’s first_login flag is 1 (or true)

Flow:
-----
 1.User sends a POST /api/auth/login request with username & password.
 2.System authenticates the user.
 3.System checks first_login flag:
   If true, respond with requirePasswordChange: true
   If false, respond with JWT tokens and user roles.

Postconditions:
---------------
User is either prompted to change password (first login) or successfully logged in.

Execute login request with curl:
curl -v -X POST http://localhost:8080/api/auth/login \
-H "Content-Type: application/json" \
-d '{
  "username": "admin",
  "password": "Admin@123"
}'

Output1:
-------
{"requirePasswordChange":true,"message":"Password change required before login"}

Output2:
-------
{"role":["ADMIN"],
"refreshToken":"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2Mzc2MDI4MiwiZXhwIjoxNzY0MzY1MDgyfQ.FIwVWuwC_cd0R0fVr0E0BnUVyHz6IPjfJ8u0NfDQEiI",
"token":"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2Mzc2MDI4MiwiZXhwIjoxNzYzNzYzODgyfQ.3lu425aoyskgH5EO9y_HnoqfGckhLqiLT-s1A2N4xrE",
"requirePasswordChange":false
}

Use Case 2: Refresh JWT Access Token
------------------------------------
Title: Extend user session by refreshing access token using a refresh token.

Actors:
-------
 Primary Actor: Authenticated User (with valid refresh token)
 System: Authentication Service (Spring Boot + JWT)

Preconditions:
--------------
 1.User is already registered and exists in the database.
 2.User has a valid refresh token issued from a prior login.
 3.Refresh token has not expired.

Postconditions:
--------------
 1.A new access token is issued.
 2.Old refresh token remains valid (optional: can rotate refresh token if implemented).

Description / Flow:
------------------
 1.User sends a POST request to /api/auth/refresh-token with the refresh token.
 2.System extracts the username from the refresh token.
 3.System loads user details from the database.
 4.System validates the refresh token for integrity and expiration.
 5.System generates a new access token.
 6.System returns the new access token to the user.

Execute refresh-token request with curl:
curl -v -X POST http://localhost:8080/api/auth/refresh-token \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2Mzc2MDI4MiwiZXhwIjoxNzY0MzY1MDgyfQ.FIwVWuwC_cd0R0fVr0E0BnUVyHz6IPjfJ8u0NfDQEiI"

Output:
------
{"token":"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2Mzc2Mjg0NSwiZXhwIjoxNzYzNzY2NDQ1fQ.ErpVHRGqpuVq0IiyIcHlYpQELX6mOU6OJxOjemtamQA"}

Exceptions / Alternate Flows:
-----------------------------
 1.Invalid token: System responds with 401 Unauthorized.

 2.Expired token: System responds with 401 Unauthorized and message “Refresh token expired. Please login again.”

 Execute refresh-token request with curl:
 curl -v -X POST http://localhost:8080/api/auth/refresh-token \
 -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVzIjpbIkFETUlOIl0sImlhdCI6MTc2Mzc2NTgyMywiZXhwIjoxNzYzNzY1ODUzfQ.wg-DFqu0OtTgmFTq89eVJa3SCtGNEbe0DTwyRkWGrNA"

 Output:
 -------
 < HTTP/1.1 401 
 < X-Content-Type-Options: nosniff
 < X-XSS-Protection: 0
 < Cache-Control: no-cache, no-store, max-age=0, must-revalidate
 < Pragma: no-cache
 < Expires: 0
 < X-Frame-Options: DENY
 < Content-Type: application/json
 < Transfer-Encoding: chunked
 < Date: Fri, 21 Nov 2025 23:23:28 GMT 
 * Connection #0 to host localhost left intact
 {"message":"Refresh token has expired"}

 3.User not found: System responds with 404 Not Found


Use Case 3: Change Password on First Login
------------------------------------------
Title: Force a user to update their temporary password on first login.

Actors:
-------
Primary Actor: User / Admin (with temporary password)

Preconditions:
--------------
1.User account exists in the database.
2.User has firstLogin = true.
3.User knows the temporary password assigned by the admin.

Postconditions:
--------------
1.User password is updated in the database (BCrypt encoded).
2.firstLogin flag is set to false.
3.User can log in with the new password.

Description / Flow:
-------------------
1.User sends a POST request to /api/users/change-password-first-login with their username, old password, and new password.
2.System retrieves the user from the database by username.
3.System validates the old password against the stored encoded password.
4.System validates the new password against security rules (minimum 8 characters, at least 1 uppercase, 1 lowercase, 1 digit).
5.System ensures the new password is different from the old password.
6.System encodes the new password using BCrypt and updates the user record.
7.System sets firstLogin = false and records the password change timestamp.
8.System returns a success message to the user.

Execute request with curl:
curl -v -X POST http://localhost:8080/api/users/change-password-first-login \
-H "Content-Type: application/json" \
-d '{
  "username": "admin",
  "oldPassword": "Admin@123",
  "newPassword": "User@1234"
}'

Output:
------
"Password changed successfully"

Use Case 4: Create Account
-----------------------------------
Title: Admin creates a new account with a temporary password.

Actors:
-------
Primary Actor: Admin

Preconditions:
--------------
1.Admin account already exists and it is logged in.
2.Admin has the required privileges to create other user's accounts.
3.Role exists in the database.

Postconditions:
---------------
1.A new user account is created in the database.
2.The account has a temporary password (Temp@123).
3.firstLogin flag is set to true for the user.

Description / Flow:
-------------------
1.Admin sends a POST request to /api/users/create-account with the user’s email and role-name.
2.System checks if the username already exists.
3.System creates a new User entity with:
  1.username from request
  2.Password set to Temp@123 (BCrypt encoded)
  3.enabled = true
  4.firstLogin = true
4.System assigns the role to the new user.
5.System saves the user in the database.
6.System returns a success message along with the temporary password.

Execute request with curl:
curl -v -X POST http://localhost:8080/api/users/create-account \
-H "Content-Type: application/json" \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..." \
-d '{ "email": "newuser@example.com", "role":"HR" }'

Output:
-------
"Account created successfully. Username: newuser, Temporary password: Temp@123"

Use Case 5: Get User's Profile
------------------------------
Title : User wants to see his/her profile

Actors:
-------
Primary Actor: Authenticated User

Preconditions:
--------------
1.User is logged in with a valid JWT token.
2.UserProfile may or may not exist in the database.

Postconditions:
--------------
1.If profile exists → existing profile data is returned.
2.If profile does NOT exist → system creates an empty profile row and returns default values.
3.API returns a well-structured UserProfileResponse object.

Description / Flow:
-------------------
1.User sends a GET request to /api/users/profile with the JWT token.
2.System extracts the username from Authentication object.
3.System checks if the user exists in the users table.
4.If profile does not exist:
  1.System creates a new empty UserProfile
  2.Sets default values ("") for all profile fields
5.System converts the data into a UserProfileResponse DTO.
6.System returns:
  1.Basic user info (id, username, email, roles)
  2.Profile info (name, phone, department, yearOfStudy, photoUrl)

Execute request with curl:
curl -v -X GET http://localhost:8080/api/profile \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."

Output1:
--------
{
  "userId": 2,
  "username": "amrita",
  "email": "amrita@gmail.com",
  "role": "HR MANAGER",
  "fullName": "Amrita Patil",
  "department": "IT",
  "phone": "475767877",
  "photoUrl": "",
  "yearOfStudy": ""
}

Output2:
--------
{
  "userId": 10,
  "username": "alok",
  "email": "alok@gmail.com",
  "role": "TECH LEAD",
  "fullName": "",
  "department": "",
  "phone": "",
  "photoUrl": "",
  "yearOfStudy": ""
}

Use Case 6: Deactivate User
---------------------------
Title: Admin deactivates a user account

Actors:
-------
Primary Actor: Admin

Preconditions:
---------------
Admin is logged in with a valid JWT token.
Admin has ADMIN authority.
The target user exists and is currently active (enabled = true).

Postconditions:
---------------
The target user account is marked as inactive (enabled = false).
deactivatedAt timestamp is set to the current date/time.
The user can no longer log in until reactivated.

Description / Flow:
-------------------
Admin sends a PUT request to /api/users/{id}/deactivate.
System verifies that the caller has ADMIN authority.
System checks if the user with the given ID exists and is active.
System updates the user entity:
enabled = false
deactivatedAt = LocalDateTime.now()
System saves the updated user entity in the database.
System returns a success message confirming deactivation.

Execute request with curl:
curl -v -X PUT http://localhost:8080/api/users/5/deactivate \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."

Output:
-------
{ "message": "User account deactivated successfully", "userId": 5 }

Use Case 7: Get All Active Users
--------------------------------
Title: Admin retrieves all active users

Actors:
-------
Primary Actor: Admin

Preconditions:
--------------
Admin is logged in with a valid JWT token.
Admin has ADMIN authority.

Postconditions:
---------------
Returns a list of all users where enabled = true.
Each user is represented as a UserResponse DTO with roles included.

Description / Flow:
-------------------
Admin sends a GET request to /api/users/active.
System verifies that the caller has ADMIN authority.
System queries the database for all users with enabled = true.
System converts each user entity to a UserResponse DTO:
id, username, email, roles, enabled
System returns the list of active users to the client.

Execute request with curl:
curl -v -X GET http://localhost:8080/api/users/active \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."

Output:
-------
   [{
        "uid": 1,
        "username": "bob",
        "email": "bob@example.com",
        "roles": [
            "HR MANAGER"
        ],
        "enabled": true
    },
    {
        "uid": 2,
        "username": "admin",
        "email": "admin@example.com",
        "roles": [
            "ADMIN"
        ],
        "enabled": true
    },
    {
        "uid": 8,
        "username": "newuser",
        "email": "newuser@gmail.com",
        "roles": [
            "HR MANAGER"
        ],
        "enabled": true
    }]
    
Use Case 8: Generate Attendance QR Code
---------------------------------------
Title: User generates QR code from Attendance Page

Actors:
------
Primary Actor: Any User other than Admin

Preconditions:
-------------
User is logged in with a valid JWT token.
User account is enabled.
Attendance for the current day is not already completed.
Backend service is running.

Postconditions:
---------------
A new QR token is generated on the UI.
QR token is stored in the backend with:
user
date
expiry time
used = false
Any previously active QR token for the day is invalidated.

Description / Flow:
------------------
User logs in (mobile or laptop).
User opens the Attendance page.
System generates a random QR token on the UI.
UI sends the QR token to the backend for registration.
Backend validates user and attendance state.
Backend stores the QR token with a short expiry.

API Details:
------------
Endpoint
POST /api/attendance/token/register

Headers
Authorization: Bearer <JWT_TOKEN>

Request Body
{
  "token": "QR-abc123xyz"
}

Expected Result:
---------------
QR code is visible on UI.
Token expires after configured time (e.g., 5 minutes).

Use Case 9: Mark Attendance (Check-In / Check-Out)
--------------------------------------------------
Title: User marks attendance by scanning QR code

Actors:
-------
Primary Actor: User (Employee)
Secondary Actor: Admin / HR (monitoring)

Preconditions:
-------------
QR token is valid and not expired.
User is logged in on mobile device.
Camera access is granted (HTTPS or localhost).
QR token has not been used before.

Postconditions:
---------------
First scan → user is checked in
Second scan → user is checked out
Attendance record is created or updated
QR token is marked as used

Description / Flow:
-------------------
First Scan (Check-In)

User opens QR scanner on laptop.
User scans the QR code shown on mobile.
System validates the QR token.
No attendance exists for today.
System records check-in time.
Attendance status is set to CHECKED_IN.

Second Scan (Check-Out)

User scans a new QR code.
System validates the QR token.
Existing attendance record is found.
System calculates worked duration.
System records check-out time.

Business Rules
If worked duration ≥ 8 hours → normal checkout.
If worked duration < 8 hours → early checkout:
Email notification is sent to manager.

API Details:
-----------
Endpoint
POST /api/attendance/mark

Request Body
{
  "token": "QR-abc123xyz"
}

Possible Responses
Success (Check-In / Check-Out)
{
  "status": "CHECKED_OUT",
  "checkInTime": "2026-02-04T09:30:00",
  "checkOutTime": "2026-02-04T18:10:00"
}

Error – Attendance Already Completed
{
  "status": 409,
  "message": "Attendance already completed for today"
}

Use Case 10: Early Checkout Notification
----------------------------------------
Title: System notifies manager on early checkout

Actors:
------
Primary Actor: System
Secondary Actor: Manager

Preconditions:
-------------
User attempts checkout before completing 8 hours.
User has a mapped manager with valid email.
SMTP configuration is valid.

Postconditions:
--------------
Email is sent to the manager.

Description / Flow:
------------------
User performs second scan.
System calculates total worked minutes.
System detects early checkout.
System triggers email notification.
Manager receives early checkout alert.

Sample Email Content
Subject
Early Checkout Alert – <Employee Name>

Body

Employee <Name> has checked out early today.

Check-in Time : 09:45 AM
Check-out Time: 04:20 PM
Worked Duration: 6h 35m
Required Duration: 8h

Use Case 11: Get My Attendance Records
--------------------------------------
Title:User retrieves own attendance history

Actors:
------
Primary Actor: User

Preconditions:
--------------
User is logged in.
User has attendance records.

Postconditions:
---------------
System returns attendance records for the logged-in user.

API Details
-----------
Endpoint
GET /api/attendance/my

Headers
Authorization: Bearer <JWT_TOKEN>

Use Case 12: System Marks Absent Users
--------------------------------------
Title: System marks absent users automatically

Actors:
------
Primary Actor: System (Scheduler)

Preconditions:
--------------
Time is 11:05 PM (configured cron job).
User has no attendance record for the day.

Postconditions:
--------------
Attendance record is created with status ABSENT.

Description / Flow:
-------------------
Scheduled job runs daily.
System fetches all active users.
Users without attendance records are marked absent.
Records are saved in database.

Testing Scenario (Two Devices):
-------------------------------

Device 1: Admin (Laptop)
------------------------
Scans QR code
Camera access via HTTPS / localhost
Marks check-in and check-out
Receives early checkout alerts (manager role)

Device 2: User (Mobile):
-----------------------
Generates QR code
Monitors attendance

Authorization Rules:
-------------------
Attendance marking requires valid QR token.
Attendance data access is restricted to logged-in users.
Admin-only endpoints are protected by role checks.
    

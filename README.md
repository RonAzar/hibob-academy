🛠️ Employee Feedback System

Overview

The Employee Feedback System is a backend service that facilitates employee feedback collection, either anonymously or identified. It allows HR or managers to review and respond to feedback, improving transparency and employee satisfaction within the organization. The system manages users, roles, and feedback with JWT-based authorization.

The project file can be found at the following path:
hibob-academy-->src-->main-->kotlin-->com-->hibob-->academy-->employee_feedback_feature


📂 Project Structure

Here’s a breakdown of the main files and their purposes:

	DAO Layer (Data Access Object)
	•	EmployeeDao.kt: Manages employee CRUD operations.
	•	FeedbackDao.kt: Handles feedback CRUD operations.
	•	FeedbackResponseDao.kt: Deals with CRUD operations for feedback responses.
	Service Layer
	•	EmployeeAuthenticationService.kt: Implements authentication logic for employees.
	•	FeedbackService.kt: Manages feedback logic, including submission, history retrieval, and filtering.
	•	FeedbackResponseService.kt: Implements logic for managing feedback responses.
	Resource Layer (REST API Endpoints)
	•	EmployeeAuthResource.kt: Handles login and logout functionality.
	•	FeedbackResource.kt: Manages feedback submission, updating, and retrieval endpoints.
	•	ResponseResource.kt: Manages HR responses to feedback.
	Utility and Validator Files
	•	RolePermissionValidator.kt: Ensures appropriate permissions for users based on their role.
	•	ValidateSubmission.kt: Handles the validation of feedback submissions.
	•	AuthenticationFilter.kt: Provides middleware for validating JWT tokens in API requests.
	SQL Files (Migration Layer)
	•	V202409231005__company_table.sql: Defines the company table.
	•	V202409231010__employees_table.sql: Defines the employees table.
	•	V202409231020__feedback_table.sql: Defines the feedback table and indexes.
	•	V202409251323__response_table.sql: Defines the response table and related indexes.
	•	add_unique_index_to_employees_table.sql: Adds a unique index to the employees table.
	•	drop_idx_feedback_is_anonymous.sql: Drops the is_anonymous index from the feedback table.

🚀 How to Run the Project
	1.	Clone the Repository:
  `git clone https://github.com/your-repo/employee-feedback-system.git`
  `cd employee-feedback-system`

2.	Database Setup
Ensure PostgreSQL is installed. Then run the provided SQL scripts to create the necessary tables.
  `psql -U <your-username> -d <your-database> -f V202409231005__company_table.sql`
  `psql -U <your-username> -d <your-database> -f V202409231010__employees_table.sql`
  `psql -U <your-username> -d <your-database> -f V202409231020__feedback_table.sql`

3.	Gradle Setup
The project uses Gradle as the build tool. The Gradle Wrapper is provided to ensure the correct Gradle version is used.
	•	To download and install Gradle, run the wrapper script:
  `./gradlew wrapper`

	•	To build the project, run:
  `./gradlew build`

4.	Run the Application
Start the application with the following command:
   `./gradlew bootRun`

5.	Access the API
Use Postman or a similar tool to interact with the endpoints:
	•	Login: /api/auth/employee/login
	•	Submit Feedback: /api/feedback/submit
	•	Get Feedback History: /api/feedback/history
	•	Submit Response: /api/response/submit

6.	Run Tests
To run the unit tests for the DAO and service layers:
  `./gradlew test`

🧪 Testing

	•	Unit Tests: DAO and service layers are thoroughly tested to ensure the system handles CRUD operations and business logic correctly.
	•	Manual Testing: APIs can be tested using Postman to ensure proper functioning.

🔒 Security

	•	JWT Authentication: All API endpoints are secured via JWT tokens, ensuring only authorized users can perform actions.
	•	Role-Based Access Control: Permissions are validated based on roles (e.g., Admin, HR, Employee).

💡 Key Features

	•	Anonymous Feedback: Employees can submit feedback anonymously to foster honest communication.
	•	Feedback Filtering: HR/Admin can filter feedback based on various criteria like department, feedback date, etc.
	•	Role Management: Ensures appropriate access for different user roles (e.g., HR, Admin).

🛠 Topics Provided

	•	Kotlin
	•	jOOQ (Database Operations)
	•	PostgreSQL (DBMS)
	•	JWT (Authentication)
	•	REST API
	•	Unit Testing
	•	Gradle

More detailed API documentation can be found in the HLD-Employee Feedback System.pdf.

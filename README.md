PatientManagement

PatientManagement is a backend-centric, microservices-based application designed to manage patient workflows and healthcare operations efficiently. It is modular, scalable, and built with a focus on clean architecture and service decoupling.

The application provides key services such as patient management, billing, analytics, and authentication, with service discovery and containerized deployment support.

Architecture Overview

The system is designed using a microservices architecture, where each service is independently deployable and communicates via REST APIs. The architecture includes:

Module	Description
ApiGateway	Central entry point for routing client requests to internal microservices.
AuthService	Manages authentication, authorization, and security of the platform.
BillingManagement	Handles billing, invoicing, and payment processes for patient care.
EurekaServer	Service registry enabling service discovery for microservices.
analytics-service	Collects and processes analytics and metrics for operational insights.
patientService	Core service containing patient domain logic and CRUD operations.
docker-compose.yml	Defines local orchestration for running all services with Docker Compose.
Technology Stack

Backend: Java 11+, Spring Boot, Spring Cloud

Service Discovery: Eureka Server

Containerization: Docker & Docker Compose

APIs: RESTful HTTP APIs

Build & Dependency Management: Maven

Prerequisites

Before running the application locally, ensure the following are installed:

Java JDK 11 or higher

Docker & Docker Compose

Maven 3.6 or higher


Also Reccomended to Check the yml file of Analytics service and put your own detaile if not the mail service will throw errors 
thank you 

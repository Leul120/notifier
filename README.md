# SafeStatusNotifier

A **Spring Boot 3.4.4** backend service providing real-time device status monitoring with secure user access control, GraphQL API, and JWT-based authentication. The system enables users to track device metrics (battery, connectivity, online status) and manage access relationships between monitoring parties.

---

## Table of Contents

- [What This App Does (Plain English)](#what-this-app-does-plain-english)
- [Architecture Overview](#architecture-overview)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [Entity Relationship Model](#entity-relationship-model)
- [Security Architecture](#security-architecture)
- [GraphQL Schema](#graphql-schema)
- [API Endpoints](#api-endpoints)
- [Configuration](#configuration)
- [Deployment](#deployment)
- [Database Schema](#database-schema)

---

## What This App Does (Plain English)

### The Big Picture
SafeStatusNotifier is like a "Find My Friends" meets "Find My iPhone" - but for keeping track of people you care about. It lets you:

- **Check if someone's phone is online or offline**
- **See their battery level** (so you know if they're about to go dark)
- **Know what type of network they're on** (WiFi, 4G, 5G)
- **Send and receive alerts** when something important happens

### Real-World Use Cases

| Scenario | How It Helps |
|----------|--------------|
| **Parent tracking child** | Mom wants to know if her teenager's phone is dying before they drive home late |
| **Elderly care** | Adult children can monitor if elderly parent's phone is on and charged |
| **Field workers** | Manager checks if delivery drivers have connectivity issues |
| **Safety check-ins** | Friend wants to know if someone made it home safely (online = likely home) |
| **Travel companions** | Group traveling together can see if someone lost signal or power |

### How It Works (Simple Version)

1. **Person A** installs an app on their phone that sends status updates (battery %, online/offline, network type) to this server
2. **Person B** asks for permission to see Person A's status
3. **Person A approves** - now Person B can check if Person A's phone is online, how much battery is left, etc.
4. If Person A's battery drops below a threshold or they go offline, Person B gets a notification

### Privacy First
- Nobody can see your status unless you **explicitly approve** them
- You can **revoke access** anytime
- All data is encrypted and requires login
- You see who is monitoring you and who you are monitoring

### Key Terms for Non-Developers

| Term | Simple Explanation |
|------|-------------------|
| **Device Status** | Is the phone on/off, battery %, WiFi or cellular |
| **Access Request** | Someone asking "Can I see your phone status?" |
| **Notification** | An alert sent when something important happens (low battery, went offline) |
| **Monitor/Requester** | The person who wants to check on someone else |
| **Target** | The person being checked on |
| **Approve/Deny** | Saying yes or no to an access request |

---

## Architecture Overview

```
┌─────────────────────────────────────────────────────────────────┐
│                        Client Layer                              │
│         (Mobile App / Web Dashboard / Monitoring UI)            │
└──────────────────────┬──────────────────────────────────────────┘
                       │ HTTPS
┌──────────────────────▼──────────────────────────────────────────┐
│                     API Gateway                                  │
│              (Spring Boot 3.4.4 + Embedded Tomcat)              │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐ │
│  │   GraphQL API   │  │   REST Auth     │  │   JWT Filter    │ │
│  │   (Data Fetch)  │  │   (Login/Reg)   │  │  (Validation)   │ │
│  └────────┬────────┘  └─────────────────┘  └─────────────────┘ │
├───────────┼──────────────────────────────────────────────────────┤
│           Service Layer (Business Logic)                         │
│  ┌──────────────┐ ┌──────────────┐ ┌──────────────┐             │
│  │ UserService  │ │ DeviceStatus │ │ Notification │             │
│  │              │ │   Service    │ │   Service    │             │
│  └──────────────┘ └──────────────┘ └──────────────┘             │
│  ┌──────────────┐ ┌──────────────┐ ┌──────────────┐             │
│  │AccessService │ │Authentication│ │   JwtService │             │
│  │              │ │   Service    │ │              │             │
│  └──────────────┘ └──────────────┘ └──────────────┘             │
├──────────────────────────────────────────────────────────────────┤
│           Data Access Layer (Spring Data JPA)                    │
│  ┌──────────────┐ ┌──────────────┐ ┌──────────────┐             │
│  │UserRepository│ │DeviceStatus  │ │ Notification │             │
│  │              │ │  Repository  │ │  Repository  │             │
│  └──────────────┘ └──────────────┘ └──────────────┘             │
│  ┌──────────────┐ ┌──────────────┐                              │
│  │AccessRelation│ │AccessRelation│                              │
│  │  Repository  │ │  Repository  │                              │
│  └──────────────┘ └──────────────┘                              │
└──────────────────────────────────────────────────────────────────┘
                       │ JDBC
┌──────────────────────▼──────────────────────────────────────────┐
│              PostgreSQL 14+ (Managed via Render)                  │
└──────────────────────────────────────────────────────────────────┘
```

---

## Technology Stack

| Layer | Technology | Version |
|-------|-----------|---------|
| **Framework** | Spring Boot | 3.4.4 |
| **Language** | Java | 21 (LTS) |
| **Web Layer** | Spring Web MVC | 6.x |
| **Data Access** | Spring Data JPA | 3.x |
| **Security** | Spring Security | 6.x |
| **API Protocol** | GraphQL (Spring Boot Starter) | 3.x |
| **Database** | PostgreSQL | 14+ |
| **Authentication** | JWT (JJWT) | 0.11.5 |
| **Utilities** | Apache Commons Lang3 | 3.x |
| **DateTime** | GraphQL DateTime Starter | 6.0.0 |
| **Build Tool** | Maven | 3.9.6 |
| **Container** | Docker | 24.x |
| **Deployment** | Render (Cloud) | - |

---

## Project Structure

```
safeStatusNotifier/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/safeStatusNotifier/safeStatusNotifier/
│   │   │       ├── SafeStatusNotifierApplication.java    # Bootstrap
│   │   │       ├── config/                               # Configurations
│   │   │       │   ├── SecurityConfiguration.java          # Security chain
│   │   │       │   ├── JwtAuthenticationFilter.java        # JWT filter
│   │   │       │   ├── CustomCorsConfiguration.java        # CORS policy
│   │   │       │   ├── GraphQLGlobalExceptionHandler.java # Error handling
│   │   │       │   └── TaskSchedulerConfig.java            # Scheduler
│   │   │       ├── controllers/                          # API Controllers
│   │   │       │   ├── AuthenticationController.java       # REST Auth
│   │   │       │   ├── UserController.java                 # GraphQL User
│   │   │       │   ├── DeviceStatusController.java         # GraphQL Device
│   │   │       │   ├── NotificationController.java         # GraphQL Notify
│   │   │       │   ├── AccessController.java               # GraphQL Access
│   │   │       │   └── AdminController.java                # Admin Ops
│   │   │       ├── entity/                               # JPA Entities
│   │   │       │   ├── User.java                           # Core User
│   │   │       │   ├── DeviceStatus.java                   # Device metrics
│   │   │       │   ├── AccessRelationship.java           # Access grants
│   │   │       │   ├── StatusNotification.java           # Notifications
│   │   │       │   └── Role.java                         # Role enum
│   │   │       ├── repositories/                         # Data Layer
│   │   │       │   ├── UserRepository.java
│   │   │       │   ├── DeviceStatusRepository.java
│   │   │       │   ├── AccessRelationshipRepository.java
│   │   │       │   └── StatusNotificationRepository.java
│   │   │       ├── requests/                             # DTOs/Requests
│   │   │       │   ├── SignUpRequest.java
│   │   │       │   ├── SignInRequest.java
│   │   │       │   ├── DeviceStatusUpdateRequest.java
│   │   │       │   ├── GrantAccessRequest.java
│   │   │       │   └── StatusNotificationDto.java
│   │   │       └── services/                             # Business Logic
│   │   │           ├── interfaces/                         # Contracts
│   │   │           │   ├── UserService.java
│   │   │           │   ├── DeviceStatusService.java
│   │   │           │   ├── NotificationService.java
│   │   │           │   ├── AccessService.java
│   │   │           │   ├── AuthenticationService.java
│   │   │           │   └── JwtService.java
│   │   │           └── impl/                               # Implementations
│   │   │               ├── UserServiceImpl.java
│   │   │               ├── DeviceStatusServiceImpl.java
│   │   │               ├── NotificationServiceImpl.java
│   │   │               ├── AccessServiceImpl.java
│   │   │               ├── AuthenticationServiceImpl.java
│   │   │               └── JwtServiceImpl.java
│   │   └── resources/
│   │       ├── application.properties                    # Config
│   │       └── graphql/                                  # Schema Files
│   │           ├── user.graphqls
│   │           ├── auth.graphqls
│   │           ├── deviceStatus.graphqls
│   │           ├── statusNotification.graphqls
│   │           └── accessRelationship.graphqls
│   └── test/
│       └── java/                                         # Unit Tests
├── dockerfile                                            # Container spec
├── pom.xml                                               # Maven config
└── README.md                                             # Documentation
```

---

## Entity Relationship Model

```
┌──────────────────┐       ┌─────────────────────┐       ┌──────────────────┐
│      _user       │       │    device_statuses  │       │   notifications  │
├──────────────────┤       ├─────────────────────┤       ├──────────────────┤
│ PK  id UUID      │1     *│ PK  id UUID         │       │ PK  id UUID      │
│     name         ├───────┤ FK  user_id UUID    │       │ FK  user_id UUID │
│     email UNIQUE │       │     isOnline        │       │     title        │
│     phone        │       │     batteryLevel    │       │     message      │
│     password     │       │     networkType     │       │     type ENUM    │
│     role ENUM    │       │     lastSeen        │       │     timestamp    │
└──────────────────┘       └─────────────────────┘       │     isRead       │
         │                                                 └──────────────────┘
         │
         │1               ┌─────────────────────────┐
         │                │   access_relationships  │
         │*               ├─────────────────────────┤
         ├───────────────►│ PK  id UUID             │
         │  (requester)   │ FK  requester_id UUID   │
         │                │ FK  target_id UUID      │
         │                │     status ENUM         │
         │                │     createdAt           │
         │                └─────────────────────────┘
         │
         │  * (target)
         └───────────────►
```

### Entity Descriptions

| Entity | Purpose | Key Fields |
|--------|---------|------------|
| **User** | Core identity implementing `UserDetails` | UUID `id`, email (unique), password (bcrypt), phone, `Role` |
| **DeviceStatus** | Real-time device telemetry | `isOnline`, `batteryLevel` (0-100), `networkType`, `lastSeen` |
| **StatusNotification** | Alert system for status changes | `title`, `message`, `type` (INFO/SUCCESS/WARNING/ERROR), `isRead` |
| **AccessRelationship** | Bilateral monitoring permissions | `requester` ↔ `target`, `status` (PENDING/APPROVED/DENIED) |
| **Role** | Authorization enum | `ADMIN`, `USER` |

---

## Security Architecture

### JWT Token Flow

```
Client                                                Server
  │                                                     │
  │ POST /api/auth/signin                               │
  │ {email, password}                                   │
  ├────────────────────────────────────────────────────►│
  │                                                     │
  │                           ┌───────────────┐        │
  │                           │ BCrypt Verify │        │
  │                           └───────┬───────┘        │
  │                                   │                │
  │                           ┌───────▼───────┐        │
  │                           │ Generate JWT  │        │
  │                           │ (HS256, 24h)  │        │
  │                           └───────┬───────┘        │
  │                                                     │
  │◄────────────────────────────────────────────────────┤
  │ {accessToken, user}                                 │
  │                                                     │
  │───────────────┐                                     │
  │ Store Token   │                                     │
  │───────────────┘                                     │
  │                                                     │
  │ Authorization: Bearer <token>                       │
  ├────────────────────────────────────────────────────►│
  │                           ┌───────────────┐        │
  │                           │ Validate JWT  │        │
  │                           │ Extract Claims│        │
  │                           └───────┬───────┘        │
  │                                   │                │
  │                           ┌───────▼───────┐        │
  │                           │ Load UserDetails      │
  │                           │ Set SecurityContext   │
  │                           └───────┬───────┘        │
  │                                                     │
  │◄────────────────────────────────────────────────────┤
  │ Protected Resource                                  │
```

### Security Configuration

| Component | Implementation |
|-----------|----------------|
| **Password Encoder** | `BCryptPasswordEncoder` (strength 10) |
| **JWT Algorithm** | HS256 (HMAC-SHA256) |
| **Token Expiration** | 24 hours (access), configurable |
| **Secret Key** | Base64-encoded 256-bit key |
| **Session Management** | Stateless (JWT-based) |
| **CSRF** | Disabled (stateless API) |
| **CORS** | Custom configuration for cross-origin |

### Authorization Rules

```java
// Current Implementation
/api/auth/**          → PERMIT_ALL
/**                   → AUTHENTICATED

// Role-based (available but unused)
/api/admin/**         → ROLE_ADMIN
/api/user/**          → ROLE_USER
```

---

## GraphQL Schema

### Schema Distribution

| File | Domain | Operations |
|------|--------|------------|
| `user.graphqls` | User management | `getCurrentUser`, `getUserById` |
| `auth.graphqls` | Authentication | `signup`, `signin` |
| `deviceStatus.graphqls` | Device telemetry | `getUserDeviceStatus`, `updateDeviceStatus` |
| `statusNotification.graphqls` | Notifications | `notificationsForCurrentUser`, `notificationsForUser`, `createNotification` |
| `accessRelationship.graphqls` | Access control | `usersMonitoringMe`, `monitoredUsers`, `accessRequests`, `grantAccess`, `approveAccessRequest`, `denyAccessRequest`, `revokeAccess` |

### Type Definitions

```graphql
# Core Types
type User {
  id: ID!
  name: String!
  email: String!
  password: String!
  phone: Int
}

type DeviceStatus {
  id: ID!
  user: User
  isOnline: Boolean
  batteryLevel: Int
  networkType: String
  lastSeen: LocalDateTime
}

type StatusNotification {
  id: ID!
  user: User
  title: String!
  message: String
  type: NotificationType
  timeStamp: LocalDateTime
  read: Boolean
}

type AccessRelationship {
  id: ID!
  requester: User
  target: User
  status: AccessStatus
  createdAt: LocalDateTime
}

# Enums
enum NotificationType { INFO, SUCCESS, WARNING, ERROR }
enum AccessStatus { PENDING, APPROVED, DENIED }

# Inputs
input SignUpRequest {
  name: String!
  email: String!
  password: String!
  phone: Int
}

input DeviceStatusUpdateRequest {
  isOnline: Boolean!
  batteryLevel: Int!
  networkType: String!
}

input GrantAccessRequest {
  email: String!
}
```

---

## API Endpoints

### REST Endpoints (Authentication)

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| `POST` | `/api/auth/signup` | User registration | `SignUpRequest` | `JwtAuthenticationRequest` |
| `POST` | `/api/auth/signin` | User login | `SignInRequest` | `JwtAuthenticationRequest` |
| `POST` | `/api/auth/refresh` | Token refresh | `RefreshTokenRequest` | `JwtAuthenticationRequest` |
| `GET` | `/api/auth` | Health check | - | `String` |

### GraphQL Endpoint

| Endpoint | Method | Headers Required |
|----------|--------|------------------|
| `/graphql` | POST | `Authorization: Bearer <jwt>` |

### GraphQL Query Examples

```graphql
# Get current user
query {
  getCurrentUser {
    id
    name
    email
    phone
  }
}

# Get device status for user
query {
  getUserDeviceStatus(userId: "uuid-here") {
    isOnline
    batteryLevel
    networkType
    lastSeen
  }
}

# List notifications for current user
query {
  notificationsForCurrentUser {
    id
    title
    message
    type
    read
    timeStamp
  }
}

# Get access requests
query {
  accessRequests {
    id
    requester {
      name
      email
    }
    target {
      name
      email
    }
    status
    createdAt
  }
}
```

### GraphQL Mutation Examples

```graphql
# Sign up
mutation {
  signup(input: {
    name: "John Doe"
    email: "john@example.com"
    password: "securepassword"
    phone: 1234567890
  }) {
    user {
      id
      name
      email
    }
    accessToken
  }
}

# Update device status
mutation {
  updateDeviceStatus(input: {
    isOnline: true
    batteryLevel: 85
    networkType: "5G"
  }) {
    id
    isOnline
    batteryLevel
    lastSeen
  }
}

# Grant access to monitor someone
mutation {
  grantAccess(input: {
    email: "target@example.com"
  }) {
    id
    status
    createdAt
  }
}

# Approve access request
mutation {
  approveAccessRequest(id: "request-uuid") {
    id
    status
  }
}

# Create notification
mutation {
  createNotification(input: {
    userId: "user-uuid"
    title: "Low Battery"
    message: "Battery below 20%"
    type: WARNING
  }) {
    id
    title
    timestamp
  }
}
```

---

## Configuration

### Application Properties

```properties
# Application
spring.application.name=safeStatusNotifier
server.port=8081

# Database (PostgreSQL on Render)
spring.datasource.url=jdbc:postgresql://dpg-d3eac3ogjchc738g3890-a.oregon-postgres.render.com:5432/notifier_k4pl
spring.datasource.username=admin
spring.datasource.password=<REDACTED>
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Logging
logging.level.org.springframework.web=INFO
logging.level.org.hibernate=ERROR
logging.level.com.example.notifier=DEBUG
```

### Key Configuration Notes

| Property | Purpose |
|----------|---------|
| `server.port=8081` | Application binds to port 8081 (Docker exposes 8080) |
| `ddl-auto=update` | Auto-migrates schema on startup (dev-friendly, not production) |
| `show-sql=true` | Logs all SQL queries (disable in production) |

---

## Deployment

### Docker Configuration

```dockerfile
# Build Stage
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Runtime Stage
FROM openjdk:21
WORKDIR /app
COPY --from=build /app/target/safeStatusNotifier-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Build & Run Commands

```bash
# Local Development
mvn spring-boot:run

# Build JAR
mvn clean package -DskipTests

# Docker Build
docker build -t safe-status-notifier .

# Docker Run
docker run -p 8080:8080 safe-status-notifier
```

### Production Deployment (Render)

- **Platform**: Render Web Service
- **Database**: Managed PostgreSQL on Render
- **Build Command**: `mvn clean package -DskipTests`
- **Start Command**: `java -jar target/safeStatusNotifier-0.0.1-SNAPSHOT.jar`

---

## Database Schema

### Physical Schema (PostgreSQL)

```sql
-- Users Table
CREATE TABLE _user (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20)
);

-- Device Status Table
CREATE TABLE device_statuses (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES _user(id) ON DELETE CASCADE,
    is_online BOOLEAN NOT NULL,
    battery_level INTEGER NOT NULL,
    network_type VARCHAR(255) NOT NULL,
    last_seen TIMESTAMP NOT NULL
);

-- Notifications Table
CREATE TABLE notifications (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES _user(id) ON DELETE CASCADE,
    title VARCHAR(255) NOT NULL,
    message VARCHAR(1000) NOT NULL,
    type VARCHAR(20) NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    is_read BOOLEAN NOT NULL
);

-- Access Relationships Table
CREATE TABLE access_relationships (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    requester_id UUID NOT NULL REFERENCES _user(id) ON DELETE CASCADE,
    target_id UUID NOT NULL REFERENCES _user(id) ON DELETE CASCADE,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT unique_request UNIQUE (requester_id, target_id)
);

-- Indexes for Performance
CREATE INDEX idx_device_status_user ON device_statuses(user_id);
CREATE INDEX idx_notifications_user ON notifications(user_id);
CREATE INDEX idx_access_requester ON access_relationships(requester_id);
CREATE INDEX idx_access_target ON access_relationships(target_id);
```

---

## Dependencies Reference

```xml
<!-- Core Spring Boot -->
spring-boot-starter-web        3.4.4
spring-boot-starter-data-jpa   3.4.4
spring-boot-starter-security   3.4.4
spring-boot-starter-graphql    3.4.4

<!-- Database -->
postgresql                       42.7.x (runtime)

<!-- Authentication -->
jjwt-api                         0.11.5
jjwt-impl                        0.11.5 (runtime)
jjwt-jackson                     0.11.5 (runtime)

-->
Utilities -->
commons-lang3                    3.x

<!-- GraphQL Extensions -->
graphql-datetime-spring-boot-starter 6.0.0

<!-- Development -->
lombok                           1.18.32
```

---

## Development Notes

### Code Quality Considerations

1. **Hardcoded Secrets**: JWT secret key is hardcoded in `JwtServiceImpl.java` - move to environment variables
2. **Database Credentials**: Exposed in `application.properties` - use Render environment variables
3. **CORS**: Custom configuration allows all origins - restrict for production
4. **Logging**: Debug logging enabled - reduce in production
5. **DDL Mode**: `ddl-auto=update` can be dangerous in production - use Flyway/Liquibase

### Architecture Patterns Used

- **Layered Architecture**: Controller → Service → Repository → Entity
- **Repository Pattern**: Spring Data JPA interfaces
- **DTO Pattern**: Request/Response objects separate from entities
- **Service Interface + Impl**: Loose coupling via interfaces
- **Dependency Injection**: Spring IoC throughout

---

## License

Proprietary - SafeStatusNotifier Project

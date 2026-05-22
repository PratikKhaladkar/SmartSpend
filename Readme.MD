# 💰 SmartSpend — AI Powered Expense Tracker & Financial Advisor

A smart, AI-driven expense tracking application that lets users manage their finances and query their transaction data using **natural language**. Built with Spring Boot and powered by **Gemini 3.1 Flash-Lite** via Spring AI.

---

## ✨ Features

- 🔐 **JWT Authentication** with HTTP-only cookies and refresh token rotation
- 💬 **Natural Language Queries** — ask things like *"How much did I spend on food last month?"*
- 📊 **Smart Filtering** — intelligent date range parsing (this week, last 3 months, Q1 2026, etc.)
- 🔁 **Recurring Transactions** — track salary, subscriptions, and other recurring items
- 🗂️ **Custom Categories** — create your own expense categories and income sources
- 💡 **AI Insights** — spending recommendations and financial advice
- 📈 **Chart Data** — AI returns structured chart data for visual representation
- 🐳 **Dockerized** — runs with a single `docker-compose up` command

---

## 🛠️ Tech Stack

| Layer | Technology |
|---------|------------|
| Language | Java 17 |
| Framework | Spring Boot 3.5 |
| Security | Spring Security 6 + JWT |
| AI | Spring AI + Gemini 3.1 Flash-Lite |
| Database | MySQL 8.0 |
| ORM | Spring Data JPA + Hibernate |
| Containerization | Docker + Docker Compose |
| Build Tool | Maven |

---

## 📁 Project Structure

```text
src/main/java/com/expense_tracker/
├── ai/                          # AI service, tools, chat history
├── auth/                        # Auth service and requirements
├── config/                      # App and security configuration
├── controllers/
│   ├── AuthController           # login, register, logout, refresh
│   ├── TransactionController    # CRUD + filter transactions
│   ├── ExpenseCategoryController
│   ├── IncomeSourceController
│   ├── RecurringTransactionController
│   ├── UserProfileController
│   └── ChatController           # AI chat + recent insights
├── dtos/                        # Request and response DTOs
├── entities/                    # JPA entities
├── enums/                       # TransactionType, TransactionFrequency
├── exceptions/                  # Custom exceptions
├── GlobalExceptionHandling/     # Global exception handler
├── repositories/                # JPA repositories
├── security/                    # JWT filter, utility, entry point
└── service/                     # Business logic
```

---

## 🗄️ Database Schema

| Entity | Description |
|----------|-------------|
| `User` | Stores user credentials and profile |
| `Transaction` | Individual income/expense records |
| `RecurringTransaction` | Recurring financial items |
| `ExpenseCategory` | Default + custom expense categories per user |
| `IncomeSource` | Default + custom income sources per user |
| `RefreshToken` | Refresh token rotation with expiry |
| `ChatHistory` | Persisted AI chat queries and responses |

---

## 🔌 API Reference

**Base URL**

```text
http://localhost:8090/smartspend
```

### 🔐 Auth — `/auth`

| Method | Endpoint | Description |
|----------|------------|-------------|
| POST | `/auth/register` | Register new user |
| POST | `/auth/login` | Login and receive JWT cookies |
| POST | `/auth/refresh` | Refresh access token |
| POST | `/auth/logout` | Logout and clear cookies |

---

### 💳 Transactions — `/transaction`

| Method | Endpoint | Description |
|----------|------------|-------------|
| POST | `/transaction/create` | Create a new transaction |
| GET | `/transaction/getAll` | Get all transactions |
| PUT | `/transaction/edit/{tid}` | Edit a transaction |
| DELETE | `/transaction/delete/{tid}` | Delete a transaction |
| GET | `/transaction/filter` | Filter transactions by criteria |

---

### 🔁 Recurring Transactions — `/recurring/transaction`

| Method | Endpoint | Description |
|----------|------------|-------------|
| POST | `/recurring/transaction/create` | Create recurring transaction |
| GET | `/recurring/transaction/getAll` | Get all recurring transactions |
| PATCH | `/recurring/transaction/edit/{tid}/{newAmount}` | Edit amount |
| DELETE | `/recurring/transaction/delete/{tid}` | Delete recurring transaction |

---

### 🗂️ Expense Categories — `/expense/category`

| Method | Endpoint | Description |
|----------|------------|-------------|
| POST | `/expense/category/create/{expenseCategory}` | Create custom category |

---

### 💵 Income Sources — `/income/source`

| Method | Endpoint | Description |
|----------|------------|-------------|
| POST | `/income/source/create/{incomeSource}` | Create custom income source |

---

### 👤 Profile — `/profile`

| Method | Endpoint | Description |
|----------|------------|-------------|
| GET | `/profile/get` | Get user profile |
| PATCH | `/profile/change/password` | Change password |

---

### 🤖 AI Chat — `/ai`

| Method | Endpoint | Description |
|----------|------------|-------------|
| POST | `/ai/chat` | Send natural language query |
| GET | `/ai/recent/insights` | Get recent chat history |

---

## 🤖 AI Capabilities

The AI advisor understands natural language queries like:

```text
How much did I spend this month?
Compare my March, April, and May expenses
What are my food expenses in the last 3 months?
How much did I save last month?
Show expenses between March 1 and April 15
What percentage of my income did I spend?
Where should I cut down spending?
Show my grocery expenses in Q1 2026
```

### AI Response Structure

```json
{
  "responseType": "TEXT | CHART",
  "message": "Your total expenses this month are ₹54,239",
  "chartType": "BAR | PIE | LINE | NONE",
  "labels": ["Food", "Rent", "Travel"],
  "values": [2550, 16450, 10190],
  "recommendations": [
    "Your travel spending is 19% of total expenses",
    "Consider reducing discretionary spending"
  ]
}
```

---

## 🐳 Running with Docker

### Prerequisites

- Docker Desktop installed
- Docker Compose installed

### Steps

#### 1. Clone the repository

```bash
git clone https://github.com/PratikKhaladkar/SmartSpend.git
cd SmartSpend
```

#### 2. Set up environment variables

```bash
cp .env.example .env
```

Fill in your values in `.env`

#### 3. Build the JAR

```bash
./mvnw clean package -DskipTests
```

#### 4. Start the containers

```bash
docker-compose up --build
```

#### 5. App is running at

```text
http://localhost:8090/smartspend
```

---

## ⚙️ Environment Variables

Copy `.env.example` to `.env`

```env
# Database
MYSQL_ROOT_PASSWORD=your_password
MYSQL_DATABASE=your_db_name

# Spring Boot Datasource
DB_URL=jdbc:mysql://expense-tracker-db:3306/your_db_name
DB_USERNAME=root
DB_PASSWORD=your_password

# JWT
JWT_SECRET=your_secret_key

# Server
SERVER_PORT=8090

# Profile
SPRING_PROFILES_ACTIVE=dev

# AI
GEMINI_API_KEY=your_gemini_api_key
```

> **Note:** Never commit your `.env` file. It is already in `.gitignore`.

---

## 🔒 Security Design

- **JWT** stored in HTTP-only cookies (not accessible via JavaScript)
- **Refresh token rotation** — each refresh generates a new token and invalidates the old one
- **CSRF disabled** — safe because APIs only accept `application/json` and strict CORS allows only frontend origins
- **Stateless architecture** — no server-side sessions

Cookie configuration:

```text
HttpOnly=true
Secure=true (production)
```

---

## 🚀 Running Locally (Without Docker)

### Prerequisites

- Java 17
- Maven
- MySQL

### 1. Create a MySQL database

### 2. Configure application.properties

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/your_db
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

### 3. Add Gemini API key

```properties
spring.ai.google.genai.api-key=your_gemini_api_key
```

### 4. Run the application

```bash
./mvnw spring-boot:run
```

---

## Default Expense Categories

```text
FOOD
GROCERY
RENT
TRAVEL
SHOPPING
HOSPITAL
SUBSCRIPTION
OTHER
```

---

## Default Income Sources

```text
SALARY
BUSINESS
FREELANCE
INVESTMENT
PASSIVE
OTHER
```

---

## 👨‍💻 Author

**Pratik Khaladkar**

GitHub: https://github.com/PratikKhaladkar
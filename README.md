# News_Aggregator_API

A secure, JWT-based News Aggregator built with Spring Boot 3.5+ that allows users to:

- Register and login securely
- Save their topic preferences
- Fetch news based on those preferences (via GNews API)
- Search for news using keywords
- Fully protected with Spring Security and JWT authentication

---

# Features

- ✅ User registration & login using JWT
- ✅ Secure endpoints with role-based access
- ✅ Integration with [GNews API](https://gnews.io)
- ✅ Search endpoint: `/api/news/search/{keyword}`
- ✅ In-memory H2 database
- ✅ Global exception handling
- ✅ Ready for Postman testing

---

# Tech Stack

- Java 21
- Spring Boot 3.5.3
- Spring Security
- WebClient (Spring WebFlux)
- JWT (jjwt)
- H2 In-Memory Database

---

# Setup & Run

### 1. Clone the Repository
```bash
git clone https://github.com/your-username/news-app-springboot.git
cd news-app-springboot
```

### 2. Add GNews API Key

Set your API key in `src/main/resources/application.properties`:

```
gnews.api.key=YOUR_API_KEY_HERE
```
**NOTE**APIKey is already provided by me, you can add your own if you wish.

Sign up at [gnews.io](https://gnews.io) to get one.

### 3. Build and Run
```bash
./mvnw spring-boot:run
```

---

# API Endpoints

| Method | Endpoint                      | Description                        | Auth Required |
|--------|-------------------------------|------------------------------------|---------------|
| POST   | `/api/register`               | Register a new user                | ❌            |
| POST   | `/api/login`                  | Login and get JWT token            | ❌            |
| GET    | `/api/preferences`            | Get user preferences               | ✅            |
| PUT    | `/api/preferences`            | Update user preferences            | ✅            |
| GET    | `/api/news`                   | Get news based on preferences      | ✅            |
| GET    | `/api/news/search/{keyword}`  | Search news by keyword             | ✅            |

---

# Postman Collection

A complete Postman collection is included:
- `News_Aggregator_API.postman_collection.json`
- `News_Aggregator_Environment.json`

---

# Future Improvements

- ✅ Role-based admin endpoints
- ⏳ User-friendly frontend (React or Angular)
- ⏳ Support pagination and filtering in `/news`

---

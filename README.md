# Foreign Exchange Service (ienufx)

A reactive Spring Boot application that provides foreign exchange rate information using the Frankfurter API.

## Features

- Get exchange rates between different currencies
- Reactive/non-blocking implementation using Spring WebFlux
- Clean, minimal codebase with Java 21 records
- Health check endpoint

## API Endpoints

### Get Exchange Rate
```http
GET /api/v1/exchange-rate?from={fromCurrency}&to={toCurrency}
```

Example:
```bash
curl "localhost:8082/api/v1/exchange-rate?from=USD&to=EUR"
```

Response:
```json
{
  "fromCurrency": "USD",
  "toCurrency": "EUR",
  "rate": 0.86259,
  "date": "2025-11-17"
}
```

### Health Check
```http
GET /api/v1/exchange-rate/health
```

## Running the Application

### Prerequisites
- Java 21 or later
- Gradle (or use the included Gradle wrapper)

### Build and Run
```bash
# Build the project
./gradlew build

# Run the application
./gradlew bootRun
```

The application will start on port 8082.

## Project Structure

```
src/
├── main/
│   ├── java/gr/ienu/ienufx/
│   │   ├── Main.java                    # Application entry point
│   │   ├── controller/
│   │   │   └── ExchangeRateController.java  # REST endpoints
│   │   ├── dto/
│   │   │   ├── ExchangeRateResponse.java     # Response record
│   │   │   └── FrankfurterResponse.java      # External API response record
│   │   └── service/
│   │       └── ExchangeRateService.java      # Business logic
│   └── resources/
│       └── application.properties        # Configuration
└── test/
    └── java/gr/ienu/ienufx/
        └── MainTest.java                # Basic context test
```

## External APIs

This service uses the [Frankfurter API](https://api.frankfurter.app/) to fetch real-time exchange rates.

## Technologies Used

- **Java 21** with Records
- **Spring Boot 4.0** (Snapshot)
- **Spring WebFlux** for reactive programming
- **Gradle** for build management

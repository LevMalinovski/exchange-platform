# 💱 Exchange Platform

This project is a Spring Boot + Kotlin REST API simulating a basic asset exchange platform. It supports account-based
asset operations with full asynchronous processing, saga orchestration, and extensible architecture.

---

## ✅ Features

* **User Account Operations**

    * Deposit and withdraw fiat balances
    * Buy and sell assets (Crypto, Stocks, Commodities)
    * Convert one asset to another (e.g., USD → BTC)

* **Asset Support**

    * Asset types: `FIAT`, `CRYPTO`, `STOCK`, `COMMODITY`
    * Predefined assets like `EUR`, `BTC`, `ETH`, `AAPL`, `TSLA`, `GOLD`, `SILVER`

* **Architecture**

    * CQRS pattern
    * Saga coordination with compensation support
    * Database-backed job queue for background operation processing (no Kafka)
    * Custom exchange rate provider with pluggable strategy

* **API**

    * Get account and asset balances
    * Initiate deposits, withdrawals, buys, sells, and conversions (processed asynchronously)
    * Swagger UI with enriched endpoint documentation

---

## ⚙️ Tech Stack

* Kotlin + Spring Boot
* PostgreSQL + Liquibase
* Testcontainers for integration tests
* Swagger/OpenAPI for documentation
* Gradle for build automation
* Prometheus + Grafana for monitoring and metrics

---

## 🔪 Tests

* ✅ Unit tests cover business logic (not fully)
* ✅ Integration tests for REST endpoints and database interactions
* ✅ Testcontainers-based PostgreSQL instance
* ♻️ Full saga flow testing with compensation logic validation

> **Important**: All business logic must be covered by tests. In case external services (e.g., bank APIs or asset
> providers) are used, revisit the saga logic and make sure compensation actions are clearly implemented and tested.

---

## 🚀 Getting Started

### 1. Run

```bash
./run.sh
```

* Starts:

    * PostgreSQL database
    * Spring Boot API
    * Prometheus for metrics collection
    * Grafana for visualization

### 2. Run Locally

```bash
./gradlew clean bootRun
```

---
🌐 URLs

* Exchange Platform API: http://localhost:8080

* Swagger UI: http://localhost:8080/swagger-ui/index.html#/

* Grafana: http://localhost:3000 (user: grafana, password: grafana)

* Prometheus: http://localhost:9090

---

## 📈 Monitoring (Suggested)

For performance insights, consider collecting metrics like:

* Number of failed/compensated saga operations
* Asset conversion latency
* Transaction success/failure rates
* Balance inconsistencies

(These can be visualized using Prometheus + Grafana)

---

## 🤩 Extras

* Clean integration points for switching rate providers
* Pessimistic locking for transactional safety
* Validation for asset types during operations

---

# Changelog

All notable changes to this project will be documented in this file.

## [2.0.0] - 2026-10-31 (`v2-virtual-threads`)

### Architecture & Concurrency
- **Migrated from Reactive WebFlux to Spring Web + Virtual Threads**:
  - Replaced `spring-boot-starter-webflux`, `spring-boot-starter-webclient`, `spring-boot-starter-data-redis-reactive`, and `reactor-test` with `spring-boot-starter-web` and `spring-boot-starter-data-redis` in `pom.xml`.
  - Enabled virtual threads globally in `application.properties` (`spring.threads.virtual.enabled=true`) for Tomcat HTTP request handling and `@Scheduled` tasks.
  - Refactored `NotificationScheduler` to use `Executors.newVirtualThreadPerTaskExecutor()` to evaluate notifications and send blocking Firebase push notifications concurrently without blocking carrier threads.
  - Refactored `BinancePollingScheduler` from reactive `WebClient` / Netty connection pools to Spring's synchronous `RestClient` with 1s connect/read timeouts.
  - Refactored `KeyDbService` from `ReactiveRedisTemplate` (`Mono`/`Flux`) to `StringRedisTemplate` returning standard Java types (`String`, `List`, `Set`, `Long`, `Boolean`).
  - Refactored `NotificationService`, `CryptoDTOMapper`, and all REST controllers (`CryptoController`, `NotificationController`, `UserController`, `DebugController`) from reactive streams to standard synchronous methods and collections.
  - Added `"virtual": thread.isVirtual()` and updated to `thread.threadId()` in `/api/debug/threads`.

### Dependencies, Runtime & Security (CVE Remediations)
- Upgraded Java version (`java.version`) to **Java 27** (with OpenJDK `26.0.2` and `27.0.1` installed in `.jdks`).
- Upgraded `org.springframework.boot:spring-boot-starter-parent` from `4.0.1` to `4.1.2` (latest GA).
- **Migrated to Jackson 3 (`tools.jackson`)**:
  - Refactored `CryptoDTOMapper` from legacy `com.fasterxml.jackson.databind.ObjectMapper` to `tools.jackson.databind.json.JsonMapper` (`3.2.4`) and removed duplicate Jackson 2 dependencies (`com.fasterxml.jackson.core:jackson-databind` and `jackson-module-parameter-names`) from `pom.xml`.
- Upgraded `com.google.firebase:firebase-admin` from `9.7.0` to `9.11.0` (latest).
- Added explicit `com.google.guava:guava` `33.7.2-jre` dependency to eliminate the Java 25+ `sun.misc.Unsafe::objectFieldOffset` terminal deprecation warning from older Guava `33.4.0-jre` (`AbstractFuture` now uses `VarHandle`).
- **Resolved all transitive CVEs flagged by Mend.io**:
  - Imported `tools.jackson:jackson-bom:3.2.4` in `<dependencyManagement>` to upgrade Spring Boot 4's transitive `tools.jackson.core:jackson-databind` from `3.1.7` to `3.2.4` (fixes `CVE-2026-110879`).
  - Set `<logback.version>1.6.6</logback.version>` to upgrade `ch.qos.logback:logback-classic` from `1.5.38` to `1.6.6` (fixes `CVE-2026-19880`).
  - Imported `io.opentelemetry:opentelemetry-bom:1.67.0` to upgrade `firebase-admin`'s transitive `opentelemetry-api` from `1.62.0` to `1.67.0` (fixes `CVE-2026-118259`).
  - Excluded unused `android-json`, `json-path`, and `json-smart` (and its transitive `asm:9.7.1`) from `spring-boot-starter-test` (fixes `CVE-2026-110974`, `CVE-2026-113602`, and `CVE-2026-110260` series).

### Bug Fixes & Code Modernizations
- **`FirebaseInitializer`**: Replaced legacy `javax.annotation.PostConstruct` with `jakarta.annotation.PostConstruct` so Spring Boot 4 properly invokes `init()` on startup (fixing `IllegalStateException: FirebaseApp with name [DEFAULT] doesn't exist`). Also switched to `FirebaseOptions.builder()` and added a guard against duplicate initialization.
- **`PushNotificationService`**: Replaced deprecated `Message.Builder.setToken(...)` with `setFid(...)` for Firebase Admin `9.11.0` and moved `Logger` to a `static final` field.
- **`NotificationScheduler` & `DirectionChangeModel`**:
  - Fixed varargs call in `NonPriceTargetNotificationSender` (`getNotificationMessage(gain)` instead of passing `List.of(gain)`, which previously caused a `ClassCastException: ImmutableCollections$List12 cannot be cast to Double` in `RallyModel`).
  - Fixed trend comparison in `DirectionChangeModel.getNotificationMessage` to check `Number` values properly instead of `Double.equals(Integer)`.
- **`NotificationController`**: Replaced `@Autowired` field injection with constructor injection.
- **`docker-compose.yml`**: Removed obsolete `version: '3.8'` attribute for Docker Compose v2.
- **Graceful KeyDB Startup Handling**: Added exception handling in `NotificationScheduler` and `KeyDbService` so temporary `RedisLoadingException` errors while KeyDB loads its dataset into memory are logged cleanly as warnings rather than uncaught stack traces.
- Removed unused imports in `NonTargetNotificationModel` and `PriceTargetNotificationModel`.

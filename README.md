# CryptoNotify
Java WebFlux (Reactor) project that consumes real-time Binance data and serves notifications to clients.  
Push notifications are delivered via Firebase. Every user should have a Firebase token to receive notifications.  
CryptoNotifyUI serves as a React Native–based mobile application that connects to this backend:  
https://github.com/SzaszSzilard/CryptoNotifyUI

### How to run
1. Clone the repository:
   ```bash
   git clone https://github.com/SzaszSzilard/CryptoNotify
   ```

2. Docker Compose:
   ```bash
   docker compose up
   ```
   
3. **Run the application from your IDE** or using Maven:
   ```bash
   mvn spring-boot:run
   ```
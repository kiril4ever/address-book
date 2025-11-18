# Address Book Application

## 🚀 Features

- **User Authentication**: Sign up, login, logout with Spring Security
- **Contact Management**: Create, read, update, delete contacts
- **Public/Private Access**: Public contact viewing with private management
- **Search Functionality**: Real-time search by contact name
- **CSV Export**: Export contacts to CSV format
- **Microservices Architecture**:
    - Weather Service: Provides weather data for contact locations
- **Event-Driven**: Kafka message broker for inter-service communication
- **Dockerized**: All services run in Docker containers
- **Responsive UI**: Bootstrap-based responsive interface


## 📋 Prerequisites

- Java 21
- Docker & Docker Compose
- Git

## 🛠️ Installation & Setup

### Option 1: Docker Compose (Recommended)

1. **Clone the repository**:
   ```bash
   git clone https://github.com/kiril4ever/address-book.git
   cd address-book
   docker-compose up -d
   # Build the application
    mvn clean package
   # Run from IntelliJ or command line
    java -jar target/address-book-0.0.1-SNAPSHOT.jar

Ports
8080: Main Application

8081: Weather Service

8082: Notification Service

3306: MySQL Database

9092: Kafka Broker

2181: Zookeeper

🧪 Testing the Application
Create a test user:

Go to: http://localhost:8080/signup

Create a new account

Test Kafka integration:

After signup, check Docker logs for Kafka messages

Weather service and Notification service should process the event

Add contacts:

Login and add contacts with addresses

Weather data will be displayed for each contact

Test search:

Use the search box to filter contacts by name

Export contacts:

Click "Export CSV" to download contacts

🔍 Monitoring
Check Service Status
bash
docker-compose ps
View Logs
bash
# All services
docker-compose logs

# Specific service
docker-compose logs main-app
docker-compose logs weather-service
docker-compose logs notification-service
docker-compose logs kafka
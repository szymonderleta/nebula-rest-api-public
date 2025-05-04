# Nebula REST API - Help Documentation

## Table of Contents
1. [Overview](#overview)
2. [Installation and Setup](#installation-and-setup)
3. [Configuration](#configuration)
4. [API Documentation](#api-documentation)
5. [Development Guidelines](#development-guidelines)
6. [Troubleshooting](#troubleshooting)

## Overview

Nebula REST API is the home application for the Andromeda platform, designed to provide a centralized login and access point to various game applications. This application serves as a gateway, allowing users to authenticate and navigate to the games and services offered by the platform. It also stores general user settings, allows users to upload profile images, and update their preferences.

### Current Version
**v 2.0.0 Beta Release**  
This version is intended for testing purposes in a pre-production environment.

### Built With
- **Java SDK 21**
- **Spring Boot Framework 3.2**
- **Hibernate**
- **MariaDB**

### Key Features
- User authentication and secure login functionality
- Centralized navigation to game applications hosted on the Andromeda platform
- RESTful API to communicate with the platform's back-end services
- User profile and settings management
- Image upload capabilities
- Achievement tracking system
- Theme customization

## Installation and Setup

### Prerequisites
- Java SDK 21
- Maven 3.8+
- MariaDB 10.5+
- SSL certificate (self-signed or trusted)

### Installation Steps

1. **Clone the repository**
   ```bash
   git clone https://your-repository-url/nebula-rest-api.git
   cd nebula-rest-api
   ```

2. **Set up environment variables**
   The application requires several environment variables to be set:
   ```bash
   export SPRING_CLOUD_CONFIG_USERNAME=your_config_username
   export SPRING_CLOUD_CONFIG_PASSWORD=your_config_password
   export HEADER_FOR_AUTH_SERVER=your_auth_header
   export APP_JWT_SECRET=your_jwt_secret
   export USER_AVATAR_UPLOAD_PATH=/path/to/avatar/storage
   export SPRING_SECURITY_USERNAME=your_security_username
   export SPRING_SECURITY_PASSWORD=your_security_password
   export DATABASE_URL=jdbc:mariadb://localhost:3306/nebula_db
   export DATABASE_USERNAME=your_db_username
   export DATABASE_PASSWORD=your_db_password
   export CERT_PASSWORD=your_certificate_password
   ```

3. **Build the application**
   ```bash
   mvn clean package
   ```

4. **Run the application**
   ```bash
   java -jar target/nebula-rest-api.jar
   ```

## Configuration

### Application Properties
The main configuration file is `application.properties`. Key configurations include:

- **Server Configuration**
  - `server.port=8081` - The port on which the application runs
  - `server.ssl.enabled=true` - Enables SSL for secure connections

- **Database Configuration**
  - `spring.datasource.url=${DATABASE_URL}` - Database connection URL
  - `spring.datasource.username=${DATABASE_USERNAME}` - Database username
  - `spring.datasource.password=${DATABASE_PASSWORD}` - Database password
  - `spring.datasource.driver-class-name=org.mariadb.jdbc.Driver` - Database driver

- **Authentication Server Configuration**
  - `auth.serv.api.v1.url=https://milkyway.local:8555/andromeda-authorization-server/api/v1/` - Base URL for the authentication server

- **File Upload Configuration**
  - `spring.servlet.multipart.max-file-size=10MB` - Maximum file size for uploads
  - `spring.servlet.multipart.max-request-size=10MB` - Maximum request size for uploads
  - `image.avatar.path=${USER_AVATAR_UPLOAD_PATH}` - Path for storing user avatars

### SSL Configuration
The application uses SSL for secure communication. You need to provide a keystore file:

1. Place your keystore file in `src/main/resources/keystore.p12`
2. Set the keystore password in the environment variable `CERT_PASSWORD`

#### Adding a Self-Signed Certificate to Trusted Certificates

If you're using a self-signed certificate, you may need to add it to your trusted certificates:

1. **Extract the SSL certificate** from the server:
   ```bash
   echo -n | openssl s_client -connect milkyway.local:8555 -servername milkyway.local | openssl x509 > milkyway.crt
   ```

2. **Add the certificate to system trusted certificates:**
   ```bash
   sudo cp milkyway.crt /usr/local/share/ca-certificates/milkyway.crt
   sudo update-ca-certificates
   ```

3. **Add the certificate to Java TrustStore:**
   ```bash
   sudo keytool -import -trustcacerts -keystore $JAVA_HOME/lib/security/cacerts -storepass changeit -alias milkyway-cert -file milkyway.crt
   ```

## API Documentation

The API documentation is available at the following URLs when the application is running:

- **Swagger UI**: [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)
- **OpenAPI JSON**: [http://localhost:8081/v3/api-docs](http://localhost:8081/v3/api-docs)

### Main API Endpoints

The application provides several RESTful endpoints:

- **Account Management**
  - `/api/v1/account/register` - Register a new user account
  - `/api/v1/account/confirm` - Confirm user registration
  - `/api/v1/account/reset-password` - Reset user password
  - `/api/v1/account/change-password` - Change user password

- **User Management**
  - `/api/v1/users` - Get user information and update user profiles
  - `/api/v1/users/settings` - Manage user settings

- **Authentication**
  - `/api/v1/token` - Manage JWT tokens

- **Game Management**
  - `/api/v1/games` - Access and manage games

- **User Achievements**
  - `/api/v1/achievements` - Track and manage user achievements

- **Profile Customization**
  - `/api/v1/themes` - Manage UI themes
  - `/api/v1/genders` - Manage gender options
  - `/api/v1/nationalities` - Manage nationality options
  - `/api/v1/images` - Upload and manage user images

## Development Guidelines

### Project Structure
The project follows a standard Spring Boot application structure:

- `src/main/java/pl/derleta/nebula` - Main source code
  - `config` - Configuration classes
  - `controller` - REST controllers
  - `domain` - Domain models and entities
  - `repository` - Data access layer
  - `service` - Business logic
  - `util` - Utility classes

### Coding Standards
- Follow Java coding conventions
- Use meaningful variable and method names
- Write comprehensive JavaDoc comments
- Create unit tests for all new functionality

### Adding New Features
1. Create a new branch for your feature
2. Implement the feature with appropriate tests
3. Submit a pull request for review
4. Address any feedback from code review
5. Merge the feature once approved

## Troubleshooting

### Common Issues

#### Connection to Config Server Fails
If you're having trouble connecting to the config server:
1. Verify that the config server is running
2. Check that your environment variables are set correctly
3. Ensure that the SSL certificate is properly configured

#### Database Connection Issues
If you can't connect to the database:
1. Verify that MariaDB is running
2. Check your database credentials
3. Ensure that the database exists and is accessible

#### SSL Certificate Problems
If you're experiencing SSL certificate issues:
1. Verify that your keystore file is in the correct location
2. Check that the keystore password is set correctly
3. Follow the steps in the SSL Configuration section to add the certificate to trusted certificates

### Getting Help
If you encounter issues not covered in this documentation, please:
1. Check the logs for error messages
2. Consult the Spring Boot documentation
3. Contact the development team for assistance

---

## Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.2.2/maven-plugin/reference/html/)
* [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
* [Spring Security Reference](https://docs.spring.io/spring-security/reference/index.html)

# Nebula REST API

## Overview

Nebula REST API is the home application based on REST API, for the Andromeda platform, designed to provide a centralized login and access
point to various game applications currently under construction. This application serves as a gateway, allowing users to
authenticate and navigate to the games and services offered by the platform. It also stores general user settings,
allows users to upload profile images, and update their preferences.

## Current Version

**v 3.0.0 Public Release**  
- Added over 400 unit and integration tests.  
- Added an endpoint to refresh the accessToken (using the cookie with a valid refreshToken). Removed the old JWT token cookie system and replaced it with both accessToken and refreshToken, which are now stored as HttpOnly cookies. The accessToken is valid for 1 hour, while the refreshToken is valid for over 20 days.  
- Fixed small errors and refactored some parts of the code.

**v 2.0.0 Beta Release**  
This version is intended for testing purposes in a pre-production environment.

## Built With

- **Java SDK 21**
- **Spring Boot Framework 3.2**
- **Hibernate**

## Functional Features

- User authentication and secure login functionality.
- Centralized navigation to game applications hosted on the Andromeda platform.
- RESTful API to communicate with the platform's back-end services.
- Integration with Swagger for API documentation and testing.

## Non-Functional Features

- High performance and low latency API responses.
- Designed for scalability to accommodate future games and extensions.
- Secure data transmission and storage.
- Easily customizable for new features or changes.

## API Documentation

The API documentation is available at the following URLs:

- [Swagger UI](http://localhost:8081/swagger-ui/index.html)
- [OpenAPI JSON](http://localhost:8081/v3/api-docs)

## TODO

- Improve the password reset mechanism by implementing a verification link with expiration instead of directly sending a
  new password via email.

# Nebula REST API

<div align="left">

![Author](https://img.shields.io/badge/Author-Szymon%20Derleta-white?style=for-the-badge)

![Release](https://img.shields.io/badge/Release-Public%20Release-green?style=for-the-badge)  
![Version](https://img.shields.io/badge/Version-3.1.0-green?style=for-the-badge)

📄 Changelog: [CHANGELOG.md](info/CHANGELOG.md)  
🔗 Repository: [GitHub - Nebula Rest Api](https://github.com/szymonderleta/nebula-rest-api-public)  
🛠️ Jenkins Pipeline: [JENKINS.md](info/JENKINS.md)  
🆘 Help: [HELP.md](info/HELP.md)
</div>

## Overview

Nebula REST API is the home application based on REST API, for the Andromeda platform, designed to provide a centralized login and access
point to various game applications currently under construction. This application serves as a gateway, allowing users to
authenticate and navigate to the games and services offered by the platform. It also stores general user settings,
allows users to upload profile images, and update their preferences.

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

- High-performance and low-latency API responses.
- Designed for scalability to accommodate future games and extensions.
- Secure data transmission and storage.
- Easily customizable for new features or changes.

## API Documentation

After running the app in localhost, the API documentation will be available at the following URLs:

- [Swagger UI](http://localhost:8081/swagger-ui/index.html)
- [OpenAPI JSON](http://localhost:8081/v3/api-docs)

## TODO

- Improve the password reset mechanism by implementing a verification link with expiration instead of directly sending a
  new password via email.

# Meetings-REST-API
## About
This repository contains REST API for managing Meetings.     
Also, project has authentication and authorization implemented.    
Bearer Authorization is required for most endpoints(except /login and /users/post)    
H2 In memory database is used, everytime project is launched clean DB is generated containing 2 users:    
1. User1
    1. username:user
    2. password:user
    3. role:ROLE_USER
2. User2
   1. username:admin
   2. password:admin
   3. role:ROLE_ADMIN

JDK version 17.0.4.1
## Pre-requisites
Before launching the project generate app.key and app.pub files in /src/main/resources package.
In app.key put private RSA key in PKCS #8 format
In app.pub put public RSA key in PKCS #8 format
Keys can be generated at https://www.csfieldguide.org.nz/en/interactives/rsa-key-generator/
## Quickstart
You can start project by opening terminal and going to root directory and executing command:
```
./mvnw spring-boot:run
```
Swagger documentation can be accessed via endpoint ```/swagger-ui/index.html```






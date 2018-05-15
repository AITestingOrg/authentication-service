# User Service

[![Build Status](https://travis-ci.org/AITestingOrg/authentication-service.svg?branch=master)](https://travis-ci.org/AITestingOrg/authentication-service)

User Service is an authentication microservice using OAuth2 to generate Json Web Tokens for valid user credentials to access other microservices in this project. 
# Features

  - Authtenticate with valid user credentials
  - Generate JWT
  - Validate tokens

## Open source being used

User Service uses a number of open source projects to work properly:

* [Spring Framework]
* [Gradle]
* [Docker]
* [Docker Compose JUnit Rule]
* [MySQL]

## Running the service

User Service requires [Docker] v18+ to run.

Once Docker is installed you can build from source or use the predefined images available at [Docker Hub](https://hub.docker.com/u/aista/dashboard/)

### Building from source
Using gradle wrapper and docker-compose:
```sh
cd user-service
./gradlew clean build
docker-compose -f docker-compose-local.yml up --build
```
This will build the application and generate the jar file to be placed in a container and run two other containers with MySQL and a personalized version of Eureka called discovery-service.

If you are running Docker Toolbox you need to declare these environment variables to sucessfully pass all tests (update the values with your corresponding local machine values):

```sh
$Env:DOCKER_COMPOSE_LOCATION = "C:\Program Files\Docker Toolbox\docker-compose.exe"
$Env:DOCKER_LOCATION = "C:\Program Files\Docker Toolbox\docker.exe"
$Env:DOCKER_MACHINE_IP = "192.168.99.100"
```

### Using Docker Hub images
User Service is very easy to run from the images on Docker Hub.

By default, the Docker will expose port 8091, so change this within the docker-compose.yml file if necessary.

```sh
cd user-service
docker-compose up
```

[//]: # (Reference links)

   [Spring Framework]: <https://spring.io/>
   [Gradle]: <https://gradle.org/>
   [Docker]: <https://www.docker.com/>
   [Docker Compose JUnit Rule]: <https://github.com/palantir/docker-compose-rule>
   [MySQL]: <https://www.mysql.com/>


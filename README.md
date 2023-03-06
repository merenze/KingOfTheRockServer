# King of the Rock - Server

King of the Rock is a video game for four players, designed for Android by a four-student team for [COM S 309: Software Development Practices](https://www.cs.iastate.edu/courses/com-s-309) at Iowa State University. The backend is built with [Spring Boot](https://spring.io/projects/spring-boot).

The objective is to collect materials through trade and random generation in order to build structures worth points.

This project contains all the files necessary to run the server application.

## A Note About This Project's Completeness
This application is the product of the spring 2022 semester.
It was developed primarily as a vehicle for learning Spring Boot and collaborative development practices.
It was abandoned at the semester's conclusion and is not under active development.

Though the API is fully functional, the time constraints of learning Spring Boot mean that it is not necessarily optimized.
For example, to circumvent issues with one-to-many relations not loading,
several [entity provider services](https://github.com/merenze/KingOfTheRockServer/tree/master/src/main/java/coms309/s1yn3/backend/service/entityprovider) were defined to force eager loading.
This may cause performance issues at larger scales.

## API
TODO

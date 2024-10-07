# Great Library of Alexandria

### Completed Work

* Entities for complete data model
* Persistence mechanisms for all entities
* Service layer components for Authors and Works
* Endpoints via controllers for Authors and Works
* Integration tests to verify correct behavior
* Intellij coverage report is available at:
  * https://kevansimpson.github.io/library-alexandria/
* Data model diagram, which can be edited with [UMLet](https://www.umlet.com/), is available:
  * [via source code](./src/main/resources/public/catalog.png)
  * [via endpoint](http://localhost:8080/catalog.png)

### Work To Be Done

* Unit tests
* Logging
* Endpoints/controllers for child entities of Works

### Conventions and Assumptions
* Authors and Works can be independent of each other. An Author can have zero Works and a Work can be anonymously written with zero Authors.
* Works endpoints offer cascading behavior; meaning the entire Work object graph will be utilized.

### Seed Data
By default, the application is seeded with sample data to exercise all of the entities.
To start the application _without_ seed data, run the following command:

```shell
./mvnw clean spring-boot:run -Dspring-boot.run.arguments="--seed.data=false"
```

__Note:__ Integration tests (i.e. ones annotated with `@SpringBootTest`, rely on presence of seed data.
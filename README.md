# Great Library of Alexandria

### Completed Work

* Entities for complete data model
* Persistence mechanisms for all entities
* Service layer components for Authors and Works
* Endpoints via controllers for Authors and Works
* Data model diagram, which is available:
  * [via source code](./src/main/resources/public/catalog.png)
  * [via endpoint](http://localhost:8080/catalog.png)

### Work To Be Done

* Unit and integration tests
* Logging
* Endpoints/controllers for child entities of Works

### Conventions and Assumptions
* Authors and Works can be independent of each other. An Author can have zero Works and a Work can be anonymously written with zero Authors.
* Works endpoints offer cascading behavior; meaning the entire Work object graph will be utilized.
# Favoruite movies :)

### Stack used

* Spring-boot
* Java 17
* H2 (memory database) to have some data
* JPA

### What does this sample do ?
after you have logged (using user1 or user2) you can the see a list of movie
from this list you can mark/unmark your favorites ones.

some data (1000 movies) is already provided.

movies can be also filtered by genre

### I want to see it in action.

`build and start the application` use the file `scripts/app_interaction.sh` to see in action
or
run `AppIntegrationTest` under test

### What does this sample have ?

some tests, not everything is covered (no 100%) some rest api are covered.
in test/resources/ there are examples of response (*.json)

this sample contains basic auth, via username & password. the 2 users that exists are
`user1` and `user2` (password same as username)
junit to cover auth, is included.

spring-boot:build-image builds (a docker image)

rest controller based on `https://spring.io/projects/spring-hateoas`


### What does this sample DOES NOT have ?

* docker or any non in memory database
* currently, there is a very basic auth and CSRF protection (is disabled)
* there is only configuration for using h2 -> console is enabled
* integration test -> there is one added on 4 Jul
* not optimized
* swagger
* validation
* error handling

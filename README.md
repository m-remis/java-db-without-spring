#### Simple demo application that provides REST APIs for managing user posts.

Program in Java language that is processing commands from FIFO queue using Producer –
Consumer pattern.
Supported commands are the following:

* Add - adds a user into a database
* PrintAll – prints all users into standard output
* DeleteAll – deletes all users from database

User is defined as database table "SUSERS"

- "USER_ID": integer
- "USER_GUID": string
- "USER_NAME": string

This project uses:

* Java 17
* Maven
* In memory H2 database
* Hibernate

### Make sure to have installed

[Git](https://git-scm.com/downloads)

[JDK 17 or later](https://adoptium.net)

[Maven 3.8.8 or later](https://maven.apache.org/download.cgi)

### DB Structure

During unit tests, this project uses simple in memory H2 database with initial SQL script for schema creation.

[SQL Script used for table cration](src/test/resources/schema.sql)

### Build & Test:

```
mvn clean install
```


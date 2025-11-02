# Simple Forums
> This is a Forum webapp, it offers basic functionality such as account registration/login, creating communities, posts, and commenting on posts, also liking and disliking comments is also possible in this app.

## Requirements
| Requirement | Version |
| ----------- |--------|
| Java | 21 or later |
| Spring Boot | 3.4.0 or later |
| Postgresql | 17 or later |
| Docker | 28.1.1, build 4eba377 or latest |
| Maven | 3.9.11 (3e54c93a704957b63ee3494413a2b544fd3d825b) or later |
| Make | GNU Make 4.4.1|

## Database setup
create an .env (or .env.properties) or set up using terminal.
```
DB_URL=jdbc:postgresql://[URL]:[PORT]/postgres
DB_USERNAME=postgres
DB_PASSWORD=password
```
*Note: If using supabase, use Transaction pooler method instead of Direct connection method.

## Pooling
Configure as required, already configured in application.properties
```
spring.datasource.hikari.initialization-fail-timeout=0
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.minimum-idle=2
spring.datasource.hikari.maximum-pool-size=5
```

## Create tables
modify validate to update
```
spring.jpa.hibernate.ddl-auto=update
```

## Run
```
mvn spring-boot:run
```
OR
```
make
```

## Build Docker Image and Run
```
docker build -t simpleforums:latest .
docker run -it -p 8080:8080 simpleforums
```

OR

```
make build
```

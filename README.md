# Simple Forums
> This is a Forum webapp, it offers basic functionality such as account registration/login, creating communities, posts, and commenting on posts, also liking and disliking comments is also possible in this app. The app will run on 8080 port by default.

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
Create an .env file in the root folder
```
DB_URL=jdbc:postgresql://[HOST]:[PORT]/[DB_NAME]
DB_USERNAME=username
DB_PASSWORD=password
```
OR
Create variables in terminal session
```
#Linux
export DB_URL=jdbc:postgresql://[HOST]:[PORT]/[DB_NAME]
export DB_USERNAME=username
export DB_PASSWORD=password

#windows
set DB_URL=jdbc:postgresql://[HOST]:[PORT]/[DB_NAME]
set DB_USERNAME=username
set DB_PASSWORD=password
```
Use Postgres on local machine or on cloud.
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
Modify application.properties
```
spring.jpa.hibernate.ddl-auto=create # First run, this will auto create tables
spring.jpa.hibernate.ddl-auto=validate # Subsequent runs
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
## Contributing
Contributions are welcome! Please feel free to submit a Pull Request. For major changes, please open an issue first to discuss your proposed changes.

## License
This project is licensed under the [MIT License](LICENSE)

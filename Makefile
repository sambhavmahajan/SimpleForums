run:
	mvn spring-boot:run
build:
	docker build -t simpleforums:latest .
	docker run -it -p 8080:8080 simpleforums

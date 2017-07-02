This is Spring boot example
=====================================

# Features
```
	- restful apis with http request / response for json format
	- datasource for mysql
	- redis
	- spring security for restful api
	- scheduler
	- logback

```

You can run like this,

# Run
```
	1. in eclipse debug
		- Project: tz-spring-boot
		- Main class: com.example.Application
		- Program arguments: --spring.profiles.active=local
	
	2. in eclipse
		mvn spring-boot:run
		mvn spring-boot:run -Drun.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8888"
	
	3. in console
		mvn clean compile package
	
	4. run prod mode
		mvn spring-boot:run
		or
		java -jar target/tz-spring-boot-1.0-SNAPSHOT.jar --spring.profiles.active=local
```

# Configuration
```
	# For hsqldb
		application-local.properties
		
		<dependency>
			<groupId>org.hsqldb</groupId>
			<artifactId>hsqldb</artifactId>
		</dependency>
		
	# For mysql
		application-local.properties_mysql_redis
		
		<dependency>
			<groupId>mysql</groupId>
			<artifactId>mysql-connector-java</artifactId>
		</dependency>
	
```

# Test
```
	http://localhost:8088/rest/company/{"companyId":"1","user":"user1","password":"password1"}

	http://localhost:8088/rest/book/{"bookId":"1","bookName":"bookName1","user":"user1","password":"password1"}
	
```

# Swagger
```
	http://localhost:8088/v2/api-docs
	
	http://localhost:8088/swagger-ui.html

```

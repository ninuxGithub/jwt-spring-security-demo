# 项目来自github
    修改使用mysql数据库
    
# JWT Spring Security Demo

![Screenshot from running application](etc/screenshot-jwt-spring-security-demo.png?raw=true "Screenshot JWT Spring Security Demo")

## About
This is just a simple demo for using **JSON Web Token (JWT)** with **Spring Security** and
**Spring Boot 2**. This solution is partially based on the blog entry
[REST Security with JWT using Java and Spring Security](https://www.toptal.com/java/rest-security-with-jwt-spring-security-and-java)
and the demo project [Cerberus](https://github.com/brahalla/Cerberus). Thanks to the authors!

[![Build Status](https://travis-ci.org/szerhusenBC/jwt-spring-security-demo.svg?branch=master)](https://travis-ci.org/szerhusenBC/jwt-spring-security-demo)

## Requirements
This demo is build with with Maven 3 and Java 1.8.

## Usage
Just start the application with the Spring Boot maven plugin (`mvn spring-boot:run`). The application is
running at [http://localhost:8080](http://localhost:8080).

There are three user accounts present to demonstrate the different levels of access to the endpoints in
the API and the different authorization exceptions:
```
Admin - admin:admin
User - user:password
Disabled - disabled:password (this user is disabled)
```

There are three endpoints that are reasonable for the demo:
```
/auth - authentication endpoint with unrestricted access
/persons - an example endpoint that is restricted to authorized users (a valid JWT token must be present in the request header)
/protected - an example endpoint that is restricted to authorized users with the role 'ROLE_ADMIN' (a valid JWT token must be present in the request header)
```

I've written a small Javascript client and put some comments in the code that hopefully makes this demo
understandable.

### Generating password hash for new users

I'm using [bcrypt](https://en.wikipedia.org/wiki/Bcrypt) to encode passwords. Your can generate your hashes with this simple tool: [Bcrypt Generator](https://www.bcrypt-generator.com)

### Using another database

Actually this demo is using an embedded H2 database that is automatically configured by Spring Boot. If you want to connect to another database you have to specify the connection in the *application.yml* in the resource directory. Here is an example for a MySQL DB:

```
spring:
  jpa:
    hibernate:
      # possible values: validate | update | create | create-drop
      ddl-auto: create-drop
  datasource:
    url: jdbc:mysql://localhost/myDatabase
    username: myUser
    password: myPassword
    driver-class-name: com.mysql.jdbc.Driver
```

*Hint: For other databases like MySQL sequences don't work for ID generation. So you have to change the GenerationType in the entity beans to 'AUTO' or 'IDENTITY'.*

You can find a reference of all application properties [here](http://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html).

### Using Flyway

https://github.com/szerhusenBC/jwt-spring-security-demo/issues/81

## Docker
This project has a docker image. You can find it at [https://hub.docker.com/r/hubae/jwt-spring-security-demo/](https://hub.docker.com/r/hubae/jwt-spring-security-demo/).

## Questions
If you have project related questions please take a look at the [past questions](https://github.com/szerhusenBC/jwt-spring-security-demo/issues?utf8=%E2%9C%93&q=is%3Aissue%20is%3Aopen%2Cclosed%20label%3Aquestion%20) or create a new ticket with your question.

*If you have questions that are not directly related to this project (e.g. common questions to the Spring Framework or Spring Security etc.) please search the web or look at [Stackoverflow](http://www.stackoverflow.com).*

Sorry for that but I'm very busy right now and don't have much time.

## Interesting projects

* [spring-security-pac4j](https://github.com/pac4j/spring-security-pac4j) a Spring Boot integration for Pac4j (a Java security engine that covers JWT beside others)
* For more complex microservice environments take a look here: [Using JWT with Spring Security OAuth](http://www.baeldung.com/spring-security-oauth-jwt)

## External resources

Dan Vega (https://twitter.com/therealdanvega) created a video that explained this project quite fine. Thanks to him!

https://youtu.be/mD3vmgksvz8

## Creator

**Stephan Zerhusen**

* <https://twitter.com/stzerhus>
* <https://github.com/szerhusenBC>

## Copyright and license

The code is released under the [MIT license](LICENSE?raw=true).

---------------------------------------

Please feel free to send me some feedback or questions!


spring boot 使用H2数据库
https://www.cnblogs.com/austinspark-jessylu/p/8065979.html


## init mysql table

```sql
-- initialize tables
DROP TABLE IF EXISTS `user_authority`;
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `authority`;


CREATE TABLE user
(
id integer NOT NULL,
username VARCHAR(50) NOT NULL,
password VARCHAR(100) NOT NULL,
firstname VARCHAR(50) NOT NULL,
lastname VARCHAR(50) NOT NULL,
email VARCHAR(50) NOT NULL,
enabled boolean,
lastpasswordresetdate timestamp NOT NULL,
CONSTRAINT user_pkey PRIMARY KEY (id)
);

DROP TABLE IF EXISTS `authority`;

CREATE TABLE authority
(
id integer NOT NULL,
name VARCHAR(50) NOT NULL,
CONSTRAINT authority_pkey PRIMARY KEY (id)
);

CREATE TABLE user_authority
(
user_id integer NOT NULL,
authority_id integer NOT NULL,
CONSTRAINT fk_authority_id_user_authority FOREIGN KEY (authority_id)
REFERENCES authority (id) MATCH SIMPLE
ON UPDATE NO ACTION ON DELETE NO ACTION,
CONSTRAINT fk_USER_user_authority FOREIGN KEY (user_id)
REFERENCES user (id) MATCH SIMPLE
ON UPDATE NO ACTION ON DELETE NO ACTION
);

-- insert data
INSERT INTO user (ID, USERNAME, PASSWORD, FIRSTNAME, LASTNAME, EMAIL, ENABLED, LASTPASSWORDRESETDATE) VALUES (1, 'admin', '$2a$08$lDnHPz7eUkSi6ao14Twuau08mzhWrL4kyZGGU5xfiGALO/Vxd5DOi', 'admin', 'admin', 'admin@admin.com', 1, STR_TO_DATE('01/01/2016', '%c/%e/%Y %r'));
INSERT INTO user (ID, USERNAME, PASSWORD, FIRSTNAME, LASTNAME, EMAIL, ENABLED, LASTPASSWORDRESETDATE) VALUES (2, 'user', '$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC', 'user', 'user', 'enabled@user.com', 1, STR_TO_DATE('01/01/2016','%c/%e/%Y %r'));
INSERT INTO user (ID, USERNAME, PASSWORD, FIRSTNAME, LASTNAME, EMAIL, ENABLED, LASTPASSWORDRESETDATE) VALUES (3, 'disabled', '$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC', 'user', 'user', 'disabled@user.com', 0, STR_TO_DATE('01/01/2016','%c/%e/%Y %r'));

INSERT INTO authority (ID, NAME) VALUES (1, 'ROLE_USER');
INSERT INTO authority (ID, NAME) VALUES (2, 'ROLE_ADMIN');

INSERT INTO user_authority (USER_ID, AUTHORITY_ID) VALUES (1, 1);
INSERT INTO user_authority (USER_ID, AUTHORITY_ID) VALUES (1, 2);
INSERT INTO user_authority (USER_ID, AUTHORITY_ID) VALUES (2, 1);
INSERT INTO user_authority (USER_ID, AUTHORITY_ID) VALUES (3, 1);
```

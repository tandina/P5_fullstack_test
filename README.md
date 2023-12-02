# Yoga

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 14.1.0.

## Start the project

Git clone:

> git clone https://github.com/tandina/P5_fullstack_test

Go inside folder:

> cd yoga

Install dependencies:

> npm install

Launch Front-end:

> npm run start;


## Ressources

### Mockoon env 

### Postman collection

For Postman import the collection

> ressources/postman/yoga.postman_collection.json 

by following the documentation: 

https://learning.postman.com/docs/getting-started/importing-and-exporting-data/#importing-data-into-postman


### MySQL
create in Mysql workbench a table test with username root and password rootmysql.

setup your MySqln environnement, in application.propreties copy and paste this:
spring.sql.init.mode=always
spring.datasource.url=jdbc:mysql://localhost:3306/test?allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=rootmysql

spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5InnoDBDialect
spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
spring.jpa.show-sql=true
oc.app.jwtSecret=openclassrooms
oc.app.jwtExpirationMs=86400000
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.format_sql=true
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE

SQL script for creating the schema is available `ressources/sql/script.sql`

By default the admin account is:
- login: yoga@studio.com
- password: test!1234


### Test

#### E2E
start front end server : ng serve
start api
Launching e2e test on terminal :

> npm cypress:run

Launching e2e test on a web brower :

> npm cypress:open
Generate coverage report (you should launch e2e test before):

> npm run e2e:coverage

Report is available here:

> P5_fullstack_test/front/coverage/lcov-report/index.html

#### Unitary test
before you start unitray testing, if ng serve is running stop it first.

Launching test on terminal and get a report:
> npx jest --coverage

Report is available here:
P5_fullstack_test\front\coverage\jest\lcov-report\index.html


Launching test on terminal without report:
> npm run test

for following change:

> npm run test:watch

#### JUnit 5 test
test on IntelliJ
in maven widget, click on install to launch every test and generate a jacoco report.

Report is available here: 
P5_fullstack_test\back\target\site\jacoco\index.html

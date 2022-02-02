# Spring PetClinic Sample Application
[![pipeline status](https://git.linux.iastate.edu/tghosh/petclinic-mysql/badges/withView/pipeline.svg)](https://git.linux.iastate.edu/tghosh/petclinic-mysql/commits/withView)

## Branch Structure:
### master:
Simple REST Application, independent data tables, Returns JSON Object/Array as response
### withView:
Simple Web Application, independent data tables, Incorporates View with HTML templates. Returns a web page. Also demonstration for logging.

Almost same as master REST Application. Have to add templates under resources, and return the corresponding HTML page in Controller.

### entity_relationships:
Moderate level REST application with multiple tables and relations. Incorporates OneToOne, OneToMany and ManyToMany relationships.

#### Relationships:
One 'TA' has Many 'Teams'; One 'Team' has One 'Project'; Many 'Projects' uses Many 'Technologies'

See the model files 'TA.java', 'Team.java', 'Project.java' and 'Technology.java' to understand the annotated relationship mapping. No major change in Controllers from the above branches.

## Other Configurations:
### application.properties:
Database URL, username and passwords, DDL Config, Port

### pom.xml:
External dependencies (Search for dependencies in "Maven Central")

## How to Run the Petclinic application:
1. Clone it your local device
2. Start your local MYSQL database instance if not running
3. Create a new database called 'petclinic' if not already done using "CREATE DATABASE petclinic"
4. Open pom.xml and right click to run as Maven Build. If asks for goal type 'package'
5. Once build is successful, Open PetClinicApplication.java; right click to run as Spring Boot App
6. Once you get the message "Started the Petclinic application..." in your console without any error, go to database to confirm the tables are created
7. If the tables are created, copy the content of data.sql under resources and run them in your MySQL server. You can run them from your work bench or command line.
8. Go to each controller, and for each endpoint, that exists in this project, a sample endpoint URL is provided, hit it from Postman/Advanced Rest Client

### See the course Piazza for more instructions.

## Some helpful links:
[HelloWorld REST Api](https://spring.io/guides/gs/rest-service/)
[Getting Started with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)
[Spring Annotations](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
[Application Properties Appendix](https://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html)

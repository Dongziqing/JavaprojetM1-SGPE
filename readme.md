## Background
In today's educational environment, teachers need to effectively manage students' projects to ensure that they achieve good academic results during their training. To meet this need, one teacher decided to create an application to manage student projects for the current academic year. The application aims to provide a centralized platform where teachers and students can connect, but the student identity only allows viewing and querying of relevant grades, enabling teachers to easily track student, course and project information to better support students' academic development, while ensuring that the information is timely and unbiased.
## Environment
* Maven
* JavaFX
* Spring
* MySQL 8.0
* JDK 17.0
### Please ensure that your jdk version is higher than 13 and that the JAVAFX module is included.
## Quick Start
 First, create a database using the studentmanage.sql in the Database folder. <br> 
 Then, execute the following commands:<br>
 
 ```mvn clean package```<br>
 ```cd target```<br>
 ```java --module-path "./libs" --add-modules javafx.controls,javafx.fxml -jar arletteziqing-1.0-SNAPSHOT.jar```

 ## Authors
 * Dongziqing
 * SOUFO DJUIKUI Arlette

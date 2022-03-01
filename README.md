# King of the Rock


## Description



### Dependencies
 


## Installation  

To install the server API as a service,
* Execute `install.sh`
* Start the service: `systemctl start kingoftherock.service`

To run the server API without installing as a service,
* Set properties in `Backend/application.properties` (see example file)
* Build the executable JAR: `mvn -f Backend/ clean package`
* Run the JAR: `java -jar Backend/target/KingOfTheRock-DEMO.jar`

## Contributors 
* Dave Arlt (dcarlt@iastate.edu)
* Noah Cordova (ncordova@iastate.edu)
* Matthew Renze (merenze@iastate.edu)
* Dan Rosenhamer (djr2@iastate.edu)


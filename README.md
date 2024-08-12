Run Instructions
  * Prerequisites  &nbsp;&nbsp;&nbsp;&nbsp;Java 11
1. Unpack the zip
2. Open a terminal and go to the folder you unpacked the zip in
3. Run the jar via the java -jar command, the jar is in the target folder 
  * eg: java -jar target/trueaccord-0.0.1-SNAPSHOT.jar
4. Fire a GET request to localhost:8080/debts

    this can be done via curl in another terminal (curl --location --request GET 'localhost:8080/debts'), postman, etc
5. If you want to run against different endpoints, you can change them in the application.yml file in src/main/resources, the app can then be rebuilt using mvn clean install if you have maven setup

Time Overview
  * Planning
  * Writing Code
  * Confirming Functionality
  * Minor Adjustments
  * Writing Unit Tests
    
Process
  * Setup Blank App
  * Create Models
  * Setup Communicator to Ingest Data
  * Setup Configuration classes
  * Create Controller
  * Confirm Apps Runs and can fetch data
  * Create Service Class
  * Double check it matches all acceptance Criteria
  * Run App
  * Write UnitTests
  
Changes With More Time
  * Would have done better data ingestion/validation
  * More robust Exception Handling and Response Generation for errors
  
Logic Location
  * All of the External request retrieval is done in PaymentCommunicatorImpl
  * All debt/payment/plan data processing and collation is done in DebtServiceImpl
  * Date Formatting is done within the Debt model toString override
  
Design Decisions/Assumptions
  * That the data from the URLs would be clean
  * I decided to make it an WebService when I decided to use Java since it is the language I have been using the most recently, it probably would have been easier to just write is as a script in python
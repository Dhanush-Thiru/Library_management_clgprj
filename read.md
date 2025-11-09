1. Prerequisites
You must have the following installed:

Java Development Kit (JDK): Version 8 or newer.

MySQL Server: Running on localhost:3306 (or adjust the JDBC_URL in the Java file).

MySQL Connector/J (JDBC Driver): This is a .jar file that allows Java to talk to MySQL. You will need to download it from the official MySQL website. Place the downloaded .jar file (e.g., mysql-connector-j-8.0.33.jar) in an easily accessible folder.

2. Database Setup
First, you need to create the database and the required table structure.

Open your MySQL Client (e.g., MySQL Workbench, DBeaver, or the mysql command-line tool).

Execute the SQL script: Copy the entire content of the library_setup.sql file and run it. This will:

Create the library_db database.

Create the books table.

Insert three initial records.

3. Configure Java Credentials
You must update the placeholder password in the Java file to match your MySQL root password.

Edit LibraryManager.java and change the value of DB_PASSWORD:

Java

private static final String DB_USER = "root";
private static final String DB_PASSWORD = "your_mysql_password"; // <--- CHANGE THIS
4. Compile and Run the Java Application
Navigate to the directory where you saved LibraryManager.java using your command prompt or terminal.

Step 4a: Compile the Java Code
You must include the JDBC driver in the classpath during compilation. Use the -classpath or -cp flag.

Bash

# Replace <PATH_TO_CONNECTOR_JAR> with the actual path and filename of your downloaded driver file.
javac -cp <PATH_TO_CONNECTOR_JAR> LibraryManager.java
Example (Linux/macOS):

Bash

javac -cp /path/to/mysql-connector-j-8.0.33.jar LibraryManager.java
Step 4b: Run the Application
Execute the compiled class (LibraryManager) again, ensuring the JDBC driver is included in the classpath.

Bash

# Run the application, using the same classpath as compilation
java -cp .:<PATH_TO_CONNECTOR_JAR> LibraryManager
Example (Linux/macOS):

Bash

java -cp .:/path/to/mysql-connector-j-8.0.33.jar LibraryManager
Example (Windows): Note the use of semicolon (;) instead of colon (:) as the path separator.

Bash

java -cp .;C:\path\to\mysql-connector-j-8.0.33.jar LibraryManager

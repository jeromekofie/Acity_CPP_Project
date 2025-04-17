## Java Vehicle Rental System

# Project Structure
- src/VehicleRentalGUI.java` - Main Java source file
- lib/mariadb-java-client-3.5.2.jar` - MariaDB JDBC driver
- .vscode/settings.json` - VS Code Java configuration

## Compilation and Run Instructions

### Compile:
javac -cp "lib/mariadb-java-client-3.5.2.jar" src/VehicleRentalGUI.java



## Run:
java -cp "lib/mariadb-java-client-3.5.2.jar;src" VehicleRentalGUI

## Database Info:
- DB Name: vehiclerental
- Table: users (username, password, role)
- Table: vehicles (id, make, model, color, type)

Make sure to have a running MariaDB instance and a test user like:
INSERT INTO users (username, password, role) VALUES ('admin', 'admin', 'admin');

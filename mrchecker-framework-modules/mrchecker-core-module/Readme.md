# This module features two pom files

The first one is to build mr checker module.
The second one is to build gui tool to handle encryption.

To run the second one run a command like `mvn -f pom_gui.xml clean package -DskipTests=true` - as the tests are for module not for gui app we can skip them.
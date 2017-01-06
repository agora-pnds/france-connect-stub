# Description

Thanks to great work from [A. Sirko](https://github.com/asirko) and [D. Gombert](https://github.com/dgombert) we now have 
a stub for France Connect OpenIdConnect provider (https://franceconnect.gouv.fr)

# Prerequisite

- [Java 7](https://java.com)
- [Maven](https://maven.apache.org)

# Installation

```sh
git clone https://github.com/tchabaud/france-connect-stub
cd france-connect-stub
mvn clean compile package
java -jar target/france-connect-stub.jar -httpPort=1234 -Ddir.stub=$(realpath src/main/resources/json)
```
